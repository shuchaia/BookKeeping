package com.example.bookkeeping.db;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.example.bookkeeping.R;
import com.example.bookkeeping.dao.AccountsDao;
import com.example.bookkeeping.dao.TypesDao;
import com.example.bookkeeping.db.TallyDatabase;
import com.example.bookkeeping.entity.Account;
import com.example.bookkeeping.entity.Type;

import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DBManager {
    public static TallyDatabase tallyDatabase;
    private static TypesDao typesDao;
    private static AccountsDao accountsDao;

    /**
     * 初始化数据库
     * @param context
     */
    public static void initDB(Context context) {
        tallyDatabase = Room.databaseBuilder(context, TallyDatabase.class, "tally.db")
                .addMigrations()
                .build();
        typesDao = tallyDatabase.typesDao();
        accountsDao = tallyDatabase.accountsDao();

        if (typesDao.getAllTypes() == null || typesDao.getAllTypes().size() == 0) {
            typesDao.insertTypes(new Type("其他", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,0,"#5b9bd5"),
                    new Type("餐饮", R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,0,"#ed7d31"),
                    new Type("交通", R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,0,"#70ad47"),
                    new Type("购物", R.mipmap.ic_gouwu,R.mipmap.ic_gouwu_fs,0,"#ffc000"),
                    new Type("服饰", R.mipmap.ic_fushi,R.mipmap.ic_fushi_fs,0,"#4472c4"),
                    new Type("日用品", R.mipmap.ic_riyongpin,R.mipmap.ic_riyongpin_fs,0,"#91d024"),
                    new Type("娱乐", R.mipmap.ic_yule,R.mipmap.ic_yule_fs,0,"#b235e6"),
                    new Type("零食", R.mipmap.ic_lingshi,R.mipmap.ic_lingshi_fs,0,"#02ae75"),
                    new Type("烟酒茶", R.mipmap.ic_yanjiu,R.mipmap.ic_yanjiu_fs,0,"#95a2ff"),
                    new Type("学习", R.mipmap.ic_xuexi,R.mipmap.ic_xuexi_fs,0,"#fa8080"),
                    new Type("医疗", R.mipmap.ic_yiliao,R.mipmap.ic_yiliao_fs,0,"#ffc076"),
                    new Type("住宅", R.mipmap.ic_zhufang,R.mipmap.ic_zhufang_fs,0,"#fae768"),
                    new Type("水电煤", R.mipmap.ic_shuidianfei,R.mipmap.ic_shuidianfei_fs,0,"#87e885"),
                    new Type("通讯", R.mipmap.ic_tongxun,R.mipmap.ic_tongxun_fs,0,"#63b2ee"),
                    new Type("人情往来", R.mipmap.ic_renqingwanglai,R.mipmap.ic_renqingwanglai_fs,0,"#73abf5"),


                    new Type("其他", R.mipmap.in_qt,R.mipmap.in_qt_fs,1,"#cb9bff"),
                    new Type("薪资", R.mipmap.in_xinzi,R.mipmap.in_xinzi_fs,1,"#434348"),
                    new Type("奖金", R.mipmap.in_jiangjin,R.mipmap.in_jiangjin_fs,1,"#90ed7d"),
                    new Type("借入", R.mipmap.in_jieru,R.mipmap.in_jieru_fs,1,"#f7a35c"),
                    new Type("收债", R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,1,"#8085e9"),
                    new Type("利息收入", R.mipmap.in_lixifuji,R.mipmap.in_lixifuji_fs,1,"#05f8d6"),
                    new Type("投资回报", R.mipmap.in_touzi,R.mipmap.in_touzi_fs,1,"#0082fc"),
                    new Type("二手交易", R.mipmap.in_ershoushebei,R.mipmap.in_ershoushebei_fs,1,"#fdd845"),
                    new Type("意外所得", R.mipmap.in_yiwai,R.mipmap.in_yiwai_fs,1,"#22ed7c"));
        }
    }

    /**
     * 获取所有收支分类
     * @param kind
     * @return
     */
    public static List<Type> getTypeList(int kind) {
        return typesDao.getTypeList(kind);
    }

    /**
     * 获得数据库中某一天的具体记录
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static List<Account> getAccountsByTime(int year, int month, int day){
        return accountsDao.getAccountsByTime(year, month, day);
    }

    /**
     * 获取某一天某分类的总金额
     * @param year
     * @param month
     * @param kind
     * @param day
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static float getTotalMoney(int year, int month, int day, int kind){
        float total = 0.0f;
        List<Account> accounts = accountsDao.getAccountsByTimeAndKind(year, month, day, kind);
        float sum = 0.0f;
        for (Account account : accounts) {
            sum += account.getMoney();
        }
        total = sum;
        return total;
    }

    /**
     * 获取某一月某分类的总金额
     * @param year
     * @param month
     * @param kind
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static float getTotalMoney(int year, int month, int kind){
        float total = 0.0f;
        List<Account> accounts = accountsDao.getAccountsByTimeAndKind(year, month, kind);
        float sum = 0.0f;
        for (Account account : accounts) {
            sum += account.getMoney();
        }
        total = sum;
        return total;
    }

    /**
     * 获取某一年某分类的总金额
     * @param year
     * @param kind
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static float getTotalMoney(int year, int kind){
        float total = 0.0f;
        List<Account> accounts = accountsDao.getAccountsByTimeAndKind(year, kind);
        float sum = 0.0f;
        for (Account account : accounts) {
            sum += account.getMoney();
        }
        total = sum;
        return total;
    }

    /**
     * 获得某一月份某一分类下的总支出or收入
     * @param typeName
     * @param year
     * @param month
     * @param kind
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static float getTotalMoney(String typeName, int year, int month, int kind){
        float total = 0.0f;
        List<Account> accounts = accountsDao.getAccountsByTimeAndKindAndType(year, month, kind, typeName);
        float sum = 0.0f;
        for (Account account : accounts) {
            sum += account.getMoney();
        }
        total = sum;
        return total;
    }

    /**
     * 获取某一月份中的所有记录
     * @param year
     * @param month
     * @return
     */
    public static List<Account> getMonthlyAccounts(int year, int month){
        return accountsDao.getMonthlyAccounts(year, month);
    }



        /**
         * 删除几条记录
         * @param accounts
         */
    public static void delectAccount(Account... accounts){
        accountsDao.deleteAccounts(accounts);
    }

    /**
     * 根据关键字搜索记录
     * @param keyword
     * @return
     */
    public static List<Account> getAccountsLikeKeyword(String keyword){
        return accountsDao.getAccountsLikeKeyword("%"+keyword+"%");
    }

    /**
     * 清空所有记账记录
     */
    public static void clearAllAccounts(){
        List<Account> allAccounts = accountsDao.getAllAccounts();
        accountsDao.deleteAccounts(allAccounts.toArray(new Account[0]));
    }

    /**
     * 获取记录数据中的所有年份信息（已去重）
     * @return
     */
    public static List<Integer> getYears(){
        return accountsDao.getYears();
    }
}
