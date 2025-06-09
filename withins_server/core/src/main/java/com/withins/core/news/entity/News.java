package com.withins.core.news.entity;

import com.withins.core.BaseEntity;
import com.withins.core.news.enums.NewsType;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "news")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class News extends BaseEntity {

    @Column(name = "news_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long baseId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NewsType type;

    @Column(nullable = false)
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "welfare_center_id")
    private WelfareCenter welfareCenter;
    private LocalDate createdAt;
}
