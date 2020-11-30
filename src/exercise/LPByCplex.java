package exercise;

import domain.ConstOut;
import utils.ListUtils;
import utils.XmlUtils;

import java.util.ArrayList;


public class LPByCplex {
    static ConstOut constOut = XmlUtils.readForConstOutRegu("./xmlFiles/constOutRegu");

    public static void main(String[] args) {

        double[][] count = new double[12][11];
        // 设置水利年起始月份
        int month_start = 5;
        ArrayList<Integer> month = new ArrayList<>(11);
        for (int i = 0; i < 12; i++) {
            if(month_start+i>12){
                month.add(month_start+i-12);
                continue;
            }
            month.add(month_start+i);
        }
        for (int i = 0; i < month.size(); i++) {
            count[i][0] = constOut.getyRainoffs().get(month.get(i) - 1).getRainoff();
        }
        double vmax = 26.7, vmin = 7.9, nMax = 170, nMin = 0;
        double outqMax = 700, outqmin = 10, powerMaxq = 500;
        double qOut;
        double step = 0.05;
        double[] vStates = getVstate(vmin, vmax, step);
        for (int i = 0; i < month.size(); i++) {
            double Nbest = 0, vbegin, vend=0;
            if (i == 0) {
                vbegin = 10;
            } else {
                vbegin = count[i - 1][4];
            }
            double zbegin = ListUtils.getInterpolation(constOut.getZV(), vbegin, 1);
            double funValue = Double.MIN_VALUE;
            for (int j = 0; j < vStates.length; j++) {
                qOut = constOut.getyRainoffs().get(month.get(i) - 1).getRainoff() - (vStates[j] - vbegin) * 1e8 / (constOut.getMonth()[month.get(i) - 1] * 86400);
                // 出库流量约束
                if (qOut > outqMax || qOut < outqmin) {
//                    System.out.println("不符合标准" + qOut);
                    continue;
                }
                // 获取发电量
                double qPower = 0;
                double lostWater = 0;
                if (qOut <= powerMaxq) {
                    qPower = qOut;
                } else {
                    qPower = powerMaxq;
                    lostWater = qOut - powerMaxq;
                    System.out.println("产生弃水" + lostWater);
                }
                if(Math.abs(vStates[j]-vbegin)>vbegin*0.5){
                    continue;
                }
                // 获取下游水位
                double zdown = ListUtils.getInterpolation(constOut.getQZ(), qOut, 0);
                // 获取水头
                double H = 0.5 * (zbegin + ListUtils.getInterpolation(constOut.getZV(), vStates[j], 1)) - zdown - constOut.getHlost();
                double N = constOut.getK() * qPower * H / 10000;
                // 出力约束
                if (N > nMax || N < nMin) {
                    continue;
                }
                // 设置目标函数
                double count10;
                if (i == 0) {
                    count10 = 0;
                } else {
                    count10 = count[i - 1][10];
                }
                double w = 0;
                w = w + N * constOut.getMonth()[month.get(i) - 1] * 24 + count10;
                if (w > funValue) {
                    funValue = w;
                    Nbest = N;
                    vend = vStates[j];
                }
            }
            count[i][2] = vend - vbegin;
            count[i][1] = count[i][0] - count[i][2] * 1e8 / (constOut.getMonth()[month.get(i) - 1] * 86400);
            System.out.println(count[i][2]);
            count[i][3] = vbegin;
            count[i][4] = vend;
            count[i][5] = (vbegin + vend) / 2;
            count[i][6] = ListUtils.getInterpolation(constOut.getZV(), count[i][6], 1);
            count[i][7] = ListUtils.getInterpolation(constOut.getQZ(), count[i][2], 0);
            count[i][8] = count[i][7] - count[i][8] - constOut.getHlost();
            count[i][9] = Nbest;
            count[i][10] = funValue;
        }

        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[0].length; j++) {
                System.out.print(String.format("%.2f", count[i][j]) + " ");
            }
            System.out.println();
        }
    }


    private static double[] getVstate(double zmin, double zmax, double rate) {
        double result[] = new double[(int) ((zmax - zmin) / rate + 1)];
        for (int i = 0; i < result.length; i++) {
            result[i] = zmin + i * rate;
        }
        return result;
    }
}
