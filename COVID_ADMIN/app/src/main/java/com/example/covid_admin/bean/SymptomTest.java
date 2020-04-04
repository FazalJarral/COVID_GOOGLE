package com.example.covid_admin.bean;

public class SymptomTest {
    boolean fever ;
    boolean cough;
    boolean runnyNose;
    boolean tiredness;
    boolean cardiac_pressure;
    String temperature;
    User user;
    String city;
    String id;

    public SymptomTest() {
        this.fever = false;
        this.cough = false;
        this.runnyNose = false;
        this.tiredness = false;
        this.cardiac_pressure = false;
    }

    public boolean isFever() {
        return fever;
    }

    public void setFever(boolean fever) {
        this.fever = fever;
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
