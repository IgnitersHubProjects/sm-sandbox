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

    @RequestMapping("/login")
    public String random(Model model) {
        return "redirect:/getlogin";
    }

    @RequestMapping("/home")
    public String getHome(Model model) throws IOException {
        // ! If API token not present it will get
        iiflService.saveIIFLtoken();
        iiflService.saveInstrumentData();
        return "home";
    }

    @RequestMapping("/getlogin")
    public String getlogin(Model model) {
        return "login";
    }

    @RequestMapping("/getregister")
    public String getRegister(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/postregister")
    public String registerUser(@ModelAttribute User user) {

        System.out.println("inside postregister");
        user.setRoles(Arrays.asList(
                new Role("ROLE_USER")));
        if (userService.registerUser(user)) {
            return "redirect:/getlogin";

        } else {
            return "redirect:/getregister";
        }

    }

    // !temp file
    @RequestMapping("/temp")
    public String gettemp(Model model) throws JsonMappingException, JsonProcessingException {

        return "temp";
    }


}
