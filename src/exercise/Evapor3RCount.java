package exercise;

import domain.XajProgress;
import domain.XAJmode;
import utils.ExcelUtils;
import utils.XmlUtils;

import java.util.ArrayList;

public class Evapor3RCount {
    static XAJmode xaJmode = XmlUtils.getEvaporAndR("./xmlFiles/Evapor3RCount.xml");

    public static void main(String[] args) {
        ArrayList<XajProgress> xajProgresses = xaJmode.getXajProgresses();
        double Wu_init = xajProgresses.get(0).getWU();
        double Wl_init = xajProgresses.get(0).getWL();
        double Wd_init = xajProgresses.get(0).getWD();
        ArrayList<XajProgress> result = new ArrayList<>();
        ArrayList<Integer> ts = new ArrayList<>();
        for (int i = 0; i < xajProgresses.size(); i++) {
            double p = xajProgresses.get(i).getP();
            if (i == 0) {
                XajProgress ERResult = XAJmode.evaporRCount3(xaJmode, xajProgresses.get(0), xajProgresses.get(0), p);
                xajProgresses.get(0).setWU(ERResult.getWU());
                xajProgresses.get(0).setWL(ERResult.getWL());
                xajProgresses.get(0).setWD(ERResult.getWD());
                result.add(ERResult);
            } else {

                XajProgress ERResult = XAJmode.evaporRCount3(xaJmode, xajProgresses.get(i - 1), xajProgresses.get(i), p);
                result.add(ERResult);
            }
        }
        double[][] resultForExcel = new double[xajProgresses.size()][13];
        for (int i = 0; i < resultForExcel.length; i++) {
            for (int j = 0; j < resultForExcel[0].length; j++) {
                resultForExcel[i][0] = result.get(i).getP();
                resultForExcel[i][1] = result.get(i).getE0();
                resultForExcel[i][2] = result.get(i).getEP(xaJmode);
                resultForExcel[i][3] = result.get(i).getEU();
                resultForExcel[i][4] = result.get(i).getEL();
                resultForExcel[i][5] = result.get(i).getED();
                resultForExcel[i][6] = result.get(i).getEU() + result.get(i).getEL() + result.get(i).getED();
                resultForExcel[i][7] = resultForExcel[i][0] - resultForExcel[i][6];
                if (i == 0) {
                    resultForExcel[i][8] = Wu_init;
                    resultForExcel[i][9] = Wl_init;
                    resultForExcel[i][10] = Wd_init;
                } else {
                    resultForExcel[i][8] = result.get(i - 1).getWU();
                    resultForExcel[i][9] = result.get(i - 1).getWL();
                    resultForExcel[i][10] = result.get(i - 1).getWD();
                }
                resultForExcel[i][11] = resultForExcel[i][8] + resultForExcel[i][9] + resultForExcel[i][10];
                resultForExcel[i][12] = result.get(i).getR();
            }
            ts.add(result.get(i).getT());
        }
        ExcelUtils.submitEvapor3RCount(resultForExcel, ts, "./results/Evapor3RResult.xls");
    }
}
