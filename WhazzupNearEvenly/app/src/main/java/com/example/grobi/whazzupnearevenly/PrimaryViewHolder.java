package com.example.grobi.whazzupnearevenly;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by grobi on 03.05.17.
 */

public class PrimaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.cardViewVenueNameText)
    TextView venueName;
    @BindView(R.id.venueId)
    TextView venueId;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;



    private Activity act;

    public PrimaryViewHolder(View itemView, final Activity act) {
        super(itemView);
        ButterKnife.bind(act);
        this.act = act;

        recyclerView = (RecyclerView)act.findViewById(R.id.recyclerView);
        venueName = (TextView)itemView.findViewById(R.id.cardViewVenueNameText);
        venueName.setOnClickListener(this);
        venueId.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(act, "click", Toast.LENGTH_SHORT).show();
        switch (v.getId()){
            case R.id.venueName:
                Toast.makeText(act, "name clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.venueId:
                Toast.makeText(act,"id clicked", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public TextView getVenueName() {
        return venueName;
    }

    public void setVenueName(TextView venueName) {
        this.venueName = venueName;
    }
}
