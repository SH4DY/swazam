package ac.tuwien.sa13.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ac.tuwien.sa13.entity.Request;
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
public class RequestController {

	private static final Logger logger = LoggerFactory
			.getLogger(RequestController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private IRequestService requestService;
	
	private PeerManager peerManager;
	
	@Autowired
	private ITransactionService transactionService;

	@RequestMapping(value = "/requests", method = RequestMethod.GET)
	@ResponseBody
	public List<Request> getRequests(Locale locale, Model model,
			@RequestParam(value = "token") String token)
			throws IOException {
		User user = userService.getUserWithToken(token);
		List<Request> requests = new ArrayList<Request>();
		if (user != null) {// In that case, token should be ok
			requests = requestService.getRequests(user);
		}

		return requests;
	}

	//Request call with token and fingerprint
	@RequestMapping(value = "/request", method = RequestMethod.POST)
	@ResponseBody
	public Request postRequest(
			Locale locale,
			Model model,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "fingerprint", defaultValue = "") String fingerprint)
			throws IOException {

		peerManager = PeerManager.getInstance();
		User user = userService.getUserWithToken(token);
		Request request = new Request();

		if (user != null) {// In that case, token should be ok
			request.setUser(user);
			request.setFingerprint(fingerprint);
			request.setDate(new java.sql.Date((new Date().getTime())));

			Transaction transaction = transactionService.allowRequest(user, request);
			if (transaction != null) {
				requestService.add(request);
				transactionService.add(transaction);
				peerManager.issueRequest(request);
				return request;
			}
		}
		return new Request();
	}

	@RequestMapping(value = "/result", method = RequestMethod.POST)
	@ResponseBody
	public Request postResult(Locale locale, Model model,
			@RequestParam(value = "id", required=true) long id,
			@RequestParam(value = "result", required=true) String result,
			@RequestParam(value = "user", required=true) String user) throws IOException {
		
		Request r = requestService.find(id);
		r.setResult(result);
		requestService.update(r);
		
		User u = userService.getUser(user);
		
		Transaction t = transactionService.rewardUser(u, r);
		transactionService.add(t);
		
		return r;
	}
	
//	//Request call with real request entity object
//	@RequestMapping(value = "/request", method = RequestMethod.POST)
//	@ResponseBody
//	public Request postRequestWithEntity(
//			Locale locale,
//			Model model,
//			@RequestParam(value = "token", defaultValue = "") String token,
//			@RequestParam(value = "request") Request request)
//			throws IOException {
//
//		User user = userService.getUserWithToken(token);
//		Request requestGenerated = new Request();
//		
//		if (user != null) {// In that case, token should be ok
//			request.setUser(user);
//			requestService.add(request);
//			//TODO P2P: Here the request is generated, go with it and bring it into the P2P part
//		}
//
//		return request;
//	}
}
