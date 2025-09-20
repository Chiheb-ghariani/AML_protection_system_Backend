package com.aml.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq")
    @SequenceGenerator(name = "accounts_seq", sequenceName = "accounts_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    private Double balance;

    private LocalDate creationDate;

    // Temporary User field for service logic, not persisted
    @Transient
    private User user; // Marked as transient to avoid mapping to a column

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}