package com.example.local.demoDbApp.pojo;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
public class UserPojo {
    @Id
    @TableGenerator(name = "user_id_generator", pkColumnValue = "user_id", initialValue = 100000, allocationSize = 20)
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "user_id_generator")
    private Integer id;

    @Column(nullable = false)
    private String username;
}