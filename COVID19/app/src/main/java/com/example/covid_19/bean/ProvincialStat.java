package com.example.covid_19.bean;

import androidx.annotation.NonNull;

public class ProvincialStat {
    float AJK;
    float  Balochistan;
    float Gilgit;
    float KPK;
    float Punjab;
    float Sindh;

    public ProvincialStat() {
    }

    public float getAJK() {
        return AJK;
    }

    public void setAJK(float AJK) {
        this.AJK = AJK;
    }

    public float getBalochistan() {
        return Balochistan;
    }

    public void setBalochistan(float balochistan) {
        Balochistan = balochistan;
    }

    public float getGilgit() {
        return Gilgit;
    }

    public void setGilgit(float gilgit) {
        Gilgit = gilgit;
    }

    public float getKPK() {
        return KPK;
    }

    public void setKPK(float KPK) {
        this.KPK = KPK;
    }

    public float getPunjab() {
        return Punjab;
    }

    public void setPunjab(float punjab) {
        Punjab = punjab;
    }

    public float getSindh() {
        return Sindh;
    }

    public void setSindh(float sindh) {
        Sindh = sindh;
    }

    @NonNull
    @Override
    public String toString() {
        return getPunjab() + getAJK() + getBalochistan() + getGilgit() + getKPK() + getSindh() + "";
    }
}
