package com.example.grobi.whazzupnearevenly;

import com.google.gson.annotations.SerializedName;

/**
 * Created by grobi on 04.05.17.
 */

public class FousquareLocation {

    @SerializedName("address")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "FousquareLocation{" +
                "address='" + address + '\'' +
                '}';
    }
}
