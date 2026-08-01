package com.project.tms.securityService.service;

import com.project.tms.commonService.dto.ApiResponse;
import com.project.tms.commonService.dto.AuthResponseDTO;
import com.project.tms.securityService.dto.LoginRequestDTO;
import com.project.tms.securityService.dto.UserRegisterDto;
import com.project.tms.securityService.entity.Users;
import com.project.tms.securityService.enums.Status;
import com.project.tms.securityService.repository.UsersRepository;
import com.project.tms.securityService.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UsersRepository usersRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user after validating that the email and phone number
     * are unique. The user's password is securely encoded before being saved.
     *
     * @param request the registration request containing the user's details
     * @return an {@code ApiResponse} indicating whether the registration was
     *         successful or the reason for failure
     */
    @Override
    public ApiResponse<Void> register(UserRegisterDto request) {

        if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse<>("Email already exists", false);
        }

        if (usersRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            return new ApiResponse<>("Phone number already exists", false);
        }

        Users user = new Users();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        /**
         * Encode the raw password. Generally, a good encoding algorithm applies a
         * SHA-1 or greater hash combined with an 8-byte or greater randomly generated salt.
         */
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());
        user.setStatus(Status.Active);

        usersRepository.save(user);

        return new ApiResponse<>("User registered successfully", true);
    }



    /**
     * Authenticates a user using the provided login credentials and generates
     * a JWT token upon successful authentication.
     *
     * @param request the login request containing the user's email and password
     * @return an {@code ApiResponse} containing the authentication result and
     *         JWT token if the login is successful
     */
    @Override
    public ApiResponse<AuthResponseDTO> login(LoginRequestDTO request) {

        Optional<Users> userOptional = usersRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new ApiResponse<>("Invalid email or password", false);
        }

        Users user = userOptional.get();

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            return new ApiResponse<>("Invalid email or password", false);
        }

        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole().name()
        );

        AuthResponseDTO authData = new AuthResponseDTO(
                token,
                jwtUtil.extractExpiration(token),
                user.getUserId(),
                user.getUserName(),
                user.getRole().name()
        );

        return new ApiResponse<>("Login successful", true, authData);
    }



    /**
     * Retrieves the details of the currently authenticated user based on
     * the provided email address.
     *
     * @param email the email address of the authenticated user
     * @return an {@code ApiResponse} containing the current user's details
     *         if found, or an appropriate error message otherwise
     */
    @Override
    public ApiResponse<AuthResponseDTO> getCurrentUser(String email) {

        Optional<Users> userOptional = usersRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return new ApiResponse<>("User not found", false);
        }

        Users user = userOptional.get();

        AuthResponseDTO data = new AuthResponseDTO(
                null,
                null,
                user.getUserId(),
                user.getUserName(),
                user.getRole().name()
        );

        return new ApiResponse<>("Current user fetched successfully", true, data);
    }
}
