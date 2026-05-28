package com.project.librarymanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrowers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate membershipDate;

    @Builder.Default
    @Column(nullable = false)
    private boolean hasOverdueItems = false;
}