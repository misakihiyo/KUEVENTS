package com.example.root.kuevents;

/**
 * Created by ezan on 1/14/18.
 */

public class Event {
    private String title;
    private String desc;
    private String image;
    private String start_date;
    private String end_date;
    private String start_time;
    private String end_time;


    public Event(String title, String desc, String image, String start_date, String end_date, String start_time, String end_time) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

}






