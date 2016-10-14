package com.nnenkov.mvh_sugarorm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by nik on 11.08.16.
 */
public class MVHFuelTypes extends SugarRecord implements Parcelable {

    private String fuelType;
    private String fuelName;

    public MVHFuelTypes() {
    }

    public MVHFuelTypes(String fuelType, String fuelName) {
        this.fuelType = fuelType;
        this.fuelName = fuelName;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFuelName() {
        return fuelName;
    }

    public void setFuelName(String fuelName) {
        this.fuelName = fuelName;
    }

    @Override
    public String toString() {
        return fuelName;
    }

    protected MVHFuelTypes(Parcel in) {
        fuelType = in.readString();
        fuelName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fuelType);
        dest.writeString(fuelName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MVHFuelTypes> CREATOR = new Parcelable.Creator<MVHFuelTypes>() {
        @Override
        public MVHFuelTypes createFromParcel(Parcel in) {
            return new MVHFuelTypes(in);
        }

        @Override
        public MVHFuelTypes[] newArray(int size) {
            return new MVHFuelTypes[size];
        }
    };
}