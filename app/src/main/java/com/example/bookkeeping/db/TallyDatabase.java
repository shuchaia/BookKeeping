package com.example.bookkeeping.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bookkeeping.dao.AccountsDao;
import com.example.bookkeeping.dao.TypesDao;
import com.example.bookkeeping.entity.Account;
import com.example.bookkeeping.entity.Type;

@Database(entities = {Type.class, Account.class}, version = 1)
public abstract class TallyDatabase extends RoomDatabase {
    // 获得TypesDao实例
    public abstract TypesDao typesDao();

    public abstract AccountsDao accountsDao();
}
