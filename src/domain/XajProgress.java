package domain;


/**
 * 三水源计算对象
 */
public class XajProgress {
    // 三次蒸发的产流计算
    double WU;
    double WL;
    double WD;

    double EU;
    double EL;
    double ED;
    double E0;
    double EP;

    double p;
    double R;

    //三水源划分
    double RS;
    double RSS;
    double RG;

    double S;
    int t;// 默认是日
    double FR;

    // 汇流计算
    // 坡面汇流
    double TRS;
    double TRSS;
    double TRG;
    double TR;
    // 河网汇流
    double Qout;

    public XajProgress() {
    }

    public double getWU() {
        return WU;
    }

    public void setWU(double WU) {
        this.WU = WU;
    }

    public double getWL() {
        return WL;
    }

    public void setWL(double WL) {
        this.WL = WL;
    }

    public double getWD() {
        return WD;
    }

    public void setWD(double WD) {
        this.WD = WD;
    }

    public double getEU() {
        return EU;
    }

    public void setEU(double EU) {
        this.EU = EU;
    }

    public double getEL() {
        return EL;
    }

    public void setEL(double EL) {
        this.EL = EL;
    }

    public double getED() {
        return ED;
    }

    public void setED(double ED) {
        this.ED = ED;
    }

    public double getE0() {
        return E0;
    }

    public void setE0(double e0) {
        E0 = e0;
    }

    public double getEP(XAJmode xaJmode) {
        return E0*xaJmode.getK();
    }

    public void setEP(double EP) {
        this.EP = EP;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public double getRS() {
        return RS;
    }

    public void setRS(double RS) {
        this.RS = RS;
    }

    public double getRSS() {
        return RSS;
    }

    public void setRSS(double RSS) {
        this.RSS = RSS;
    }

    public double getRG() {
        return RG;
    }

    public void setRG(double RG) {
        this.RG = RG;
    }

    public double getS() {
        return S;
    }

    public void setS(double s) {
        S = s;
    }

    public double getFR() {
        return FR;
    }

    public void setFR(double fr) {
        this.FR = fr;
    }

    public double getTRS() {
        return TRS;
    }

    public void setTRS(double TRS) {
        this.TRS = TRS;
    }

    public double getTRSS() {
        return TRSS;
    }

    public void setTRSS(double TRSS) {
        this.TRSS = TRSS;
    }

    public double getTRG() {
        return TRG;
    }

    public void setTRG(double TRG) {
        this.TRG = TRG;
    }

    public void setTR(double tr) {
        this.TR = tr;
    }

    public double getTR() {
        return TR;
    }

    public double getQout() {
        return Qout;
    }

    public void setQout(double qout) {
        Qout = qout;
    }
}
