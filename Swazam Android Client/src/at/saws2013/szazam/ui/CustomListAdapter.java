package at.saws2013.szazam.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import at.saws2013.szazam.entities.IListItem;
import at.saws2013.szazam.entities.ListItemTypes;

/**
 * Custom ArrayAdapter to be used within the HistoryActivity
 * to display either requests or transactions, could also be
 * used to display both
 * 
 * @author René
 *
 */
public class CustomListAdapter extends ArrayAdapter<IListItem> {
	
	private LayoutInflater inflater;

	public CustomListAdapter(Context context, int resource,
			List<IListItem> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItem(position).getView(inflater, convertView, parent);
	}
	
	@Override
    public int getViewTypeCount() {
        return ListItemTypes.values().length;
    }
	
	@Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }
	
	

}
