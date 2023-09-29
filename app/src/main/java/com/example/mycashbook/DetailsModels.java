package com.example.mycashbook;

import java.util.Date;

public class DetailsModels  {
    private double nominal;
    private String desc;
    private Date date;
    private String type;

    public DetailsModels(double nominal, String desc, Date date, String type) {
        this.nominal = nominal;
        this.desc = desc;
        this.date = date;
        this.type = type;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
