package ac.tuwien.sa13.service;

import ac.tuwien.sa13.entity.User;

public interface IUserService extends IGenericService<User, Long> {
 
	User getUser(String name);
	User getUserWithToken(String token);
}
