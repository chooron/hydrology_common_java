package domain;

import java.util.ArrayList;

/**
 * 萨克门托模型
 */
public class SacramentoMode {

    // 模型参数
    double PCTIM;// 不透水面占全流域百分比
    double ADIMP;// 可变不透水面积
    double SARVA;// 河道湖泊占全流域面积百分比
    double UZTWM;// 上层张力水容量
    double UZFWM;// 上层自由水容量
    double UZK;// 上层自由水日出流系数
    double ZPERC;// 下渗系数,下层最干旱时的最大下渗率
    double REXP;// 下渗指数,下层随上层蓄水量变化的函数形成
    double LZTWM;// 下层张力水容量
    double LZFSM;// 下层浅层自由水容量
    double LZFPM;// 下层深层自由水容量
    double LZSK;// 下层浅层自由水出流系数
    double LZPK;// 下层深层自由水出流系数
    double PFREE;// 下渗水量直接补给下层自由水比例
    double RSERV;// 下层自由水未蒸发部分所占比例
    double SIDE;// 不闭合的地下水出流量,未检测到的地下水占总基流百分比
    double SSOUT;// 河槽总径流中的径流损失系数

    // 过程计算输入
    ArrayList<Double> UH;// 无因次单位线
    double M;// 单位线底宽
    double varT;// 计算时段步长
    ArrayList<SacramentProgress> progresses;
    // 初始输入
    double UZTWC;// 上层张力水含量
    double UZFWC;// 上层自由水含量
    double LZTWC;// 下层张力水含量
    double LZFSC;// 下层浅层自由水含量
    double LZFPC;// 下层深层自由水含量
    double ADIMC;// 可变不透水面积上的含水量
    double QC0;// 地下水初始流量
    private double KC;
    private double PAREA;


    public SacramentoMode() {
    }

    public double getPCTIM() {
        return PCTIM;
    }

    public void setPCTIM(double PCTIM) {
        this.PCTIM = PCTIM;
    }

    public double getADIMP() {
        return ADIMP;
    }

    public void setADIMP(double ADIMP) {
        this.ADIMP = ADIMP;
    }

    public double getSARVA() {
        return SARVA;
    }

    public void setSARVA(double SARVA) {
        this.SARVA = SARVA;
    }

    public double getUZTWM() {
        return UZTWM;
    }

    public void setUZTWM(double UZTWM) {
        this.UZTWM = UZTWM;
    }

    public double getUZFWM() {
        return UZFWM;
    }

    public void setUZFWM(double UZFWM) {
        this.UZFWM = UZFWM;
    }

    public double getUZK() {
        return UZK;
    }

    public void setUZK(double UZK) {
        this.UZK = UZK;
    }

    public double getZPERC() {
        return ZPERC;
    }

    public void setZPERC(double ZPERC) {
        this.ZPERC = ZPERC;
    }

    public double getREXP() {
        return REXP;
    }

    public void setREXP(double REXP) {
        this.REXP = REXP;
    }

    public double getLZTWM() {
        return LZTWM;
    }

    public void setLZTWM(double LZTWM) {
        this.LZTWM = LZTWM;
    }

    public double getLZFSM() {
        return LZFSM;
    }

    public void setLZFSM(double LZFSM) {
        this.LZFSM = LZFSM;
    }

    public double getLZFPM() {
        return LZFPM;
    }

    public void setLZFPM(double LZFPM) {
        this.LZFPM = LZFPM;
    }

    public double getLZSK() {
        return LZSK;
    }

    public void setLZSK(double LZSK) {
        this.LZSK = LZSK;
    }

    public double getLZPK() {
        return LZPK;
    }

    public void setLZPK(double LZPK) {
        this.LZPK = LZPK;
    }

    public double getPFREE() {
        return PFREE;
    }

    public void setPFREE(double PFREE) {
        this.PFREE = PFREE;
    }

    public double getRSERV() {
        return RSERV;
    }

    public void setRSERV(double RSERV) {
        this.RSERV = RSERV;
    }

    public double getSIDE() {
        return SIDE;
    }

    public void setSIDE(double SIDE) {
        this.SIDE = SIDE;
    }

    public double getSSOUT() {
        return SSOUT;
    }

    public void setSSOUT(double SSOUT) {
        this.SSOUT = SSOUT;
    }

    public ArrayList<Double> getUH() {
        return UH;
    }

    public void setUH(ArrayList<Double> UH) {
        this.UH = UH;
    }

    public double getM() {
        return M;
    }

    public void setM(double m) {
        M = m;
    }

    public double getVarT() {
        return varT;
    }

    public void setVarT(double varT) {
        this.varT = varT;
    }

    public ArrayList<SacramentProgress> getProgresses() {
        return progresses;
    }

    public void setProgresses(ArrayList<SacramentProgress> progresses) {
        this.progresses = progresses;
    }

    public double getUZTWC() {
        return UZTWC;
    }

    public void setUZTWC(double UZTWC) {
        this.UZTWC = UZTWC;
    }

    public double getUZFWC() {
        return UZFWC;
    }

    public void setUZFWC(double UZFWC) {
        this.UZFWC = UZFWC;
    }

    public double getLZTWC() {
        return LZTWC;
    }

    public void setLZTWC(double LZTWC) {
        this.LZTWC = LZTWC;
    }

    public double getLZFSC() {
        return LZFSC;
    }

    public void setLZFSC(double LZFSC) {
        this.LZFSC = LZFSC;
    }

    public double getLZFPC() {
        return LZFPC;
    }

    public void setLZFPC(double LZFPC) {
        this.LZFPC = LZFPC;
    }

    public double getADIMC() {
        return ADIMC;
    }

    public void setADIMC(double ADIMC) {
        this.ADIMC = ADIMC;
    }

    public double getQC0() {
        return QC0;
    }

    public void setQC0(double QC0) {
        this.QC0 = QC0;
    }

    public static SacramentoMode RunoffCaculation(SacramentoMode mode, SacramentProgress progress) {
        // 1.直接径流量计算
        //(1) 固定不透水面上的直接径流
        progress.setROIMP(progress.getPP() * mode.getPCTIM());
        // (2) 可变不透水面积上的直接径流
        double PAV = progress.getPP() - (mode.getUZTWM() - mode.getUZTWC()); // 有效降雨
        double PINC = PAV / mode.getVarT(); // 步长有效降雨
        double PIN = 0; // 可变不透水层面积上的直接降雨
        for (int i = 0; i < mode.getVarT(); i++) {
            PIN += PINC * (mode.getADIMC() - mode.getUZTWC()) / mode.getLZTWM() * mode.getADIMP();
        }
        progress.setADDRO(PIN);
        // 2.地面径流量
        //(1) 透水面积上的地面径流
        double PEX = 1; // 满足上层土壤缺水量后剩余的降雨量
        double ADSUR = 0;
        for (int i = 0; i < mode.getVarT(); i++) {
            ADSUR += PEX * mode.getPAREA();
        }
        //(2) 可变不透水面积上的地面径流

        return null;
    }

    public double getKC() {
        return KC;
    }

    public void setKC(double kc) {
        this.KC = kc;
    }

    public double getPAREA() {
        return PAREA;
    }

    public void setPAREA(double parea) {
        this.PAREA = parea;
    }
}
