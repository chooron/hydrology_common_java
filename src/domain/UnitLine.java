package domain;

import java.util.Calendar;

/**
 * 时段单位线对象
 */
public class UnitLine {
    Calendar calendar;
    double rainoff;
    double netRain;

    public UnitLine(Calendar calendar, double rainoff, double netRain) {
        this.calendar = calendar;
        this.rainoff = rainoff;
        this.netRain = netRain;
    }

    public UnitLine() {
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public double getRainoff() {
        return rainoff;
    }

    public void setRainoff(double rainoff) {
        this.rainoff = rainoff;
    }

    public double getNetRain() {
        return netRain;
    }

    public void setNetRain(double netRain) {
        this.netRain = netRain;
    }

    @Override
    public String toString() {
        return "UnitLine{" +
                "calendar=" + calendar +
                ", rainoff=" + rainoff +
                ", netRain=" + netRain +
                '}';
    }
}
