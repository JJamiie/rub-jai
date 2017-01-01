package com.rashata.jamie.spend.util;

/**
 * Created by jjamierashata on 5/23/16 AD.
 */
public class GuideItem {
    private String title;
    private String description1;
    private String description2;
    private int imgGuide;
    private Boolean isDescription;

    public GuideItem(String title, String description1, String description2, int imgGuide, Boolean isDescription) {
        this.title = title;
        this.description1 = description1;
        this.description2 = description2;
        this.imgGuide = imgGuide;
        this.isDescription = isDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public int getImgGuide() {
        return imgGuide;
    }

    public void setImgGuide(int imgGuide) {
        this.imgGuide = imgGuide;
    }

    public Boolean getDescription() {
        return isDescription;
    }

    public void setDescription(Boolean description) {
        isDescription = description;
    }
}
