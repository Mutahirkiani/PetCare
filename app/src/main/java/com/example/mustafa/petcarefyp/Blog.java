package com.example.mustafa.petcarefyp;

/**
 * Created by Zubair on 8/29/2017.
 */


public class Blog {

    String title;
    String description;
    String image;

    String id;

    public  Blog(){

    }


    public Blog(String title, String description, String image,String id) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.id=id;
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
