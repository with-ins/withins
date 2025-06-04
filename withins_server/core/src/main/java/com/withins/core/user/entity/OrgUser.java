package com.withins.core.user.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@DiscriminatorValue("org")
@SuperBuilder
@Getter
@NoArgsConstructor
public final class OrgUser extends User {

    private String password;

    @Override
    protected String getDiscriminatorValue() {
        return "org";
    }


}
