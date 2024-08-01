package com.igniters.sm.sandbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igniters.sm.sandbox.model.User;
import com.igniters.sm.sandbox.service.UserService;

@RestController
// @RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    
    @RequestMapping("hello")
    public String hello(){
        return "Hello";
    }

    @PostMapping("/register")
    ResponseEntity<String> registerUser(@RequestBody User user){
         

        if(userService.registerUser(user)){
            return ResponseEntity.ok("User registered successfully");
        }
        else{
            return ResponseEntity.badRequest().body("User registration failed");
        }


    }



    @RequestMapping("/home")
    public String home() {
        return "Home Page";
    }



     
}
