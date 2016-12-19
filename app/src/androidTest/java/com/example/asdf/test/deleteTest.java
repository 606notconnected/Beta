package com.example.asdf.test;

import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.example.asdf.httpClient.httpClient;
import com.google.gson.Gson;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Useradmin on 2016/12/13.
 */
public class deleteTest extends TestCase {

    private delete delete;
    @Before
    public void setUp() throws Exception {
//        super.setUp();
        delete=new delete();
    }
    @After
    public void tearDown() throws Exception {

    }
    @Test
    protected void onCreate(Bundle savedInstanceState)  throws Exception{
        httpClient tmp1 =new httpClient();
        android.os.Handler handler1;
        handler1 = new android.os.Handler() {
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Gson gson = new Gson();
                pictures pic = gson.fromJson(tmp, pictures.class);
                String re = pic.getresult();
                System.out.println(re+"??");
                delete.dialog(9);
                assertEquals(re, "true");

            }
        };
        tmp1.getParamTest("http://120.27.7.115:1010/api/Image_Delete?account=15505903134", handler1);
    }
    class pictures
    {
        public  String result;
        public List<String> imageName;
        public String getresult(){
            return result;
        }
        public List<String> getPictureList() {
            return imageName;
        }
    }
}