package com.example.parsaniahardik.json_parse_listview;

public class TennisModel {

    private String name, country, city, imgURL, id;

    public String getImgURL(){
        return imgURL;
    }

    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
