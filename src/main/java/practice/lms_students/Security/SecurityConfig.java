package practice.lms_students.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/courses/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/lessons/course/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/courses/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/courses/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/lessons/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/lessons/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/enrollments").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/enrollments/student/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/enrollments/course/**").hasRole("TEACHER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
        return builder.build();
    }
}
