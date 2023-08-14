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
    List<Account> getAllAccounts();

    @Query("SELECT * FROM account_tb where year = :year AND month = :month AND day = :day ORDER BY id DESC")
    List<Account> getAccountsByTime(int year, int month, int day);

    @Query("SELECT * FROM account_tb where year = :year AND month = :month AND kind = :kind ORDER BY month")
    List<Account> getAccountsByTimeAndKind(int year, int month, int kind);

    @Query("SELECT * FROM account_tb where year = :year AND kind = :kind ORDER BY year")
    List<Account> getAccountsByTimeAndKind(int year, int kind);

    @Query("SELECT * FROM account_tb where year = :year AND month = :month AND day = :day AND kind = :kind ORDER BY day")
    List<Account> getAccountsByTimeAndKind(int year, int month, int day, int kind);

    // 可以通过加单引号的方式把%跟keyword拼接
    //@Query("SELECT * FROM account_tb where beizhu LIKE '%'+(:keyword)+'%' OR type_name LIKE '%'+(:keyword)+'%'")
    //直接在DBManger里拼接，这里传入的keyword本身就带有%
    @Query("SELECT * FROM account_tb where beizhu LIKE :keyword OR type_name LIKE :keyword")
    List<Account> getAccountsLikeKeyword(String keyword);

    @Query("SELECT * FROM account_tb where year = :year AND month = :month ORDER BY day")
    List<Account> getMonthlyAccounts(int year, int month);

    @Query("SELECT DISTINCT year FROM account_tb ORDER BY year DESC")
    List<Integer> getYears();

    @Query("SELECT * FROM account_tb where year = :year AND month = :month AND kind =:kind AND type_name =:typeName ORDER BY day")
    List<Account> getAccountsByTimeAndKindAndType(int year, int month, int kind, String typeName);
}
