package com.paymybuddy.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Transaction;

/**
 * Transaction Repository class.
 *
 * @author Ludovic Tuccio
 */
@Repository
@Transactional
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

}
