package com.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.User;

/**
 * UserService class.
 *
 * @author Ludovic Tuccio
 */
@Service
public class ConnectionService implements IConnectionService {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("ConnectionService");

    // @Autowired
    // private UserRepository userRepository;

    // @Autowired
    // private AppAccountRepository appAccountRepository;

    // @Autowired
    // private ConnectionRepository connectionRepository;

    @Override
    public User addConnection(final String email) {
        return null;

    }

    @Override
    public User deleteConnection(final String email) {

        return null;
    }

    @Override
    public User getConnection(final int id) {
        return null;
        // return connectionRepository.getConnectionById(id);
    }

}
