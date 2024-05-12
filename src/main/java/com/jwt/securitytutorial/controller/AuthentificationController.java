package com.jwt.securitytutorial.controller;

import com.jwt.securitytutorial.model.AuthenticationResponse;
import com.jwt.securitytutorial.model.User;
import com.jwt.securitytutorial.service.AuthentificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthentificationController {
    private final AuthentificationService authentificationService;

    public AuthentificationController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request
    ){
     return  ResponseEntity.ok(authentificationService.register(request));
    }

//    @PostMapping("/login"){
//        public ResponseEntity<AuthenticationResponse> login(
//                @RequestBody User request){
//
//        }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ){

  return ResponseEntity.ok(authentificationService.authenticate(request));
    }

}
