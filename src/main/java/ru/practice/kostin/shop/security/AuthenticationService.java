package ru.practice.kostin.shop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.practice.kostin.shop.service.dto.product.NewUserDTO;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authManager;

    /**
     * Authenticates user
     * @param userDTO user credentials
     */
    public void authenticateUser(NewUserDTO userDTO){
        //create token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        //authenticate user by token
        Authentication authentication = authManager.authenticate(token);
        //set authentication to context manually
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
