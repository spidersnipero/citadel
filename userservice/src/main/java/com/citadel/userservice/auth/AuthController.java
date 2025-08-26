package com.citadel.userservice.auth;

import com.citadel.userservice.DTO.AuthRequestDTO;
import com.citadel.userservice.DTO.AuthResponseDTO;
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
    public ResponseEntity<String> registerUser(@Valid @RequestBody AuthRequestDTO request){
        try {
            String message = authService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }
        catch (RuntimeException e){
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> signIn(@Valid @RequestBody AuthRequestDTO request){
        AuthResponseDTO response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestHeader("Authorization") String authHeader){
        if(!authHeader.startsWith("Bearer ")){
            return  ResponseEntity.badRequest().build();
        }

        String refreshToken = authHeader.substring(7);
        AuthResponseDTO authResponseDTO = authService.refreshToken(refreshToken);
        return  ResponseEntity.ok(authResponseDTO);
    }

}
