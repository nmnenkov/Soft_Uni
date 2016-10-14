package com.nnenkov.mvh_sugarorm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by nik on 07.10.16.
 */

public class MVHVehicleTypes extends SugarRecord implements Parcelable {

    private String typeName;
    private String icoName;

    public MVHVehicleTypes() {
    }

    public MVHVehicleTypes(String typeName, String icoName) {
        this.typeName = typeName;
        this.icoName = icoName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIcoName() {
        return icoName;
    }

    public void setIcoName(String icoName) {
        this.icoName = icoName;
    }

    @Override
    public String toString() {
        return typeName;
    }

    protected MVHVehicleTypes(Parcel in) {
        typeName = in.readString();
        icoName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeName);
        dest.writeString(icoName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MVHVehicleTypes> CREATOR = new Parcelable.Creator<MVHVehicleTypes>() {
        @Override
        public MVHVehicleTypes createFromParcel(Parcel in) {
            return new MVHVehicleTypes(in);
        }

        @Override
        public MVHVehicleTypes[] newArray(int size) {
            return new MVHVehicleTypes[size];
        }
    };
}