package domain;

public class SacramentProgress {
    private double PP; // 时段降雨量
    private double ROIMP; // 固定不透水面上的直接径流
    private double E0;
    private double EP;
    private double ADDRO; // 可变不透水形成的直接径流
    private double ADSUR; // 地面径流

    public double getPP() {
        return PP;
    }

    public void setPP(double pp) {
        PP = pp;
    }


    public void setROIMP(double roimp) {
        this.ROIMP = roimp;
    }

    public double getROIMP() {
        return ROIMP;
    }

    public double getE0() {
        return E0;
    }

    public void setE0(double e0) {
        this.E0 = e0;
    }

    public double getEP() {
        return EP;
    }

    public void setEP(double EP) {
        this.EP = EP;
    }

    public void setADDRO(double addro) {
        this.ADDRO = addro;
    }

    public double getADDRO() {
        return ADDRO;
    }

    public void setADSUR(double adsur) {
        this.ADSUR = adsur;
    }

    public double getADSUR() {
        return ADSUR;
    }
}
