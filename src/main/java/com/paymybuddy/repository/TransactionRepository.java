package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Transaction;

/**
 * Transaction Repository class.
 *
 * @author Ludovic Tuccio
 */
@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

}
