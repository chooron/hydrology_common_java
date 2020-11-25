package exercise;

import aaf.core.QuerySet;
import aaf.dataset.DBDataSet;
import common.ActionResponse;
import server.ActionCode;
import server.DataBaseConnection;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ShaanXiModeDemo extends ActionResponse {

    public ShaanXiModeDemo() {
        super(ActionCode.ShaanXiModeDemo);
    }

    public ShaanXiModeDemo(int actionCode) {
        super(actionCode);
    }

    @Override
    public boolean checkUser(String username, String password) {
        return false;
    }

    @Override
    public String execute(String inputXmlStrings) {
        return null;
    }

    @Override
    public String actionReturnSchema() {
        return null;
    }

    @Override
    public String actionHelper() {
        return null;
    }

    @Override
    public String actionInputSchema() {
        return null;
    }

    @Override
    public String actionName() {
        return null;
    }

    public static void main(String[] args) {
        DataBaseConnection.setConnection(ActionCode.serviceName);
        String sqlForPara = "select * from shaanximodePara";
        DBDataSet dbForPara = new DBDataSet();
        dbForPara.setSQL(sqlForPara);
        QuerySet qsForPara = new QuerySet();
        ArrayList<Double> paras = new ArrayList<>();
        ArrayList<String> describes = new ArrayList<>();
        while (!dbForPara.isEmpty()) {
            String describe = dbForPara.getData("describe").toString();
            double para = ((BigDecimal) dbForPara.getData("para")).doubleValue();
            describes.add(describe);
            paras.add(para);
            if( dbForPara.hasNext()){
                dbForPara.goNext();
            }else {
                break;
            }
        }

        System.out.println(paras);
        System.out.println(describes);
    }
}
