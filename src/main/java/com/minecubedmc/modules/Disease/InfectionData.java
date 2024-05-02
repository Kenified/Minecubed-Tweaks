package com.minecubedmc.modules.Disease;

import java.io.Serializable;

public class InfectionData implements Serializable {
    private int infectionTries;

    public InfectionData() {
        this.infectionTries = 0;
    }

    public int getInfectionTries() {
        return infectionTries;
    }

    public void incrementInfectionTries() {
        this.infectionTries++;
    }

    public void resetInfectionTries() {
        this.infectionTries = 0;
    }
}
