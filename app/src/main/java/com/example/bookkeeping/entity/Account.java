package com.example.bookkeeping.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "account_tb")
public class Account {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "type_name")
    public String typeName;
    @ColumnInfo(name = "s_image_id")
    public int sImageId;
    public String beizhu;
    public float money;
    public String time; // 保存时间字符串
    public int year;
    public int month;
    public int day;
    public int kind; // 支出0，收入1

    public Account() {
    }

    @Ignore
    public Account(String typeName, int sImageId) {
        this.typeName = typeName;
        this.sImageId = sImageId;
    }

    @Ignore
    public Account(String typeName, int sImageId, String beizhu, float money, String time, int year, int month, int day, int kind) {
        this.typeName = typeName;
        this.sImageId = sImageId;
        this.beizhu = beizhu;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
