package com.jwt.securitytutorial.service;


import com.jwt.securitytutorial.model.AuthenticationResponse;
import com.jwt.securitytutorial.model.User;
import com.jwt.securitytutorial.model.Role;
import com.jwt.securitytutorial.model.Token;
import com.jwt.securitytutorial.repository.TokenRepository;
import com.jwt.securitytutorial.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthentificationService {
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private AuthenticationManager authenticationManager;
    public AuthentificationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }
   User user = new User();
   user.setFirstName(request.getFirstName());
   user.setLastName(request.getLastName());
   user.setUsername(request.getUsername());
   user.setPassword(passwordEncoder.encode(request.getPassword()));

   user.setRole(request.getRole());
    System.out.println(user.getPassword());
   user = userRepository.save(user);

   String jwt = jwtService.generateToken(user);
   saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User registration was successful");
//   return new AuthenticationResponse(token);

    }

    public AuthenticationResponse authenticate(User request){
     authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(
                     request.getUsername(),
                     request.getPassword()
             )
     );
     User user = userRepository.findByUsername(request.
             getUsername()).orElseThrow();
    String jwt = jwtService.generateToken(user);

//    return new AuthenticationResponse(token);
        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User login was successful");

    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }


    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
