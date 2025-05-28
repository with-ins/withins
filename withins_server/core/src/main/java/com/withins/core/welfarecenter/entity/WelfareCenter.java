package com.withins.core.welfarecenter.entity;

import com.withins.core.BaseEntity;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String region;
}
