package ru.example.gamecalendar.model;

public class Game {
    private String name;
    private String date;
    private String platform;
    private String genre;
    private String picURL;

    public Game() {

    }

    public Game(String name, String date, String platform, String genre, String picURL) {
        this.name = name;
        this.date = date;
        this.platform = platform;
        this.genre = genre;
        this.picURL = picURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPlatform() {
        return platform;
    }

    public String getGenre() {
        return genre;
    }

    public String getPicURL() {
        return picURL;
    }
}
