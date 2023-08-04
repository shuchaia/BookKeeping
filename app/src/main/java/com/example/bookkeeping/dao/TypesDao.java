package com.example.bookkeeping.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookkeeping.entity.Type;

import java.util.List;

@Dao
public interface TypesDao {
    @Insert
    public void insertTypes(Type... types);

    @Delete
    public void deleteTypes(Type... types);

    @Update
    public void updateTypes(Type... types);

    @Query("SELECT * FROM type_tb where kind=:kind")
    public List<Type> getTypeList(int kind);

    @Query("SELECT * FROM type_tb")
    public List<Type> getAllTypes();
}
