package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.security.accounting.model.Account;

public interface AccountRepo extends MongoRepository<Account, String> {

}