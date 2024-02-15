package telran.security.accounting.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import telran.security.accounting.dto.AccountDto;

@Document(collection = "accounts")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {
	@Id
	String email;
	String hashPassword;
	String[] roles;

	public static Account of(AccountDto accountDto) {
		return new Account(accountDto.email(), accountDto.password(), accountDto.roles());
	}

	public AccountDto build() {
		return new AccountDto(email, hashPassword, roles);
	}
}