package ac.tuwien.sa13.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import ac.tuwien.sa13.entity.User;
import ac.tuwien.sa13.service.IUserService;
import ac.tuwien.sa13.service.UserService;

@Component
public class LoginValidator implements Validator {

	@Autowired
	IUserService userService;
	
	@Override
	public boolean supports(Class<?> arg0) {
		return arg0.equals(ac.tuwien.sa13.entity.User.class);
	}

	@Override
	public void validate(Object command, Errors errors) {
		User user = (User) command;
		
		ValidationUtils.rejectIfEmpty(errors, "name", "user.missingUsername", "Username must be given");
		ValidationUtils.rejectIfEmpty(errors, "password", "user.missingPassword", "Password must be given");
		
	}

}
