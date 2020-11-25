package exercise;

import domain.ConstOut;
import utils.ListUtils;
import utils.XmlUtils;

import java.util.ArrayList;


public class LPByCplex {
    static ConstOut constOut = XmlUtils.readForConstOutRegu("F:\\Tech_task\\constOutRegu");

    public static void main(String[] args) {

        double[][] count = new double[12][12];
        ArrayList<Integer> month = new ArrayList<>(12);
        month.add(5);
        month.add(6);
        month.add(7);
        month.add(8);
        month.add(9);
        month.add(10);
        month.add(11);
        month.add(12);
        month.add(1);
        month.add(2);
        month.add(3);
        month.add(4);
        ArrayList<Integer> month_order = month;
        for (int i = 0; i < month.size(); i++) {
            count[i][0] = constOut.getyRainoffs().get(month_order.get(i) - 1).getRainoff();
        }
        double vmax = 26.7, vmin = 7.9, nMax = 70, nMin = 10;
        double outqMax = 700, outqmin = 100, powerMaxq = 500;

        double step = 0.05;
        double[] vStates = getVstate(vmin, vmax, step);
        System.out.println(vStates);
        ArrayList<Double> nSum = new ArrayList<>();
        for (int i = 0; i < month.size(); i++) {
            double wMax = 0, Nbest = 0;
            double vbegin, vend = 0;
            if (i == 0) {
                vbegin = vmax;
            } else {
                vbegin = count[i - 1][3];//随表格记得改
            }
            double zbegin = ListUtils.getInterpolation(constOut.getZV(), vbegin, 1);
            for (int j = 0; j < vStates.length; j++) {
                double qOut = constOut.getyRainoffs().get(month_order.get(i) - 1).getRainoff() - (vStates[i] - vbegin) * 1e8 / (constOut.getMonth()[month_order.get(i) - 1] * 86400);
                // 出库流量约束
                if (qOut > outqMax || qOut < outqmin) {
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
                // 获取下游水位
                double zdown = ListUtils.getInterpolation(constOut.getQZ(), qOut, 0);
                // 获取水头
                double H = 0.5 * (zbegin + ListUtils.getInterpolation(constOut.getZV(), vStates[j], 1)) - zdown - constOut.getHlost();
                double N = constOut.getK() * qPower * H / 10000;
                // 出力约束
                if (N > nMax || N < nMin) {
                    continue;
                }
                double w = N * constOut.getMonth()[month_order.get(i) - 1] / 3600 + count[i - 1][11];
                if (w > wMax) {
                    w = wMax;
                    vend = vStates[j];
                    Nbest = N;
                }
            }
            if (i == 0) {
                count[i][11] = wMax;
            } else {
                count[i][11] = count[i - 1][11] + wMax;
            }
            count[i][3] = vend - vbegin;
            count[i][2] = count[i][0] - count[i][3] * 1e8 / (constOut.getMonth()[month_order.get(i) - 1] * 86400);
            count[i][1] = count[i][0] - count[i][3] * 1e8 / (constOut.getMonth()[month_order.get(i) - 1] * 86400);
            count[i][4] = vbegin;
            count[i][5] = vend;
            count[i][6] = (vbegin + vend) / 2;
            count[i][7] = ListUtils.getInterpolation(constOut.getZV(), count[i][6], 1);
            count[i][8] = ListUtils.getInterpolation(constOut.getQZ(), count[i][2], 0);
            count[i][9] = count[i][8] - count[i][7] - constOut.getHlost();
            count[i][10] = Nbest;
        }

        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[0].length; j++) {
                System.out.print(count[i][j] + " ");
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
