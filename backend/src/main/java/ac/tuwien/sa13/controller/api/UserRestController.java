package ac.tuwien.sa13.controller.api;

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

import ac.tuwien.sa13.entity.User;
import ac.tuwien.sa13.service.IUserService;

@Controller
@RequestMapping("api")
public class UserRestController {

	@Autowired
	private IUserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@RequestMapping(value = "user/{id}", method = RequestMethod.GET)
	@ResponseBody
	public User get(@PathVariable Long id, Locale locale, Model model) throws IOException {
		//logger.info("I�m the first controller");
		
		User u = userService.find(id);	
		return u;
	}
	
	@RequestMapping(value = "user", method = RequestMethod.GET)
	@ResponseBody
	public List<User> list(Locale locale, Model model) throws IOException {
		//logger.info("I�m the first controller");
		
		List<User> l = userService.list();	
		return l;
	}
	/**
	 * 
	 * @param user
	 * @param locale
	 * @param model
	 */
	@RequestMapping(value = "user/add", method = RequestMethod.POST)
	@ResponseBody
	public void add(@RequestBody User user, Locale locale, Model model) {		
		userService.add(user);
	}
	
	@RequestMapping(value = "user/update", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody User user, Locale locale, Model model) {
		userService.update(user);
	}
}
