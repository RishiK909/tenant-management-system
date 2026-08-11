package com.project.tms.securityService.service;


import com.project.tms.securityService.entity.Users;
import com.project.tms.securityService.repository.UsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of Spring Security's {@link UserDetailsService}.
 * Responsible for loading a user's authentication details (email, password, role)
 * from the database so that Spring Security can authenticate and authorize requests.
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    /**
     * Constructor-based injection is used instead of field injection (@Autowired)
     * for better testability and to make dependencies explicit and immutable.
     *
     * @param usersRepository repository used to fetch user data from the database
     */
    public UserDetailServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Loads a user's authentication details by their email (used as the username).
     * Called internally by Spring Security during authentication.
     *
     * @param username the email identifying the user whose data is required
     * @return a {@link UserDetails} object containing the user's email, password,
     *         and role-based authority (prefixed with "ROLE_" as required by
     *         Spring Security's hasRole() convention)
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}