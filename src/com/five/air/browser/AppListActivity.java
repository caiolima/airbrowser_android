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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ParserError", "ParserError" })
public class AppListActivity extends Activity implements AirServerDiscoveryListener{

	private ListView app_list;
	private LinearLayout lt_loading,lt_no_list;
	private AppsLineAdapter adapter;
	private LocationManager locationManager;
	private Location mLocation;
	
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location
			// provider.
			mLocation = location;

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	private boolean isNetworkAvailable() {
//	    ConnectivityManager connectivityManager 
//	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//	    return activeNetworkInfo != null;
		return true;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.app_list);

		app_list = (ListView) findViewById(R.id.lst_app);
		lt_loading = (LinearLayout) findViewById(R.id.lt_progress);
		lt_no_list=(LinearLayout) findViewById(R.id.lt_message_error);
//		adapter = new ArrayAdapter<Application>(this, R.id.textView1) {
//
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//				Application app = getItem(position);
//
//				convertView = mInflater.inflate(R.layout.app_item, null);
//
//				TextView appName = (TextView) convertView
//						.findViewById(R.id.txt_app_name);
//				TextView desc = (TextView) convertView
//						.findViewById(R.id.txt_desc);
//				ImageView img = (ImageView) convertView
//						.findViewById(R.id.img_app);
//
//				appName.setText(app.getName());
//				desc.setText(app.getDescription());
//				try {
//
//					ImageDownloadTask.executeDownload(
//							AppListActivity.this, img, new URL(app.getIcon()));
//
//				} catch (MalformedURLException e) {
//
//				}
//
//				return convertView;
//			}
//
//		};

		adapter=new AppsLineAdapter(this);
		
		
		app_list.setAdapter(adapter);
		

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		
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
	protected void onStop() {
		super.onStop();
		
		locationManager.removeUpdates(locationListener);
	}



//	private class GetAppListSync extends AsyncTask<Void, Void, Void> {
//
//		private Application app;
//		private long initTime;
//
//		public GetAppListSync() {
//			initTime = System.currentTimeMillis();
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			long timePassed;
//			do {
//				timePassed = System.currentTimeMillis() - initTime;
//			} while (mLocation == null && timePassed <= 15000000);
//
//			
//			if(mLocation==null){
//				Toast.makeText(AppListActivity.this, "Erro na conex�o", Toast.LENGTH_SHORT).show();
//				return null;
//			}
//			
//			double lat=mLocation.getLatitude();
//			double lon=mLocation.getLongitude();
//			
//			WebService web = new WebService(Constants.app_server);
//			HashMap<String, String> map=new HashMap<String, String>();
//			map.put("lat", Double.toString(lat));
//			map.put("long", Double.toString(lon));
//			
//			String appList = web.webGet("/apps_list", map);
//
//			if (appList != null) {
//				try {
//					JSONObject appListJSON = new JSONObject(appList);
//
//					app = Application.createFromJSON(appListJSON);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//
//			if (app != null) {
//				adapter.add(app);
//				
//				lt_loading.setVisibility(View.GONE);
//				app_list.setVisibility(View.VISIBLE);
//			}else{
//				lt_loading.setVisibility(View.GONE);
//				lt_no_list.setVisibility(View.VISIBLE);
//			}
//
//			
//
//		}
//
//	}



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
