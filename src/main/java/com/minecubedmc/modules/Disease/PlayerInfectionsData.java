package com.minecubedmc.modules.Disease;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerInfectionsData implements Serializable {

    private final ConcurrentHashMap<String, InfectionData> infections;

    public PlayerInfectionsData() {
        this.infections = new ConcurrentHashMap<>();
    }

    public final Map<String, InfectionData> getInfections() {
        return Collections.unmodifiableMap(infections);
    }

    public void addInfection(String infectionName) {
        // If player is infected again
        if (infections.containsKey(infectionName)) {
            // Extend infection time
            infections.get(infectionName).resetInfectionTries();
        }
        else {
            // Add new infection
            infections.put(infectionName, new InfectionData());
        }
    }

    public void removeInfection(String infectionName) {
        infections.remove(infectionName);
    }


}