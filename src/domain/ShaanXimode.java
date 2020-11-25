package domain;


import java.util.ArrayList;

/**
 * 陕北模型
 */
public class ShaanXimode {
    //---------常规变量-----------
    private int type; // 下渗曲线选取
    private ArrayList<Double> ps; // 降雨过程
    private ArrayList<Double> es; //蒸发过程
    private double varT;  // 时间步长
    private double bx; // 下渗能力分布曲线指数
    private double area; //流域面积
    private double winit; //初始土壤含水
    private ArrayList<Double> ws; //土壤含水量
    private ArrayList<Double> rs; //土壤含水量

    //------霍顿下渗曲线参数-------
    private double f0; // 流域平均最大下渗能力
    private double fc; // 流域平均稳定下渗能力
    private double k; // 下渗能力衰弱系数
    private double err_limit; // 土壤含水计算的允许误差

    //------菲利普下渗曲线参数------
    private double A; // 菲利普下渗曲线参数
    private double B;

    public ShaanXimode() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Double> getPs() {
        return ps;
    }

    public void setPs(ArrayList<Double> ps) {
        this.ps = ps;
    }

    public ArrayList<Double> getEs() {
        return es;
    }

    public void setEs(ArrayList<Double> es) {
        this.es = es;
    }

    public double getVarT() {
        return varT;
    }

    public void setVarT(double varT) {
        this.varT = varT;
    }

    public double getBx() {
        return bx;
    }

    public void setBx(double bx) {
        this.bx = bx;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getWinit() {
        return winit;
    }

    public void setWinit(double winit) {
        this.winit = winit;
    }

    public ArrayList<Double> getWs() {
        return ws;
    }

    public void setWs(ArrayList<Double> ws) {
        this.ws = ws;
    }

    public ArrayList<Double> getRs() {
        return rs;
    }

    public void setRs(ArrayList<Double> rs) {
        this.rs = rs;
    }

    public double getF0() {
        return f0;
    }

    public void setF0(double f0) {
        this.f0 = f0;
    }

    public double getFc() {
        return fc;
    }

    public void setFc(double fc) {
        this.fc = fc;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getErr_limit() {
        return err_limit;
    }

    public void setErr_limit(double err_limit) {
        this.err_limit = err_limit;
    }

    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    @Override
    public String toString() {
        return "ShaanXimode{" +
                "type=" + type +
                ", ps=" + ps +
                ", es=" + es +
                ", varT=" + varT +
                ", bx=" + bx +
                ", area=" + area +
                ", ws=" + ws +
                ", f0=" + f0 +
                ", fc=" + fc +
                ", k=" + k +
                ", err_limit=" + err_limit +
                ", A=" + A +
                ", B=" + B +
                '}';
    }

    // 土壤含水量变化量计算
    private static ShaanXimode soilWCount(ShaanXimode shaanXimode) {
        ArrayList<Double> ps = shaanXimode.getPs();
        ArrayList<Double> es = shaanXimode.getEs();
        ArrayList<Double> rs = shaanXimode.getRs();
        ArrayList<Double> ws = shaanXimode.getWs();
        double ft = 0.0;
        ws.add(shaanXimode.getWinit());
        for (int i = 0; i < ps.size(); i++) {
            if (ps.get(i) - es.get(i) < 0) {
                rs.add(0.0);
                double ws2 = ws.get(i) - (ps.get(i) - es.get(i));
                ws.add(ws2);
            } else {
                if (shaanXimode.type == 0) {
                    ft = HortonInfil(shaanXimode, ws.get(i));
                } else if (shaanXimode.type == 1) {
                    ft = PhillipsInfli(shaanXimode, ws.get(i));
                }
                // 计算流域该时段的最大点下渗能力
                double fmm = ft * (1 + shaanXimode.getBx());
                if (ps.get(i) - es.get(i) > fmm) {
                    // 全流域产流
                    double rs2 = ps.get(i) - es.get(i) - fmm;
                    double ws2 = ws.get(i) + ft;
                    rs.add(rs2);
                    ws.add(ws2);
                } else {
                    double rs2 = ps.get(i) - es.get(i) - ft + ft * Math.pow(1 - (ps.get(i) - es.get(i)) / fmm, shaanXimode.getBx() + 1);
                    double ws2 = ws.get(i) + ps.get(i) - es.get(i) - rs2;
                    rs.add(rs2);
                    ws.add(ws2);
                }
            }
        }
        shaanXimode.setWs(ws);
        shaanXimode.setRs(rs);
        return shaanXimode;
    }

    /**
     * 菲利普斯下渗曲线计算
     *
     * @param shaanXimode 陕北模型
     * @param w1          时段初土壤含水
     * @return 下渗
     */
    private static double PhillipsInfli(ShaanXimode shaanXimode, double w1) {
        double ft = shaanXimode.B * shaanXimode.B * (1 + Math.sqrt(1 + shaanXimode.getA() * w1
                / (shaanXimode.B * shaanXimode.B))) / w1 + shaanXimode.getA();
        return ft;
    }

    /**
     * 霍顿下渗曲线计算
     *
     * @param shaanXimode 陕北模型
     * @param w1          时段初土壤含水
     * @return 下渗
     */
    private static double HortonInfil(ShaanXimode shaanXimode, double w1) {
        // 由经验初设模型参数
        double T = w1 / shaanXimode.getF0();

        double w1Bycount = shaanXimode.getFc() * T + 1 / shaanXimode.getK() * (shaanXimode.getF0() -
                shaanXimode.getFc()) * (1 - Math.exp(-shaanXimode.getK() * T));
        double fBycount = 0;
        while (Math.abs(w1 - w1Bycount) > shaanXimode.getErr_limit()) {
            fBycount = shaanXimode.getFc() + (shaanXimode.getF0() - shaanXimode.getFc()) * Math.exp(-shaanXimode.getK() * T);
            T = T + (w1 - w1Bycount) / fBycount;
            w1Bycount = shaanXimode.getFc() * T + 1 / shaanXimode.getK() * (shaanXimode.getF0() -
                    shaanXimode.getFc()) * (1 - Math.exp(-shaanXimode.getK() * T));
        }
        return fBycount;
    }

}
