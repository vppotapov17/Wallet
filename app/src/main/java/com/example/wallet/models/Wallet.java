package com.example.wallet.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Wallet implements Serializable {
    private int flagIcon;
    private String name;

    private HashMap<String, Double> values;

    public Wallet(int flagIcon, String name){
        setFlagIcon(flagIcon);
        setName(name);
        values = new HashMap<>();
    };


    public int getFlagIcon() {
        return flagIcon;
    }

    public void setFlagIcon(int flagIcon) {
        this.flagIcon = flagIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Double> getValues(){
        return values;
    }

    public void addValue(Double value, String date){
        values.put(date, value);
    }
}
