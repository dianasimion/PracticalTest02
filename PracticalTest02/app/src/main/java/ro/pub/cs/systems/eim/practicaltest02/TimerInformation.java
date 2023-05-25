package ro.pub.cs.systems.eim.practicaltest02;

import androidx.annotation.NonNull;

public class TimerInformation {
    private final String hour;
    private final String minute;

    private final String ip;


    public TimerInformation(String hour, String minute, String ip) {
        this.hour = hour;
        this.minute = minute;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }
    public String getHour() {
        return hour;
    }
    public String getMinute() {
        return minute;
    }

    @NonNull
    @Override
    public String toString() {
        return "Timer info{" + "ip='" + ip + '\'' + "hour='" + hour + '\'' + ", minute='" + minute + '}';
    }
}
