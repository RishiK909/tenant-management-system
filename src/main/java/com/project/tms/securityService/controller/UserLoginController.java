package com.project.tms.securityService.controller;


import com.project.tms.commonService.dto.ApiResponse;
import com.project.tms.securityService.dto.UserRegisterDTO;
import com.project.tms.securityService.entity.Users;
import com.project.tms.securityService.enums.Status;
import com.project.tms.securityService.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class UserLoginController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserLoginController(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody UserRegisterDTO request) {

        if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>("Email already exists", false));
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStatus(Status.Active);

        usersRepository.save(user);

        return ResponseEntity
                .ok(new ApiResponse<>("User registered successfully", true));
    }

}

