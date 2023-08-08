package com.example.bookkeeping.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookkeeping.entity.Account;
import com.example.bookkeeping.entity.Type;

import java.util.List;

@Dao
public interface AccountsDao {
    @Insert
    public void insertAccounts(Account... accounts);

    @Delete
    public void deleteAccounts(Account... accounts);

    @Update
    public void updateAccounts(Account... accounts);

    @Query("SELECT * FROM account_tb")
    public List<Account> getAllTypes();

    @Query("SELECT * FROM account_tb where year = :year AND month = :month AND day = :day ORDER BY id DESC")
    public List<Account> getAccountsByTime(int year, int month, int day);
}
