package com.withins.crawl;

import com.withins.core.welfarecenter.entity.News;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;

import java.util.List;

@RequiredArgsConstructor
public class ListNewsItemWriter implements ItemWriter<List<News>> {
    private final EntityManagerFactory entityManagerFactory;
    private final JpaItemWriter<News> jpaItemWriter;

    public ListNewsItemWriter(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.jpaItemWriter = new JpaItemWriterBuilder<News>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    @Override
    public void write(Chunk<? extends List<News>> chunk) throws Exception {
        for (List<News> newsList : chunk.getItems()) {
            jpaItemWriter.write(new Chunk<>(newsList));
        }
    }
}