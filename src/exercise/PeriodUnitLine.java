package exercise;

import domain.UnitLine;
import utils.ExcelUtils;
import utils.XmlUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class PeriodUnitLine {
    public static void main(String[] args) {
        // 分析法计算
        ArrayList<UnitLine> unitLines = XmlUtils.readForUnitLineCount("F:\\Tech_task\\xmlFiles\\unitLine.xml");
/*        for (UnitLine unitLine : unitLines) {
            System.out.println(unitLine.getCalendar().get(Calendar.MONTH));
        }*/

        ArrayList<Integer> tempIndex = new ArrayList<>();
        for (int i = 0; i < unitLines.size(); i++) {
            if (unitLines.get(i).getNetRain() != 0) {

                tempIndex.add(i);
            }
        }

        ArrayList<ArrayList> counts = new ArrayList<>();
        ArrayList<Calendar> times = new ArrayList<>();
        ArrayList<Double> runoffs = new ArrayList<>();
        ArrayList<Double> netRains = new ArrayList<>();
        for (UnitLine unitLine : unitLines) {
            times.add(unitLine.getCalendar());
            runoffs.add(unitLine.getRainoff());
            if (unitLine.getNetRain() != 0) {
                netRains.add(unitLine.getNetRain());
            }
        }
        counts.add(times);
        counts.add(runoffs);
        counts.add(netRains);
        for (int i = 0; i < netRains.size(); i++) {
            ArrayList<Double> netRainoff_i = new ArrayList<>();
            if (i == 0) {
                //第一个净雨产流过程
                netRainoff_i.add((Double) counts.get(1).get(0));
                netRainoff_i.add((Double) counts.get(1).get(1));
            } else {
                //其余产流过程
                for (int j = 0; j < i+1; j++) {
                    netRainoff_i.add(0.0);
                }
            }
            counts.add(netRainoff_i);
        }
        ArrayList<Double> line = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < netRains.size(); i++) {
            if (netRains.get(i) > 0.0) {
                index++;
            }
        }
        for (int i = 0; i < runoffs.size(); i++) {
            // 开始正式运算
            double rawRunoff = runoffs.get(i);
            for (int j = 1; j < index; j++) {
                double netRain_j = i - j > 0 ? line.get(i - j) * netRains.get(j) / 10 : 0;
                if (netRain_j!=0) {//i+j > counts.get(3+j).size()
                    counts.get(3 + j).add(netRain_j);
                }
                rawRunoff -= netRain_j;
            }
            if (i > 1) {
                counts.get(3).add(rawRunoff);
            }
            line.add((Double) counts.get(3).get(i) / netRains.get(0) * 10);
        }
        counts.add(line);
        for (int i = 0; i < counts.size(); i++) {
            System.out.println(counts.get(i));
        }
        ExcelUtils.submitUnitLineCount(counts,"F:\\Tech_task\\results\\UnitLineCount.xls");
    }
}

