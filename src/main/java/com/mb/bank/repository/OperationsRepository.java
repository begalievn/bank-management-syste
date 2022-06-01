package com.mb.bank.repository;

import com.mb.bank.entity.Operations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationsRepository extends JpaRepository<Operations, Long> {
}
