package ac.tuwien.sa13.controller;

import java.io.IOException;
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
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRequestService requestService;
	
	@Autowired
	private ITransactionService transactionService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws IOException {
		
		PeerManager peerManager = PeerManager.getInstance();
		return "home";	
	}
	
	@RequestMapping(value = "/test_user_request", method = RequestMethod.GET)
	public String test_user_request(Locale locale, Model model) throws IOException {
		
		User user = new User();
		user.setName("admin");
		user.setPassword("password");
		
		Request req1 = new Request();
		req1.setFingerprint("adsf");
		req1.setUser(user);
		Request req2 = new Request();
		req2.setFingerprint("asdfasdfafsd");
		req2.setUser(user);
		
		userService.add(user);
		requestService.add(req1);
		requestService.add(req2);
		
		User userFromDB = userService.getUser("admin");
		List<Request> requestsFromDB = requestService.getRequests(user);
		
		System.out.println(userFromDB.getPassword());
		System.out.println(requestsFromDB.get(0).getUser().getId());
		//System.out.println("ENDE" + user.toString() + requestsFromDB.get(0).toString());
//		
//				
		return "home";	
	}
	
	@RequestMapping("resttesting")
    @ResponseBody
    public User restTest() {
		 
		 User u = userService.getUser("max");
		 return u;
    }
}
