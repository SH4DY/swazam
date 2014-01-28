package at.saws2013.szazam.store;

import java.util.List;

import org.json.JSONArray;

import at.saws2013.szazam.entities.IListItem;
import at.saws2013.szazam.entities.Request;

public interface IRequestStore {
	
	List<IListItem> getRequestHistory();
	void storeRequest(Request request);
	List<IListItem> transformToList(JSONArray responseJSON);

}
