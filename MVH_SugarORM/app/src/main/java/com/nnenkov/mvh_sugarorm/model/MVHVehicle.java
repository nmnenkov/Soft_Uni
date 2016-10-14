package com.nnenkov.mvh_sugarorm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Date;


/**
 * Created by nik on 11.08.16.
 */

public class MVHVehicle extends SugarRecord implements Parcelable {

    private String nickname;
    private String regNumber;
    private MVHVehicleTypes vType;
    private MVHVehicleBrands vBrand;
    private MVHVehicleModels vModel;
    private String vEngin;
    private Integer hasSecondaryFuelTank;
    private MVHFuelTypes fuelTank1FuelType;
    private Integer fuelTank1Volume;
    private MVHFuelTypes fuelTank2FuelType;
    private Integer fuelTank2Volume;
    private Integer primaryFuelTypeID;
    private String manufactureYear;
    private Date purchaseDate;
    private String price;
    private Date warrantyValidityDate;


    public MVHVehicle(){

    }

    public MVHVehicle(String nickname, String regNumber, MVHVehicleTypes vType, MVHVehicleBrands vBrand, MVHVehicleModels vModel, String vEngin, Integer hasSecondaryFuelTank, MVHFuelTypes fuelTank1FuelType, Integer fuelTank1Volume, MVHFuelTypes fuelTank2FuelType, Integer fuelTank2Volume, Integer primaryFuelTypeID, String manufactureYear, Date purchaseDate, String price, Date warrantyValidityDate) {
        this.nickname = nickname;
        this.regNumber = regNumber;
        this.vType = vType;
        this.vBrand = vBrand;
        this.vModel = vModel;
        this.vEngin = vEngin;
        this.hasSecondaryFuelTank = hasSecondaryFuelTank;
        this.fuelTank1FuelType = fuelTank1FuelType;
        this.fuelTank1Volume = fuelTank1Volume;
        this.fuelTank2FuelType = fuelTank2FuelType;
        this.fuelTank2Volume = fuelTank2Volume;
        this.primaryFuelTypeID = primaryFuelTypeID;
        this.manufactureYear = manufactureYear;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.warrantyValidityDate = warrantyValidityDate;
    }

    @Override
    public String toString() {
        return nickname+","+vModel;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public MVHVehicleTypes getvType() {
        return vType;
    }

    public void setvType(MVHVehicleTypes vType) {
        this.vType = vType;
    }

    public MVHVehicleBrands getvBrand() {
        return vBrand;
    }

    public void setvBrand(MVHVehicleBrands vBrand) {
        this.vBrand = vBrand;
    }

    public MVHVehicleModels getvModel() {
        return vModel;
    }

    public void setvModel(MVHVehicleModels vModel) {
        this.vModel = vModel;
    }

    public String getvEngin() {
        return vEngin;
    }

    public void setvEngin(String vEngin) {
        this.vEngin = vEngin;
    }

    public Integer getHasSecondaryFuelTank() {
        return hasSecondaryFuelTank;
    }

    public void setHasSecondaryFuelTank(Integer hasSecondaryFuelTank) {
        this.hasSecondaryFuelTank = hasSecondaryFuelTank;
    }

    public MVHFuelTypes getFuelTank1FuelType() {
        return fuelTank1FuelType;
    }

    public void setFuelTank1FuelType(MVHFuelTypes fuelTank1FuelType) {
        this.fuelTank1FuelType = fuelTank1FuelType;
    }

    public Integer getFuelTank1Volume() {
        return fuelTank1Volume;
    }

    public void setFuelTank1Volume(Integer fuelTank1Volume) {
        this.fuelTank1Volume = fuelTank1Volume;
    }

    public MVHFuelTypes getFuelTank2FuelType() {
        return fuelTank2FuelType;
    }

    public void setFuelTank2FuelType(MVHFuelTypes fuelTank2FuelType) {
        this.fuelTank2FuelType = fuelTank2FuelType;
    }

    public Integer getFuelTank2Volume() {
        return fuelTank2Volume;
    }

    public void setFuelTank2Volume(Integer fuelTank2Volume) {
        this.fuelTank2Volume = fuelTank2Volume;
    }

    public Integer getPrimaryFuelTypeID() {
        return primaryFuelTypeID;
    }

    public void setPrimaryFuelTypeID(Integer primaryFuelTypeID) {
        this.primaryFuelTypeID = primaryFuelTypeID;
    }

    public String getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getWarrantyValidityDate() {
        return warrantyValidityDate;
    }

    public void setWarrantyValidityDate(Date warrantyValidityDate) {
        this.warrantyValidityDate = warrantyValidityDate;
    }

    protected MVHVehicle(Parcel in) {
        nickname = in.readString();
        regNumber = in.readString();
        vType = (MVHVehicleTypes) in.readValue(MVHVehicleTypes.class.getClassLoader());
        vBrand = (MVHVehicleBrands) in.readValue(MVHVehicleBrands.class.getClassLoader());
        vModel = (MVHVehicleModels) in.readValue(MVHVehicleModels.class.getClassLoader());
        vEngin = in.readString();
        hasSecondaryFuelTank = in.readByte() == 0x00 ? null : in.readInt();
        fuelTank1FuelType = (MVHFuelTypes) in.readValue(MVHFuelTypes.class.getClassLoader());
        fuelTank1Volume = in.readByte() == 0x00 ? null : in.readInt();
        fuelTank2FuelType = (MVHFuelTypes) in.readValue(MVHFuelTypes.class.getClassLoader());
        fuelTank2Volume = in.readByte() == 0x00 ? null : in.readInt();
        primaryFuelTypeID = in.readByte() == 0x00 ? null : in.readInt();
        manufactureYear = in.readString();
        long tmpPurchaseDate = in.readLong();
        purchaseDate = tmpPurchaseDate != -1 ? new Date(tmpPurchaseDate) : null;
        price = in.readString();
        long tmpWarrantyValidityDate = in.readLong();
        warrantyValidityDate = tmpWarrantyValidityDate != -1 ? new Date(tmpWarrantyValidityDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeString(regNumber);
        dest.writeValue(vType);
        dest.writeValue(vBrand);
        dest.writeValue(vModel);
        dest.writeString(vEngin);
        if (hasSecondaryFuelTank == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(hasSecondaryFuelTank);
        }
        dest.writeValue(fuelTank1FuelType);
        if (fuelTank1Volume == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(fuelTank1Volume);
        }
        dest.writeValue(fuelTank2FuelType);
        if (fuelTank2Volume == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(fuelTank2Volume);
        }
        if (primaryFuelTypeID == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(primaryFuelTypeID);
        }
        dest.writeString(manufactureYear);
        dest.writeLong(purchaseDate != null ? purchaseDate.getTime() : -1L);
        dest.writeString(price);
        dest.writeLong(warrantyValidityDate != null ? warrantyValidityDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MVHVehicle> CREATOR = new Parcelable.Creator<MVHVehicle>() {
        @Override
        public MVHVehicle createFromParcel(Parcel in) {
            return new MVHVehicle(in);
        }

        @Override
        public MVHVehicle[] newArray(int size) {
            return new MVHVehicle[size];
        }
    };
}