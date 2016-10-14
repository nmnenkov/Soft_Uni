package com.nnenkov.mvh_sugarorm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by nik on 03.10.16.
 */

public class MVHVehicleModels extends SugarRecord implements Parcelable {

    private MVHVehicleBrands mvhVehicleBrands;
    private String code;
    private String title;

    public MVHVehicleModels() {
    }

    public MVHVehicleModels(MVHVehicleBrands mvhVehicleBrands, String code, String title) {
        this.mvhVehicleBrands = mvhVehicleBrands;
        this.code = code;
        this.title = title;
    }

    public MVHVehicleBrands getMvhVehicleBrands() {
        return mvhVehicleBrands;
    }

    public void setMvhVehicleBrands(MVHVehicleBrands mvhVehicleBrands) {
        this.mvhVehicleBrands = mvhVehicleBrands;
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

    protected MVHVehicleModels(Parcel in) {
        mvhVehicleBrands = (MVHVehicleBrands) in.readValue(MVHVehicleBrands.class.getClassLoader());
        code = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mvhVehicleBrands);
        dest.writeString(code);
        dest.writeString(title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MVHVehicleModels> CREATOR = new Parcelable.Creator<MVHVehicleModels>() {
        @Override
        public MVHVehicleModels createFromParcel(Parcel in) {
            return new MVHVehicleModels(in);
        }

        @Override
        public MVHVehicleModels[] newArray(int size) {
            return new MVHVehicleModels[size];
        }
    };
}