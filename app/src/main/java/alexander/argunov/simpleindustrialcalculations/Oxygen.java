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

    public double getAirDissipation() {
        return airDissipation;
    }

    public double getOxyConc() {
        return oxyConc;
    }

    public void setOxyConc(double oxyConc) {
        this.oxyConc = oxyConc;
    }

    public double getOxyFlow() {
        return oxyFlow;
    }

    public void setOxyFlow(double oxyFlow) {
        this.oxyFlow = oxyFlow;
    }

    public double getOxyInAir() {
        return oxyInAir;
    }

    public void setOxyInAir(double oxyInAir) {
        this.oxyInAir = oxyInAir;
    }

    public double getOxyPurity() {
        return oxyPurity;
    }

    public void setOxyPurity(double oxyPurity) {
        this.oxyPurity = oxyPurity;
    }

    public double getAirFlow() {
        return airFlow;
    }

    public void setAirFlow(double airFlow) {
        this.airFlow = airFlow;
    }

    public double getFurnaceOxyConc() {
        return furnaceOxyConc;
    }

    public void setFurnaceOxyConc(double furnaceOxyConc) {
        this.furnaceOxyConc = furnaceOxyConc;
    }

    double calcOxyFlow() {
        oxyFlow = (airFlow * (oxyConc - oxyInAir)) / (oxyPurity - oxyInAir);
        return oxyFlow;
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
}
