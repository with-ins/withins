package com.withins.core.builders;

import com.withins.core.news.entity.News;
import com.withins.core.news.enums.NewsType;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;

@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsBuilder {

	private Long id;
	private String title = "테스트 제목";
	private NewsType type = NewsType.NOTICE;
	private String link = "https://example.com/example";
	private WelfareCenter welfareCenter = null;
	private LocalDate newsCreatedAt = LocalDate.now();

	public static NewsBuilder News() {
		return new NewsBuilder();
	}

	public News build() {
		return News.builder()
				.id(this.id)
				.title(this.title)
				.type(this.type)
				.link(this.link)
				.welfareCenter(this.welfareCenter)
				.newsCreatedAt(this.newsCreatedAt)
				.build();
	}
}
