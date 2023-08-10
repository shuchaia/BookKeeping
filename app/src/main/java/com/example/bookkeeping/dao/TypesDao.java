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
    void insertTypes(Type... types);

    @Delete
    void deleteTypes(Type... types);

    @Update
    void updateTypes(Type... types);

    @Query("SELECT * FROM type_tb where kind=:kind")
    List<Type> getTypeList(int kind);

    @Query("SELECT * FROM type_tb")
    List<Type> getAllTypes();
}
