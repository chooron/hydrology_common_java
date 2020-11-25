package exercise;

import domain.YDemand;
import domain.YRainoff;
import jxl.read.biff.BiffException;
import utils.ExcelUtils;
import utils.ListUtils;
import utils.XmlUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;


/**
 * topic：等流量调节
 * Author：charon
 */

public class IsoRegulation {
    public static void main(String[] args) throws IOException, BiffException {
        ArrayList<String> parrlist;
        parrlist = XmlUtils.readXmlForIsoRegulation("F:\\Tech_task\\xmlFiles\\isoRegulation.xml");
        String fileName = parrlist.get(0);
        String sheetName = parrlist.get(1);
        int year = Integer.parseInt(parrlist.get(2));
        // 读取excel数据
        ArrayList<YRainoff> yRainoffs = ExcelUtils.getExcelForRainoff(fileName, sheetName, year);
        ArrayList<YDemand> yDemands = ExcelUtils.getExcelForDemand(fileName, sheetName, year);
        // 定流量计算
        // 设置兴利流量
        double irrigationFlow = 50;
        ArrayList<Double> regulation = isoFlowRegulation(yRainoffs, yDemands, irrigationFlow);
        ExcelUtils.submitForIsoRegulation(regulation, fileName, sheetName);
    }

    /**
     * 等流量调节计算(已知用水需求)
     *
     * @param yRainoffs      年内径流对象数组
     * @param yDemands       年内供水需求对象数组
     * @param irrigationFlow 兴利调节流量
     * @return 等流量调节计算结果
     */
    public static ArrayList<Double> isoFlowRegulation(ArrayList<YRainoff> yRainoffs, ArrayList<YDemand> yDemands, double irrigationFlow) {
        int t_gs = 1;
        // 用数组整合对象中的数据
        ArrayList<Double> rainoffs = new ArrayList<>();
        ArrayList<Double> demands = new ArrayList<>();
        ArrayList<Integer> month = new ArrayList<>();
        double totalRain = 0;
        for (YRainoff yRainoff : yRainoffs) {
            rainoffs.add(yRainoff.getRainoff());
            month.add(yRainoff.getMonth());
            totalRain += yRainoff.getRainoff();
        }
        for (YDemand yDemand : yDemands) {
            demands.add(yDemand.getWaterDemand());
        }
        // 判断流量最多连续多少小于等于供水
        // 供水期入库流量
        ArrayList<Double> rainoffs_gs = new ArrayList<>();
        // 供水期用水量
        ArrayList<Double> demands_gs = new ArrayList<>();
        ArrayList<Integer> month_gs = new ArrayList<>();
        System.out.println("----------------供水期期调节过程------------------");
        for (int i = 0; i < rainoffs.size(); i++) {
            if (i == 0) {
                System.out.print("初设供水期为：");
            }
            if (rainoffs.get(i) > demands.get(i)) {
                t_gs = i;
                break;
            }
            rainoffs_gs.add(rainoffs.get(i));
            demands_gs.add(demands.get(i));
            month_gs.add(month.get(i));
            System.out.print(month.get(i) + "月 ");
        }
        System.out.println();
        // 计算供水期放水过程
        double releaseWater_gs = (ListUtils.sum(rainoffs_gs) + irrigationFlow) / (t_gs);
        ArrayList<Double> releaseWaters_gs = new ArrayList<>();
        for (int i = 0; i < rainoffs_gs.size(); i++) {
            releaseWaters_gs.add(releaseWater_gs);
        }
        System.out.println("初设得到的供水过程为：" + releaseWaters_gs);
        // 供水期选择计算
        ArrayList<Double> releaseWaters_gs_final = selectGs(t_gs, month_gs, rainoffs_gs, releaseWaters_gs, demands_gs, releaseWater_gs, irrigationFlow);

        // 计算蓄水期放水过程
        ArrayList<Double> rainoffs_xs = ListUtils.selectByIndexForDouble(rainoffs, t_gs, rainoffs.size() - 1);
        ArrayList<Double> demands_xs = ListUtils.selectByIndexForDouble(demands, t_gs, rainoffs.size() - 1);
        double rainoffs_xs_sum = ListUtils.sum(rainoffs_xs);
        int t_xs = 12 - t_gs;
        double releaseWater_xs = (rainoffs_xs_sum - irrigationFlow) / t_xs;
        ArrayList<Double> releaseWaters_xs = new ArrayList<>();
        System.out.println("----------------蓄水期调节过程------------------");
        System.out.print("初设蓄水期为：");
        ArrayList<Integer> month_xs = ListUtils.selectByIndexForInt(month, t_gs, rainoffs.size() - 1);
        for (int i = 0; i < rainoffs_xs.size(); i++) {
            releaseWaters_xs.add(releaseWater_xs);
            System.out.print(month_xs.get(i) + "月 ");
        }
        System.out.println();
        System.out.println("初设得到的供水过程为：" + releaseWaters_xs);
        // 蓄水期选择计算
        ArrayList<Double> releaseWaters_xs_final = selectXs(t_xs, month_xs, rainoffs_xs, rainoffs_xs_sum, releaseWaters_xs, demands_xs, releaseWater_xs, irrigationFlow);
        // 输出得到年内等流量调节过程
        releaseWaters_gs_final.addAll(releaseWaters_xs_final);
        return releaseWaters_gs_final;
    }

