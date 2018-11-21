package com.example.mustafa.petcarefyp;

/**
 * Created by raja m afzal on 3/26/2018.
 */

public class Ads {
    String title,number,city,country,additional,price,image,gender;
    public  Ads(){

    }

    public Ads(String title, String number, String city,String country,String additional,String price,String image,String gender) {
        this.title = title;
        this.number = number;
        this.image = image;
        this.city=city;
        this.country=country;
        this.additional=additional;
        this.price=price;
        this.image=image;
        this.gender=gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public String getAdditional() {
        return additional;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getNumber() {
        return number;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
