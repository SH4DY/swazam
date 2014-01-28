package ac.tuwien.sa13.service;

import java.util.List;

import ac.tuwien.sa13.entity.Request;
import ac.tuwien.sa13.entity.User;

public interface IRequestService extends IGenericService<Request, Long> {
 
	List<Request> getRequests(User user);
}
