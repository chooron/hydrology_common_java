package exercise;

import domain.FloodCount;
import utils.ExcelUtils;
import utils.ListUtils;
import utils.XmlUtils;

public class FloodRegulation {
    static FloodCount floodCount = XmlUtils.readFlood("./xmlFiles/floodRegulation.xml");

    public static void main(String[] args) {
        double[][] result = reservoirFloodCount(floodCount);
        /*for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }*/
        ExcelUtils.submitFloodRegulation(result,"./results/floodRegulationResult.xls");
    }

    private static double[][] reservoirFloodCount(FloodCount floodCount) {
        double[][] result = new double[floodCount.gethRainoffs().size()][8];
        result[0][2] = 0;
        result[0][4] = 0;
        result[0][5] = 0;
        result[0][7] = floodCount.getZxian();
        result[0][6] = relationCount(floodCount.getRelationOfzvq(), floodCount.getZxian(), 0, 1);
        for (int i = 0; i < result.length; i++) {
            result[i][0] = floodCount.getHours().get(i);
            result[i][1] = floodCount.gethRainoffs().get(i);
        }
        result[0][3] = result[0][1];//假设计算初下泄流量等于入库
        for (int i = 1; i < result.length; i++) {
            result[i][2] = 0.5 * (result[i - 1][1] + result[i][1]);
            result[i][3] = trialForFlood(100, result, i);
            result[i][4] = (result[i - 1][3] + result[i][3]) * 0.5;
            result[i][5] = (result[i][2] - result[i][4]) * 3600 * (result[i][0] - result[i - 1][0]) / 10000;
            result[i][6] = result[i - 1][6] + result[i][5];
            result[i][7] = relationCount(floodCount.getRelationOfzvq(), result[i][6], 1, 0);
        }
        return result;
    }


    private static double trialForFlood(double init, double[][] result, int i) {
        double q2 = result[i - 1][3];
        if (result[i - 1][1] < result[i][1]) {
            q2 += init;
        } else {
            q2 -= init;
        }
        double rate = 1;
        double v2 = (result[i][2] - 0.5 * (result[i - 1][3] + q2)) * (result[i][0] - result[i - 1][0]) * 3600 / 10000 + result[i - 1][6];
        double q2Bycount = relationCount(floodCount.getRelationOfzvq(), v2, 1, 2);
        while (Math.abs(q2 - q2Bycount) > 0.1) {
            if (Math.abs(q2 - q2Bycount) > 5 && Math.abs(q2 - q2Bycount) < 20) {
                rate = 0.5;
            } else if (Math.abs(q2 - q2Bycount) > 1 && Math.abs(q2 - q2Bycount) <= 5) {
                rate = 0.3;
            } else if (Math.abs(q2 - q2Bycount) <= 1 && Math.abs(q2 - q2Bycount) > 0.1) {
                rate = 0.1;
            }
            if (q2 - q2Bycount > 0) {
                q2 -= rate;
            } else {
                q2 += rate;
            }
            v2 = (result[i][2] - 0.5 * (result[i - 1][3] + q2)) * (result[i][0] - result[i - 1][0]) * 3600 / 10000 + result[i - 1][6];
            if (v2 > floodCount.getRelationOfzvq()[floodCount.getRelationOfzvq().length - 1][1]) {
                System.out.println("第" + floodCount.getHours().get(i) + "时段达到设计水位，此时来多少泄多少");
            }
            q2Bycount = relationCount(floodCount.getRelationOfzvq(), v2, 1, 2);
        }
        return q2;
    }

    private static double relationCount(double[][] relationOfzvq, double v, int a, int b) {
        double[][] temp = new double[relationOfzvq.length][2];
        for (int i = 0; i < relationOfzvq.length; i++) {
            temp[i][0] = relationOfzvq[i][a];
            temp[i][1] = relationOfzvq[i][b];
        }
        return ListUtils.getInterpolation(temp, v, 0);
    }

}
