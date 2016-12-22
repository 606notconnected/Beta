package com.example.asdf.httpClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;


public class httpImage {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*10000000; //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    public static final String SUCCESS="1";
    public static final String FAILURE="0";
    public static Bitmap bitmap =null;

    /** * android上传文件到服务器
     * @param file 需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    public  static String uploadFile(File file,String RequestURL,Handler handler,String tele) {



        ByteArrayOutputStream bos = null;
        String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成
         String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; //内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); conn.setReadTimeout(TIME_OUT); conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false); //不允许使用缓存
            conn.setRequestMethod("POST"); //请求方式
            conn.setRequestProperty("Charset", CHARSET);
            //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if(file!=null) {
                /** * 当文件不为空，把文件包装并且上传 */
                OutputStream outputSteam=conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);

                //写account
                //设置文字头
                StringBuffer sb1 = new StringBuffer();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY); sb1.append(LINE_END);
                sb1.append("Content-Disposition: attachment; name=Account" + LINE_END);
                sb1.append(LINE_END);

                //写文字
               Log.i("tmp",tele);
                sb1.append(tele + LINE_END);
                dos.write(sb1.toString().getBytes());

                //写文件
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY); sb.append(LINE_END);
                /**
                 * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字
                 */
                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());

                //写文件
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());



                //写结尾
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功
                 * 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                Log.e(TAG, "response code:"+res);
                if(res==200)
                {
                    InputStream inputStream = null;
                    if (null != conn.getHeaderField("Content-Encoding")) {
                        inputStream = new GZIPInputStream(conn.getInputStream());
                    } else {
                        inputStream = conn.getInputStream();
                    }

                    bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[10240];
                    int len1 = -1;
                    while ((len1 = inputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, len1);
                    }
                    bos.flush();
                    byte[] resultByte = bos.toByteArray();
                    String resultString = new String(resultByte);
                    resultString =resultString.substring(1,resultString.length()-1);
                    Log.i("resultString","233"+ resultString);

                    Message msg = new Message();
                    msg.obj = resultString;
                    handler.sendMessage(msg);
                }
            }
        } catch (MalformedURLException e)
        { e.printStackTrace(); }
        catch (IOException e)
        { e.printStackTrace(); }
        return FAILURE;
    }

    public static Bitmap getBitmap(String imageUri,Handler handler) {
        // 显示网络上的图片

        try {
            //BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            //bitmapOptions.inSampleSize = 4;

            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap =  BitmapFactory.decodeStream(is);
            is.close();
            conn.disconnect();

            Log.i(TAG, "image download finished." + imageUri);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Message msg = new Message();
        msg.what = 0x233;
        msg.obj = "true";
        Log.i(TAG,"2333");
        handler.sendMessage(msg);
        return bitmap;
    }
}