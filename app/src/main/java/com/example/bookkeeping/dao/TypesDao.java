package com.example.bookkeeping.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookkeeping.entity.Type;

import java.nio.file.WatchEvent;
import java.util.List;

@Dao
public interface TypesDao {
    @Insert
    void insertTypes(Type... types);

    @Delete
    void deleteTypes(Type... types);

    @Update
    void updateTypes(Type... types);

    @Query("SELECT * FROM type_tb WHERE type_name='添加' AND kind=:kind")
    Type getAddType(int kind);

    @Query("SELECT * FROM type_tb WHERE kind=:kind")
    List<Type> getTypeList(int kind);

    @Query("SELECT * FROM type_tb")
    List<Type> getAllTypes();
}
