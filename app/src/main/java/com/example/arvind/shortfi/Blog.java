package com.example.arvind.shortfi;



public class Blog {
    private String Name,Desciption,video;
    public Blog() {
    }
    public Blog(String video, String name, String desciption) {
        this.video = video;
        Name = name;
        Desciption = desciption;
    }

    public String getName() {
        return Name;
    }

    public String getDesciption() {
        return Desciption;
    }

    public String getVideo() {
        return video;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDesciption(String desciption) {
        Desciption = desciption;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
