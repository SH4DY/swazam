package at.saws2013.szazam.store.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;
import at.saws2013.szazam.App;
import at.saws2013.szazam.entities.IListItem;
import at.saws2013.szazam.entities.Request;
import at.saws2013.szazam.entities.Transaction;
import at.saws2013.szazam.store.ITransactionStore;

/**
 * Singleton class which stores and gets transactions
 * from shared preferences
 * 
 * @author René
 *
 */
public class TransactionStore extends ContextWrapper implements ITransactionStore {
	
	private static final String TRANSACTION = "transactions";
	private volatile static TransactionStore instance;
	private SharedPreferences prefs;
	private Gson gson;
	
	private TransactionStore(Context context){
		super(context);
		gson = new Gson();
	}
	
	public static TransactionStore getInstance(Context context){
		if (instance == null){
			synchronized (TransactionStore.class) {
				if (instance == null){
					instance = new TransactionStore(context);
				}
			}
		}
		return instance;
	}

	@Override
	public List<IListItem> getTransactionHistory() {
		prefs = getSharedPreferences(App.PREFS, MODE_MULTI_PROCESS);
		Type collectionType = new TypeToken<List<Transaction>>(){}.getType();
		String transactionsOnDisk = prefs.getString(TRANSACTION, null);
		Log.d(TRANSACTION, "" + transactionsOnDisk);
		List<Transaction> transactions = gson.fromJson(transactionsOnDisk, collectionType);
		List<IListItem> transactionsList;
		if (transactions != null){
			transactionsList = new ArrayList<IListItem>(transactions);
			Collections.reverse(transactionsList);
		} else {
			transactionsList = new ArrayList<IListItem>();
		}
		return transactionsList;
	}

	@Override
	public void storeTransactions(JSONArray transactions) {
		prefs = getSharedPreferences(App.PREFS, MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(TRANSACTION, transactions.toString());
		editor.commit();
	}

	public List<IListItem> transformToList(JSONArray responseJSON) {
		Type collectionType = new TypeToken<List<Transaction>>(){}.getType();
		List<Transaction> transactions = gson.fromJson(responseJSON.toString(), collectionType);
		List<IListItem> transactionsList;
		if (transactions != null){
			transactionsList = new ArrayList<IListItem>(transactions);
			Collections.reverse(transactionsList);
		} else {
			transactionsList = new ArrayList<IListItem>();
		}
		return transactionsList;
	}

}
