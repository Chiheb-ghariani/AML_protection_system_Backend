package com.aml.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_seq")
    @SequenceGenerator(name = "transactions_seq", sequenceName = "transactions_seq", allocationSize = 1)
    private Long id;

    private Double amount;

    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING) // Line 27
    @Column(name = "transaction_type")
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;

    // Define the enum inside the class
    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER
    }
}