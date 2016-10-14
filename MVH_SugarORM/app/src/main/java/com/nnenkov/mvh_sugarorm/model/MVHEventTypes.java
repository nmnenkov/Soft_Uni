package com.nnenkov.mvh_sugarorm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by nik on 11.08.16.
 */


public class MVHEventTypes extends SugarRecord implements Parcelable {

    private String eventName;
    private String description;
    private String eventIcon;

    public MVHEventTypes() {
    }

    public MVHEventTypes(String eventName, String description, String eventIcon) {
        this.eventName = eventName;
        this.description = description;
        this.eventIcon = eventIcon;
    }


    @Override
    public String toString() {
        return eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventIcon() {
        return eventIcon;
    }

    public void setEventIcon(String eventIcon) {
        this.eventIcon = eventIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected MVHEventTypes(Parcel in) {
        eventName = in.readString();
        description = in.readString();
        eventIcon = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(description);
        dest.writeString(eventIcon);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MVHEventTypes> CREATOR = new Parcelable.Creator<MVHEventTypes>() {
        @Override
        public MVHEventTypes createFromParcel(Parcel in) {
            return new MVHEventTypes(in);
        }

        @Override
        public MVHEventTypes[] newArray(int size) {
            return new MVHEventTypes[size];
        }
    };
}