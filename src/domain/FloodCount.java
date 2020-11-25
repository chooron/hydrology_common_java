package domain;

import java.util.ArrayList;
import java.util.Arrays;

public class FloodCount {
    double t = 60 * 60;//计算单位时段
    double[][] relationOfzvq; //z-v-q关系曲线
    double Zxian;
    ArrayList<Double> hRainoffs;//时段洪水过程
    ArrayList<Double> hours;//洪水过程对应的时间

    public FloodCount(double t, double[][] relationOfzvq, double Zxian, ArrayList<Double> hRainoffs, ArrayList<Double> hours) {
        this.t = t;
        this.relationOfzvq = relationOfzvq;
        this.Zxian = Zxian;
        this.hRainoffs = hRainoffs;
        this.hours = hours;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public double[][] getRelationOfzvq() {
        return relationOfzvq;
    }

    public void setRelationOfzvq(double[][] relationOfzvq) {
        this.relationOfzvq = relationOfzvq;
    }

    public double getZxian() {
        return Zxian;
    }

    public void setZxian(double zxian) {
        Zxian = zxian;
    }

    public ArrayList<Double> gethRainoffs() {
        return hRainoffs;
    }

    public void sethRainoffs(ArrayList<Double> hRainoffs) {
        this.hRainoffs = hRainoffs;
    }

    public ArrayList<Double> getHours() {
        return hours;
    }

    public void setHours(ArrayList<Double> hours) {
        this.hours = hours;
    }

    public FloodCount() {
    }

    @Override
    public String toString() {
        return "FloodCount{" +
                "t=" + t +
                ", relationOfzvq=" + Arrays.toString(relationOfzvq) +
                ", Zxian=" + Zxian +
                ", hRainoffs=" + hRainoffs +
                ", hours=" + hours +
                '}';
    }
}
