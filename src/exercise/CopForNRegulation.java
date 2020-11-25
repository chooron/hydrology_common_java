package exercise;

import domain.ConstOut;
import utils.ExcelUtils;
import utils.ListUtils;
import utils.XmlUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * 规定出力的水能计算
 */
public class CopForNRegulation {
    static double T = 30.4 * 24 * 3600;

    public static void main(String[] args) {
        ConstOut constOut = XmlUtils.readForConstOutRegu("F:\\Tech_task\\xmlFiles\\constOutRegu");
        // 三种方案进行出力计算
        double[][] result1 = positiveCalcu(constOut);
        ExcelUtils.submitCalcuForCOR(result1, orderForMonth(getMonth_gs(constOut), 12), "F:\\Tech_task\\results\\正序求解.xls");
        double[][] result2 = negativeCalcu(constOut);
        ExcelUtils.submitCalcuForCOR(result2, orderForMonth(getMonth_gs(constOut), 12), "F:\\Tech_task\\results\\逆序求解.xls");
        double[][] result3 = zFeaturegetN(constOut);
        ExcelUtils.submitCalcuForCOR(result3, orderForMonth(getMonth_gs(constOut), 12), "F:\\Tech_task\\results\\Z正Z死求N.xls");
    }

    private static double[][] zFeaturegetN(ConstOut constOut) {
        // 先估算N大概值
        double vnormal = ListUtils.getInterpolation(constOut.getZV(), constOut.getZnormal(), 0);
        double vdead = ListUtils.getInterpolation(constOut.getZV(), constOut.getZdead(), 0);
        double zup = ListUtils.getInterpolation(constOut.getZV(), (vnormal + vdead) / 2, 1);
        double v = (vnormal - vdead) * 1e8 / T;
        Map<Double, ArrayList<Integer>> resultMap = IsoForNRegulation.IsoRegulation(constOut.getyRainoffs(), 6, 6, v);
        Iterator<Double> iterator = resultMap.keySet().iterator();
        ArrayList<Double> keyList = new ArrayList<>();
        while (iterator.hasNext()) {
            keyList.add(iterator.next());
        }
        double Q_gs = (double) ListUtils.getMin(keyList).get("value");
        double zdown = ListUtils.getInterpolation(constOut.getQZ(), Q_gs, 0);
        double N = (zup - zdown - constOut.getHlost()) * Q_gs * constOut.getK() / 10000;
        double zdeadBycount = 0.0;
        ConstOut tempConstOut = new ConstOut(N, constOut.getK(), constOut.getHlost(), constOut.getZnormal(), constOut.getZdead(), constOut.getT(), constOut.getZV(), constOut.getQZ(), constOut.getyRainoffs());
        int a = 1;
        ArrayList<Integer> month_gs = getMonth_gs(constOut);
        while (Math.abs(zdeadBycount - tempConstOut.getZdead()) > 0.1) {
            double[][] calcu = positiveCalcu(tempConstOut);
            double vdeadBycount = calcu[calcu.length - 1][3] + calcu[calcu.length - 1][2];
            zdeadBycount = ListUtils.getInterpolation(tempConstOut.getZV(), vdeadBycount, 1);
            double rate = 1.0;
            if (Math.abs(zdeadBycount - tempConstOut.getZdead()) < 20 && Math.abs(zdeadBycount - tempConstOut.getZdead()) >= 5) {
                rate *= 0.5;
            } else if (Math.abs(zdeadBycount - tempConstOut.getZdead()) < 5 && Math.abs(zdeadBycount - tempConstOut.getZdead()) >= 1) {
                rate *= 0.3;
            } else if (Math.abs(zdeadBycount - tempConstOut.getZdead()) < 1) {
                rate *= 0.1;
            }
            if (a > 50) {
                rate *= 0.1;
            }
            if (a > 100) {
                break;
            }
            if (zdeadBycount - tempConstOut.getZdead() > 0) {
                N += rate;
            } else {
                N -= rate;
            }
            tempConstOut.setN(N);
            a++;
        }
        double[][] result = positiveCalcu(tempConstOut);
        return result;
    }

