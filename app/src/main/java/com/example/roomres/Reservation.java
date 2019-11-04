package com.example.roomres;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reservation implements Serializable {

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

    public Reservation(){
    }

    public Reservation(int FromTime, int ToTime, String UserId, String Purpose, int RoomId){
        fromTime = FromTime;
        toTime = ToTime;
        userId = UserId;
        purpose = Purpose;
        roomId = RoomId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public int getFromTime() {
        return fromTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }

    public int getToTime() {
        return toTime;
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




}
