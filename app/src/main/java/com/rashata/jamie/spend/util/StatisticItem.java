package com.rashata.jamie.spend.util;

/**
 * Created by jjamierashata on 5/23/16 AD.
 */
public class StatisticItem {
    private int id;
    private String title;
    private Boolean isClicked;

    public StatisticItem(int id, String title, Boolean isClicked) {
        this.id = id;
        this.title = title;
        this.isClicked = isClicked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getClicked() {
        return isClicked;
    }

    public void setClicked(Boolean clicked) {
        isClicked = clicked;
    }
}
