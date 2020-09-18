package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.PersonalTransfer;

/**
 * PersonalTransfer Repository class.
 *
 * @author Ludovic Tuccio
 */
@Repository
public interface PersonalTransferRepository
        extends JpaRepository<PersonalTransfer, Long> {

    /**
     * Method used to find a personal transfer by appAccount.
     *
     * @param myAppAccount AppAccount object
     */
    PersonalTransfer findByMyAppAccount(AppAccount myAppAccount);

}
