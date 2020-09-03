package com.paymybuddy.repository;

import javax.transaction.Transactional;

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
@Transactional
public interface PersonalPaymentRepository extends JpaRepository<PersonalPayment, Long> {

    /**
     * Method used to find an user application account by email.
     *
     * @param email
     */
    PersonalPayment findByMyAppAccount(AppAccount myAppAccount);

}
