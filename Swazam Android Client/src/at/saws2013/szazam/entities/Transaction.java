package at.saws2013.szazam.entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.saws2013.szazam.R;

public class Transaction implements IListItem {
	
	private String date;
	private Boolean isReward;
	
	public Transaction(String date, Boolean isReward) {
		super();
		this.date = date;
		this.isReward = isReward;
	}

	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public Boolean isReward() {
		return isReward;
	}


	public void setReward(Boolean isReward) {
		this.isReward = isReward;
	}


	@Override
	public int getViewType() {
		return ListItemTypes.TRANSACTION.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater,
			View convertView, ViewGroup parent) {
		RequestViewholder viewholder;
		if (convertView == null){
			convertView = inflater.inflate(R.layout.listitem_transaction, parent, false);
		}
		viewholder = (RequestViewholder) convertView.getTag();
		if (viewholder == null){
			viewholder = new RequestViewholder();
			viewholder._date = (TextView) convertView.findViewById(R.id.tx_date);
			viewholder._value = (TextView) convertView.findViewById(R.id.tx_value);
		}
		
		viewholder._date.setText(date);
		viewholder._value.setText("is reward: " + isReward);
		
		
		return convertView;
	}
	
	class RequestViewholder{
		TextView _date, _value;
	}
}
