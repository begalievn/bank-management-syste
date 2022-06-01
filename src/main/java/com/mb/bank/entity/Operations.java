package com.mb.bank.entity;

import com.mb.bank.enums.PaymentType;
import com.mb.bank.enums.TrnStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.print.attribute.standard.DateTimeAtCreation;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Operations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private double sum;

    @OneToOne
    private User from;

    @OneToOne
    private User to;

    @Column(name = "txn_date")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private TrnStatus result;
}
