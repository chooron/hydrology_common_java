package exercise;

import domain.YsRainoff;
import utils.ExcelUtils;
import utils.ListUtils;
import utils.XmlUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * 不连续样本的水文频率计算
 */
public class hydroFreq {

    public static void main(String[] args) {
        String xmlPath = "./xmlFiles/hydroFreq.xml";
        ArrayList<YsRainoff> ysRainoffs = XmlUtils.readForHydroFre(xmlPath);
        ArrayList<Object> paraList = XmlUtils.readForHydroFrePara(xmlPath);
        int N1 = (int) paraList.get(0);
        int N2 = (int) paraList.get(1);
        int n = (int) paraList.get(2);
        double t1 = (double) paraList.get(3);
        double t2 = (double) paraList.get(4);
        ArrayList<Double> rainoffs = new ArrayList<>();
        double avg = ysRainoffs.stream().mapToDouble(YsRainoff::getRainoff).average().getAsDouble();

        ArrayList<Integer> items = new ArrayList<>();
        items.add(1);  // 特大级个数
        items.add(1);  // 较大级个数
        items.add(1);  // 平水级个数
        items.add(0);  // N2期处于特大级的个数
        items.add(0);  // n期处于特大级、较大级个数
        items.add(0);  // n期处于特大级个数
        for (YsRainoff ysRainoff : ysRainoffs) {
            rainoffs.add(ysRainoff.getRainoff());
        }
        ArrayList<YsRainoff> ysRainoffByorder = new ArrayList<>();
//        System.out.println(ysRainoffs);
        orderForRank(ysRainoffs, rainoffs, ysRainoffByorder, avg, items, t1, t2);
        double sum1 = 0, sum2 = 0, sum3 = 0;
        for (int i = 0; i < items.get(0) - 1; i++) {
            ysRainoffByorder.get(i).setFreq(ysRainoffByorder.get(i).getRank() / (N1 + 1));
            sum1 += ysRainoffByorder.get(i).getRainoff();
        }
        for (int i = items.get(0) - 1; i < items.get(0) + items.get(1) - 2; i++) {
            ysRainoffByorder.get(i).setRank((int) (ysRainoffByorder.get(i).getRank() + items.get(3)));
            ysRainoffByorder.get(i).setFreq(ysRainoffByorder.get(i).getRank() / (N2 + 1));
            sum2 += ysRainoffByorder.get(i).getRainoff();
        }
        for (int i = items.get(0) + items.get(1) - 2; i < ysRainoffByorder.size(); i++) {
            ysRainoffByorder.get(i).setRank((int) (ysRainoffByorder.get(i).getRank() + items.get(4)));
            ysRainoffByorder.get(i).setFreq(ysRainoffByorder.get(i).getRank() / (n + 1));
            sum3 += ysRainoffByorder.get(i).getRainoff();
        }
        double Q_avg = (sum1 + sum2 + sum3 / (n - items.get(0) - items.get(1)) * (N1 - items.get(0) + items.get(1))) / N1;
        double cv = 0.0;
//        Q_avg = 2142;
        for (int i = 0; i < items.get(0) + items.get(1) - 2; i++) {
            System.out.println(ysRainoffByorder.get(i).getDescribe());
            cv += Math.pow(ysRainoffByorder.get(i).getRainoff() / Q_avg - 1, 2);
        }
        for (int i = items.get(0) + items.get(1) - 2; i < ysRainoffByorder.size(); i++) {
            cv += Math.pow(ysRainoffByorder.get(i).getRainoff() / Q_avg - 1, 2) / (n - items.get(0) - items.get(1)) * (N1 - items.get(0) + items.get(1));
        }
        cv = Math.sqrt(cv / (N1 - 1));
        System.out.println("Q均值为：" + Q_avg + "," + "方差为：" + cv);
//        System.out.println(ysRainoffByorder);
        System.out.println("序号  |  年份   |  年径流量  |  经验频率");
        for (int i = 0; i < ysRainoffByorder.size(); i++) {
            System.out.println(i+1+" | "+ysRainoffByorder.get(i).getYear()+" | "+ysRainoffByorder.get(i).getRainoff()+" | "+ysRainoffByorder.get(i).getFreq());
        }
        double point = 5;
        // 三点法
//        ArrayList<Double> freqParas = threePointForFreq(ysRainoffByorder, point);
        ExcelUtils.submitForhydroFreq(ysRainoffByorder,"./results/hydroFrqResult.xls");
    }

