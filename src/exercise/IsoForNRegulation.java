package exercise;

import domain.ConstOut;
import domain.YRainoff;
import utils.ExcelUtils;
import utils.ListUtils;
import utils.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 等流量的水能调节
 */
public class IsoForNRegulation {
    public static void main(String[] args) throws IOException {
        ConstOut constOut;
        constOut = XmlUtils.readForConstOutRegu("./xmlFiles/constOutRegu");
        //-------------- 插值计算曲线准备 --------------
        double[][] ZV = constOut.getZV();
        double[][] QZ = constOut.getQZ();
        double hlost = constOut.getHlost();
        double Vdead = ListUtils.getInterpolation(ZV, constOut.getZdead(), 0);
        double Vnormal = ListUtils.getInterpolation(ZV, constOut.getZnormal(), 0);
        double T = 30.4 * 24 * 3600;
        double V = (Vnormal - Vdead) * 1e8 / (30.4 * 24 * 3600);
        System.out.println(V);
        //-------------- 引用流量计算 --------------
        ArrayList<YRainoff> yRainoffs = constOut.getyRainoffs();
        Map<Double, ArrayList<Integer>> result = IsoRegulation(yRainoffs, 6, 6, V);
        ArrayList<Double> Qs = new ArrayList(result.keySet());
        double Q_gs = Qs.get(0);
        ArrayList<Integer> month_gs = result.get(Q_gs);
        double Q_xs = Qs.get(1);
        ArrayList<Integer> month_xs = result.get(Q_xs);
        ArrayList<Double> Q_use = new ArrayList<>();
        for (YRainoff yRainoff : yRainoffs) {
            Q_use.add(yRainoff.getRainoff());
        }
        for (Integer month_g : month_gs) {
            Q_use.set(month_g - 1, Q_gs);
        }
        for (Integer month_x : month_xs) {
            Q_use.set(month_x - 1, Q_xs);
        }
        System.out.println(Q_use);
        int start = 1;
        for (int i = 0; i < Q_use.size() - 1; i++) {
            if (Q_use.get(i + 1) - Q_use.get(i) > Q_use.get(i)) {//规则自定
                start = i + 1;
                break;
            }
        }
        ArrayList<Integer> month_use = new ArrayList<>();
        for (int i = start - 1; i < yRainoffs.size(); i++) {
            month_use.add(yRainoffs.get(i).getMonth());
        }
        for (int i = 0; i < start - 1; i++) {
            month_use.add(yRainoffs.get(i).getMonth());
        }
        ArrayList<Double> Q_final = new ArrayList<>();
        for (Integer m : month_use) {
            Q_final.add(Q_use.get(m-1));
        }
        //-------------- 出力计算 --------------
        double[][] answer = new double[12][9];
        answer[0][3] = Vdead;
        answer[0][4] = Vdead;
        for (int i = 0; i < answer.length; i++) {
            answer[i][0] = yRainoffs.get(month_use.get(i) - 1).getRainoff();
            answer[i][1] = Q_final.get(i);
            answer[i][2] = (answer[i][0] - answer[i][1]) * T / 1e8;
            if (i < answer.length - 1) {
                answer[i + 1][3] = answer[i][3]+answer[i][2];
                answer[i+1][4] = (answer[i + 1][3]+answer[i][3])/2;
            }
            answer[i][5] = ListUtils.getInterpolation(ZV,answer[i][4],1);
            answer[i][6] = ListUtils.getInterpolation(QZ,answer[i][1],0);
            answer[i][7] = answer[i][5]-answer[i][6]-hlost;
            answer[i][8] = constOut.getK()*answer[i][7]*answer[i][1]/10000;
        }
        //-------------- 创建表格输出结果 --------------
        ExcelUtils.submitCalcuForCOR(answer,month_use,"./results/等流量水能调节.xls");
    }


