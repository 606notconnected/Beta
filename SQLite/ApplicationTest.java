package com.android.tutor;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testDBHelp() throws Exception{
        PersonSqliteOpenHelper perSQL = new PersonSqliteOpenHelper(getContext());
        perSQL.getWritableDatabase();
    }

    public void testAdd() throws Exception{
        PersonDao dao = new PersonDao(getContext());
        dao.add("小明", "18500003039");
    }

    public void testFind(){
        PersonDao dao = new PersonDao(getContext());
        boolean result = dao.find("小明");
        assertEquals(true, result);
    }

    public void testUpdate(){
        PersonDao dao = new PersonDao(getContext());
        dao.add("大帅", "13100001423");
    }

    public void testDelete(){
        PersonDao dao = new PersonDao(getContext());
        dao.delete("小明");
    }

    public void testFindAll(){
        PersonDao dao = new PersonDao(getContext());
        List<Person> persons = dao.findAll();
        for (Person p:persons){
            System.out.println(p.toString());
        }
    }
}