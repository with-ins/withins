package com.withins.core.welfarecenter.entity;

import com.withins.core.BaseEntity;
import com.withins.core.news.enums.KoreanRegion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "welfare_centers")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class WelfareCenter extends BaseEntity {

    @Id
    @Column(name = "welfare_center_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String email;
    private String address;
    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private KoreanRegion region;

}
