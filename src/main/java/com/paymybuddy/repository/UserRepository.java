package com.paymybuddy.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.User;

/**
 * User repository class.
 *
 * @author Ludovic Tuccio
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Method repository used to find an user by email.
     *
     * @param email
     */
    User findByEmail(String email);

    /**
     * Method repository used to find an user by id.
     *
     * @param id
     */
    Optional<User> findById(Long id);

}
