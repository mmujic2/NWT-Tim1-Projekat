package the.convenient.foodie.restaurant.security.filters;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig{

    private final JwtValidationFilter jwtFilter;

    public WebSecurityConfig(JwtValidationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .permitAll()
                .and().exceptionHandling()
                // setting custom access denied handler for not authorized request
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);



        return http.build();
    }



}
