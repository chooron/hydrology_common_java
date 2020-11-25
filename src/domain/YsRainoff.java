package domain;

/**
 * 多年流量过程
 */
public class YsRainoff {
    int year;
    double rainoff;
    double freq;
    int rank;
    String describe = "年流量";

    public YsRainoff(int year, double rainoff, double freq, int rank, String describe) {
        this.year = year;
        this.rainoff = rainoff;
        this.freq = freq;
        this.rank = rank;
        this.describe = describe;
    }

    public YsRainoff() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRainoff() {
        return rainoff;
    }

    public void setRainoff(double rainoff) {
        this.rainoff = rainoff;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "YsRainoff{" +
                "year=" + year +
                ", rainoff=" + rainoff +
                ", freq=" + freq +
                ", rank=" + rank +
                ", describe='" + describe + '\'' +
                '}';
    }
}
