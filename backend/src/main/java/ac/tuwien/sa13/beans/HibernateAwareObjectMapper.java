package ac.tuwien.sa13.beans;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;


public class HibernateAwareObjectMapper extends ObjectMapper {
  
    /**
     * Register module to support lazy loaded items
     */
	public HibernateAwareObjectMapper() {
        registerModule(new Hibernate4Module());
    }

}