    /**
     * 等流量调节(用水需求未知，用于出力计算)
     *
     * @param yRainoffs 来水过程
     * @param t_gs      供水期
     * @param t_xs      蓄水期
     * @param v         兴利库容
     * @return 调节结果
     */
    public static Map<Double, ArrayList<Integer>> IsoRegulation(ArrayList<YRainoff> yRainoffs, int t_gs, int t_xs, double v) {
        ArrayList<Double> minRainoffs = new ArrayList<>();
        ArrayList<Integer> minMonths = new ArrayList<>();
        ArrayList<Double> maxRainoffs = new ArrayList<>();
        ArrayList<Integer> maxMonths = new ArrayList<>();
        ArrayList<Double> tempRainoff_gs = new ArrayList<>();
        ArrayList<Integer> tempMonth_gs = new ArrayList<>();
        ArrayList<Double> tempRainoff_xs = new ArrayList<>();
        ArrayList<Integer> tempMonth_xs = new ArrayList<>();
        for (int i = 0; i < yRainoffs.size(); i++) {
            tempRainoff_gs.add(yRainoffs.get(i).getRainoff());
            tempMonth_gs.add(yRainoffs.get(i).getMonth());
            tempRainoff_xs.add(yRainoffs.get(i).getRainoff());
            tempMonth_xs.add(yRainoffs.get(i).getMonth());
        }
        for (int i = 0; i < t_gs; i++) {
            minRainoffs.add((Double) ListUtils.getMin(tempRainoff_gs).get("value"));
            int index = (Integer) ListUtils.getMin(tempRainoff_gs).get("index");
            minMonths.add(tempMonth_gs.get(index));
            tempRainoff_gs.remove(index);
            tempMonth_gs.remove(index);
        }
        for (int i = 0; i < t_xs; i++) {
            maxRainoffs.add((Double) ListUtils.getMax(tempRainoff_xs).get("value"));
            int index = (Integer) ListUtils.getMax(tempRainoff_xs).get("index");
            maxMonths.add(tempMonth_xs.get(index));
            tempRainoff_xs.remove(index);
            tempMonth_xs.remove(index);
        }
        ArrayList<Integer> temp1 = new ArrayList<>();
        // 水库为一次供水过程
        for (int i = 0; i < tempMonth_gs.size(); i++) {
            for (int j = 0; j < tempMonth_gs.size(); j++) {
                if (i != j) {
                    temp1.add(Math.abs(tempMonth_gs.get(i) - tempMonth_gs.get(j)));
                }
            }
            if ((Integer) ListUtils.getMinForInt(temp1).get("value") > 1 && (Integer) ListUtils.getMinForInt(temp1).get("value") != 11) {
                minMonths.remove(i);
                minRainoffs.remove(i);
                t_gs--;
            }
        }
        ArrayList<Integer> temp2 = new ArrayList<>();
        for (int i = 0; i < tempMonth_xs.size(); i++) {
            for (int j = 0; j < tempMonth_xs.size(); j++) {
                if (i != j) {
                    temp2.add(Math.abs(tempMonth_xs.get(i) - tempMonth_xs.get(j)));
                }
            }
            if ((Integer) ListUtils.getMinForInt(temp2).get("value") > 1 && (Integer) ListUtils.getMinForInt(temp2).get("value") != 11) {
                maxMonths.remove(i);
                maxRainoffs.remove(i);
                t_xs--;
            }
        }
        ArrayList<Double> otherRainoff_gs = new ArrayList<>();
        ArrayList<Double> otherRainoff_xs = new ArrayList<>();
        for (YRainoff yRainoff : yRainoffs) {
            int flag_gs = 0, flag_xs = 0;
            for (Integer minMonth : minMonths) {
                if (yRainoff.getMonth() == minMonth) {
                    flag_gs = 1;
                }
            }
            for (Integer maxMonth : maxMonths) {
                if (yRainoff.getMonth() == maxMonth) {
                    flag_xs = 1;
                }
            }
            if (flag_gs == 0) {
                otherRainoff_gs.add(yRainoff.getRainoff());
            }
            if (flag_xs == 0) {
                otherRainoff_xs.add(yRainoff.getRainoff());
            }
        }
        double Q_gs = (ListUtils.sum(minRainoffs) + v) / t_gs;
        double Q_xs = (ListUtils.sum(maxRainoffs) - v) / t_xs;
        Map<Double, ArrayList<Integer>> result = new HashMap<>();
        if (Q_xs > (double) ListUtils.getMax(otherRainoff_xs).get("value") && Q_xs < (double) ListUtils.getMin(maxRainoffs).get("value")
                && Q_gs < (double) ListUtils.getMin(otherRainoff_gs).get("value") && Q_gs > (double) ListUtils.getMax(minRainoffs).get("value")) {
/*          System.out.print("供水期假设合理,为：");
            for (int i = 0; i < minMonths.size(); i++) {
                System.out.print(minMonths.get(i) + "月" + Q_gs + " ");
            }
            System.out.println();
            System.out.print("蓄水期假设合理,为：");
            for (int i = 0; i < maxMonths.size(); i++) {
                System.out.print(maxMonths.get(i) + "月" + Q_xs + " ");
            }
            System.out.println();*/
            result.put(Q_gs, minMonths);
            result.put(Q_xs, maxMonths);
            return result;
        } else if (Q_gs >= (double) ListUtils.getMin(otherRainoff_gs).get("value")) {
            t_gs++;
            result = IsoRegulation(yRainoffs, t_gs, t_xs, v);
        } else if (Q_gs <= (double) ListUtils.getMax(minRainoffs).get("value")) {
            t_gs--;
            result = IsoRegulation(yRainoffs, t_gs, t_xs, v);
        } else if (Q_xs <= (double) ListUtils.getMax(otherRainoff_xs).get("value")) {
            t_xs++;
            result = IsoRegulation(yRainoffs, t_gs, t_xs, v);
        } else if (Q_xs >= (double) ListUtils.getMin(maxRainoffs).get("value")) {
            t_xs--;
            result = IsoRegulation(yRainoffs, t_gs, t_xs, v);
        }
        return result;
    }
}
