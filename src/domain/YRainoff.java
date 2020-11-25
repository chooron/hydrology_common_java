package domain;

/**
 * 年内径流过程
 */
public class YRainoff {
    int month;
    double rainoff;
    double waterUse;
    int year;
    double[] days = {31, 28, 31, 30, 31, 30, 30, 31, 31, 30, 31, 30, 31};
    String describe = "年内径流过程";

    public YRainoff(int month, double rainoff, double waterUse, int year, String describe) {
        this.month = month;
        this.rainoff = rainoff;
        this.waterUse = waterUse;
        this.year = year;
        this.describe = describe;
    }

    public YRainoff() {
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getRainoff() {
        return rainoff;
    }

    public void setRainoff(double rainoff) {
        this.rainoff = rainoff;
    }

    public double getWaterUse() {
        return waterUse;
    }

    public void setWaterUse(double waterUse) {
        this.waterUse = waterUse;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double[] getDays() {
        return days;
    }

    @Override
    public String toString() {
        return "YRainoff{" +
                "month=" + month +
                ", rainoff=" + rainoff +
                ", waterUse=" + waterUse +
                ", year=" + year +
                ", describe='" + describe + '\'' +
                '}';
    }
}
