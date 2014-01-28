package ac.tuwien.sa13.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ac.tuwien.sa13.dao.IRequestDAO;
import ac.tuwien.sa13.dao.ITransactionDAO;
import ac.tuwien.sa13.dao.IUserDAO;
import ac.tuwien.sa13.entity.Request;
import ac.tuwien.sa13.entity.Transaction;
import ac.tuwien.sa13.entity.User;

@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service("transactionService")
public class TransactionService implements ITransactionService {
    
	private static final int REQUEST_COST = 1;

	private static final int REWARD = 0;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
    private ITransactionDAO transactionDAO;
	
	@Autowired
	private IUserService userService;
	
	public void add(Transaction entity) {
		transactionDAO.add(entity);		
	}

	public void update(Transaction entity) {
		transactionDAO.update(entity);		
	}

	public void remove(Transaction entity) {
		transactionDAO.remove(entity);		
	}

	public Transaction find(Long key) {
		return transactionDAO.find(key);
	}

	public List<Transaction> list() {
		return transactionDAO.list();
	}

	/**
	 * This method checks if a user is allowed to make a request (enough tokens on his account)
	 * At the same time the method subtracts the amount of token for one request, given he
	 * can afford it.
	 */
	@Override
	public Transaction allowRequest(User user, Request request) {
		if(user != null && user.getTokens() > 0){
			int temp = user.getTokens() - REQUEST_COST;
			user.setTokens(temp);
			userService.update(user);
			Transaction transaction = new Transaction();
			transaction.setIsReward(false);
			transaction.setUser(user);
			transaction.setRequest(request);
			return transaction;
		}
		return null;
	}
	
	/**
	 * This method shall be called when a peer delivers a result for a request. The user
	 * with whom the peer is authenticated is rewarded with the defined reward.
	 * @param user
	 * @param request
	 * @return The transaction (contains user and request info) but has to be saved! Call userService.add(transaction)
	 */
	@Override
	public Transaction rewardUser(User user, Request request){
		Transaction transaction = new Transaction();
		transaction.setUser(user);
		transaction.setRequest(request);
		int temp = user.getTokens();
		user.setTokens(temp + REWARD);
		userService.update(user);
		return transaction;
	}
	@Override
	public List<Transaction> getTransactions(User user) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Transaction WHERE user_id= :user_id");
		query.setParameter("user_id", user.getId());
		
		List<Transaction> l = query.list();
		
		if (l.isEmpty()) {
			return null;
		}
		return  l;
	}

}
