package ac.tuwien.sa13.dao;

import java.util.List;
/**
 * Generic DAO
 *
 * @param <E>
 * @param <K>
 */
public interface IGenericDAO<E, K> {
    
	void add(E entity);
	
	void update(E entity);
	
	void remove(E entity);
	
	E find(K key);
    
	List<E> list();
}
