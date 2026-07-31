package com.project.tms.securityService.controller;


import com.project.tms.commonService.dto.ApiResponse;
import com.project.tms.commonService.dto.AuthResponseDTO;
import com.project.tms.securityService.dto.LoginRequestDTO;
import com.project.tms.securityService.dto.UserRegisterDto;
import com.project.tms.securityService.entity.Users;
import com.project.tms.securityService.enums.Status;
import com.project.tms.securityService.repository.UsersRepository;
import com.project.tms.securityService.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class UserLoginController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserLoginController(UsersRepository usersRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register User
     *
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@Valid @RequestBody UserRegisterDto request) {

        if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.ok().body(new ApiResponse<>("Email already exists", false));
        }

        if (usersRepository.existsByUserName(request.getUserName())) {
            return ResponseEntity.ok().body(new ApiResponse<>("User already exists", false));
        }

        Users user = new Users();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        /**
         * Encode the raw password. Generally, a good encoding algorithm applies a
         * SHA-1 or greater hash combined with an 8-byte or greater randomly generated salt.
         */
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStatus(Status.Active);

        Users savedUser = usersRepository.save(user);

        /*String token = jwtUtil.generateToken(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );*/

        AuthResponseDTO authData = new AuthResponseDTO(savedUser.getUserId(), savedUser.getUserName(), savedUser.getRole().name());

        return ResponseEntity.ok(new ApiResponse<>("User registered successfully", true, authData));
    }


    /**
     * Login User
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {

        Optional<Users> userOptional = usersRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid email or password", false));
        }

        Users user = userOptional.get();

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid email or password", false));
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getUserName(), user.getEmail(), user.getRole().name());

        AuthResponseDTO authData = new AuthResponseDTO(token, jwtUtil.extractExpiration(token), user.getUserId(), user.getUserName(), user.getRole().name());

        return ResponseEntity.ok(new ApiResponse<>("Login successful", true, authData));
    }


}