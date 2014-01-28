package at.saws2013.szazam.store;

import java.util.List;

import org.json.JSONArray;

import at.saws2013.szazam.entities.IListItem;

public interface ITransactionStore {
	List<IListItem> getTransactionHistory();
	void storeTransactions(JSONArray transactions);
	List<IListItem> transformToList(JSONArray responseJSON);
}
