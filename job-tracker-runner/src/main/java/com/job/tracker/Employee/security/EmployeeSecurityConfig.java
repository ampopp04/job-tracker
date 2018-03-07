package com.job.tracker.Employee.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

/**
 * The <class>SystemSecurityConfiguration</class> defines
 * the default employee security configuration for this project
 *
 * @author Andrew
 */
@Configuration
public class EmployeeSecurityConfig {

    /**
     * Intercept user login and create/update employee entry
     *
     * @param userDetailsService - the service for retrieving a security user entry
     */
    @Primary
    @Bean
    public LdapUserDetailsMapper systemSecurityUserDetailsLdapContextMapper(@Autowired UserDetailsService userDetailsService) {
        return new EmployeeSecurityUserDetailsContextMapper(userDetailsService);
    }

}
