//package com.example.SpirngSecEx.configuration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(customizer -> customizer.disable())
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/register/**").permitAll()
//                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
//                        .requestMatchers("/supporter/**", "/project/details/**").hasAuthority("SUPPORTER")
//                        .requestMatchers("/creator/**","/project/new").hasAuthority("CREATOR")
//                        .anyRequest().authenticated())
//                //Enable form Login for browser
//                .formLogin(Customizer.withDefaults())
//                // for http request
//                .httpBasic(Customizer.withDefaults())
//                .logout(Customizer.withDefaults())
//                .sessionManagement(session ->
//                        // IF_REQUIRED (Προεπιλεγμένη επιλογή): Το Spring Security θα δημιουργήσει ένα HttpSession μόνο αν είναι απαραίτητο (π.χ., όταν ένας χρήστης συνδέεται με formLogin).
//                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }
//}

package com.example.SpirngSecEx.configuration;

import com.example.SpirngSecEx.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private AuthEntryPointJwt unauthorizedHandler;


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean (AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://20.238.52.1:3000" ,"http://localhost:3000", "http://vue.kaparos.ip-ddns.com", "http://spring.kaparos.ip-ddns.com", "http://4.223.160.109:80", "http://172.161.49.151"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> corsConfiguration))
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**","/api/home", "/api/supporters/**", "/actuator/health/**", "/actuator/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/v2/api-docs/**",
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/project/new").hasAuthority("CREATOR")
                        .requestMatchers("/api/creator/**").hasAuthority("CREATOR")
                        .requestMatchers("/api/project/details/**").hasAuthority("SUPPORTER")
                        .requestMatchers("/api/project/support/**").hasAuthority("SUPPORTER")
                        .anyRequest().authenticated()
                )
                //DEN EXOUME SESSION den exoume login
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

//    //UPAF -> User Password Authentication Filter
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
//        provider.setUserDetailsService(myUserDetailsService);
//        return provider;
//    }
}
