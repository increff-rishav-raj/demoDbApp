package com.example.local.demoDbApp.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class UserForm {
    @NotBlank
    @Size(min = 5, max = 15, message = "username should of length between 5 and 15")
    private String username;
}
