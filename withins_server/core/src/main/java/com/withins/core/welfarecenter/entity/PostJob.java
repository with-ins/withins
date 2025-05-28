package com.withins.core.welfarecenter.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("job")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class PostJob extends Post {

    private LocalDateTime recruitmentStartDate;
    private LocalDateTime recruitmentEndDate;
}
