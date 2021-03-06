package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.AppAccount;

/**
 * AppAccount Repository class.
 *
 * @author Ludovic Tuccio
 */
@Repository
public interface AppAccountRepository extends JpaRepository<AppAccount, Long> {

    /**
     * Method used to find an user application account by id.
     *
     * @param userId
     */
    AppAccount findByUserId(Long userId);

}
