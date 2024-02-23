package sii.ms_corrector.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {	
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// We don't need CSRF for this example
		http
		.cors()
		.and()
		.csrf().disable()
				// dont authenticate this particular request
		        .authorizeHttpRequests()
		        .requestMatchers("/register", "/login", "/healthcheck", "/")
		        .permitAll()
		        .anyRequest()
		        .authenticated()
		        .and()
				// make sure we use stateless session; session won't be used to
				// store user's state.
				.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);		
		return http.build();
	}

}
