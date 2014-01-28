package ac.tuwien.sa13.dao;

import org.springframework.stereotype.Repository;

import ac.tuwien.sa13.entity.Transaction;


@Repository
public class TransactionDAO extends HibernateDAO<Transaction, Long> implements ITransactionDAO {

}
