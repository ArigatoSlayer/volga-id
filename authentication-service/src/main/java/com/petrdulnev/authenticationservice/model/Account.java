package com.petrdulnev.authenticationservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String refreshToken;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Role> authorities;
    @ColumnDefault("false")
    private Boolean deleted;
}
