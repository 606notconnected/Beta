package com.android.tutor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/18 0018.
 */
public class PersonSqliteOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库的构造方法
     * 数据库查询的结果集，为null则使用默认的结果集
     * 数据库的版本，从1开始，小于1则抛异常
     * @param context
     */
    public PersonSqliteOpenHelper(Context context) {
        super(context, "person.db", null, 1);
    }

    /**
     * 数据库在第一次被创建时调用，表结构，初始化
     * 数据类型的长度是无用的，只是给程序员看的
     * @param sqLiteDatabase 数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table person (id integer primary key autoincrement, name varchar(20), number varchar(20))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}