package utils;

import domain.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class XmlUtils {
    private static Document init(String xmlPath) {
        File file = new File(xmlPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(file);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static ArrayList<String> readXmlForIsoRegulation(String fileName) {
        String sheetName;
        String year;
        ArrayList<String> paralist = new ArrayList<>();
        Document doc = init(fileName);
        NodeList para = doc.getElementsByTagName("para");
        for (int i = 0; i < para.getLength(); i++) {
            fileName = doc.getElementsByTagName("fileName").item(i).getFirstChild().getNodeValue();
            sheetName = doc.getElementsByTagName("sheetName").item(i).getFirstChild().getNodeValue();
            year = doc.getElementsByTagName("year").item(i).getFirstChild().getNodeValue();
            paralist.add(fileName);
            paralist.add(sheetName);
            paralist.add(year);

        }
        return paralist;
    }

    public static ArrayList<YsRainoff> readForHydroFre(String xmlPath) {
        ArrayList<YsRainoff> ysRainoffs = new ArrayList<>();
        Document doc = init(xmlPath);
        // 获取离散数据
        NodeList Ns = doc.getElementsByTagName("N");
        for (int i = 0; i < Ns.getLength(); i++) {
            int year = Integer.parseInt(doc.getElementsByTagName("year").item(i).getFirstChild().getNodeValue());
            double rainoff = Double.parseDouble(doc.getElementsByTagName("rainoff").item(i).getFirstChild().getNodeValue());
            YsRainoff ysRainoff = new YsRainoff();
            ysRainoff.setDescribe("N" + Ns.item(i).getAttributes().item(0).getTextContent());
            ysRainoff.setYear(year);
            ysRainoff.setRainoff(rainoff);
            ysRainoffs.add(ysRainoff);
        }
        // 获取连续数据
        String excelFile = doc.getElementsByTagName("fileName").item(0).getFirstChild().getNodeValue();
        String sheetName = doc.getElementsByTagName("sheetName").item(0).getFirstChild().getNodeValue();
        ArrayList<YsRainoff> ysRainoffs_conti = null;
        try {
            ysRainoffs_conti = ExcelUtils.getExcelForFreq(excelFile, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert ysRainoffs_conti != null;
        ysRainoffs.addAll(ysRainoffs_conti);
        return ysRainoffs;
    }

    public static ArrayList<Object> readForHydroFrePara(String xmlPath) {
        ArrayList<Object> paraList = new ArrayList<>();
        Document doc = init(xmlPath);
        NodeList paras = doc.getElementsByTagName("para");
        for (int i = 0; i < paras.getLength(); i++) {
            int N1 = Integer.parseInt(doc.getElementsByTagName("N1-len").item(0).getFirstChild().getNodeValue());
            int N2 = Integer.parseInt(doc.getElementsByTagName("N2-len").item(0).getFirstChild().getNodeValue());
            int n = Integer.parseInt(doc.getElementsByTagName("n-len").item(0).getFirstChild().getNodeValue());
            double p1 = Double.parseDouble(doc.getElementsByTagName("p1").item(0).getFirstChild().getNodeValue());
            double p2 = Double.parseDouble(doc.getElementsByTagName("p2").item(0).getFirstChild().getNodeValue());
            paraList.add(N1);
            paraList.add(N2);
            paraList.add(n);
            paraList.add(p1);
            paraList.add(p2);
        }
        return paraList;
    }

    public static ConstOut readForConstOutRegu(String xmlPath) {
        Document doc = init(xmlPath);
        NodeList context = doc.getElementsByTagName("context");
        ConstOut constOut = new ConstOut();
        constOut.setN(Double.parseDouble(doc.getElementsByTagName("N").item(0).getFirstChild().getNodeValue()));
        constOut.setK(Double.parseDouble(doc.getElementsByTagName("k").item(0).getFirstChild().getNodeValue()));
        Map<String, Integer> map = new HashMap<>();
        map.put(doc.getElementsByTagName("unit").item(0).getFirstChild().getNodeValue(), Integer.valueOf(doc.getElementsByTagName("count").item(0).getFirstChild().getNodeValue()));
        constOut.setT(map);
        constOut.setHlost(Double.parseDouble(doc.getElementsByTagName("Hlost").item(0).getFirstChild().getNodeValue()));
        constOut.setZnormal(Double.parseDouble(doc.getElementsByTagName("Znormal").item(0).getFirstChild().getNodeValue()));
        constOut.setZdead(Double.parseDouble(doc.getElementsByTagName("Zdead").item(0).getFirstChild().getNodeValue()));
        ArrayList<String> excelNames = new ArrayList<>();
        ArrayList<String> sheetNames = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            excelNames.add(doc.getElementsByTagName("excelName").item(i).getFirstChild().getNodeValue());
            sheetNames.add(doc.getElementsByTagName("sheetName").item(i).getFirstChild().getNodeValue());
        }
        String[] names = new String[]{"ZV", "QZ", "Q"};
        for (int i = 0; i < excelNames.size(); i++) {
            try {
                ExcelUtils.getListForCOR(constOut, excelNames.get(i), sheetNames.get(i), names[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return constOut;
    }

    public static ArrayList<YRainoff> readQcomeAndQuse(String xmlPath) {
        Document doc = init(xmlPath);
        NodeList context = doc.getElementsByTagName("context");
        String excelPath = doc.getElementsByTagName("excelPath").item(0).getFirstChild().getNodeValue();
        String sheetName = doc.getElementsByTagName("sheetName").item(0).getFirstChild().getNodeValue();
        ArrayList<YRainoff> yRainoffs = ExcelUtils.getExcelForQcANdQu(excelPath, sheetName);
        return yRainoffs;
    }

    public static FloodCount readFlood(String xmlPath) {
        Document doc = init(xmlPath);
        String excelPath1 = doc.getElementsByTagName("QExcelName").item(0).getFirstChild().getNodeValue();
        String sheetName1 = doc.getElementsByTagName("QsheetName").item(0).getFirstChild().getNodeValue();
        String excelPath2 = doc.getElementsByTagName("relationExcelName").item(0).getFirstChild().getNodeValue();
        String sheetName2 = doc.getElementsByTagName("relationsheetName").item(0).getFirstChild().getNodeValue();
        FloodCount floodCount = ExcelUtils.getExcelForFlood(excelPath1, sheetName1, excelPath2, sheetName2);
        double zxian = Double.parseDouble(doc.getElementsByTagName("Zxian").item(0).getFirstChild().getNodeValue());
        floodCount.setZxian(zxian);
        return floodCount;
    }

    public static XAJmode getEvaporAndR(String xmlPath) {
        Document doc = init(xmlPath);
        String excelPath1 = doc.getElementsByTagName("ParaExcelName").item(0).getFirstChild().getNodeValue();
        String sheetName1 = doc.getElementsByTagName("ParasheetName").item(0).getFirstChild().getNodeValue();
        String excelPath2 = doc.getElementsByTagName("EAndRDataExcelName").item(0).getFirstChild().getNodeValue();
        String sheetName2 = doc.getElementsByTagName("EAndRDatasheetName").item(0).getFirstChild().getNodeValue();
        XAJmode xaJmode = ExcelUtils.getExcelForEvaporRpara(excelPath1, sheetName1);
        ArrayList<XajProgress> xajProgresses = ExcelUtils.getExcelForEvaporRData(excelPath2, sheetName2);
        double WU = Double.parseDouble(doc.getElementsByTagName("WU").item(0).getFirstChild().getNodeValue());
        double WL = Double.parseDouble(doc.getElementsByTagName("WL").item(0).getFirstChild().getNodeValue());
        double WD = Double.parseDouble(doc.getElementsByTagName("WD").item(0).getFirstChild().getNodeValue());
        xajProgresses.get(0).setWD(WD);
        xajProgresses.get(0).setWU(WU);
        xajProgresses.get(0).setWL(WL);
        xaJmode.setXajProgresses(xajProgresses);
        return xaJmode;
    }

    public static ArrayList<UnitLine> readForUnitLineCount(String xmlPath) {
        Document doc = init(xmlPath);
        String excelPath = doc.getElementsByTagName("excelName").item(0).getFirstChild().getNodeValue();
        String sheetName = doc.getElementsByTagName("sheetName").item(0).getFirstChild().getNodeValue();
        ArrayList<UnitLine> unitLines = ExcelUtils.getExcelForUnitLine(excelPath, sheetName);
        return unitLines;
    }

    public static MaskingenMode readForMaskingenCal(String xmlPath) {
        Document doc = init(xmlPath);
        String runoffExcelName = doc.getElementsByTagName("runoffExcelName").item(0).getFirstChild().getNodeValue();
        String runoffSheet = doc.getElementsByTagName("runoffSheet").item(0).getFirstChild().getNodeValue();
        MaskingenMode maskingenMode = ExcelUtils.getMaskingenRunoff(runoffExcelName, runoffSheet);
        double x = Double.parseDouble(doc.getElementsByTagName("x").item(0).getFirstChild().getNodeValue());
        double K = Double.parseDouble(doc.getElementsByTagName("K").item(0).getFirstChild().getNodeValue());
        maskingenMode.setX(x);
        maskingenMode.setK(K);
        return maskingenMode;
    }

    public static XAJmode readForXajmode(String xmlPath) {
        Document doc = init(xmlPath);
        NodeList paras = doc.getElementsByTagName("para");
        Element para = (Element) paras.item(0);

        NodeList nodeMap = para.getChildNodes();
        XAJmode newInstance = null;
        Class<?> xajMode = null;
        try {
            xajMode = Class.forName("domain.XAJmode");
            newInstance = (XAJmode) xajMode.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < nodeMap.getLength(); i++) {
            String methodName = nodeMap.item(i).getNodeName();
            double value = Double.parseDouble(nodeMap.item(i).getNodeValue());
            Method method;
            try {
                method = xajMode.getMethod("set" + methodName, String.class);
                method.invoke(newInstance, value);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return newInstance;
    }

/*    private static ArrayList<String> parseElement(Element element, ArrayList<String> paraList) {
        String tagName = element.getNodeName();


        // element元素的所有属性构成的NamedNodeMap对象，需要对其进行判断
        NamedNodeMap map = element.getAttributes();

        // 如果存在属性，则打印属性
        if (null != map) {
            for (int i = 0; i < map.getLength(); i++) {
                // 获得该元素的每一个属性
                Attr attr = (Attr) map.item(i);

                // 属性名和属性值
                String attrName = attr.getName();
                String attrValue = attr.getValue();

            }
        }


        // 至此已经打印出了元素名和其属性
        // 下面开始考虑它的子元素
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            // 获取每一个child
            Node node = children.item(i);
            // 获取节点类型
            short nodeType = node.getNodeType();

            if (nodeType == Node.ELEMENT_NODE) {
                // 如果是元素类型，则递归输出
                parseElement((Element) node, paraList);
            } else if (nodeType == Node.TEXT_NODE) {
                // 如果是文本类型，则输出节点值，及文本内容
                paraList.add(node.getNodeValue());
            } else if (nodeType == Node.COMMENT_NODE) {
                // 如果是注释，则输出注释
                Comment comment = (Comment) node;
                // 注释内容
                String data = comment.getData();
            }
        }

        // 所有内容处理完之后，输出，关闭根节点
        return paraList;
    }*/


}
