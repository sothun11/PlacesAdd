package com.example.placesadd;



public class Places {

    private String name;
    private String phone;
    private String lat;
    private String lng;

    public Places() {
        name = null;
        phone = null;
        lat = null;
        lng = null;
    }

    public Places(String n, String p, String lt, String lg) {
        name = n;
        phone = p;
        lat = lt;
        lng = lg;
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

    public String printPlace() {
        return this.getName() + " " + this.getPhone() + " " + this.getLat() + " " + this.getLng();
    }

}

