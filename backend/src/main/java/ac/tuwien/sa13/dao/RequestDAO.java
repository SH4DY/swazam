package ac.tuwien.sa13.dao;

import org.springframework.stereotype.Repository;

import ac.tuwien.sa13.entity.Request;
import ac.tuwien.sa13.entity.User;


@Repository
public class RequestDAO extends HibernateDAO<Request, Long> implements IRequestDAO {

}
