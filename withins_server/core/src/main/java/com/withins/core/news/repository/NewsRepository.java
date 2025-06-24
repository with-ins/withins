package com.withins.core.news.repository;

import com.withins.core.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT n.link FROM News n WHERE n.link IN :links")
    Set<String> findLinkSetByLinkIn(@Param("links") List<String> links);
}
