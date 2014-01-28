package ac.tuwien.sa13.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ac.tuwien.sa13.dao.IRequestDAO;
import ac.tuwien.sa13.dao.IUserDAO;
import ac.tuwien.sa13.entity.Request;
import ac.tuwien.sa13.entity.User;

@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service("requestService")
public class RequestService implements IRequestService {
    
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
    private IRequestDAO requestDAO;
	
	public void add(Request entity) {
		requestDAO.add(entity);		
	}

	public void update(Request entity) {
		requestDAO.update(entity);		
	}

	public void remove(Request entity) {
		requestDAO.remove(entity);		
	}

	public Request find(Long key) {
		return requestDAO.find(key);
	}

	public List<Request> list() {
		return requestDAO.list();
	}

	@Override
	public List<Request> getRequests(User user) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Request WHERE user_id= :user_id");
		query.setParameter("user_id", user.getId());
		
		List<Request> l = query.list();
		
		if (l.isEmpty()) {
			return null;
		}
		return  l;
	}

}
