package exercise;

import domain.MaskingenMode;
import utils.ExcelUtils;
import utils.XmlUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MaskingenCalculus {
    public static void main(String[] args) {
        MaskingenMode maskingenMode = XmlUtils.readForMaskingenCal("./xmlFiles/maskingenMode.xml");
//        System.out.println(maskingenMode);
        // 计算时段
        Calendar calendar1 = maskingenMode.getUnitLines().get(0).getCalendar();
        Calendar calendar2 = maskingenMode.getUnitLines().get(1).getCalendar();
        double varHour = getVarHour(calendar1, calendar2);
        // 求解C0,C1,C2
        double K = maskingenMode.getK();
        double x = maskingenMode.getX();
        double C0 = (0.5 * varHour - K * x) / (0.5 * varHour + K - K * x);
        double C1 = (0.5 * varHour + K * x) / (0.5 * varHour + K - K * x);
        double C2 = (-0.5 * varHour + K - K * x) / (0.5 * varHour + K - K * x);
//        System.out.println(C0);
//        System.out.println(C1);
//        System.out.println(C2);
//        System.out.println(varHour);
        // 马斯京根演算
        double[][] counts = new double[maskingenMode.getUnitLines().size()][7];
        counts[0][1] = 0;
        counts[0][2] = 0;
        counts[0][3] = 0;
        counts[0][4] = maskingenMode.getRealQs().get(0);
        for (int i = 0; i < counts.length; i++) {
            counts[i][0] = maskingenMode.getUnitLines().get(i).getRainoff();
            counts[i][5] = maskingenMode.getRealQs().get(i);
            if (i >= 1) {
                counts[i][1] = counts[i][0] * C0;
                counts[i][2] = counts[i - 1][0] * C1;
                counts[i][3] = counts[i - 1][4] * C2;
                counts[i][4] = counts[i][1] + counts[i][2] + counts[i][3];
            }
            counts[i][6] = counts[i][4] - counts[i][5];
        }
/*        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[0].length; j++) {
                System.out.print(counts[i][j] + " ");
            }
            System.out.println();
        }*/
        ExcelUtils.submitMaskingenResult(counts, C0, C1, C2, maskingenMode, "./results/MaskingenResult.xls");
    }


    /**
     * 计算时间差
     *
     * @param calendar1 时段一
     * @param calendar2 时段二
     * @return 时间差
     */
    private static double getVarHour(Calendar calendar1, Calendar calendar2) {
        //默认不超过24小时
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH");
        String dateStr1 = sdf.format(calendar1.getTime());
        String dateStr2 = sdf.format(calendar2.getTime());
        String[] str1s = dateStr1.split(" ");
        String[] str1ss = str1s[0].split("-");
        double month1 = Double.parseDouble(str1ss[0]);
        double day1 = Double.parseDouble(str1ss[1]);
        double hour1 = Double.parseDouble(str1s[1]);

        String[] str2s = dateStr2.split(" ");
        String[] str2ss = str2s[0].split("-");
        double month2 = Double.parseDouble(str2ss[0]);
        double day2 = Double.parseDouble(str2ss[1]);
        double hour2 = Double.parseDouble(str2s[1]);

        if (month1 != month2) {
            hour2 += 24;
        } else if (day1 != day2 && month1 == month2) {
            hour2 += 24;
        }
        double varHour = hour2 - hour1;
        return varHour;
    }
}
