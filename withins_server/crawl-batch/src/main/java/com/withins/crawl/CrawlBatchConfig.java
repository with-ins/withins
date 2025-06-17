package com.withins.crawl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.withins.core.news.component.NewsReader;
import com.withins.core.news.entity.News;
import com.withins.core.welfarecenter.component.WelfareCenterReader;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
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
import org.springframework.util.CollectionUtils;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CrawlBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    private final NewsReader newsReader;
    private final WelfareCenterReader welfareCenterReader;

    @Value("${aws.lambda.function}")
    private String lambdaFunction;

    @Bean
    public Job allCentersNewsCrawlJob() {
        return new JobBuilder("allCentersNewsCrawlJob", jobRepository)
                .start(allCentersNewsCrawlStep())
                .next(processS3FilesStep())
                .build();
    }

    @Bean
    public Step allCentersNewsCrawlStep() {
        return new StepBuilder("allCentersNewsCrawlStep", jobRepository)
                .tasklet(allCentersNewsCrawlerTasklet(null), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet allCentersNewsCrawlerTasklet(
            @Value("#{jobParameters['targetDate']}") String targetDate
    ) {
        return new AllCentersNewsCrawlerTasklet(
                lambdaClient(),
                lambdaFunction,
                LocalDate.parse(targetDate),
                welfareCenterReader
        );
    }

    @Bean
    public Step processS3FilesStep() {
        return new StepBuilder("processS3FilesStep", jobRepository)
                //TODO S3에 접근할 때 네트워크 오류 등 재시도하면 정상적으로 동작하는 경우 재시도 로직을 구현해야 한다. 어떤 예외가 발생할 지 확실하지 않아 실제 문제가 발생하면 구현한다.
                .<List<S3NewsFileDto>, List<News>>chunk(1, transactionManager) // 청크 크기는 1로 유지 (각 파일이 하나의 청크)
                .reader(s3NewsFileItemReader(null))
                .processor(newsItemProcessor())
                .writer(listNewsItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public S3NewsFileItemReader s3NewsFileItemReader(
            @Value("#{jobExecutionContext['successfulS3Paths']}") List<String> s3Paths) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if(CollectionUtils.isEmpty(s3Paths)) {
            return new S3NewsFileItemReader(null, objectMapper);
        }

        List<Resource> resources = s3Paths.stream()
                .map((s3Path) -> S3Resource.from(s3Path, s3Client()))
                .collect(java.util.stream.Collectors.toList());

        return new S3NewsFileItemReader(resources, objectMapper);
    }

    @Bean
    public ItemProcessor<List<S3NewsFileDto>, List<News>> newsItemProcessor() {
        return new ListNewsItemProcessor(newsReader, welfareCenterReader);
    }

    @Bean
    public ListNewsItemWriter listNewsItemWriter() {
        return new ListNewsItemWriter(entityManagerFactory);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().build();
    }

    @Bean
    public LambdaClient lambdaClient() {
        return LambdaClient.builder().build();
    }
}
