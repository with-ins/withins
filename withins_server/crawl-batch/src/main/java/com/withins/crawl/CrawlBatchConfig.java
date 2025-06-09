package com.withins.crawl;

import com.withins.core.welfarecenter.entity.Post;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import com.withins.core.welfarecenter.repository.WelfareCenterRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.transaction.PlatformTransactionManager;
import software.amazon.awssdk.services.lambda.LambdaClient;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CrawlBatchConfig {
    public static final int CHUNK_SIZE = 1;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    private final WelfareCenterRepository welfareCenterRepository;

    @Value("${aws.lambda.function}")
    private String lambdaFunction;

    @Bean
    public Job crawlingJob() {
        return new JobBuilder("crawlingJob", jobRepository)
                .start(lambdaCrawlingStep())
//                .next(processS3FilesStep())
                .build();
    }

    @Bean
    public Step lambdaCrawlingStep() {
        return new StepBuilder("lambdaCrawlingStep", jobRepository)
                .tasklet(lambdaCrawlingTasklet(null), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet lambdaCrawlingTasklet(
            @Value("#{jobParameters['syncDate']}") String syncDateStr
    ) {

        // 문자열 날짜를 LocalDate로 변환
        LocalDate syncDate = (syncDateStr != null) 
            ? LocalDate.parse(syncDateStr) 
            : LocalDate.now(); // 기본값으로 현재 날짜 사용

        return new LambdaCrawlingTasklet(
                LambdaClient.builder().build(),
                lambdaFunction,
                welfareCenterRepository,
                syncDate
        );
    }

//    @Bean
//    public Step processS3FilesStep() {
//        return new StepBuilder("processS3FilesStep", jobRepository)
//                .<PostDto, Post>chunk(CHUNK_SIZE, transactionManager)
//                .reader(multiS3Reader(null))
//                .processor(itemProcessor())
//                .writer(jpaItemWriter())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public MultiResourceItemReader<PostDto> multiS3Reader(
//            @Value("#{jobExecutionContext['successfulS3Paths']}") List<String> s3Paths) {
//
//        MultiResourceItemReader<PostDto> multiReader = new MultiResourceItemReader<>();
//
//        if (s3Paths != null && !s3Paths.isEmpty()) {
//            // S3 경로 목록을 Resource 배열로 변환
//            Resource[] resources = s3Paths.stream()
//                    .map(S3Resource::new)
//                    .toArray(Resource[]::new);
//
//            multiReader.setResources(resources);
//            multiReader.setDelegate(s3JsonItemReader());
//
//            log.info("Configured MultiResourceItemReader with {} S3 resources", resources.length);
//        } else {
//            log.warn("No S3 paths provided for MultiResourceItemReader");
//        }
//
//        return multiReader;
//    }
//
//    @Bean
//    public JsonItemReader<PostDto> s3JsonItemReader() {
//        return new JsonItemReaderBuilder<PostDto>()
//                .name("s3JsonItemReader")
//                .jsonObjectReader(new JacksonJsonObjectReader<>(PostDto.class))
//                .build();
//    }

//    @Bean
//    public ItemProcessor<PostDto, Post> itemProcessor() {
//        return item -> {
//            WelfareCenter welfareCenter = welfareCenterRepository.findByName(item.getWelfareCenterName())
//                    .orElseThrow(IllegalArgumentException::new);
//
//            return Post.builder()
//                    .url(item.getUrl())
//                    .title(item.getTitle())
//                    .welfareCenter(welfareCenter)
//                    .build();
//        };
//    }
//
//    @Bean
//    public JpaItemWriter<Post> jpaItemWriter() {
//        return new JpaItemWriterBuilder<Post>()
//                .entityManagerFactory(entityManagerFactory)
//                .usePersist(true)
//                .build();
//    }
}
