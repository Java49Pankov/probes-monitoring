package telran.probes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfiguration {
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors(custom -> custom.disable());
		http.csrf(custom -> custom.disable());
		http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}

}