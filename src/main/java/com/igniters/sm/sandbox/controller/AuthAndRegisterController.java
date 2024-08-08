package com.igniters.sm.sandbox.controller;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.igniters.sm.sandbox.model.Role;
import com.igniters.sm.sandbox.model.User;
import com.igniters.sm.sandbox.service.AuthAndRegisService;

@Controller
public class AuthAndRegisterController {

    @Autowired
    private AuthAndRegisService userService;

    // getLogin
    // postLogin

    // getregister
    // postregister
    @RequestMapping("/login")
    public String random(Model model) {
        return  "redirect:/getlogin";
    }


    @RequestMapping("/home")
    public String getHome(Model model) {
        return "home";
    }

    @RequestMapping("/getlogin")
    public String getlogin(Model model) {
        return "login";
    }


    @RequestMapping("/getregister")
    public String getRegister(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }


    @PostMapping("/postregister")
    public String registerUser(@ModelAttribute User user) {

        System.out.println("inside postregister");     
        user.setRoles(Arrays.asList(
            new Role("ROLE_USER")
        ));
        if (userService.registerUser(user)) {
            return "redirect:/getlogin";
           
        } else {
            return "redirect:/getregister";
        }

    }



    //temp file
    @RequestMapping("/temp")
    public String gettemp(Model model) {
        return "temp";
    }

}
