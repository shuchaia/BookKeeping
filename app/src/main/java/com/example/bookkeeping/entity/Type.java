package com.example.bookkeeping.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "type_tb")
public class Type {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "type_name")
    public String typeName;

    @ColumnInfo(name = "image_id")
    public int imageId;

    @ColumnInfo(name = "s_image_id")
    public int sImageId;

    @ColumnInfo(name = "color_string")
    public String colorString;

    public int kind;

    public Type() {
    }

    @Ignore
    public Type(String typeName, int imageId, int sImageId, int kind, String colorString) {
        this.typeName = typeName;
        this.imageId = imageId;
        this.sImageId = sImageId;
        this.colorString = colorString;
        this.kind = kind;
    }

    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
