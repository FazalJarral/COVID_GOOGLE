package com.example.covid_19.bean;

import androidx.annotation.NonNull;

public class Tip  {
    String title;
    String desc;

    boolean expanded;

    public Tip(String title, String desc) {
        this.desc = desc;
        this.title = title;
        this.expanded = false;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public String toString() {
        return title + " /n" + desc;
    }
}
