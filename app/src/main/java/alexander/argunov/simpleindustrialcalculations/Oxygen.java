package alexander.argunov.simpleindustrialcalculations;

import java.io.Serializable;

class Oxygen implements Serializable {

    private double oxyConc;
    private double oxyFlow;
    private double oxyInAir;
    private double oxyPurity;
    private double airFlow;
    private double furnaceOxyConc;
    private double airDissipation;

    Oxygen() {
        airFlow = 0;
        oxyConc = 0;
        oxyFlow = 0;
        oxyInAir = 0;
        oxyPurity = 0;
        furnaceOxyConc = 0;
        airDissipation = 0;
    }

    double getAirDissipation() {
        return airDissipation;
    }

    double getOxyConc() {
        return oxyConc;
    }

    void setOxyConc(double oxyConc) {
        this.oxyConc = oxyConc;
    }

    double getOxyFlow() {
        return oxyFlow;
    }

    void setOxyFlow(double oxyFlow) {
        this.oxyFlow = oxyFlow;
    }

    double getOxyInAir() {
        return oxyInAir;
    }

    void setOxyInAir(double oxyInAir) {
        this.oxyInAir = oxyInAir;
    }

    double getOxyPurity() {
        return oxyPurity;
    }

    void setOxyPurity(double oxyPurity) {
        this.oxyPurity = oxyPurity;
    }

    double getAirFlow() {
        return airFlow;
    }

    void setAirFlow(double airFlow) {
        this.airFlow = airFlow;
    }

    double getFurnaceOxyConc() {
        return furnaceOxyConc;
    }

    void setFurnaceOxyConc(double furnaceOxyConc) {
        this.furnaceOxyConc = furnaceOxyConc;
    }

    void calcOxyFlow() {
        oxyFlow = (airFlow * (oxyConc - oxyInAir)) / (oxyPurity - oxyInAir);
    }

    double calcOxyConc() {
        double oxygenInAir = airFlow * oxyInAir / 100;
        double oxygenInOxygen = oxyFlow * oxyPurity / 100;
        oxyConc = (oxygenInAir + oxygenInOxygen) / (airFlow + oxyFlow) * 100;
        return oxyConc;
    }

    double calcAirDissipation() {
        airDissipation = oxyFlow * (oxyPurity - oxyInAir) / (furnaceOxyConc - oxyInAir) - airFlow;
        return airDissipation;
    }

    double calcMinFurnaceOxyConc() {
        double maxAirFlowOnKompresssor = R.string.air_flow_max;
        double oxygenInAir = maxAirFlowOnKompresssor * oxyInAir / 100;
        double oxygenInOxygen = oxyFlow * oxyPurity / 100;
        return (oxygenInAir + oxygenInOxygen) / (maxAirFlowOnKompresssor + oxyFlow) * 100;

    }
}
