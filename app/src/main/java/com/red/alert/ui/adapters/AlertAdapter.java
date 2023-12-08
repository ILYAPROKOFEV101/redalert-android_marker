package com.red.alert.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.red.alert.R;
import com.red.alert.config.ThreatTypes;
import com.red.alert.model.Alert;

import java.util.List;

public class AlertAdapter extends ArrayAdapter<Alert> {
    Activity mActivity;
    List<Alert> mAlerts;

    public AlertAdapter(Activity activity, List<Alert> alerts) {
        // Call super function with the item layout and initial alerts
        super(activity, R.layout.alert, alerts);

        // Set data members
        this.mAlerts = alerts;
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Prepare view holder
        ViewHolder viewHolder;

        // Don't have a cached view?
        if (convertView == null) {
            // Get inflater service
            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Inflate the alert layout
            convertView = layoutInflater.inflate(R.layout.alert, null);

            // Create a new view holder
            viewHolder = new ViewHolder();

            // Cache the view resources
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.alertTitle);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.alertDesc);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);

            // Store it in tag
            convertView.setTag(viewHolder);
        }
        else {
            // Get cached convert view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Retrieve the alert
        Alert alert = mAlerts.get(position);

        // Got an alert?
        if (alert != null) {
            // Set area localized zone
            viewHolder.title.setText(alert.localizedCity);

            // System alert?
            if (alert.threat.equals(ThreatTypes.SYSTEM)) {
                // Set desc to "system message"
                alert.desc = alert.localizedThreat;

                // Hide time
                viewHolder.time.setVisibility(View.GONE);
            }
            else {
                // Show time
                viewHolder.time.setVisibility(View.VISIBLE);
            }

            // Set area names
            viewHolder.desc.setText(alert.desc);

            // Show alert type & user-friendly time
            viewHolder.time.setText(alert.localizedThreat + " • " + alert.dateString);

            // Custom threat icons
            if (alert.threat.contains(ThreatTypes.RADIOLOGICAL_EVENT)) {
                viewHolder.image.setImageResource(R.drawable.ic_radiological_event);
            }
            else if (alert.threat.contains(ThreatTypes.HOSTILE_AIRCRAFT_INTRUSION)) {
                viewHolder.image.setImageResource(R.drawable.ic_hostile_aircraft_intrusion);
            }
            else if (alert.threat.contains(ThreatTypes.HAZARDOUS_MATERIALS)) {
                viewHolder.image.setImageResource(R.drawable.ic_hazardous_materials);
            }
            else if (alert.threat.contains(ThreatTypes.TSUNAMI)) {
                viewHolder.image.setImageResource(R.drawable.ic_tsunami);
            }
            else if (alert.threat.contains(ThreatTypes.MISSILES)) {
                viewHolder.image.setImageResource(R.drawable.ic_launcher);
            }
            else if (alert.threat.contains(ThreatTypes.TERRORIST_INFILTRATION)) {
                viewHolder.image.setImageResource(R.drawable.ic_terrorist_infiltration);
            }
            else if (alert.threat.contains(ThreatTypes.EARTHQUAKE)) {
                viewHolder.image.setImageResource(R.drawable.ic_earthquake);
            }
            else {
                viewHolder.image.setImageResource(R.drawable.ic_launcher);
            }
        }

        // Return the view
        return convertView;
    }

    public boolean hasStableIds() {
        // IDs are unique
        return true;
    }

    public static class ViewHolder {
        public TextView time;
        public TextView title;
        public TextView desc;
        public ImageView image;
    }
}