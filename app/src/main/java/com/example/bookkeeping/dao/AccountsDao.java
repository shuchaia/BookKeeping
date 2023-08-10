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
    void insertAccounts(Account... accounts);

    @Delete
    void deleteAccounts(Account... accounts);

    @Update
    void updateAccounts(Account... accounts);

    @Query("SELECT * FROM account_tb")
    List<Account> getAllTypes();

    @Query("SELECT * FROM account_tb where year = :year AND month = :month AND day = :day ORDER BY id DESC")
    List<Account> getAccountsByTime(int year, int month, int day);

    @Query("SELECT * FROM account_tb where year = :year AND month = :month AND kind = :kind")
    List<Account> getAccountsByTimeAndKind(int year, int month, int kind);

    @Query("SELECT * FROM account_tb where year = :year AND kind = :kind")
    List<Account> getAccountsByTimeAndKind(int year, int kind);

    @Query("SELECT * FROM account_tb where year = :year AND month = :month AND day = :day AND kind = :kind")
    List<Account> getAccountsByTimeAndKind(int year, int month, int day, int kind);
}
