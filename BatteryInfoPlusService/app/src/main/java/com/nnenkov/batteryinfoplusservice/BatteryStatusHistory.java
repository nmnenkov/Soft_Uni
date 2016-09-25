package com.nnenkov.batteryinfoplusservice;

import java.util.Date;

/**
 * Created by nik on 24.09.16.
 */

public class BatteryStatusHistory {
    Date eventDate;
    int plugged;
    int scale;
    int health;
    int status;
    int rawlevel;
    int level;

    public BatteryStatusHistory(Date eventDate, int plugged, int scale, int health, int status, int rawlevel, int level) {
        this.eventDate = eventDate;
        this.plugged = plugged;
        this.scale = scale;
        this.health = health;
        this.status = status;
        this.rawlevel = rawlevel;
        this.level = level;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public int getPlugged() {
        return plugged;
    }

    public int getScale() {
        return scale;
    }

    public int getHealth() {
        return health;
    }

    public int getStatus() {
        return status;
    }

    public int getRawlevel() {
        return rawlevel;
    }

    public int getLevel() {
        return level;
    }
}
