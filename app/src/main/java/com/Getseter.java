package com;

/**
 * Created by user on 9/2/2017.
 */

public class Getseter {

    String _id;
    String _name;
    String _desc;
    String _count;

    String img;
    String cdate;
    String udate;
    String udate3;
    String delTime;

    public Getseter() {

    }

    ////  8
    public Getseter(String id, String name, String _desc,String _count, String img, String cdate,String udate,String udate3,String delTime){
        this._id = id;
        this._name = name;
        this._desc = _desc;
        this._count = _count;
        this.img = img;
        this.cdate = cdate;
        this.udate = udate;
        this.udate3 = udate3;
        this.delTime = delTime;
    }

    // getting ID
    public String getID(){
        return this._id;
    }

    // setting id
    public void setID(String id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getDesc(){
        return this._desc;
    }

    // setting phone number
    public void setDesc(String _desc){
        this._desc = _desc;
    }

    public String getCount(){
        return this._count;
    }

    // setting phone number
    public void setCount(String _count){
        this._count = _count;
    }

    public String getImg(){
        return this.img;
    }
    // setting name
    public void setImg(String img){
        this.img = img;
    }
    // getting phone number
    public String getCdate(){
        return this.cdate;
    }

    // setting phone number
    public void setCdate(String cdate){
        this.cdate = cdate;
    }

    public String getUdate(){
        return this.udate;
    }


    // setting phone number
    public void setUdate(String udate){
        this.udate = udate;
    }

    public void setUdate3(String udate3){
        this.udate3 = udate3;
    }

    public String getUdate3() {
        return udate3;
    }

    public String getDelTime() {
        return delTime;
    }

    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }
}
