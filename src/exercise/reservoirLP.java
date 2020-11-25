package exercise;


import domain.ConstOut;
import utils.ListUtils;
import utils.XmlUtils;

import java.util.ArrayList;

/**
 * 水库动态规划,暂未做完
 */
public class reservoirLP {
    // 水库优化调度对象
    static ConstOut constOut = XmlUtils.readForConstOutRegu("F:\\Tech_task\\xmlFiles\\constOutRegu");
    //定义集合数组来存取每个阶段的可行状态和对应收益
    static ArrayList AllValidZState[] = new ArrayList[13];
    static ArrayList AllBestProfit[] = new ArrayList[13];
    //定义集合数组来存取每个阶段的最佳决策，收益，弃水,发电流量
    static ArrayList AllBestChoose[] = new ArrayList[12];
    static ArrayList ProfitNowStage[] = new ArrayList[12];
    static ArrayList LostWater[] = new ArrayList[12];
    static ArrayList PowerFlow[] = new ArrayList[12];
    //水位离散步长
    static double rate = 0.5;
    //获取状态变量
    static double[] state = getState(rate);

    // 约束条件
    // 出库流量限制
    static double maxOutq = 700;
    static double minOutq = 100;
    // 发电流量限制
    static double powerMaxq = 200;
    static double powerMinq = 50;
    //最大出力、最小出力限制
    public static double nPowerMax = 70;
    public static double nPowerMin = 10;

    // 定义起调和止调水位
    static double zStart = 750, zEnd = 750;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        dp();
        long endTime = System.currentTimeMillis();
        System.out.println("算法运行时间：" + (endTime - startTime) + "ms");
        System.out.println("最佳收益" + AllBestProfit[12]);

//		for(int month = 0;month<13;month++){
//
//			System.out.println(month+"可行状态变量数为"+AllValidState[month].size());
//			for(int i = 0;i<AllValidState[month].size();i++){
//				System.out.println(AllValidState[month].get(i));
//			}
//		}

        System.out.println("*********************************************");

        System.out.println("第" + 12 + "月末最佳状态:" + AllValidZState[12].get(0));
//		System.out.println("第"+12+"月决策"+AllBestChoose[11].get(0));
        double bestChoose = (Double) AllBestChoose[11].get(0);
//		System.out.println("第"+12+"月末最佳收益"+AllBestProfit[12].get(0));
//		System.out.println("第"+12+"月当月出力:"+ProfitNowStage[11].get(0));
//		System.out.println("第"+12+"月当月发电流量:"+PowerFlow[11].get(0));
//		System.out.println("第"+12+"月当月弃水:"+LostWater[11].get(0));
        for (int month = 11; month > 0; month--) {
            int index = AllValidZState[month].indexOf(bestChoose);
            System.out.println("第" + month + "月末最佳状态:" + AllValidZState[month].get(index));

//			System.out.println("第"+month+"月决策"+AllBestChoose[month-1].get(index));
            bestChoose = (Double) AllBestChoose[month - 1].get(index);

//			System.out.println("第"+month+"月末最佳收益"+AllBestProfit[month].get(index));
//			System.out.println("第"+month+"月当月出力:"+ProfitNowStage[month-1].get(index));
//			System.out.println("第"+month+"月当月发电流量:"+PowerFlow[month-1].get(index));
//			System.out.println("第"+month+"月当月弃水:"+LostWater[month-1].get(index));
        }

