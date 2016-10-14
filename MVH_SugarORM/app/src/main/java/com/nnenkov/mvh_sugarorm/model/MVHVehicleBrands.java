package com.nnenkov.mvh_sugarorm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by nik on 03.10.16.
 */

public class MVHVehicleBrands extends SugarRecord implements Parcelable {

    private String code;
    private String title;

    public MVHVehicleBrands() {
    }

    public MVHVehicleBrands(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    protected MVHVehicleBrands(Parcel in) {
        code = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MVHVehicleBrands> CREATOR = new Parcelable.Creator<MVHVehicleBrands>() {
        @Override
        public MVHVehicleBrands createFromParcel(Parcel in) {
            return new MVHVehicleBrands(in);
        }

        @Override
        public MVHVehicleBrands[] newArray(int size) {
            return new MVHVehicleBrands[size];
        }
    };
}