package com.grupoa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserTable")
public class User {

    @Id
    private Long ra;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "cpf")
    @NonNull
    private String cpf;

}
