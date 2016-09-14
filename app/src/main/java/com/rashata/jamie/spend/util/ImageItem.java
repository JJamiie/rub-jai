package com.rashata.jamie.spend.util;

/**
 * Created by jjamierashata on 5/23/16 AD.
 */
public class ImageItem {
    private int resId;
    private String title;

    public ImageItem(int resId, String title) {
        super();
        this.title = title;
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public int getResId() {
        return resId;
    }

}
