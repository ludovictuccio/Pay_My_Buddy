package com.paymybuddy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.User;

/**
 * UserDao repository class.
 *
 * @author Ludovic Tuccio
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Method used to find an user by email.
     *
     * @param email
     */
    User findByEmail(String email);

    /**
     * Method used to find an user by id.
     *
     * @param id
     */
    Optional<User> findById(Long id);

}
