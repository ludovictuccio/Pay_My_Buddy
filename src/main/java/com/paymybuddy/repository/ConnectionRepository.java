package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.User;

/**
 * ConnectionRepository class.
 *
 * @author Ludovic Tuccio
 */
@Repository
public interface ConnectionRepository extends JpaRepository<User, Integer> {

    @Query("SELECT c FROM User c where c.id =:id ")
    public User getConnectionById(@Param("id") int id);
}
