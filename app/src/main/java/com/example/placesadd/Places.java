package com.example.placesadd;



public class Places {

    private String name;
    private String phone;
    private String lat;
    private String lng;
    private String serviceType;

    public Places(String n, String p, String lt, String lg, String sType) {
        name = n;
        phone = p;
        lat = lt;
        lng = lg;
        serviceType = sType;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getServiceType() {
        return serviceType;
    }

}

