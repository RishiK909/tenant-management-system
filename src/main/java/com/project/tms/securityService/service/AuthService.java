package com.project.tms.securityService.service;


import com.project.tms.commonService.dto.ApiResponse;
import com.project.tms.commonService.dto.AuthResponseDTO;
import com.project.tms.securityService.dto.LoginRequestDTO;
import com.project.tms.securityService.dto.UserRegisterDto;

public interface AuthService {

    ApiResponse<Void> register(UserRegisterDto request);

    ApiResponse<AuthResponseDTO> login(LoginRequestDTO request);

    ApiResponse<AuthResponseDTO> getCurrentUser(String email);

}
