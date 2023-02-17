package org.niwri.backgroundchanger;

public class Date {
    private String day;
    private int hour;
    private int minute;
    private int second;

    public Date(String day, int hour, int minute, int second) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    //Getters
    public String getDay() { return day; }

    public int getHour() { return hour; }

    public int getMinute() { return minute; }

    public int getSecond() { return second; }

    //Setters
    public void setDay(String day) { this.day = day; }

    public void setHour(int hour) { this.hour = hour; }

    public void setMinute(int minute) { this.minute = minute; }

    public void setSecond(int second) { this.second = second; }
}
