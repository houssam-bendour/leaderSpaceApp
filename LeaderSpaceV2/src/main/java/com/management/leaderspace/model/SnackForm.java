package com.management.leaderspace.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SnackForm {
    private Map<UUID, Integer> snackQuantities = new HashMap<>();

    public Map<UUID, Integer> getSnackQuantities() {
        return snackQuantities;
    }

    public void setSnackQuantities(Map<UUID, Integer> snackQuantities) {
        this.snackQuantities = snackQuantities;
    }
}
