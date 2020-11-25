package domain;

import java.util.ArrayList;

public class MaskingenMode {
    // 马斯京根模型参数
    double x;//流量比重系数
    double K;//蓄量流量关系曲线坡度
    ArrayList<UnitLine> unitLines;//获取单位线的地面入流及时间的数据
    ArrayList<Double> realQs; //实际出流过程

    public MaskingenMode(double x, double k,  ArrayList<UnitLine> unitLines, ArrayList<Double> realQs) {
        this.x = x;
        K = k;
        this.unitLines = unitLines;
        this.realQs = realQs;
    }

    public MaskingenMode() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getK() {
        return K;
    }

    public void setK(double k) {
        K = k;
    }


    public ArrayList<UnitLine> getUnitLines() {
        return unitLines;
    }

    public void setUnitLines(ArrayList<UnitLine> unitLines) {
        this.unitLines = unitLines;
    }

    public ArrayList<Double> getRealQs() {
        return realQs;
    }

    public void setRealQs(ArrayList<Double> realQs) {
        this.realQs = realQs;
    }

    @Override
    public String toString() {
        return "MaskingenMode{" +
                "x=" + x +
                ", K=" + K +
                ", realQs=" + realQs +
                ", unitLines=" + unitLines +
                '}';
    }
}
