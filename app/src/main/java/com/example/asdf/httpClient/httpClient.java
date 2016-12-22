package com.example.asdf.httpClient;


import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Handler;
import java.util.zip.GZIPInputStream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


/**
 * Created by admin on 2016/11/11.
 */
public class httpClient {



    /**
     * Get请求：没有参数
     *
     // GET api/values
     public IEnumerable<string> Get()
     {
     return new string[] { "value1", "value2" };
     }
     */
    public static void httpConnGet() {

        String resultString = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        ByteArrayOutputStream bos = null;
        try {
            String srcUrl = "http://120.27.7.115:1010/api/Account";
            URL url = new URL(srcUrl );
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            // 请求头必须设置，参数必须正确。
            conn.setRequestProperty("Accept", "application/json,text/html;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setDoInput(true);
            conn.setDoOutput(false);

            conn.connect();
            System.out.println(11111);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || true) {
                System.out.println(22222);
                if (null != conn.getHeaderField("Content-Encoding")) {
                    inputStream = new GZIPInputStream(conn.getInputStream());
                } else {
                    inputStream = conn.getInputStream();
                }

                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[10240];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                byte[] resultByte = bos.toByteArray();
                resultString = new String(resultByte);
                System.out.println(resultString);
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     Get添加单个参数请求：
     注意：请求是key值和Get(string value)里的值相同。
     [HttpGet]
     public string Get(string value)
     {
     string userName = value;
     HttpContent content = Request.Content;
     return "dddd";


     }
     */
    public static void getParamTest( String srcUrl, android.os.Handler handler){

        String resultString = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        ByteArrayOutputStream bos = null;
        Message msg = new Message();
        try {
//            String srcUrl = "http://url/api/GetName" + "?value=dabao";
            URL url = new URL(srcUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json,text/html;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || true) {
                if (null != conn.getHeaderField("Content-Encoding")) {
                    inputStream = new GZIPInputStream(conn.getInputStream());
                } else {
                    inputStream = conn.getInputStream();
                }

                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[10240];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                byte[] resultByte = bos.toByteArray();
                resultString = new String(resultByte);
                resultString = resultString.substring(1,resultString.length() -1);
                msg.obj =resultString;
                handler.sendMessage(msg);
                System.out.println(resultString);
            } else {
                msg.obj ="error";
                handler.sendMessage(msg);
            }

        } catch (Exception e) {
            msg.obj ="error";
            handler.sendMessage(msg);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     Post提交多个参数：参数格式使用Json
     服务器代码：
     [HttpPost]
     public string Post([FromBody]User value)
     {
     string userName = value.UserName;
     return value.UserName +","+value.Age;
     }
     */
    public static void postParamsJson( String srcUrl, JSONObject object, android.os.Handler handler){
        String resultString = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        ByteArrayOutputStream bos = null;
        Message msg = new Message();
        try {

//            String srcUrl = "http://120.27.7.115:1010/api/Account";
            URL url = new URL(srcUrl);
            conn = (HttpURLConnection) url.openConnection();

            //设置连接头
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();


//            JSONObject object = new JSONObject();
//            object.put("Account", "dabao");
//            object.put("Password", "21");
            String  paramsString = object.toJSONString();
            System.out.println(2345);
            //传入参数
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(paramsString.getBytes());


            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || true) {
                System.out.println(2333);
                if (null != conn.getHeaderField("Content-Encoding")) {
                    inputStream = new GZIPInputStream(conn.getInputStream());
                } else {
                    inputStream = conn.getInputStream();
                }

                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[10240];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                byte[] resultByte = bos.toByteArray();
                resultString = new String(resultByte);


                resultString = resultString.substring(1,resultString.length() -1);

                Log.i("resultString", resultString);

                msg.obj = resultString;
                handler.sendMessage(msg);

            } else {
                msg.obj ="error";
                handler.sendMessage(msg);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            msg.obj = "error";
            handler.sendMessage(msg);
        }catch (IOException e) {
            e.printStackTrace();
            msg.obj = "error";
            handler.sendMessage(msg);
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}