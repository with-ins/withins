package com.withins.core.user.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@DiscriminatorValue("form")
@SuperBuilder
@Getter
@NoArgsConstructor
public final class FormUser extends User {

    private String password;
    private String email;

    @Override
    protected String getDiscriminatorValue() {
        return "form";
    }

}
