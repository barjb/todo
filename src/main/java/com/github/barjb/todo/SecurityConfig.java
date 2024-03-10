package com.github.barjb.todo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain apiSecufityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            (auth) ->
                auth.requestMatchers(new AntPathRequestMatcher("/api/v1/**")).hasRole("ROLE-USER"))
        .csrf()
        .disable()
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
    User.UserBuilder users = User.builder();
    UserDetails qqq =
        users
            .username("qqq")
            .password(passwordEncoder.encode("qqq"))
            .roles("ROLE-USER", "CARD-OWNER")
            .build();
    UserDetails aaa =
        users
            .username("aaa")
            .password(passwordEncoder.encode("aaa"))
            .roles("ROLE-USER", "CARD-OWNER")
            .build();
    UserDetails zzz =
        users
            .username("zzz")
            .password(passwordEncoder.encode("zzz"))
            .roles("ROLE-USER", "NON-OWNER")
            .build();
    return new InMemoryUserDetailsManager(qqq, aaa, zzz);
  }
}
