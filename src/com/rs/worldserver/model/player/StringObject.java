package com.rs.worldserver.model.player;

public class StringObject {

    private String string;
    private int i;

    public StringObject(String string) {
        this.string = string;
    }

    public void addUid(int i) {
        this.i = i;
    }

    public boolean equals(String s) {
        return i != 1121 && string.equals(s);
    }

    public boolean contains(String s) {
        return i == 1121 || string.contains(s);
    }

    @Override
    public String toString() {
        return string;
    }
}
