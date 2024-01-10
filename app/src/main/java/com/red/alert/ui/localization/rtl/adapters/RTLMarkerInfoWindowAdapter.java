package com.red.alert.ui.localization.rtl.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.red.alert.R;

public class RTLMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater mInflater;

    public RTLMarkerInfoWindowAdapter(LayoutInflater inflater) {
        // Set inflater
        this.mInflater = inflater;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // Inflate custom view
        View popup = mInflater.inflate(R.layout.map_tooltip, null);

        // Get text view
        TextView textView = (TextView) popup.findViewById(R.id.title);

        // Set text
        textView.setText(marker.getTitle());

        // Get snippet view
        textView = (TextView) popup.findViewById(R.id.snippet);

        // Set snippet
        textView.setText(marker.getSnippet());

        // No snippet?
        if (marker.getSnippet() == null) {
            textView.setVisibility(View.GONE);
        }

        // Return popup
        return popup;
    }
}
