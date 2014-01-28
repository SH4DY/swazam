package at.saws2013.szazam.entities;

import org.json.JSONException;
import org.json.JSONObject;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.saws2013.szazam.R;

public class Request implements IListItem {
	
	private Long id;
	private String fingerprint;
	private String result;
	private String user = null;
	private String date;
	
	public Request(Long id, String fingerprint, String result, String date){
		this.id = id;
		this.fingerprint = fingerprint;
		try {
			JSONObject resultJson = new JSONObject(result);
			this.result = resultJson.optString("title") + " - " + resultJson.optString("artist") + " - " + resultJson.optString("album");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	
	public void setFingerprint(Fingerprint fingerprint){
		this.fingerprint = fingerprint.toString();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public int getViewType() {
		return ListItemTypes.REQUEST.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater,
			View convertView, ViewGroup parent) {
		RequestViewholder viewholder;
		if (convertView == null){
			convertView = inflater.inflate(R.layout.listitem_request, parent, false);
		}
		viewholder = (RequestViewholder) convertView.getTag();
		if (viewholder == null){
			viewholder = new RequestViewholder();
			viewholder._date = (TextView) convertView.findViewById(R.id.tx_date);
			viewholder._result = (TextView) convertView.findViewById(R.id.tx_result);
		}
		
		viewholder._date.setText(date);
		viewholder._result.setText(result != null ? result : "no result");
		
		
		return convertView;
	}
	
	class RequestViewholder{
		TextView _date, _result;
	}

}
