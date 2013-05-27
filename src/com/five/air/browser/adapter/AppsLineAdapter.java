package com.five.air.browser.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import com.five.air.browser.model.Application;
import com.five.air.browser.model.ApplicationList;
import com.five.air.browser.ultils.ImageDownloadTask;
import com.five.air.browser.ultils.ImageUtils;

import com.five.air.browser.MainActivity;
import com.five.air.browser.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppsLineAdapter extends BaseAdapter {

	private Vector<ApplicationList> list = new Vector<ApplicationList>();
	private Context ctx;
	private LayoutInflater inflater;

	public AppsLineAdapter(Context ctx) {
		this.ctx = ctx;
		this.inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public ApplicationList getItem(int pos) {
		// TODO Auto-generated method stub
		return list.get(pos);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ApplicationList list = getItem(position);

		if (list != null) {
			convertView = inflater.inflate(R.layout.app_list_itm, null);

			for (int i = 0; i < list.getApplications().size(); i++) {
				int icon_id = -1, app_name_id = -1, content_id=-1;

				switch (i) {
				case 0:
					icon_id = R.id.icon_1;
					app_name_id = R.id.name_1;
					content_id =R.id.app_1;
					break;
				case 1:
					icon_id = R.id.icon_2;
					app_name_id = R.id.name_2;
					content_id =R.id.app_2;
					break;
				case 2:
					icon_id = R.id.icon_3;
					app_name_id = R.id.name_3;
					content_id =R.id.app_3;
					break;
				case 3:
					icon_id = R.id.icon_4;
					app_name_id = R.id.name_4;
					content_id =R.id.app_4;
					break;

				}
				ImageView icon = (ImageView) convertView.findViewById(icon_id);
				TextView app_name = (TextView) convertView
						.findViewById(app_name_id);
				LinearLayout lt_app=(LinearLayout) convertView.findViewById(content_id);
				
				final Application app = list.get(i);

				try {
					ImageDownloadTask.executeDownload(ctx, icon,
							new URL(app.getIcon()));
				} catch (MalformedURLException e) {

				}

				app_name.setText(app.getName());

				lt_app.setVisibility(View.VISIBLE);
				lt_app.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent=new Intent(ctx,MainActivity.class);
						Bundle bundle=new Bundle();
						
						bundle.putString("app_url", app.getUrl());
						
						intent.putExtras(bundle);
						
						ctx.startActivity(intent);
						
					}
				});
			}

		}

		return convertView;
	}

	public void add(Application app) {

		ApplicationList lastList = null;

		try {
			lastList = list.lastElement();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (lastList == null || lastList.size() == 4) {
			ApplicationList newList = new ApplicationList();
			newList.add(app);

			list.add(newList);
		} else {
			lastList.add(app);
		}

		notifyDataSetChanged();
	}

	public void clear() {
		list.clear();
		notifyDataSetChanged();
	}
}
