package com.rashata.jamie.spend.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jjamierashata on 5/23/16 AD.
 */
public class CategoryItem implements Parcelable {
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

    protected CategoryItem(Parcel in) {
        resId = in.readInt();
        title = in.readString();
        id = in.readInt();
        isShow = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resId);
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeByte((byte) (isShow ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryItem> CREATOR = new Creator<CategoryItem>() {
        @Override
        public CategoryItem createFromParcel(Parcel in) {
            return new CategoryItem(in);
        }

        @Override
        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };

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
