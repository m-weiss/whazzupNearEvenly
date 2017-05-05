package com.example.grobi.whazzupnearevenly;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by grobi on 25.04.17.
 */

public class FoursquareData {

    private String name;
    private String id;
    @SerializedName("location")
    private FoursquareLocation location;
    private Response response;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FoursquareLocation getLocation() {
        return location;
    }

    public void setLocation(FoursquareLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "FoursquareData{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", location=" + location +
                ", response=" + response +
                '}';
    }

    public ArrayList<FoursquareData> createFoursquareDataList(){
        ArrayList<FoursquareData> datas = new ArrayList<>();
        datas.add(new FoursquareData());
        return datas;
    }

    public Response getResponse() {
        return response;
    }

    class Response{
        private ArrayList<FoursquareData> venues;

        private String name;
        private String address;
        private Float lat;
        private Float lng;
        private int distance;

        public String getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }


        public ArrayList<FoursquareData> getVenues(){
            return venues;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "venues=" + venues +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", lat=" + lat +
                    ", lng=" + lng +
                    ", distance=" + distance +
                    '}';
        }
    }

    public class FoursquareLocation {
        @SerializedName("address")
        private String address;
        private Float lat;
        private Float lng;
        private int distance;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Float getLat() {
            return lat;
        }

        public void setLat(Float lat) {
            this.lat = lat;
        }

        public Float getLng() {
            return lng;
        }

        public void setLng(Float lng) {
            this.lng = lng;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "FoursquareLocation{" +
                    "address='" + address + '\'' +
                    ", lat=" + lat +
                    ", lng=" + lng +
                    ", distance=" + distance +
                    '}';
        }
    }

}
