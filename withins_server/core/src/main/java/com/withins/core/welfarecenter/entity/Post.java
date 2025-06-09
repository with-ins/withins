package com.withins.core.welfarecenter.entity;

import com.withins.core.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "category")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Post extends BaseEntity {

    @Getter
    public enum Category {
        /*
         서브 클래스의 @DiscriminatorValue 값이 enum의 discriminatorValue 값과 일치해야 됩니다.
         */
        NOTICE("notice"),
        JOB("job");

        private final String discriminatorValue;

        Category(String discriminatorValue) {
            this.discriminatorValue = discriminatorValue;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "welfare_center_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WelfareCenter welfareCenter;

    private String url;

    /*
     상속 전략의 구분자(Discriminator)를 사용하는 'category' 컬럼과 매핑됩니다.
     insertable=false, updatable=false 로 JPA가 자동으로 관리하도록 설정했습니다.
     */
    @Column(name = "category", insertable = false, updatable = false)
    private String category ;
}
