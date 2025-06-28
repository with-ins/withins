package com.withins.api.controller;

import com.withins.core.signup.RequestSignup;
import com.withins.core.signup.ResponseSignup;
import com.withins.core.user.service.SignupService;
import com.withins.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api/v1/signup")
//@RequiredArgsConstructor
//public class SignupController {
//
//    private final UserService userService;
//    private final SignupService signupService;
//
//    @GetMapping("/exists/username")
//    public ResponseEntity<Boolean> existsUsername(@RequestParam String username) {
//        boolean exists = userService.readByUsername(username).isPresent();
//        return ResponseEntity.ok(exists);
//    }
//
//    @GetMapping("/exists/email")
//    public ResponseEntity<Boolean> existsEmail(@RequestParam String email) {
//        boolean exists = userService.readByEmail(email).isPresent();
//        return ResponseEntity.ok(exists);
//    }
//
//    @PostMapping
//    public ResponseEntity<ResponseSignup> signup(@Validated @RequestBody RequestSignup requestSignup, BindingResult result) {
//        if (result.hasErrors()) {
//            return ResponseEntity.badRequest().body(ResponseSignup.error(result));
//        }
//        ResponseSignup signup = signupService.signup(requestSignup);
//        return  ResponseEntity.ok(signup);
//    }
//
//}
