package com.withins.core.welfarecenter.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@DiscriminatorValue("notice")
@Getter
public class PostNotice extends Post {
    public PostNotice() {
    }
}

