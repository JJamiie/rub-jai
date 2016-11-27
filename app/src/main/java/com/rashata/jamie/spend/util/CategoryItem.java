package com.rashata.jamie.spend.util;

/**
 * Created by jjamierashata on 5/23/16 AD.
 */
public class CategoryItem {
    private int resId;
    private String title;
    private int id;
    private boolean isShow;

    public CategoryItem(int id, int resId, String title, boolean isShow) {
        super();
        this.title = title;
        this.resId = resId;
        this.id = id;
        this.isShow = isShow;
    }

    public String getTitle() {
        return title;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
