package telran.probes.controller;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.service.AccountProviderService;
import telran.security.accounting.dto.AccountDto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountProviderController {
	final AccountProviderService accountingService;

	@GetMapping("${app.account.provider.url:accounts}" + "/{email}")
	AccountDto getAccount(@PathVariable String email) {
		return accountingService.getAccounts(email);
	}
}