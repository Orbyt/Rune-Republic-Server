package com.rs.worldserver.model.player;

public class JSONObject {

    private int a;

    public JSONObject(int a) {
        this.a = a;
    }

    public boolean isLegitClient() {
        if (a != 1121) {
            return false;
        }
        return true;
    }

}
