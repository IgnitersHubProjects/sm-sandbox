package com.igniters.sm.sandbox.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

      http.authorizeHttpRequests(authorize -> {
     
         authorize.requestMatchers("/getregister","/getlogin","/postregister").permitAll();
         authorize.anyRequest().authenticated();
      });

      http.formLogin(formLogin ->{
        formLogin.loginPage("/login")
                 .loginProcessingUrl("/authenticate")
                 .successForwardUrl("/home") 
                 .failureForwardUrl("/getlogin")
                 .usernameParameter("email")
                 .passwordParameter("password");
      });

      http.csrf(AbstractHttpConfigurer :: disable);

      http.logout(logoutForm -> {
        logoutForm.logoutUrl("/logout"); 
        logoutForm.logoutSuccessUrl("/login");
  
      });
    
      return http.build();
        
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

}
