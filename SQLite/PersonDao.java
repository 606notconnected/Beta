package com.android.tutor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/18 0018.
 */
public class PersonDao {

    private PersonSqliteOpenHelper helper;

    public PersonDao(Context context) {
        helper = new PersonSqliteOpenHelper(context);
    }

    public void add(String name, String number){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into person (name,number) values (?,?)", new Object[]{name,number});
        db.close();
    }

    public boolean find(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from person where name=?", new String[]{name});
        boolean result = cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }

    public void update(String name, String newnumber){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update person set number=? where name=?", new Object[]{newnumber, name});
        db.close();
    }

    public void delete(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from person where name=?",new String[]{name});
        db.close();
    }

    public List<Person> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Person> persons = new ArrayList<Person>();
        Cursor cursor = db.rawQuery("select name,id,number from person ",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String number = cursor.getString(cursor.getColumnIndex("number"));
            Person p = new Person(id,name,number);
            persons.add(p);
        }
        cursor.close();
        db.close();
        return persons;
    }
}
