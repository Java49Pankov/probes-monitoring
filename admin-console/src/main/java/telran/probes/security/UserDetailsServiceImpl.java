package telran.probes.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.security.accounting.dto.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	@Value("${app.account.provider.host:localhost}")
	String host;
	@Value("${app.account.provider.port:8989}")
	int port;
	@Value("${app.account.provider.url:/accounts}")
	String url;
	final RestTemplate restTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AccountDto account = getAccount(username);
		String[] roles = Arrays.stream(account.roles())
				.map(role -> "ROLE_" + role).toArray(String[]::new);
		log.debug("username: {}, password: {}, roles: {}",
				account.email(), account.password(), Arrays.deepToString(roles));
		return new User(account.email(), account.password(),
				AuthorityUtils.createAuthorityList(roles));
	}

	private AccountDto getAccount(String username) {
		AccountDto res = null;
		try {
			ResponseEntity<?> responseEntity = restTemplate.exchange(getFullUrl(username), HttpMethod.GET, null,
					AccountDto.class);
			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				throw new Exception((String) responseEntity.getBody());
			}
			res = (AccountDto) responseEntity.getBody();
		} catch (Exception e) {
			log.error("no account provided for user {}, reason: {}",
					username, e.getMessage());

		}
		log.debug("Account for username {} is {}", username, res);
		return res;
	}

	private String getFullUrl(String username) {
		String res = String.format("http://%s:%d%s/%s",
				host, port, url, username);
		log.debug("url:{}", res);
		return res;
	}

}