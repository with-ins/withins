package com.withins.crawl;

import com.withins.core.news.entity.News;
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
                .next(processS3FilesStep())
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
            @Value("#{jobParameters['targetDate']}") String targetDate
    ) {
        // 문자열 날짜를 LocalDate로 변환
        LocalDate date = (targetDate != null)
            ? LocalDate.parse(targetDate)
            : LocalDate.now(); // 기본값으로 현재 날짜 사용

        return new LambdaCrawlingTasklet(
                LambdaClient.builder().build(),
                lambdaFunction,
                welfareCenterRepository,
                date
        );
    }

    @Bean
    public Step processS3FilesStep() {
        return new StepBuilder("processS3FilesStep", jobRepository)
                .<List<NewsDto>, List<News>>chunk(1, transactionManager) // 청크 크기는 1로 유지 (각 파일이 하나의 청크)
                .reader(s3NewsFileItemReader(null))
                .processor(newsItemProcessor())
                .writer(listNewsItemWriter())
                .build();
    }

//    @Bean
//    @StepScope
//    public MultiResourceItemReader<NewsDto> multiS3NewsReader(
//            @Value("#{jobExecutionContext['successfulS3Paths']}") List<String> s3Paths) {
//
//        MultiResourceItemReader<NewsDto> multiReader = new MultiResourceItemReader<>();
//
//        if (s3Paths != null && !s3Paths.isEmpty()) {
//            // S3 경로 목록을 Resource 배열로 변환
//            Resource[] resources = s3Paths.stream()
//                    .map(S3Resource::new)
//                    .toArray(Resource[]::new);
//
//            multiReader.setResources(resources);
//            multiReader.setDelegate(s3JsonNewsItemReader());
//
//            log.info("Configured MultiResourceItemReader with {} S3 resources", resources.length);
//        } else {
//            log.warn("No S3 paths provided for MultiResourceItemReader");
//        }
//
//        return multiReader;
//    }

//    @Bean
//    public JsonItemReader<NewsDto> s3JsonNewsItemReader() {
//        return new JsonItemReaderBuilder<NewsDto>()
//                .name("s3JsonNewsItemReader")
//                .jsonObjectReader(new JacksonJsonObjectReader<>(NewsDto.class))
//                .build();
//    }

    @Bean
    public ItemProcessor<List<NewsDto>, List<News>> newsItemProcessor() {
        return new ListNewsItemProcessor(welfareCenterRepository);
    }

//    @Bean
//    public JpaItemWriter<News> jpaNewsItemWriter() {
//        return new JpaItemWriterBuilder<News>()
//                .entityManagerFactory(entityManagerFactory)
//                .usePersist(true)
//                .build();
//    }

    @Bean
    public ListNewsItemWriter listNewsItemWriter() {
        return new ListNewsItemWriter(entityManagerFactory);
    }

    @Bean
    @StepScope
    public S3NewsFileItemReader s3NewsFileItemReader(
            @Value("#{jobExecutionContext['successfulS3Paths']}") List<String> s3Paths) {

        if (s3Paths != null && !s3Paths.isEmpty()) {
            List<Resource> resources = s3Paths.stream()
                    .map(S3Resource::new)
                    .collect(java.util.stream.Collectors.toList());

            return new S3NewsFileItemReader(resources);
        }

        return new S3NewsFileItemReader(java.util.Collections.emptyList());
    }
}
