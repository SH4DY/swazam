package at.saws2013.szazam.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * General Interface for items which can be displayed
 * in a listview.
 * 
 * @author René
 *
 */
public interface IListItem {
	
	public int getViewType();	
	/**
	 * Return a "prefilled" view which can then be used in 
	 * the CustomListAdapter
	 * @param inflater
	 * @param convertView
	 * @param parent
	 * @return	
	 */
	public View getView(LayoutInflater inflater, View convertView, ViewGroup parent);

}
