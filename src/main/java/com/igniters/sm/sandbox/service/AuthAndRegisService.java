package com.igniters.sm.sandbox.service;

import com.igniters.sm.sandbox.model.User;

public interface AuthAndRegisService {

   
    //! for form validating 
    //boolean isValidDetails(User user);

    //!for register new user
    boolean registerUser(User user);
}