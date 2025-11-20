package az.devlab.aiquestiongenerator.service;


import az.devlab.aiquestiongenerator.dto.JwtAuthenticationResponse;
import az.devlab.aiquestiongenerator.dto.LoginRequest;
import az.devlab.aiquestiongenerator.dto.RegisterRequest;

public interface AuthService {

    JwtAuthenticationResponse login(LoginRequest request);

    JwtAuthenticationResponse register(RegisterRequest request);

    JwtAuthenticationResponse refreshToken(String refreshToken);

    void logout(String accessToken);
}
