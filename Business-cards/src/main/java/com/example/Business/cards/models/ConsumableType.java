package com.example.Business.cards.models;

public class ConsumableType {

    private String type;

    private int sumUsed;

    public ConsumableType(String type, int sumUsed) {
        this.type = type;
        this.sumUsed = sumUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSumUsed() {
        return sumUsed;
    }

    public void setSumUsed(int sumUsed) {
        this.sumUsed = sumUsed;
    }
}
