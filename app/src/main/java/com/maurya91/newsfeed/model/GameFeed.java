package com.maurya91.newsfeed.model;

/**
 * Created by Mukesh Kumar Maurya on 19-07-2016 in project News Feed.
 */
public class GameFeed {
    private String title;
    private String feedUrl;
    private String imageUrl;
    private String time;
    private String textTime;
    private String descriptionText;
    private String author;
    private String detailNews;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTextTime() {
        return textTime;
    }

    public void setTextTime(String textTime) {
        this.textTime = textTime;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetailNews() {
        return detailNews;
    }

    public void setDetailNews(String detailNews) {
        this.detailNews = detailNews;
    }

    @Override
    public String toString() {
        return "GameFeed{" +
                "title='" + title + '\'' +
                ", feedUrl='" + feedUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", time='" + time + '\'' +
                ", textTime='" + textTime + '\'' +
                ", descriptionText='" + descriptionText + '\'' +
                ", author='" + author + '\'' +
                ", detailNews='" + detailNews + '\'' +
                '}';
    }
}
