package domain;

public class YDemand {
    int year;
    double waterDemand;
    int month;
    String describe = "年内用水需求";

    public YDemand() {
    }

    public YDemand(int year, double waterDemand, int month, String describe) {
        this.year = year;
        this.waterDemand = waterDemand;
        this.month = month;
        this.describe = describe;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getWaterDemand() {
        return waterDemand;
    }

    public void setWaterDemand(double waterDemand) {
        this.waterDemand = waterDemand;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "YDemand{" +
                "year=" + year +
                ", waterDemand=" + waterDemand +
                ", month=" + month +
                ", describe='" + describe + '\'' +
                '}';
    }
}
