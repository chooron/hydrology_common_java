package utils;

import domain.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ExcelUtils {
    private static HSSFWorkbook wb = null;

    /**
     * 等流量调节来水流量专用excel获取方法
     *
     * @param fileName  等流量调节数据(最好是绝对路径)
     * @param sheetName 表格名
     * @param year      数据年份
     * @return 年内来水流量对象
     */
    public static ArrayList<YRainoff> getExcelForRainoff(String fileName, String sheetName, int year) throws IOException {
        ArrayList<YRainoff> yRainoffs = new ArrayList<>();
        InputStream is = new FileInputStream(new File(fileName));
        wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheet(sheetName);
        wb.close();
        int lastRowNum = sheet.getLastRowNum();
        int leftCol = sheet.getRow(0).getLastCellNum();
        for (int i = 1; i < lastRowNum + 1; i++) {
            YRainoff yRainoff = new YRainoff();
            yRainoff.setYear(year);
            for (int j = 0; j < leftCol; j++) {
                HSSFRow row = sheet.getRow(i);
                HSSFCell cell = row.getCell(j);
                switch (j) {
                    case 0:
                        int month = (int) cell.getNumericCellValue();
                        yRainoff.setMonth(month);
                    case 1:
                        double value = cell.getNumericCellValue();
                        yRainoff.setRainoff(value);
                }
            }
            yRainoffs.add(yRainoff);
        }
        is.close();
        return yRainoffs;
    }

    /**
     * 等流量调节需水要求专用excel获取方法
     *
     * @param fileName  等流量调节数据(最好是绝对路径)
     * @param sheetName 表格名
     * @param year      数据年份
     * @return 年内供水要求对象
     */
    public static ArrayList<YDemand> getExcelForDemand(String fileName, String sheetName, int year) throws IOException {
        ArrayList<YDemand> yDemands = new ArrayList<>();
        InputStream is = new FileInputStream(new File(fileName));
        wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheet(sheetName);
        wb.close();
        int lastRowNum = sheet.getLastRowNum();
        int leftCol = sheet.getRow(0).getLastCellNum();
        for (int i = 1; i < lastRowNum + 1; i++) {
            YDemand yDemand = new YDemand();
            yDemand.setYear(year);
            for (int j = 0; j < leftCol; j++) {
                HSSFRow row = sheet.getRow(i);
                HSSFCell cell = row.getCell(j);
                switch (j) {
                    case 0:
                        int month = (int) cell.getNumericCellValue();
                        yDemand.setMonth(month);
                    case 2:
                        double value = cell.getNumericCellValue();
                        yDemand.setWaterDemand(value);
                }
            }
            yDemands.add(yDemand);
        }
        is.close();
        return yDemands;
    }

    /**
     * 提交数据
     *
     * @param regulation 等流量调节计算结果
     * @param fileName   文件名
     * @param sheetName  表格名
     */
    public static void submitForIsoRegulation(ArrayList<Double> regulation, String fileName, String sheetName) throws IOException {
        InputStream is = new FileInputStream(new File(fileName));
        wb = new HSSFWorkbook(is);
        is.close();
        HSSFSheet sheet = wb.getSheet(sheetName);
//        sheet.createRow(0).createCell(3);
        sheet.getRow(0).getCell(3).setCellValue("等流量调节计算过程");
        for (int i = 0; i < regulation.size(); i++) {
//            sheet.createRow(i + 1).createCell(3);
            sheet.getRow(i + 1).getCell(3).setCellValue(regulation.get(i));
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            wb.write(fos);
            fos.close();
        }
        wb.close();
    }

    /**
     * @param excelFile
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static ArrayList<YsRainoff> getExcelForFreq(String excelFile, String sheetName) throws IOException {
        ArrayList<YsRainoff> ysRainoffs = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(excelFile));
        wb = new HSSFWorkbook(fis);
        fis.close();
        HSSFSheet sheet = wb.getSheet(sheetName);
        wb.close();
        int lastRowNum = sheet.getLastRowNum();
        int leftCol = sheet.getRow(0).getLastCellNum();
        for (int i = 1; i < lastRowNum + 1; i++) {
            YsRainoff ysRainoff = new YsRainoff();
            for (int j = 0; j < leftCol; j++) {
                HSSFRow row = sheet.getRow(i);
                HSSFCell cell = row.getCell(j);
                switch (j) {
                    case 0:
                        int year = (int) cell.getNumericCellValue();
                        ysRainoff.setYear(year);
                        break;
                    case 1:
                        double value = cell.getNumericCellValue();
                        ysRainoff.setRainoff(value);
                        break;
                }
            }
            ysRainoff.setDescribe("N3");
            ysRainoffs.add(ysRainoff);
        }
        return ysRainoffs;
    }

    public static ConstOut getListForCOR(ConstOut constOut, String excelName, String sheetName, String name) throws IOException {
        InputStream is;
        is = new FileInputStream(new File(excelName));
        wb = new HSSFWorkbook(is);
        is.close();
        HSSFSheet sheet = wb.getSheet(sheetName);
        wb.close();
        int lastRowNum = sheet.getLastRowNum();
        int leftCol = sheet.getRow(0).getLastCellNum();
        double[][] array = new double[lastRowNum][leftCol];
        ArrayList<YRainoff> yRainoffs = new ArrayList<>();
        for (int i = 1; i < lastRowNum + 1; i++) {
            YRainoff yRainoff = new YRainoff();
            for (int j = 0; j < leftCol; j++) {
                HSSFRow row = sheet.getRow(i);
                HSSFCell cell = row.getCell(j);
                if (name == "Q") {
                    switch (j) {
                        case 0:
                            yRainoff.setRainoff(cell.getNumericCellValue());
                            break;
                        case 1:
                            yRainoff.setMonth((int) cell.getNumericCellValue());
                            break;
                    }
                    continue;
                }
                array[i - 1][j] = cell.getNumericCellValue();
            }
            if (name == "Q") {
                yRainoffs.add(yRainoff);
            }
        }
        switch (name) {
            case "QZ":
                constOut.setQZ(array);
                break;
            case "ZV":
                constOut.setZV(array);
                break;
            case "Q":
                constOut.setyRainoffs(yRainoffs);
                break;
        }
        return constOut;
    }

    public static void submitCalcuForCOR(double[][] answer, ArrayList<Integer> month_use, String excelName) {
        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("result");
        HSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("时段");
        row0.createCell(1).setCellValue("天然流量(m^3/s)");
        row0.createCell(2).setCellValue("引用流量(m^3/s)");
        row0.createCell(3).setCellValue("水库供蓄水量(亿m^3)");
        row0.createCell(4).setCellValue("时段初末蓄水(亿m^3)");
        row0.createCell(5).setCellValue("时段平均蓄水(亿m^3)");
        row0.createCell(6).setCellValue("上游平均水位(m)");
        row0.createCell(7).setCellValue("下游水位(m)");
        row0.createCell(8).setCellValue("平均水头(m)");
        row0.createCell(9).setCellValue("出力(万kw)");
        ArrayList<HSSFRow> rows = new ArrayList<>();
        for (int i = 1; i < answer.length + 1; i++) {
            rows.add(sheet.createRow(i));
        }
        for (int i = 0; i < month_use.size(); i++) {
            rows.get(i).createCell(0).setCellValue(month_use.get(i) + "月");
        }
        for (int i = 0; i < answer.length; i++) {
            for (int j = 1; j < answer[0].length + 1; j++) {
                rows.get(i).createCell(j).setCellValue(answer[i][j - 1]);
            }
        }

        try {
            wb.write(new FileOutputStream(excelName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("上交成功");

    }

    public static void submitQcomeAndQuse(double[][] answer, ArrayList<Integer> month_use, String excelName) {
        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("result");
        HSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("月份");
        row0.createCell(1).setCellValue("来水流量(m^3/s)");
        row0.createCell(2).setCellValue("用水流量(m^3/s)");
        row0.createCell(3).setCellValue("余水量(m^3*月)");
        row0.createCell(4).setCellValue("亏水量(m^3*月)");
        row0.createCell(5).setCellValue("月末蓄水量(m^3*月)");
        row0.createCell(6).setCellValue("弃水量(m^3)");
        row0.createCell(7).setCellValue("备注");
        ArrayList<HSSFRow> rows = new ArrayList<>();
        for (int i = 1; i < answer.length + 1; i++) {
            rows.add(sheet.createRow(i));
        }
        for (int i = 0; i < month_use.size(); i++) {
            rows.get(i).createCell(0).setCellValue(month_use.get(i) + "月");
        }
        for (int i = 0; i < answer.length; i++) {
            for (int j = 1; j < answer[0].length + 1; j++) {
                rows.get(i).createCell(j).setCellValue(answer[i][j - 1]);
            }
        }
        for (int i = 0; i < answer.length; i++) {
            if (answer[i][5] > 0) {
                rows.get(i).createCell(7).setCellValue("库满调水");
            } else if (answer[i][5] == 0 && answer[i][2] > 0) {
                rows.get(i).createCell(7).setCellValue("库空蓄水");
            } else if (answer[i][3] > 0 && i != answer.length - 1) {
                rows.get(i).createCell(7).setCellValue("供水");
            } else if (i == answer.length - 1) {
                rows.get(i).createCell(7).setCellValue("库空");
            }
        }
        try {
            wb.write(new FileOutputStream(excelName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("上交成功");
    }


    public static ArrayList<YRainoff> getExcelForQcANdQu(String excelPath, String sheetName) {
        int lastRowNum = 0;
        int leftCol = 0;
        ArrayList<YRainoff> yRainoffs = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(new File(excelPath));
            wb = new HSSFWorkbook(fis);
            fis.close();
            HSSFSheet sheet = wb.getSheet(sheetName);
            wb.close();
            lastRowNum = sheet.getLastRowNum();
            leftCol = sheet.getRow(0).getLastCellNum();
            for (int i = 1; i < lastRowNum + 1; i++) {
                HSSFRow row = sheet.getRow(i);
                YRainoff yRainoff = new YRainoff();
                yRainoff.setMonth((int) row.getCell(0).getNumericCellValue());
                yRainoff.setRainoff(row.getCell(1).getNumericCellValue());
                yRainoff.setWaterUse(row.getCell(2).getNumericCellValue());
                yRainoff.setDescribe("年内来水与用水过程");
                yRainoffs.add(yRainoff);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return yRainoffs;
    }

    public static FloodCount getExcelForFlood(String excelPath1, String sheetName1, String excelPath2, String sheetName2) {
        int lastRowNum = 0;
        int leftCol = 0;
        FileInputStream fis = null;
        FloodCount floodCount = new FloodCount();
        ArrayList<Double> Hrains = new ArrayList<>();
        ArrayList<Double> Hours = new ArrayList<>();
        try {
            fis = new FileInputStream(new File(excelPath1));
            wb = new HSSFWorkbook(fis);
            fis.close();
            HSSFSheet sheet1 = wb.getSheet(sheetName1);
            wb.close();
            lastRowNum = sheet1.getLastRowNum();
            leftCol = sheet1.getRow(0).getLastCellNum();
            for (int i = 1; i < lastRowNum + 1; i++) {
                HSSFRow row = sheet1.getRow(i);
                Hrains.add(row.getCell(1).getNumericCellValue());
                Hours.add(row.getCell(0).getNumericCellValue());
            }

            fis = new FileInputStream(new File(excelPath2));
            wb = new HSSFWorkbook(fis);
            fis.close();
            HSSFSheet sheet2 = wb.getSheet(sheetName2);
            wb.close();
            lastRowNum = sheet2.getLastRowNum();
            leftCol = sheet2.getRow(0).getLastCellNum();
            double[][] relation = new double[lastRowNum - 1][3];
            for (int i = 1; i < lastRowNum; i++) {
                HSSFRow row = sheet2.getRow(i);
                for (int j = 0; j < leftCol; j++) {
                    relation[i - 1][j] = row.getCell(j).getNumericCellValue();
                }
            }
            floodCount.sethRainoffs(Hrains);
            floodCount.setHours(Hours);
            floodCount.setRelationOfzvq(relation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return floodCount;
    }

    public static void submitFloodRegulation(double[][] result, String s) {
        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("result");
        HSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("时间(h)");
        row0.createCell(1).setCellValue("入库流量(m^3/s)");
        row0.createCell(2).setCellValue("时段平均流量(m^3/s)");
        row0.createCell(3).setCellValue("下泄量(m^3/s)");
        row0.createCell(4).setCellValue("时段平均下泄流量(m^3/s)");
        row0.createCell(5).setCellValue("时段内库存水量变化(万m^3)");
        row0.createCell(6).setCellValue("水库存水量(万m^3)");
        row0.createCell(7).setCellValue("水库水位Z(m)");
        ArrayList<HSSFRow> rows = new ArrayList<>();
        for (int i = 1; i < result.length + 1; i++) {
            rows.add(sheet.createRow(i));
        }
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                rows.get(i).createCell(j).setCellValue(result[i][j]);
            }
        }
        try {
            wb.write(new FileOutputStream(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("上交成功");
    }

    public static ArrayList<XajProgress> getExcelForEvaporRData(String excelPath1, String sheetName1) {
        int lastRowNum;
        ArrayList<XajProgress> xajProgresses = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(excelPath1));
            wb = new HSSFWorkbook(fis);
            fis.close();
            HSSFSheet sheet1 = wb.getSheet(sheetName1);
            lastRowNum = sheet1.getLastRowNum();
            for (int i = 1; i < lastRowNum + 1; i++) {
                XajProgress xajProgress = new XajProgress();
                HSSFRow row = sheet1.getRow(i);
                xajProgress.setT((int) row.getCell(0).getNumericCellValue());
                xajProgress.setP(row.getCell(1).getNumericCellValue());
                xajProgress.setE0(row.getCell(2).getNumericCellValue());
                xajProgresses.add(xajProgress);
            }
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xajProgresses;

    }

    public static XAJmode getExcelForEvaporRpara(String excelPath2, String sheetName2) {
        int lastRowNum;
        XAJmode xaJmode = new XAJmode();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(excelPath2));
            wb = new HSSFWorkbook(fis);
            fis.close();
            HSSFSheet sheet1 = wb.getSheet(sheetName2);
            lastRowNum = sheet1.getLastRowNum();
            for (int i = 1; i < lastRowNum + 1; i++) {
                HSSFRow row = sheet1.getRow(i);
                switch (row.getCell(0).getStringCellValue()) {
                    case "wum":
                        xaJmode.setWum(row.getCell(1).getNumericCellValue());
                        break;
                    case "wlm":
                        xaJmode.setWlm(row.getCell(1).getNumericCellValue());
                        break;
                    case "wdm":
                        xaJmode.setWdm(row.getCell(1).getNumericCellValue());
                        break;
                    case "b":
                        xaJmode.setB(row.getCell(1).getNumericCellValue());
                        break;
                    case "k":
                        xaJmode.setK(row.getCell(1).getNumericCellValue());
                        break;
                    case "c":
                        xaJmode.setC(row.getCell(1).getNumericCellValue());
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xaJmode;
    }

    public static void submitEvapor3RCount(double[][] result, ArrayList<Integer> ts, String excelPath) {
        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("result");
        HSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("时间(d)");
        row0.createCell(1).setCellValue("P");
        row0.createCell(2).setCellValue("E0");
        row0.createCell(3).setCellValue("EP");
        row0.createCell(4).setCellValue("EU");
        row0.createCell(5).setCellValue("EL");
        row0.createCell(6).setCellValue("ED");
        row0.createCell(7).setCellValue("E");
        row0.createCell(8).setCellValue("PE");
        row0.createCell(9).setCellValue("WU");
        row0.createCell(10).setCellValue("WL");
        row0.createCell(11).setCellValue("WD");
        row0.createCell(12).setCellValue("W");
        row0.createCell(13).setCellValue("R");
        ArrayList<HSSFRow> rows = new ArrayList<>();
        for (int i = 1; i < result.length + 1; i++) {
            rows.add(sheet.createRow(i));
        }
        for (int i = 0; i < result.length; i++) {
            rows.get(i).createCell(0).setCellValue(ts.get(i));
        }
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                rows.get(i).createCell(j + 1).setCellValue(result[i][j]);
            }
        }
        try {
            wb.write(new FileOutputStream(excelPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("上交成功");
    }

    public static ArrayList<UnitLine> getExcelForUnitLine(String excelPath, String sheetName) {
        int lastRowNum;
        ArrayList<UnitLine> unitLines = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(excelPath));
            wb = new HSSFWorkbook(fis);
            fis.close();
            HSSFSheet sheet = wb.getSheet(sheetName);
            lastRowNum = sheet.getLastRowNum();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH");
            for (int i = 1; i < lastRowNum + 1; i++) {
                UnitLine unitLine = new UnitLine();
                HSSFRow row = sheet.getRow(i);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(row.getCell(0).getStringCellValue()));
                unitLine.setCalendar(calendar);
                unitLine.setRainoff(row.getCell(1).getNumericCellValue());
                if (row.getCell(2) != null) {
                    unitLine.setNetRain(row.getCell(2).getNumericCellValue());
                }
                unitLines.add(unitLine);
            }
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return unitLines;
    }

    public static void submitUnitLineCount(ArrayList<ArrayList> counts, String excelPath) {
        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("result");
        HSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("时间(月-日 时)");
        row0.createCell(1).setCellValue("地面径流量Qd(m3/s)");
        row0.createCell(2).setCellValue("净雨rd");
        System.out.println(counts.size());
        for (int i = 3; i < counts.size() - 1; i++) {
            row0.createCell(i).setCellValue("净雨" + counts.get(2).get(i - 3) + "mm产生的径流(m3/s)");
        }
        row0.createCell(counts.size() - 1).setCellValue("单位线");
        ArrayList<HSSFRow> rows = new ArrayList<>();
        for (int i = 1; i < counts.get(0).size() + 1; i++) {
            rows.add(sheet.createRow(i));
        }
        for (int i = 0; i < counts.get(0).size(); i++) {
            Calendar calendar = (Calendar) counts.get(0).get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH");
            String dateStr = sdf.format(calendar.getTime());
            rows.get(i).createCell(0).setCellValue(dateStr);
        }

        for (int j = 1; j < counts.size(); j++) {
            for (int i = 0; i < counts.get(j).size(); i++) {
/*                if (i > counts.get(j).size()) {
                    break;
                }*/
                rows.get(i).createCell(j).setCellValue((Double) counts.get(j).get(i));
            }
        }
        try {
            wb.write(new FileOutputStream(excelPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("上交成功");
    }

    public static MaskingenMode getMaskingenRunoff(String runoffExcelName, String runoffSheet) {
        int lastRowNum;
        MaskingenMode maskingenMode = new MaskingenMode();
        ArrayList<UnitLine> unitLines = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(runoffExcelName));
            wb = new HSSFWorkbook(fis);
            fis.close();
            HSSFSheet sheet = wb.getSheet(runoffSheet);
            lastRowNum = sheet.getLastRowNum();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH");
            ArrayList<Double> realQs = new ArrayList<>();
            for (int i = 1; i < lastRowNum + 1; i++) {
                UnitLine unitLine = new UnitLine();
                HSSFRow row = sheet.getRow(i);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(row.getCell(0).getStringCellValue()));
                unitLine.setCalendar(calendar);
                unitLine.setRainoff(row.getCell(1).getNumericCellValue());
                unitLines.add(unitLine);
                double realQ = row.getCell(2).getNumericCellValue();
                realQs.add(realQ);
            }
            wb.close();
            maskingenMode.setRealQs(realQs);
            maskingenMode.setUnitLines(unitLines);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return maskingenMode;
    }

    public static void submitMaskingenResult(double[][] counts, double c0, double c1, double c2, MaskingenMode maskingenMode, String excelPath) {
        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("result");
        HSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("时间(月-日 时)");
        row0.createCell(1).setCellValue("实测入流量I");
        row0.createCell(2).setCellValue(String.format("%.2f", c0) + "*I2");
        row0.createCell(3).setCellValue(String.format("%.2f", c1) + "*I1");
        row0.createCell(4).setCellValue(String.format("%.2f", c2) + "*Q1");
        row0.createCell(5).setCellValue("演算后的出流量");
        row0.createCell(6).setCellValue("实测出流量");
        row0.createCell(6).setCellValue("出流量计算误差");
        ArrayList<HSSFRow> rows = new ArrayList<>();
        for (int i = 1; i < counts.length + 1; i++) {
            rows.add(sheet.createRow(i));
        }
        for (int i = 0; i < counts.length; i++) {
            Calendar calendar = (Calendar) maskingenMode.getUnitLines().get(i).getCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH");
            String dateStr = sdf.format(calendar.getTime());
            rows.get(i).createCell(0).setCellValue(dateStr);
        }
        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[0].length; j++) {
                rows.get(i).createCell(j + 1).setCellValue(counts[i][j]);
            }
        }
        try {
            wb.write(new FileOutputStream(excelPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("上交成功");
    }

    public static XAJmode getExcelForXaj(String ExcelName, String uh, String EAndP) {
        XAJmode xaJmode = new XAJmode();
        ArrayList<XajProgress> xajProgresses = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(new File(ExcelName));
            wb = new HSSFWorkbook(fis);
            fis.close();
            ArrayList<Double> tempData = new ArrayList<>();
            HSSFSheet sheetForEP = wb.getSheet(EAndP);
            double lastRowNum = sheetForEP.getLastRowNum();
            for (int i = 0; i < lastRowNum + 1; i++) {
                XajProgress xajProgress = new XajProgress();
                HSSFRow row = sheetForEP.getRow(i);
                xajProgress.setP(row.getCell(0).getNumericCellValue());
                xajProgress.setEP(row.getCell(1).getNumericCellValue());
                xajProgresses.add(xajProgress);
            }
            xaJmode.setXajProgresses(xajProgresses);
            HSSFSheet sheet = wb.getSheet(uh);
            double lastRowNum2 = sheet.getLastRowNum();
            for (int i = 0; i < lastRowNum2 + 1; i++) {
                HSSFRow row2 = sheet.getRow(i);
                System.out.println(row2.getCell(0).getNumericCellValue());
                tempData.add(row2.getCell(0).getNumericCellValue());
            }
            xaJmode.setUH(tempData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xaJmode;
    }

    public static double getFreq3ForhydroFreq(double interpolation, double point, String s) {
        return 0;
    }
}
