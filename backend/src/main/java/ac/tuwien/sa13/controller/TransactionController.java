package ac.tuwien.sa13.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ac.tuwien.sa13.entity.Transaction;
import ac.tuwien.sa13.entity.User;
import ac.tuwien.sa13.service.IRequestService;
import ac.tuwien.sa13.service.ITransactionService;
import ac.tuwien.sa13.service.IUserService;
import ac.tuwien.sa13.service.PeerManager;

/**
 * Handles requests for the application home page.
 */
@Controller
public class TransactionController {

	private static final Logger logger = LoggerFactory
			.getLogger(TransactionController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private ITransactionService transactionService;

	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	@ResponseBody
	public List<Transaction> getTransactions(Locale locale, Model model,
			@RequestParam(value = "token", defaultValue = "") String token)
			throws IOException {
		User user = userService.getUserWithToken(token);
		List<Transaction> transactions = new ArrayList<Transaction>();
		if (user != null) {// In that case, token should be ok
			transactions = transactionService.getTransactions(user);
		}
		return transactions;
	}


}