    /**
     * 蓄水期过程计算
     *
     * @param t_xs             蓄水期长度
     * @param month            月份
     * @param rainoffs_xs      蓄水期入库流量
     * @param rainoffs_xs_sum  蓄水期入库总流量
     * @param releaseWaters_xs 初设蓄水期过程
     * @param demands_xs       蓄水期供水需求
     * @param releaseWater_xs  蓄水期计算的平均流量
     * @param irrigationFlow   兴利调节流量
     * @return 蓄水期过程
     */
    public static ArrayList<Double> selectXs(int t_xs, ArrayList<Integer> month, ArrayList<Double> rainoffs_xs, double rainoffs_xs_sum,
                                             ArrayList<Double> releaseWaters_xs, ArrayList<Double> demands_xs, double releaseWater_xs, double irrigationFlow) {
        if (releaseWater_xs <= (Double) ListUtils.getMin(rainoffs_xs).get("value")) {
            System.out.println("满足条件，蓄水期过程为：" + releaseWaters_xs);
        } else {
            Map minRainoffsMap = ListUtils.getMin(rainoffs_xs);
            int minIndex = (int) minRainoffsMap.get("index");
            releaseWaters_xs.set(minIndex, (Double) minRainoffsMap.get("value"));
            rainoffs_xs_sum -= (double) minRainoffsMap.get("value");
            demands_xs.set(minIndex, 10e9);
            rainoffs_xs.set(minIndex, 10e9);
            t_xs--;
            double releaseWater_xs_next = (rainoffs_xs_sum - irrigationFlow) / t_xs;
            for (int i = 0; i < releaseWaters_xs.size(); i++) {
                if (releaseWaters_xs.get(i) == releaseWater_xs) {
                    releaseWaters_xs.set(i, releaseWater_xs_next);
                }
            }
            System.out.println("优先满足" + month.get(minIndex) + "月" + ",计算的Qp为：" + releaseWater_xs_next);
            selectXs(t_xs, month, rainoffs_xs, rainoffs_xs_sum, releaseWaters_xs, demands_xs, releaseWater_xs_next, irrigationFlow);
        }

        return releaseWaters_xs;
    }

    /**
     * 供水期过程计算
     *
     * @param t_gs             供水期长度
     * @param month            月份
     * @param rainoffs_gs      供水期入库流量
     * @param releaseWaters_gs 初设供水期过程
     * @param demands_gs       供水期供水需求
     * @param releaseWater_gs  供水期计算的平均流量
     * @param IrrigationFlow   兴利调节流量
     * @return 供水期过程
     */
    public static ArrayList<Double> selectGs(int t_gs, ArrayList<Integer> month, ArrayList<Double> rainoffs_gs,
                                             ArrayList<Double> releaseWaters_gs, ArrayList<Double> demands_gs, double releaseWater_gs, double IrrigationFlow) {
        if (releaseWater_gs >= (Double) ListUtils.getMax(demands_gs).get("value")) {
            System.out.println("满足条件，供水期过程为：" + releaseWaters_gs);
        } else {
            Map maxDemandMap = ListUtils.getMax(demands_gs);
            int maxIndex = (int) maxDemandMap.get("index");
            releaseWaters_gs.set(maxIndex, (Double) maxDemandMap.get("value"));
            IrrigationFlow = IrrigationFlow - (demands_gs.get(maxIndex) - rainoffs_gs.get(maxIndex));
            demands_gs.set(maxIndex, 0.0);
            rainoffs_gs.set(maxIndex, 0.0);
            t_gs--;
            double releaseWater_gs_next = (ListUtils.sum(rainoffs_gs) + IrrigationFlow) / t_gs;
            for (int i = 0; i < releaseWaters_gs.size(); i++) {
                if (releaseWaters_gs.get(i) == releaseWater_gs) {
                    releaseWaters_gs.set(i, releaseWater_gs_next);
                }
            }
            System.out.println("优先满足" + month.get(maxIndex) + "月" + ",计算的Qp为：" + releaseWater_gs_next);
            selectGs(t_gs, month, rainoffs_gs, releaseWaters_gs, demands_gs, releaseWater_gs_next, IrrigationFlow);
        }
        return releaseWaters_gs;

    }
}
