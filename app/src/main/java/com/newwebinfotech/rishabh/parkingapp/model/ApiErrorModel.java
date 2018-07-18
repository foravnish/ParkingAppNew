package com.newwebinfotech.rishabh.parkingapp.model;

/**
 * Created by Abhijit-PC on 22-Feb-17.
 */
public class ApiErrorModel {

    private int task;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;

    public void setFields(String value1,String value2,String value3,String value4,String value5){
        this.value1=value1;
        this.value2=value2;
        this.value3=value3;
        this.value4=value4;
        this.value5=value5;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public String getValue5() {
        return value5;
    }

    public void setValue5(String value5) {
        this.value5 = value5;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
}
