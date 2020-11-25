package exercise;

import domain.YRainoff;
import utils.ExcelUtils;
import utils.XmlUtils;
import java.util.ArrayList;


/**
 * 列表法求年调节水库的调节库容
 */
public class RegulationStorage {
    public static void main(String[] args) {
        ArrayList<YRainoff> yRainoffs = XmlUtils.readQcomeAndQuse("F:\\Tech_task\\xmlFiles\\QcomeAndQuse");
        int start = 1;
        for (int i = 0; i < yRainoffs.size(); i++) {
            if (yRainoffs.get(i).getRainoff() > yRainoffs.get(i).getWaterUse()) {
                start = i + 1;
                break;
            }
        }
        System.out.println(yRainoffs);
        System.out.println("水利年开始月份为：" + start + "月");
        // 列表计算
        double[][] result = new double[12][6];
        double sum = 0;
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < yRainoffs.size()-start+1; i++) {
            temp.add(i + start);
        }
        for (int i = 1; i < start; i++) {
            temp.add(i);
        }
        System.out.println(temp);
        for (int i = 0; i < 12; i++) {
            result[i][0] = yRainoffs.get(temp.get(i) - 1).getRainoff();
            result[i][1] = yRainoffs.get(temp.get(i) - 1).getWaterUse();
            if (result[i][0] - result[i][1] > 0) {
                result[i][2] = result[i][0] - result[i][1];
                result[i][3] = 0;
            } else {
                result[i][2] = 0;
                result[i][3] = result[i][1] - result[i][0];
            }
        }
        double v = 0;
        for (int i = 0; i < result.length; i++) {
            v += result[i][3];
        }
        System.out.println("调节库容为："+v);
        for (int i = 0; i < result.length; i++) {
            sum += result[i][0] - result[i][1];
            if (sum > v) {
                result[i][4] = v;
                result[i][5] = sum - v;
                sum=v;
            } else {
                result[i][4] = sum;
                result[i][5] = 0;
            }
        }
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                System.out.print(result[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        ExcelUtils.submitQcomeAndQuse(result,temp,"F:\\Tech_task\\results\\QcomeAndQuseResult.xls");
    }

}
