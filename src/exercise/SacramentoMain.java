package exercise;

import domain.SacramentProgress;
import domain.SacramentoMode;

import java.util.ArrayList;

public class SacramentoMain {
    public static void main(String[] args) {
        SacramentoMode mode = new SacramentoMode();
        // 流域蒸发计算
        ArrayList<SacramentProgress> progresses = mode.getProgresses();
        for (SacramentProgress progress : progresses) {
            progress.setEP(progress.getE0()*mode.getKC());
            // 不透水产流计算
            double ROIMP = mode.getPCTIM()*progress.getPP();
            progress.setROIMP(ROIMP);
            // 变动不透水模型
        }
        // 不透水产流计算

    }
}
