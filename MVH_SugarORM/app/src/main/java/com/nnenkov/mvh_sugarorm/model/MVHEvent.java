package com.nnenkov.mvh_sugarorm.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by nik on 11.08.16.
 */
public class MVHEvent extends SugarRecord implements Parcelable {

    private MVHVehicle vehicle;
    private Integer passedDistance;
    private Date eventDate;
    private String eventGPSLocationLatitude;
    private String eventGPSLocationLongitude;
    private String eventPhotosId;
    private MVHEventTypes eventType;
    private String amount;
    private String price;
    //    private Integer paymentsNumber;
//    private Date startDate;
//    private Date expireDate;
//    private Integer paymentsReminder;
    private MVHPart part;
    //    private Integer partReminder;
    private String notes;

    public MVHEvent() {
    }

    public MVHEvent(MVHVehicle vehicle, Integer passedDistance, Date eventDate, String eventGPSLocationLatitude, String eventGPSLocationLongitude, String eventPhotosId, MVHEventTypes eventType, String amount, String price, MVHPart part, String notes) {
        this.vehicle = vehicle;
        this.passedDistance = passedDistance;
        this.eventDate = eventDate;
        this.eventGPSLocationLatitude = eventGPSLocationLatitude;
        this.eventGPSLocationLongitude = eventGPSLocationLongitude;
        this.eventPhotosId = eventPhotosId;
        this.eventType = eventType;
        this.amount = amount;
        this.price = price;
        this.part = part;
        this.notes = notes;
    }

/*
    @Override
    public String toString() {
        String eventStr= "";
        if (this.eventType != null) {eventStr = this.eventType.getEventName()+","+""}
        return eventStr;
    }
*/

    public MVHVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(MVHVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getPassedDistance() {
        return passedDistance;
    }

    public void setPassedDistance(Integer passedDistance) {
        this.passedDistance = passedDistance;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventGPSLocationLatitude() {
        return eventGPSLocationLatitude;
    }

    public void setEventGPSLocationLatitude(String eventGPSLocationLatitude) {
        this.eventGPSLocationLatitude = eventGPSLocationLatitude;
    }

    public String getEventGPSLocationLongitude() {
        return eventGPSLocationLongitude;
    }

    public void setEventGPSLocationLongitude(String eventGPSLocationLongitude) {
        this.eventGPSLocationLongitude = eventGPSLocationLongitude;
    }

    public String getEventPhotosId() {
        return eventPhotosId;
    }

    public void setEventPhotosId(String eventPhotosId) {
        this.eventPhotosId = eventPhotosId;
    }

    public MVHEventTypes getEventType() {
        return eventType;
    }

    public void setEventType(MVHEventTypes eventType) {
        this.eventType = eventType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public MVHPart getPart() {
        return part;
    }

    public void setPart(MVHPart part) {
        this.part = part;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    protected MVHEvent(Parcel in) {
        vehicle = (MVHVehicle) in.readValue(MVHVehicle.class.getClassLoader());
        passedDistance = in.readByte() == 0x00 ? null : in.readInt();
        long tmpEventDate = in.readLong();
        eventDate = tmpEventDate != -1 ? new Date(tmpEventDate) : null;
        eventGPSLocationLatitude = in.readString();
        eventGPSLocationLongitude = in.readString();
        eventPhotosId = in.readString();
        eventType = (MVHEventTypes) in.readValue(MVHEventTypes.class.getClassLoader());
        amount = in.readString();
        price = in.readString();
        part = (MVHPart) in.readValue(MVHPart.class.getClassLoader());
        notes = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(vehicle);
        if (passedDistance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(passedDistance);
        }
        dest.writeLong(eventDate != null ? eventDate.getTime() : -1L);
        dest.writeString(eventGPSLocationLatitude);
        dest.writeString(eventGPSLocationLongitude);
        dest.writeString(eventPhotosId);
        dest.writeValue(eventType);
        dest.writeString(amount);
        dest.writeString(price);
        dest.writeValue(part);
        dest.writeString(notes);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MVHEvent> CREATOR = new Parcelable.Creator<MVHEvent>() {
        @Override
        public MVHEvent createFromParcel(Parcel in) {
            return new MVHEvent(in);
        }

        @Override
        public MVHEvent[] newArray(int size) {
            return new MVHEvent[size];
        }
    };
}