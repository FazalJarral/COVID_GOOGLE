package com.example.covid_19.bean;

public class PatientData {
    String cases;
    String todayCases;
    String deaths;
    String recovered;

    public PatientData(String cases, String todayCases, String deaths, String recovered) {
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
}
