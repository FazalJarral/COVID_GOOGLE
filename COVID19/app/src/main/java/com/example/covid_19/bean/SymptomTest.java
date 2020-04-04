package com.example.covid_19.bean;

public class SymptomTest {
    boolean isFever ;
    boolean cough;
    boolean runnyNose;
    boolean tiredness;
    boolean cardiac_pressure;
    String temperature;
    User user;
    String city;
    String id;

    public SymptomTest() {
        this.isFever = false;
        this.cough = false;
        this.runnyNose = false;
        this.tiredness = false;
        this.cardiac_pressure = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }



    public boolean isFever() {
        return isFever;
    }

    public void setisFever(boolean fever) {
        isFever = fever;
    }

    public boolean isCough() {
        return cough;
    }

    public void setCough(boolean cough) {
        this.cough = cough;
    }

    public boolean isRunnyNose() {
        return runnyNose;
    }

    public void setRunnyNose(boolean runnyNose) {
        this.runnyNose = runnyNose;
    }

    public boolean isTiredness() {
        return tiredness;
    }

    public void setTiredness(boolean tiredness) {
        this.tiredness = tiredness;
    }

    public boolean isCardiac_pressure() {
        return cardiac_pressure;
    }

    public void setCardiac_pressure(boolean cardiac_pressure) {
        this.cardiac_pressure = cardiac_pressure;
    }
}
