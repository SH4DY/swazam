package ac.tuwien.sa13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ac.tuwien.sa13.entity.User;
import ac.tuwien.sa13.service.IUserService;
import ac.tuwien.sa13.utilities.SessionIdentifierGenerator;
import ac.tuwien.sa13.utilities.Token;
import ac.tuwien.sa13.validator.LoginValidator;

@Controller
public class LoginController {
	
	@Autowired
	LoginValidator val;
	
	@Autowired
	IUserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(final ModelMap modelMap) {
		modelMap.addAttribute(new User());
        return "login"; 
    }
	
	//Used for login on website
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(final ModelMap modelMap, @ModelAttribute("user") User user, BindingResult result ) {
		val.validate(user, result);
		
		if(result.hasErrors()){
			System.out.println("ERRORS in form: " + result.getErrorCount());
			return "login";
			
		}

		//Check credentials
		String name = user.getName();
		User userTemp = userService.getUser(name);
		if(userTemp != null){
			if(userTemp.getPassword().equals(user.getPassword())){
				user = userTemp;
			}else{
				return "badcredentials";
			}
		}else{
			userTemp = new User();
			userTemp.setName(user.getName());
			userTemp.setPassword(user.getPassword());
			user = userTemp;
			userService.add(user);
		}
		
		modelMap.addAttribute("user", user);
        return "user"; 
    }
	
	//Login from App
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@ResponseBody
    public Token authenticate(final ModelMap modelMap, 
    		@RequestParam(value = "username", required=false) String username,
    		@RequestParam(value = "password", required=false) String password) 
			
	{
		
		User user = userService.getUser(username);
		Token token = new Token();
		token.setToken(SessionIdentifierGenerator.nextSessionId());
		if(user != null){
			if (user.getPassword().equals(password)) {
				user.setToken(token.getToken());
				userService.update(user);
			}else {//User exists but password wrong
				return null;
			}
		}else{
			user = new User();
			user.setName(username);
			user.setPassword(password);
			user.setToken(token.getToken());
			userService.add(user);
		}
		
		return token; 
    }
}
