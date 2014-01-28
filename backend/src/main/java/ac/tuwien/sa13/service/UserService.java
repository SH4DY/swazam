package ac.tuwien.sa13.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ac.tuwien.sa13.dao.IUserDAO;
import ac.tuwien.sa13.entity.User;

@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service("userService")
public class UserService implements IUserService {
    
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
    private IUserDAO userDAO;
	
	public void add(User entity) {
		userDAO.add(entity);		
	}

	public void update(User entity) {
		userDAO.update(entity);		
	}

	public void remove(User entity) {
		userDAO.remove(entity);		
	}

	public User find(Long key) {
		return userDAO.find(key);
	}

	public List<User> list() {
		return userDAO.list();
	}

	@Override
	public User getUser(String name) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM User WHERE name= :name");
		query.setParameter("name", name);
		
				
		List l = query.list();
		
		if (l.isEmpty()) {
			return null;
		}
		return (User) l.get(0);
	}
	
	public User getUserWithToken(String token){
		Query query = sessionFactory.getCurrentSession().createQuery("FROM User WHERE token= :token");
		query.setParameter("token", token);
				
		List l = query.list();
		
		if (l.isEmpty()) {
			return null;
		}
		return (User) l.get(0);
	}

}
