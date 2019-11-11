package com.example.roomres.MODELS;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation implements Serializable, Comparable<Reservation> {

    @SerializedName("id")
    private int id;

    @SerializedName("fromTime")
    private int fromTime;

    @SerializedName("toTime")
    private int toTime;

    @SerializedName("userId")
    private String userId;

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("roomId")
    private int roomId;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public Reservation(){}

    public Reservation(int id, int fromTime, int toTime, String userId, String purpose, int roomId) {
        this.id = id;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.userId = userId;
        this.purpose = purpose;
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromTimeOld() {
        return fromTime;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public int getToTimeOld() {
        return toTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Date getFromTime() {
        return new Date(getFromTimeOld()*1000);
    }

    public Date getToTime() {
        return new Date(getToTimeOld()*1000);
    }

    @Override
    public int compareTo(Reservation reservation) {
        return (this.getToTimeOld() < reservation.getToTimeOld() ? -1 :
                (this.getToTimeOld() == reservation.getToTimeOld() ? 0 : 1));
    }

    @NonNull
    @Override
    public String toString() {
        return "Purpose: "+ purpose + "\nFrom: " + dateFormat.format(getFromTime()) + "\nTo: " + dateFormat.format(getToTime());
    }



}
