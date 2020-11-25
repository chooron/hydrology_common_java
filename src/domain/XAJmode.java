package domain;


import java.util.ArrayList;

public class XAJmode {
    // 蒸发产流模型参数
    double wum;
    double wlm;
    double wdm;
    double b;
    double K;
    double C;

    // 蒸发降雨数据
    ArrayList<XajProgress> xajProgresses;
    double varT;

    // 出流系数
    double KSS;
    double KG;
    // 消退系数
    double KKG;
    double KKSS;

    // 模型率定参数
    double EX;//产流面积各点的自由水蓄水容量关系
    double SM;//流域平均自由水容量
    // 初始状态变量
    double wum0;//上层含水
    double wlm0;//下层含水
    double wdm0;//深层含水
    double S0;//流域初始自由水容量
    double FR0;//流域初始产流面积
    double QRSS0;//壤中流流量
    double QRG0;//基流流量

    //汇流计算参数
    double F; // 流域面积
    // 河网汇流
    ArrayList<Double> UH;// 无因次时段单位线

    public XAJmode() {
    }

    public double getWum() {
        return wum;
    }

    public void setWum(double wum) {
        this.wum = wum;
    }

    public double getWlm() {
        return wlm;
    }

    public void setWlm(double wlm) {
        this.wlm = wlm;
    }

    public double getWdm() {
        return wdm;
    }