    private static ArrayList<Double> threePointForFreq(ArrayList<YsRainoff> ysRainoffs, double point) {
        ArrayList<Double> freqParas = new ArrayList<>();
        double[][] data = new double[ysRainoffs.size()][2];
        for (int i = 0; i < ysRainoffs.size(); i++) {
            data[i][0] = ysRainoffs.get(i).getFreq();
            data[i][1] = ysRainoffs.get(i).getRainoff();
        }
        double Q1 = ListUtils.getInterpolation(data, point, 0);
        double Q2 = ListUtils.getInterpolation(data, 50, 0);
        double Q3 = ListUtils.getInterpolation(data, 100 - point, 0);
        double S = (Q1 + Q3 - 2 * Q2) / (Q1 - Q3);
        double theta1 = ExcelUtils.getFreq3ForhydroFreq(point, S, "");
        double theta2 = ExcelUtils.getFreq3ForhydroFreq(50, S, "");
        double theta3 = ExcelUtils.getFreq3ForhydroFreq(1 - point, S, "");
        double var = (Q2 - Q3) / (theta1 - theta3);
        double avgQ = Q2 - var * theta2;
        double cv = var / avgQ;
        freqParas.add(avgQ);
        freqParas.add(cv);
        return freqParas;
    }

    /**
     * 多年流量排序
     *
     * @param ysRainoffs       多年流量对象数组
     * @param rainoffs         多年流量值数组
     * @param ysRainoffByorder 多年流量排序后的对象数组
     * @param avg              暂估多年流量均值
     * @param items            统计各量级流量出现频次
     * @param t1               判断是否为特大级流量
     * @param t2               判断是否为较大级流量
     * @return 排序后的结果
     */
    public static ArrayList<YsRainoff> orderForRank(ArrayList<YsRainoff> ysRainoffs, ArrayList<Double> rainoffs,
                                                    ArrayList<YsRainoff> ysRainoffByorder, double avg, ArrayList<Integer> items, double t1, double t2) {
        if ((double) ListUtils.getMax(rainoffs).get("value") == 0.0) {
            return null;
        }
        Map<String, Object> maxMap = ListUtils.getMax(rainoffs);
        YsRainoff maxYsRainoff = ysRainoffs.get((int) maxMap.get("index"));
        if ((double) maxMap.get("value") >= t1 * avg) {
            System.out.println(maxYsRainoff.getYear() + "年流量为特大级");
            switch (maxYsRainoff.getDescribe()) {
                case "N1":
                    maxYsRainoff.setRank(items.get(0));
                    break;
                case "N2":
                    maxYsRainoff.setRank(items.get(0));
                    items.set(3, items.get(3) + 1);
                    break;
                case "N3":
                    maxYsRainoff.setRank(items.get(0));
                    items.set(3, items.get(3) + 1);
                    items.set(4, items.get(4) + 1);
                    items.set(5, items.get(5) + 1);
                    break;
            }
            items.set(0, items.get(0) + 1);
        } else if ((double) maxMap.get("value") >= t2 * avg && (double) maxMap.get("value") < t1 * avg) {
            System.out.println(maxYsRainoff.getYear() + "年流量为较大级");
            switch (maxYsRainoff.getDescribe()) {
                case "N1":
                    maxYsRainoff.setRank(items.get(1));
                    break;
                case "N2":
                    maxYsRainoff.setRank(items.get(1));
                    break;
                case "N3":
                    maxYsRainoff.setRank(items.get(1));
                    items.set(4, items.get(4) + 1);
                    break;
            }
            items.set(1, items.get(1) + 1);
        } else {
            System.out.println(maxYsRainoff.getYear() + "年流量为平水级");
            switch (maxYsRainoff.getDescribe()) {
                case "N1":
                    maxYsRainoff.setRank(items.get(2));
                    break;
                case "N2":
                    maxYsRainoff.setRank(items.get(2));
                    break;
                case "N3":
                    maxYsRainoff.setRank(items.get(2));
                    break;
            }
            items.set(2, items.get(2) + 1);
        }
        rainoffs.set((Integer) maxMap.get("index"), 0.0);
        ysRainoffByorder.add(maxYsRainoff);
        orderForRank(ysRainoffs, rainoffs, ysRainoffByorder, avg, items, t1, t2);
        return ysRainoffByorder;
    }


}
