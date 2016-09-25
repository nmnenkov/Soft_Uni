package com.nnenkov.mymusicplayer;

/**
 * Created by nik on 09.09.16.
 */

public class SongInfo {
    private long id;
    private String sNumber;
    private String sTitle;
    private String sSinger;
    private String sPath;
    private int resName;
    private Boolean playNow;


    public SongInfo(long id,String sNumber, String sTitle, String sSinger, String sPath, int resName) {
        this.id = id;
        this.sNumber = sNumber;
        this.sTitle = sTitle;
        this.sSinger = sSinger;
        this.sPath = sPath;
        this.resName = resName;
        this.playNow = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getsNumber() {
        return sNumber;
    }

    public void setsNumber(String sNumber) {
        this.sNumber = sNumber;
    }

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    public String getsSinger() {
        return sSinger;
    }

    public void setsSinger(String sSinger) {
        this.sSinger = sSinger;
    }

    public String getsPath() {
        return sPath;
    }

    public void setsPath(String sPath) {
        this.sPath = sPath;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public Boolean getPlayNow() {
        return playNow;
    }

    public void setPlayNow(Boolean playNow) {
        this.playNow = playNow;
    }
}