        System.out.println("*********************************************");

//		System.out.println("第"+12+"月末最佳状态:"+AllValidState[12].get(0));
//		System.out.println("第"+12+"月决策"+AllBestChoose[11].get(0));
        double bestChoose1 = (Double) AllBestChoose[11].get(0);
//		System.out.println("第"+12+"月末最佳收益"+AllBestProfit[12].get(0));
        System.out.println("第" + 12 + "月当月出力:" + ProfitNowStage[11].get(0));
//		System.out.println("第"+12+"月当月弃水:"+LostWater[11].get(0));
        for (int month = 11; month > 0; month--) {
            int index = AllValidZState[month].indexOf(bestChoose1);
//			System.out.println("第"+month+"月末最佳状态:"+AllValidState[month].get(index));

//			System.out.println("第"+month+"月决策"+AllBestChoose[month-1].get(index));
            bestChoose1 = (Double) AllBestChoose[month - 1].get(index);

//			System.out.println("第"+month+"月末最佳收益"+AllBestProfit[month].get(index));
            System.out.println("第" + month + "月当月出力:" + ProfitNowStage[month - 1].get(index));
//			System.out.println("第"+month+"月当月弃水:"+LostWater[month-1].get(index));

        }
    }

    public static void dp() {
        AllValidZState[0] = new ArrayList<Double>();
        AllBestProfit[0] = new ArrayList<Double>();
        AllValidZState[0].add(constOut.getZnormal());
        AllBestProfit[0].add(0.0);

        for (int month = 0; month < 12; month++) {
            AllValidZState[month + 1] = new ArrayList<Double>();
            AllBestProfit[month + 1] = new ArrayList<Double>();
            AllBestChoose[month] = new ArrayList<Double>();
            ProfitNowStage[month] = new ArrayList<Double>();
            LostWater[month] = new ArrayList<Double>();
            PowerFlow[month] = new ArrayList<Double>();

            // 本月入库流量
            double Q = constOut.getyRainoffs().get(month).getRainoff();
            // 本月时间
            double t = constOut.getMonth()[month] * 86700;
            if (month == 11) {
                double array[] = {zEnd};
                state = array;
            }
            for (int i = 0; i < state.length; i++) {
                double bestProfit = 0;
                double bestChoose = 0;
                double nowProfit = 0;
                double dropWater = 0;
                double q = 0;
                for (int j = 0; j < AllValidZState[month].size(); j++) {
                    //获取始末水位
                    double zBegin = (double) AllValidZState[month].get(j);
                    double zFinal = state[i];
                    //获取始末库容
                    double vBegin = ListUtils.getInterpolation(constOut.getZV(), zBegin, 0);
                    double vFinal = ListUtils.getInterpolation(constOut.getZV(), zFinal, 0);
                    //根据水量平衡获取出库流量q
                    double qOut = constOut.getyRainoffs().get(month).getRainoff() - (vFinal - vBegin) / t;
                    if (qOut > maxOutq || qOut < 0) {
                        System.out.println("出库流量为：" + qOut + " 发电流量约束破坏");
                        continue;
                    }
                    // 获取发电流量
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
                    double H = 0.5 * (zBegin + zEnd) - zdown - constOut.getHlost();
                    // 计算出力
                    double nPower = constOut.getK() * qPower * H / 10000;
                    if (nPower > nPowerMax) {
                        System.out.println("出力为：" + nPower + "最大出力约束破坏");
                        continue;
                    }
                    //----------此时满足所有约束-----------
                    double powerSum = nPower * t / 3600 + (double) AllBestProfit[month].get(j);
                    double punishPower = powerSum - 7.0 * Math.pow(Math.min(nPower - nPowerMin, 0), 2);
                    System.out.println("获取可运行状态");
                    if(punishPower>=bestProfit){
                        bestProfit = punishPower;
                        bestChoose = zBegin;
                        nowProfit = nPower;
                        dropWater = lostWater;
                        q = qPower;
                    }
                    if(bestProfit != 0.0){
                        AllBestProfit[month+1].add(bestProfit);
                        AllValidZState[month+1].add(state[i]);
                        AllBestChoose[month].add(bestChoose);
                        ProfitNowStage[month].add(nowProfit);
                        LostWater[month].add(dropWater);
                        PowerFlow[month].add(q);
                    }
                }
            }
        }


    }

    // 离散水位状态
    private static double[] getState(double rate) {
        double result[] = new double[(int) ((constOut.getZnormal() - constOut.getZdead()) / rate + 1)];
        for (int i = 0; i < result.length; i++) {
            result[i] = constOut.getZdead() + i * rate;
        }
        return result;
    }
}
