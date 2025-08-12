package com.citadel.userservice.auth;

import com.citadel.userservice.auth.DTO.AuthRequest;
import com.citadel.userservice.auth.DTO.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService =authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AuthRequest request){
        try {
            String message = authService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }
        catch (RuntimeException e){
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthRequest request){
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader){
        if(!authHeader.startsWith("Bearer ")){
            return  ResponseEntity.badRequest().build();
        }

        String refreshToken = authHeader.substring(7);
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return  ResponseEntity.ok(authResponse);
    }

}
