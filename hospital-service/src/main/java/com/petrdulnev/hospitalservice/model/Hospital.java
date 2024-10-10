package com.petrdulnev.hospitalservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String contactPhone;
    @ElementCollection
    private Set<String> rooms = new HashSet<>();
    @ColumnDefault("false")
    private Boolean deleted;
}
