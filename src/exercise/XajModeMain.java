package exercise;

import domain.XAJmode;
import domain.XajProgress;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import utils.BeanRefUtil;
import utils.ExcelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 新安江模型主程序
 */
public class XajModeMain {
    public static void main(String[] args) throws DocumentException {
        XAJmode xaJmode = getXajMode("./xmlFiles/xajParaAndInit");
        ArrayList<XajProgress> xajProgresses = xaJmode.getXajProgresses();
        ArrayList<XajProgress> progressByCount = new ArrayList<>();
        for (int i = 0; i < xajProgresses.size(); i++) {
            double p = xajProgresses.get(i).getP();
            if (i == 0) {
                XajProgress ERResult = XAJmode.evaporRCount3(xaJmode, xajProgresses.get(0), xajProgresses.get(0), p);
                xajProgresses.get(0).setWU(xaJmode.getWum0());
                xajProgresses.get(0).setWL(xaJmode.getWlm0());
                xajProgresses.get(0).setWD(xaJmode.getWdm0());
                progressByCount.add(ERResult);
            } else {
                XajProgress ERResult = XAJmode.evaporRCount3(xaJmode, xajProgresses.get(i - 1), xajProgresses.get(i), p);
                progressByCount.add(ERResult);
            }
        }
        xaJmode.setXajProgresses(progressByCount);
        XAJmode xaJmodeBydivide = XAJmode.waterDivide(xaJmode);
        XAJmode xajmodelByConfl = XAJmode.ConfluenceCount(xaJmodeBydivide);
        ArrayList<XajProgress> finalprogress = xajmodelByConfl.getXajProgresses();
        for (int i = 0; i < finalprogress.size(); i++) {
            System.out.print(finalprogress.get(i).getQout()+" ");
        }
    }

    //
    public static XAJmode getXajMode(String xmlPath) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(xmlPath));
        Element rootElement = document.getRootElement();
        Element paras = rootElement.element("para");
        List<Element> parasList = paras.elements();
        XAJmode xaJmode;
        Map<String, String> map = new HashMap<>();
        for (Element para : parasList) {
            String methodName = para.getName();
            String value = para.getText();
            System.out.println(methodName + " " + value);
            map.put(methodName, value);
        }
        Element data = rootElement.element("data");
        Element Excel = data.element("Excel");
        Element UH = data.element("UH");
        Element EAndP = data.element("EAndP");
        xaJmode = ExcelUtils.getExcelForXaj(Excel.getText(), UH.getText(), EAndP.getText());
        BeanRefUtil.setFieldValue(xaJmode, map);
        return xaJmode;
    }
}
