package com.jwt.securitytutorial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {
    private String token;
    private String message;

//    public AuthenticationResponse(String token, String message) {
//        this.token = token;
//        this.message = message;
//    }
}
