package com.withins.core.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("social")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
public final class SocialUser extends User {

    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerId;

    @Override
    protected String getDiscriminatorValue() {
        return "social";
    }
}
