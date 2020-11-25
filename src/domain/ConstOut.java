package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 出力调节对象
 */
public class ConstOut {
    double N;
    double k;
    double Hlost;
    double Znormal;
    double Zdead;
    double[] month = {31, 28, 31, 30, 31, 30, 30, 31, 31, 30, 31, 30, 31};
    Map<String, Integer> T;
    double[][] ZV;
    double[][] QZ;
    ArrayList<YRainoff> yRainoffs;

    public ConstOut(double n, double k, double hlost, double znormal, double zdead, Map<String, Integer> t, double[][] ZV, double[][] QZ, ArrayList<YRainoff> yRainoffs) {
        N = n;
        this.k = k;
        Hlost = hlost;
        Znormal = znormal;
        Zdead = zdead;
        T = t;
        this.ZV = ZV;
        this.QZ = QZ;
        this.yRainoffs = yRainoffs;
    }

    public ConstOut() {
    }

    public double getN() {
        return N;
    }

    public void setN(double n) {
        N = n;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getHlost() {
        return Hlost;
    }

    public void setHlost(double hlost) {
        Hlost = hlost;
    }

    public double getZnormal() {
        return Znormal;
    }

    public void setZnormal(double znormal) {
        Znormal = znormal;
    }

    public double getZdead() {
        return Zdead;
    }

    public void setZdead(double zdead) {
        Zdead = zdead;
    }

    public Map<String, Integer> getT() {
        return T;
    }

    public void setT(Map<String, Integer> t) {
        T = t;
    }

    public double[][] getZV() {
        return ZV;
    }

    public void setZV(double[][] ZV) {
        this.ZV = ZV;
    }

    public double[][] getQZ() {
        return QZ;
    }

    public void setQZ(double[][] QZ) {
        this.QZ = QZ;
    }

    public ArrayList<YRainoff> getyRainoffs() {
        return yRainoffs;
    }

    public void setyRainoffs(ArrayList<YRainoff> yRainoffs) {
        this.yRainoffs = yRainoffs;
    }

    public double[] getMonth() {
        return month;
    }

    @Override
    public String toString() {
        return "ConstOut{" +
                "N=" + N +
                ", k=" + k +
                ", Hlost=" + Hlost +
                ", Znormal=" + Znormal +
                ", Zdead=" + Zdead +
                ", T=" + T +
                ", ZV=" + Arrays.toString(ZV) +
                ", QV=" + Arrays.toString(QZ) +
                ", yRainoff=" + yRainoffs +
                '}';
    }
}
