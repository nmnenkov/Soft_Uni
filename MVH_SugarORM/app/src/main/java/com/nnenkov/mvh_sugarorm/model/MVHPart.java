package com.nnenkov.mvh_sugarorm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by nik on 11.08.16.
 */
public class MVHPart extends SugarRecord implements Parcelable {

    private String name;
    private String description;

    public MVHPart() {
    }

    public MVHPart(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected MVHPart(Parcel in) {
        name = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MVHPart> CREATOR = new Parcelable.Creator<MVHPart>() {
        @Override
        public MVHPart createFromParcel(Parcel in) {
            return new MVHPart(in);
        }

        @Override
        public MVHPart[] newArray(int size) {
            return new MVHPart[size];
        }
    };
}