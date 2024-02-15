package telran.probes.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.probes.repo.AccountRepo;
import telran.security.accounting.dto.AccountDto;
import telran.security.accounting.model.Account;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountProviderServiceImpl implements AccountProviderService {
	final AccountRepo accountRepo;

	@Override
	public AccountDto getAccount(String email) {
		Account account = accountRepo.findById(email)
				.orElseThrow(() -> new NotFoundException(String.format("account %d not found", email)));
		return account.build();
	}

}