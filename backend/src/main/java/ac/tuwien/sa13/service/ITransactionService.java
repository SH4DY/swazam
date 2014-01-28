package ac.tuwien.sa13.service;

import java.util.List;

import ac.tuwien.sa13.entity.Request;
import ac.tuwien.sa13.entity.Transaction;
import ac.tuwien.sa13.entity.User;

public interface ITransactionService extends IGenericService<Transaction, Long> {
 
	Transaction allowRequest(User user, Request request);
	Transaction rewardUser(User user, Request request);
	List<Transaction> getTransactions(User user);
}
