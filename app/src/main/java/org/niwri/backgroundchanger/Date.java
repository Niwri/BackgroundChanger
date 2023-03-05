package org.niwri.backgroundchanger;

public class Date {
    private boolean[] days;
    private int hour;
    private int minute;
    private int second;
    private boolean isPM;

    public Date(boolean[] days, int hour, int minute, int second, boolean isPM) {
        this.days = days;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.isPM = isPM;
    }

    //Getters
    public boolean[] getDay() { return days; }

    public int getHour() { return hour; }

    public int getMinute() { return minute; }

    public int getSecond() { return second; }

    public boolean isPM() { return isPM; }

    //Setters
    public void setDay(boolean[] days) { this.days = days; }

    public void setHour(int hour) { this.hour = hour; }

    public void setMinute(int minute) { this.minute = minute; }

    public void setSecond(int second) { this.second = second; }

    public void setPM(boolean isPM) { this.isPM = isPM; }
}
