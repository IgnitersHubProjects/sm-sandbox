package com.igniters.sm.sandbox.controller;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.igniters.sm.sandbox.model.Role;
import com.igniters.sm.sandbox.model.User;
import com.igniters.sm.sandbox.service.AuthAndRegisService;
import com.igniters.sm.sandbox.service.IIFLService;

@Controller
public class AuthAndRegisterController {

    @Autowired
    private AuthAndRegisService userService;

    @Autowired
    private IIFLService iiflService;

    @RequestMapping("/home")
    public String getHome(Model model) throws IOException {
        // ! check weather token and Instruments present or not if not it load data and generate token too.
        iiflService.checkIIFLtoken();
        iiflService.checkInstrumentData();      
        return "home";
    }

    @RequestMapping("/login")
    public String getlogin(Model model) {
        return "login";
    }

    @RequestMapping("/register")
    public String getRegister(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute User user) {
        // System.out.println("inside registerUser");
        user.setRoles(Arrays.asList(
                new Role("ROLE_USER")));
        if (userService.registerUser(user)) {
            return "redirect:/login";

        } else {
            return "redirect:/register";
        }
    }

}
