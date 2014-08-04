package com.five.air.browser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.five.air.browser.adapter.AppsLineAdapter;
import com.five.air.browser.model.Application;
import com.five.air.browser.ultils.Constants;
import com.five.air.browser.ultils.ImageDownloadTask;
import com.five.air.browser.ultils.WebService;
import com.five.network.utils.AirServerDiscoveryListener;
import com.five.network.utils.NetworkUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ParserError", "ParserError" })
public class AppListActivity extends Activity implements AirServerDiscoveryListener{

	private ListView app_list;
	private Button bt_search_server;
	private LinearLayout lt_loading,lt_no_list;
	private AppsLineAdapter adapter;

	//View Actions
	private OnClickListener button_listener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			lt_loading.setVisibility(View.VISIBLE);
			lt_no_list.setVisibility(View.GONE);
			
			NetworkUtils.startSearchAirServer(AppListActivity.this);
		}
	
	};
	
	private boolean isNetworkAvailable() {
		return true;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.app_list);

		app_list = (ListView) findViewById(R.id.lst_app);
		lt_loading = (LinearLayout) findViewById(R.id.lt_progress);
		lt_no_list=(LinearLayout) findViewById(R.id.lt_message_error);
		bt_search_server = (Button) findViewById(R.id.bt_research);
		
		bt_search_server.setOnClickListener(button_listener);
				
		adapter=new AppsLineAdapter(this);
		
		app_list.setAdapter(adapter);
		
		if(!isNetworkAvailable()){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Por favor, habilite uma conex�o com a internet!")
			       .setCancelable(false)
			       .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                finish();
			           }
			       }).setTitle("Aten��o!");
			AlertDialog alert = builder.create(); 
			alert.show();
			return;
		}
		
//		locationManager.requestLocationUpdates(
//				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
//		new GetAppListSync().execute();

		NetworkUtils.startSearchAirServer(this);
	}

	@Override
	public void serverDiscovered(String message) {
		try {
			JSONObject response=new JSONObject(message);
			
			String ipServer=response.getString("ip_server");
			JSONArray app_list_json=response.getJSONArray("applications");
			
			for(int i=0;i<app_list_json.length();i++){
				JSONObject app_json=app_list_json.getJSONObject(i);
				
				Application app=Application.createFromJSON(ipServer, app_json);
				adapter.add(app);
				
			}
			
			lt_loading.setVisibility(View.GONE);
			app_list.setVisibility(View.VISIBLE);
			
		} catch (JSONException e) {
			lt_loading.setVisibility(View.GONE);
			lt_no_list.setVisibility(View.VISIBLE);
		}
		
		
		
	}


	@Override
	public void serverNotFound() {
		
		lt_loading.setVisibility(View.GONE);
		lt_no_list.setVisibility(View.VISIBLE);
		
	}

}
