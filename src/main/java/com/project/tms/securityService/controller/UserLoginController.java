package com.project.tms.securityService.controller;


import com.project.tms.commonService.dto.ApiResponse;
import com.project.tms.commonService.dto.AuthResponseDTO;
import com.project.tms.securityService.dto.LoginRequestDTO;
import com.project.tms.securityService.dto.UserRegisterDto;
import com.project.tms.securityService.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserLoginController {

    private final AuthService authService;

    public UserLoginController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing the user's information
     * @return a {@code ResponseEntity} containing the registration result wrapped
     *         in an {@code ApiResponse<Void>}
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterDto request) {
        ApiResponse<Void> response = authService.register(request);
        return response.isStatus()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    /**
     * Authenticates a user using the provided login credentials.
     *
     * @param request the login request containing the user's credentials
     * @return a {@code ResponseEntity} containing the authentication result wrapped
     *         in an {@code ApiResponse<AuthResponseDTO>}
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        ApiResponse<AuthResponseDTO> response = authService.login(request);
        return response.isStatus()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    /**
     * Validates and returns the currently authenticated user.
     *
     * @param authentication the authenticated user's security context
     * @return the current user's details wrapped in an ApiResponse
     */
    @GetMapping("/current-user")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        ApiResponse<AuthResponseDTO> response = authService.getCurrentUser(email);
        return response.isStatus()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }


}