    public void setWdm(double wdm) {
        this.wdm = wdm;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getK() {
        return K;
    }

    public void setK(double k) {
        K = k;
    }

    public double getC() {
        return C;
    }

    public void setC(double c) {
        C = c;
    }

    public ArrayList<XajProgress> getXajProgresses() {
        return xajProgresses;
    }

    public void setXajProgresses(ArrayList<XajProgress> xajProgresses) {
        this.xajProgresses = xajProgresses;
    }

    public double getVarT() {
        return varT;
    }

    public void setVarT(double varT) {
        this.varT = varT;
    }

    public double getKSS() {
        return KSS;
    }

    public void setKSS(double KSS) {
        this.KSS = KSS;
    }

    public double getKG() {
        return KG;
    }

    public void setKG(double KG) {
        this.KG = KG;
    }

    public double getKKG() {
        return KKG;
    }

    public void setKKG(double KKG) {
        this.KKG = KKG;
    }

    public double getKKSS() {
        return KKSS;
    }

    public void setKKSS(double KKSS) {
        this.KKSS = KKSS;
    }

    public double getEX() {
        return EX;
    }

    public void setEX(double EX) {
        this.EX = EX;
    }

    public double getSM() {
        return SM;
    }

    public void setSM(double SM) {
        this.SM = SM;
    }

    public double getWum0() {
        return wum0;
    }

    public void setWum0(double wum0) {
        this.wum0 = wum0;
    }

    public double getWlm0() {
        return wlm0;
    }

    public void setWlm0(double wlm0) {
        this.wlm0 = wlm0;
    }

    public double getWdm0() {
        return wdm0;
    }

    public void setWdm0(double wdm0) {
        this.wdm0 = wdm0;
    }

    public double getS0() {
        return S0;
    }

    public void setS0(double s0) {
        S0 = s0;
    }

    public double getFR0() {
        return FR0;
    }

    public void setFR0(double FR0) {
        this.FR0 = FR0;
    }

    public double getQRSS0() {
        return QRSS0;
    }

    public void setQRSS0(double QRSS0) {
        this.QRSS0 = QRSS0;
    }

    public double getQRG0() {
        return QRG0;
    }

    public void setQRG0(double QRG0) {
        this.QRG0 = QRG0;
    }

    public double getF() {
        return F;
    }

    public void setF(double f) {
        F = f;
    }

    public ArrayList<Double> getUH() {
        return UH;
    }

    public void setUH(ArrayList<Double> UH) {
        this.UH = UH;
    }

    /**
     * 三层蒸发下的蓄满产流计算
     *
     * @param xaJmode
     * @param evaporR1 该对象evaporR1的W为evaporR2的初的W，也即为evaporR2的W为evapor3的初W
     * @param evaporR2
     * @param p
     * @return
     */
    public static XajProgress evaporRCount3(XAJmode xaJmode, XajProgress evaporR1, XajProgress evaporR2, double p) {
        if (p + evaporR1.getWU() >= evaporR2.getEP(xaJmode)) {
            evaporR2.setEU(evaporR2.getEP(xaJmode));
            evaporR2.setEL(0);
            evaporR2.setED(0);
        } else {
            evaporR2.setEU(evaporR1.getWU() + p);
            if (evaporR1.getWL() >= xaJmode.getC() * xaJmode.getWlm()) {
                evaporR2.setEL((evaporR2.getEP(xaJmode) - evaporR2.getEU()) * evaporR1.getWL() / xaJmode.getWlm());
                evaporR2.setED(0);
            } else if (evaporR1.getWL() < xaJmode.getC() * xaJmode.getWlm() && evaporR1.getWL() >= xaJmode.getC() * (evaporR2.getEP(xaJmode) - evaporR2.getEU())) {
                evaporR2.setEL(xaJmode.getC() * (evaporR2.getEP(xaJmode) - evaporR2.getEU()));
                evaporR2.setED(0);
            } else if (evaporR1.getWL() < xaJmode.getC() * (evaporR2.getEP(xaJmode) - evaporR2.getEU())) {
                evaporR2.setEL(evaporR1.getWL());
                evaporR2.setED(xaJmode.getC() * (evaporR2.getEP(xaJmode) - evaporR2.getEU()) - evaporR2.getEL());
            }
        }
        double W = evaporR1.getWD() + evaporR1.getWU() + evaporR1.getWL();
        double E = evaporR2.getED() + evaporR2.getEL() + evaporR2.getEU();
        double WM = xaJmode.getWdm() + xaJmode.getWlm() + xaJmode.getWum();
        double WMM = WM * (1 + xaJmode.getB());
        double A = WMM * (1 - Math.pow(1 - W / WM, 1 / (xaJmode.getB() + 1)));
        if (p - E < 0) {
            evaporR2.setR(0);
            evaporR2.setWU(evaporR1.getWU() + p - evaporR2.getEU());
            evaporR2.setWL(evaporR1.getWL() - evaporR2.getEL());
            evaporR2.setWD(evaporR1.getWD() - evaporR2.getED());
        } else {
            double PE = p - E;
            double R;
            if (A + PE < WMM) {
                R = PE + W - WM + WM * Math.pow(1 - (PE + A) / WMM, xaJmode.getB() + 1);
                evaporR2.setR(R);
            } else {
                R = PE + W - WM;
                evaporR2.setR(PE + W - WM);
            }
            double wu_extra = 0;
            double wl_extra = 0;
            if (PE + evaporR1.getWU() - R < 0) {
                evaporR2.setWU(0);
            } else if (PE + evaporR1.getWU() - R > 0 && PE + evaporR1.getWU() - R < xaJmode.getWum()) {
                evaporR2.setWU(PE + evaporR1.getWU() - R);
            } else {
                evaporR2.setWU(xaJmode.getWum());
                wu_extra = PE + evaporR1.getWU() - R - xaJmode.getWum();
            }
            if (wu_extra <= 0) {
                evaporR2.setWL(evaporR1.getWL());
            } else if (wu_extra > 0 && wu_extra + evaporR1.getWL() < xaJmode.getWlm()) {
                evaporR2.setWL(evaporR1.getWL() + wu_extra);
            } else {
                evaporR2.setWL(xaJmode.getWlm());
                wl_extra = wu_extra + evaporR1.getWL() - xaJmode.getWlm();
            }
            if (wl_extra <= 0) {
                evaporR2.setWD(evaporR1.getWD());
            } else if (wl_extra > 0 && wl_extra + evaporR1.getWL() < xaJmode.getWdm()) {
                evaporR2.setWD(wl_extra + evaporR1.getWD());
            } else if (wl_extra + evaporR1.getWL() >= xaJmode.getWdm()) {
                evaporR2.setWD(xaJmode.getWdm());
            }
        }

        return evaporR2;
    }


    /**
     * 三水源划分计算
     *
     * @param xaJmode 新安江模型
     * @return 新安江模型
     */
    public static XAJmode waterDivide(XAJmode xaJmode) {
        double S0;//上时段的平均自由水深
        double FR0;//上时段的产流面积
        double KG = xaJmode.getKG();
        double KSS = xaJmode.getKSS();
        //------模型率定参数--------
        double EX = xaJmode.getEX();//产流面积各点的自由水蓄水容量关系
        double SM = xaJmode.getSM();//流域平均自由水容量
        ArrayList<XajProgress> xajProgresses = xaJmode.getXajProgresses();
        for (int i = 0; i < xajProgresses.size(); i++) {
            double PE = xajProgresses.get(i).getP() - xajProgresses.get(i).getED() -
                    xajProgresses.get(i).getEU() - xajProgresses.get(i).getEL();

            double RS; // 地面径流
            double RSS; // 壤中流
            double RG;//地下径流
            double S;//自由水在产流面积上的平均水深
            double RSD;// 计算步长内的地面径流
            double RSSD;//计算步长内的壤中流
            double RGD; // 计算步长内的地下径流
            int N1 = (int) (24 / xaJmode.getVarT() * ((int) xajProgresses.get(i).getR() / 5 + 1));
            double KSSD = (1 - Math.pow(1 - (KG + KSS), 1.0 / N1)) / (1 + KG / KSS);//壤中流出流系数
            double KGD = KSSD * KG / KSS;//地下径流出流系数
            double FR;

            // 第一次运算与其他运算初值有不同
            if (i == 0) {
                S0 = xaJmode.getS0();
                FR0 = xaJmode.getFR0();
            } else {
                S0 = xajProgresses.get(i - 1).getS();
                FR0 = xajProgresses.get(i - 1).getFR();
            }

            if (PE <= 0) {
                RS = 0;
                RSS = S0 * KSSD * FR0;
                RGD = S0 * FR0 * KGD;
                RG = RGD;
                S = S0 - (RSS + RG) / FR0;
                FR = 0;//无降雨产流面积为0
            } else {
                FR = xajProgresses.get(i).getR() / PE > 1 ? 1 : xajProgresses.get(i).getR() / PE;  //产流面积
                S = FR0 * S0 / FR;
                double Q = xajProgresses.get(i).getR() / FR; //净雨分段计算
                int N = (int) Q / 5 + 1;
                Q /= N;
                double KSSDD = (1 - Math.pow(1 - (KGD + KSSD), 1.0 / N)) / (1 + KGD / KSSD);
                double KGDD = KSSDD * KGD / KSSD;
                RS = 0;
                RSS = 0;
                RG = 0;
                double SMM = (1 + EX) * SM;
                double SMMF;
                if (EX == 0) {
                    SMMF = SMM;
                } else {
                    SMMF = (1 - Math.pow(1 - FR, 1 / EX)) * SMM;
                }
                double SMF = SMMF / (1 + EX);
                for (int j = 0; j < N; j++) {
                    if (S > SMF) {
                        S = SMF;
                    }
                    double AU = SMMF * (1 - Math.pow(1 - S / SMF, 1 / (1 + EX))); // 与S对应的纵坐标AU
                    if (AU + Q <= 0) {
                        S = 0;
                        RGD = 0;
                        RSD = 0;
                        RSSD = 0;
                    } else {
                        if (Q + AU >= SMMF) {
                            RSD = (Q + S - SMF) * FR;
                            RSSD = SMF * KSSDD * FR;
                            RGD = SMF * FR * KGDD;
                            S = SMF - (RSSD + RGD) / FR;
                        } else {
                            RSD = (Q - SMF + S + SMF * Math.pow(1 - (Q + AU) / SMMF, 1 + EX)) * FR;
                            RSSD = (S + Q - RSD / FR) * KSSDD * FR;
                            RGD = (S + Q - RSD / FR) * KGDD * FR;
                            S = S + Q - (RSD + RSSD + RGD) / FR;
                        }
                    }
                    RS += RSD;
                    RSS += RSSD;
                    RG += RGD;
                }

            }
            xajProgresses.get(i).setRS(RS);
            xajProgresses.get(i).setRSS(RSS);
            xajProgresses.get(i).setRG(RG);
            xajProgresses.get(i).setS(S);
            xajProgresses.get(i).setFR(FR);
            // 输出RS,RSS,RG,S
        }
        return xaJmode;
    }

    public static XAJmode ConfluenceCount(XAJmode xaJmode) {
        // 参数计算
        double varT = xaJmode.getVarT();//设置计算时段
        double M = 24.0 / varT;
        double KKSS = xaJmode.getKKSS();//日模型消退系数
        double KKSSD = Math.pow(KKSS, 1 / M);
        double KKG = xaJmode.getKKG();//日模型消退系数
        double KKGD = Math.pow(KKG, 1 / M);
        // 坡地汇流计算
        ArrayList<XajProgress> xajProgresses = xaJmode.getXajProgresses();
        double U = xaJmode.getF() / (3.6 * xaJmode.getVarT()); // 单位线转换系数
        double QRSS0 = xaJmode.getQRSS0();
        double QRG0 = xaJmode.getQRG0();
        for (int i = 0; i < xajProgresses.size(); i++) {
            XajProgress xajProgress = xajProgresses.get(i);
            xajProgress.setTRS(xajProgresses.get(i).getRS()  * U);
            if (i > 0) {
                xajProgress.setTRSS(xajProgresses.get(i - 1).getTRSS() * KKSS + xajProgress.getRSS() * (1 - KKSS) * U);
                xajProgress.setTRG(xajProgresses.get(i - 1).getRG() * KKG + xajProgress.getRG() * (1 - KKG) * U);
            } else {
                xajProgress.setTRSS(QRSS0 * KKSS + xajProgress.getRSS() * (1 - KKSS) * U);
                xajProgress.setTRG(QRG0 * KKG + xajProgress.getRG() * (1 - KKG) * U);
            }
            xajProgress.setTR(xajProgress.getTRS() + xajProgress.getTRSS() + xajProgress.getTRG());
            // 河网汇流计算
            double Qout = 0;
            for (int j = 1; j < xaJmode.getUH().size(); j++) {
                if (i - j + 1 < 0) {
                    continue;
                }
                Qout += xaJmode.getUH().get(j - 1) * xajProgresses.get(i - j + 1).getTR();
            }
            xajProgresses.get(i).setQout(Qout);

        }
        return xaJmode;
    }
}
