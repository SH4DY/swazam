package ac.tuwien.sa13.dao;

import org.springframework.stereotype.Repository;

import ac.tuwien.sa13.entity.User;


@Repository
public class UserDAO extends HibernateDAO<User, Long> implements IUserDAO {

}
