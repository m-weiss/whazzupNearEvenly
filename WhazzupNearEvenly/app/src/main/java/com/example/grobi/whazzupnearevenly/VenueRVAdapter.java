package com.example.grobi.whazzupnearevenly;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by grobi on 03.05.17.
 */

//ToDo: on click on venue open detail-page

public class VenueRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<FoursquareData> venues;
    private Activity act;

    public VenueRVAdapter(ArrayList<FoursquareData> venues, Activity act){
        this.venues = venues;
        this.act = act;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_primary_viewholder, parent, false);
        viewHolder = new RecyclerViewSimpleTextViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) holder;
        configureDefaultViewHolder(vh, position);
    }

    private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder vh, int position) {
        FoursquareData venue = venues.get(position);

        if(vh.getVenueName() != null) {
            vh.getVenueName().setText(venue.getName());
            if (venue.getLocation() !=null){
                vh.getVenueAddress().setText(venue.getLocation().getAddress());
                vh.getVenueDistance().setText("distance: " + venue.getLocation().getDistance() + "m");
            }
        }
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {

        TextView venueName;
        TextView venueAddress;
        TextView venueDistance;
        Button routeBtn;

        public RecyclerViewSimpleTextViewHolder(View v) {
            super(v);
            venueName = (TextView)v.findViewById(R.id.cardViewVenueNameText);
            venueAddress = (TextView)v.findViewById(R.id.cardViewVenueLocationAddress);
            venueDistance = (TextView)v.findViewById(R.id.cardViewVenueLocationDistance);
            routeBtn = (Button)v.findViewById(R.id.routeBtn);
            routeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item = getAdapterPosition();
                    String latLng = venues.get(item).getLocation().getLat() + "," + venues.get(item).getLocation().getLng();
                    String uri = "http://maps.google.com/maps?daddr=" + latLng;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    act.startActivity(intent);
                }
            });
        }

        public TextView getVenueName() {
            return venueName;
        }

        public void setVenueName(TextView venueName) {
            this.venueName = venueName;
        }

        public TextView getVenueAddress() {
            return venueAddress;
        }

        public void setVenueAddress(TextView venueAddress) {
            this.venueAddress = venueAddress;
        }

        public TextView getVenueDistance() {
            return venueDistance;
        }

        public void setVenueDistance(TextView venueDistance) {
            this.venueDistance = venueDistance;
        }
    }
}
