package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.PersonalPayment;

/**
 * PersonalPayment Repository class.
 *
 * @author Ludovic Tuccio
 */
@Repository
public interface PersonalPaymentRepository
        extends JpaRepository<PersonalPayment, Long> {

    /**
     * Method used to find a personal payment by appAccount.
     *
     * @param myAppAccount AppAccount object
     */
    PersonalPayment findByMyAppAccount(AppAccount myAppAccount);

}
