package com.dzasek.springbootcustomauth.config;

import com.dzasek.springbootcustomauth.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/pass").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/home")
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/home?message=success")
                .failureUrl("/home?message=failure")
                .usernameParameter("customname").passwordParameter("custompassword")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home?message=loggedout");
    }

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

}