    /**
     * 已知 Zdead,N 求调节过程和Zdead
     *
     * @param constOut
     */
    private static double[][] negativeCalcu(ConstOut constOut) {
        ArrayList<Integer> month_gs = getMonth_gs(constOut);
        ArrayList<Integer> month_gs_order = orderForMonth(month_gs, 12);
        double[][] calculate = new double[month_gs.size()][9];
        calculate[month_gs.size() - 1][3] = ListUtils.getInterpolation(constOut.getZV(), constOut.getZdead(), 0);
        for (int i = month_gs.size() - 1; i >= 0; i--) {
            calculate[i][0] = constOut.getyRainoffs().get(month_gs_order.get(i) - 1).getRainoff();
            calculate[i][1] = trialFormethod(calculate[i][0], calculate[i][3], constOut.getN(), constOut.getZV(), constOut.getQZ(), calculate[i][0] + 30, 1, constOut.getHlost(), "negative");
            calculate[i][2] = (calculate[i][0] - calculate[i][1]) * T / 1e8;
            if (i > 0) {
                calculate[i - 1][3] = calculate[i][3] - calculate[i][2];
                calculate[i][4] = (calculate[i - 1][3] + calculate[i][3]) / 2;
            } else {
                calculate[i][4] = (calculate[i][3] - calculate[i][2] + calculate[i][3]) / 2;
            }
            calculate[i][5] = ListUtils.getInterpolation(constOut.getZV(), calculate[i][4], 1);
            calculate[i][6] = ListUtils.getInterpolation(constOut.getQZ(), calculate[i][1], 0);
            calculate[i][7] = calculate[i][5] - calculate[i][6] - constOut.getHlost();
            calculate[i][8] = calculate[i][7] * 8.2 * calculate[i][1] / 10000;
        }
/*        for (int i = 0; i < calculate.length; i++) {
            for (int j = 0; j < calculate[0].length; j++) {
                System.out.print(calculate[i][j] + " ");
            }
            System.out.println();
        }*/
        return calculate;
    }

    /**
     * 已知 Znormal,N 求调节过程和Zdead
     *
     * @param constOut 出力计算对象
     */
    public static double[][] positiveCalcu(ConstOut constOut) {
        // 确定供水期
        ArrayList<Integer> month_gs = getMonth_gs(constOut);
        // 供水期次序调整
        ArrayList<Integer> month_gs_order = orderForMonth(month_gs, 12);
        double[][] calculate = new double[month_gs.size()][9];
        calculate[0][3] = ListUtils.getInterpolation(constOut.getZV(), constOut.getZnormal(), 0);
        for (int i = 0; i < month_gs.size(); i++) {
            calculate[i][0] = constOut.getyRainoffs().get(month_gs_order.get(i) - 1).getRainoff();
            calculate[i][1] = trialFormethod(calculate[i][0], calculate[i][3], constOut.getN(), constOut.getZV(), constOut.getQZ(), calculate[i][0] + 30, 1, constOut.getHlost(), "positive");
            calculate[i][2] = (calculate[i][0] - calculate[i][1]) * T / 1e8;
            if (i + 1 < month_gs.size()) {
                calculate[i + 1][3] = calculate[i][3] + calculate[i][2];
                calculate[i][4] = (calculate[i + 1][3] + calculate[i][3]) / 2;
            } else if (i + 1 == month_gs.size()) {
                calculate[i][4] = (calculate[i][3] + calculate[i][2] + calculate[i][3]) / 2;
            }
            calculate[i][5] = ListUtils.getInterpolation(constOut.getZV(), calculate[i][4], 1);
            calculate[i][6] = ListUtils.getInterpolation(constOut.getQZ(), calculate[i][1], 0);
            calculate[i][7] = calculate[i][5] - calculate[i][6] - constOut.getHlost();
            calculate[i][8] = calculate[i][7] * 8.2 * calculate[i][1] / 10000;
        }
        for (int i = 0; i < calculate.length; i++) {
            for (int j = 0; j < calculate[0].length; j++) {
                System.out.print(calculate[i][j] + " ");
            }
            System.out.println();
        }
        return calculate;
    }


    /**
     * 试错法计算
     *
     * @param Q     天然流量
     * @param v     时段初蓄水
     * @param N     出力
     * @param zv    zv关系曲线
     * @param qz    qv关系曲线
     * @param q     引用流量
     * @param rate  更新因子
     * @param hlost 损失水头
     * @param desc  计算类型
     * @return 引用流量迭代结果
     */
    private static double trialFormethod(double Q, double v, double N, double[][] zv, double[][] qz, double q, double rate, double hlost, String desc) {
        double vchange, vend, vavg, zup, zdown, result, vbefore;
        switch (desc) {
            case "positive":
                vchange = (Q - q) * T / 1e8;
                vend = v + vchange;
                vavg = (vend + v) / 2;
                zup = ListUtils.getInterpolation(zv, vavg, 1);
                zdown = ListUtils.getInterpolation(qz, q, 0);
                result = Math.abs(N - (zup - zdown - hlost) * 8.2 * q / 10000);
                while (result > 0.01) {
                    if (result > 30) {
                        rate = 5;
                    } else if (result <= 30 && result > 10) {
                        rate = 3;
                    } else if (result < 10 && result > 5) {
                        rate = 0.4;
                    } else if (result <= 5 && result >= 1) {
                        rate = 0.3;
                    } else if (result < 1 && result > 0.1) {
                        rate = 0.1;
                    }
                    if (N - (zup - zdown - hlost) * 8.2 * q / 10000 > 0) {
                        q = q + rate;
                    } else if (N - (zup - zdown - hlost) * 8.2 * q / 10000 < 0) {
                        q = q - rate;
                    }
                    vchange = (Q - q) * T / 1e8;
                    vend = v + vchange;
                    vavg = (vend + v) / 2;
                    zup = ListUtils.getInterpolation(zv, vavg, 1);
                    zdown = ListUtils.getInterpolation(qz, q, 0);
                    result = Math.abs(N - (zup - zdown - hlost) * 8.2 * q / 10000);
                }
                break;
            case "negative":
                vchange = (Q - q) * T / 1e8;
                vbefore = v - vchange;
                vavg = (vbefore + v) / 2;
                zup = ListUtils.getInterpolation(zv, vavg, 1);
                zdown = ListUtils.getInterpolation(qz, q, 0);
                result = Math.abs(N - (zup - zdown - hlost) * 8.2 * q / 10000);
                while (result > 0.01) {
                    if (result > 30) {
                        rate = 5;
                    } else if (result <= 30 && result > 10) {
                        rate = 3;
                    } else if (result < 10 && result > 5) {
                        rate = 0.4;
                    } else if (result <= 5 && result >= 1) {
                        rate = 0.3;
                    } else if (result < 1 && result > 0.1) {
                        rate = 0.1;
                    }
                    if (N - (zup - zdown - hlost) * 8.2 * q / 10000 > 0) {
                        q = q + rate;
                    } else if (N - (zup - zdown - hlost) * 8.2 * q / 10000 < 0) {
                        q = q - rate;
                    }
                    vchange = (Q - q) * T / 1e8;
                    vbefore = v - vchange;
                    vavg = (vbefore + v) / 2;
                    zup = ListUtils.getInterpolation(zv, vavg, 1);
                    zdown = ListUtils.getInterpolation(qz, q, 0);
                    result = Math.abs(N - (zup - zdown - hlost) * 8.2 * q / 10000);
                }
                break;
        }
        return q;
    }

    /**
     * 调整供水期月份顺序
     *
     * @param month_gs 供水期
     * @param max      最大月份
     * @return
     */
    static ArrayList<Integer> orderForMonth(ArrayList<Integer> month_gs, int max) {
        ArrayList<Integer> month_gs_order = new ArrayList<>();
        if (month_gs.contains(max)) {
            max--;
            return orderForMonth(month_gs, max);
        } else {
            int count = 0;
            max++;
            if (ListUtils.getMaxForInt(month_gs).get("value") == 12) {
                while (max <= 12) {
                    month_gs_order.add(max);
                    max++;
                    count++;
                }
            }
            for (int i = 0; i < month_gs.size() - count; i++) {
                month_gs_order.add(i + 1);
            }
        }
        return month_gs_order;
    }

    /**
     * 获取供水期
     *
     * @param constOut 出力计算对象
     * @return
     */
    public static ArrayList<Integer> getMonth_gs(ConstOut constOut) {
        double v = (ListUtils.getInterpolation(constOut.getZV(), constOut.getZnormal(), 0) - ListUtils.getInterpolation(constOut.getZV(), constOut.getZdead(), 0)) * 1e8 / T;
        Map<Double, ArrayList<Integer>> resultMap = IsoForNRegulation.IsoRegulation(constOut.getyRainoffs(), 6, 6, v);
        Iterator<Double> iterator = resultMap.keySet().iterator();
        ArrayList<Double> keyList = new ArrayList<>();
        while (iterator.hasNext()) {
            keyList.add(iterator.next());
        }
        Map<String, Object> min = ListUtils.getMin(keyList);
        ArrayList<Integer> month_gs = resultMap.get(min.get("value"));
        return month_gs;
    }
}
