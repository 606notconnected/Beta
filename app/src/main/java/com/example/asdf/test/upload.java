package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Useradmin on 2016/10/23.
 */
public class upload extends Activity {
    httpImage httpImageTmp = new httpImage();
    private ImageView leftDrawer;
    public static ImageView pictureIma;
    public static TextView addposition;
    private double wd;
    private double jd;
    private Bitmap tmpBitmap;
    private EditText text;
    private Button cancel;
    private Button upload;
    private Handler handler1;
    private Handler handler;
    JSONObject object = new JSONObject();
    String lon;
    String lat;
    String date = null;
    httpClient tmp = new httpClient();
    String path;
    String name="";
    private String introduction;
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    /**SRC_20161110_082311.png
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        addposition = (TextView) findViewById(R.id.addposition);
        upload = (Button) findViewById(R.id.upload);
        pictureIma = (ImageView) findViewById(R.id.pictureIma);
        cancel= (Button) findViewById(R.id.cancel);
        text = (EditText) findViewById(R.id.text);
        addposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addPosition.num == 1) {
                    addposition.setText("地理位置已添加");
//                    addposition.setClickable(false);
                    pictureIma.setClickable(false);
                    jd = addPosition.longitude;
                    wd = addPosition.latitude;
                    lon= String.valueOf(jd);
                    lat=String.valueOf(wd);
                    addPosition.num = 0;
                }
                startActivity(new Intent(upload.this, addPosition.class));
            }
        });
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
                    refresh();
                    Toast.makeText(upload.this, "上传成功", Toast.LENGTH_LONG).show();
                }
            }

        };



        handler1 = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp2 = msg.obj.toString();
                Log.i("tmp2",tmp2);
                if(tmp2!=null&&mainView.tripId!=null)
                {
                    object.put("imageName", tmp2);
                    object.put("dateTime", date);
                    object.put("longitude", lon);
                    object.put("latitude", lat);
                    object.put("introduction", introduction);
                    object.put("roadID", mainView.tripId);
                    System.out.println(mainView.tripId+" Id "+tmp2+"  "+date + "  time " + lon + "  纬度 " + lat + "  经度  "+introduction);
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            tmp.postParamsJson("http://120.27.7.115:1010/api/imagemessage",object,handler);
                        }
                    }.start();

                    }

                }
//                if (tmp.equals("true")) {
////                    startActivity(new Intent(upload.this, mainView.class));
//                    Toast.makeText(upload.this, "上传成功", Toast.LENGTH_LONG).show();
//                } else if (tmp.equals("false")) {
//                    Toast.makeText(upload.this, "上传失败", Toast.LENGTH_LONG).show();
//                } else if (tmp.equals("error")) {
//                    Toast.makeText(upload.this, "服务器出错", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    Toast.makeText(upload.this, "一脸懵逼", Toast.LENGTH_LONG).show();
//                }
            //}
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.dialog);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introduction=text.getText().toString();
                if (addPosition.num == 1) {
                    jd = addPosition.longitude;
                    wd = addPosition.latitude;
                    lon= String.valueOf(jd);
                    lat=String.valueOf(wd);
                    addPosition.num = 0;
                }
                if(mainView.tripName==null) {
                    builder.setMessage("请先开始行程！");
                    // 创建对话框
                    AlertDialog ad = builder.create();
                    // 显示对话框
                    ad.show();
                }
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        File tmp1 = new File(path);
                        httpImageTmp.uploadFile(tmp1, "http://120.27.7.115:1010/api/image",handler1,login.account);

//                        System.out.println(path+"000000000000000000000000000000000000");
//                        File tmp1 = new File(path);
//                        tmp.uploadFile(tmp1, "http://120.27.7.115:1010/api/image",handler1,login.account);
//                        if(mainView.tripName!=null&&name!=null&&date!=null&&lon!=null&&lat!=null)
//                        {
//                          object.put("imageName", name);
//                          object.put("dateTime", date);
//                          object.put("longitude", lon);
//                          object.put("latitude", lat);
//                          object.put("introduction", introduction);
//                           tmp.postParamsJson("http://120.27.7.115:1010/api/image",object,handler1);}
//                             text.setText("  "+date + "  time " + wd + "  纬度 " + jd + "  经度  "+introduction);
                    }
                }.start();
//                text.setText(mainView.tripName+" "+name+"  "+date + "  time " + wd + "  纬度 " + jd + "  经度  "+introduction);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.this.finish();
            }
        });
        pictureIma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
        leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.this.finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    //加载图片
    private void showImage(String imaePath) {
        path=imaePath;
        String[] longName=path.split("/");
        int n=longName.length;
        name=longName[n-1];
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        pictureIma.setBackground(null);
        pictureIma.setImageBitmap(bm);
        try {
            ExifInterface exifInterface = new ExifInterface(
                    imaePath);
            String FDateTime = exifInterface
                    .getAttribute(ExifInterface.TAG_DATETIME);
            String latValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String lngValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String latRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String lngRef = exifInterface.getAttribute
                    (ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
                try {
                    wd = convertRationalLatLonToFloat(latValue, latRef);
                    jd = convertRationalLatLonToFloat(lngValue, lngRef);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
//            double jd=Change(lng);
//            double wd=Change(lat);
            if (FDateTime != null) {
                String[] Time = FDateTime.split(" ");
                String[] nyr = Time[0].split(":");
                date = nyr[0] + "-" + nyr[1] + "-" + nyr[2] + " " + Time[1];
            } else if (FDateTime == null) {
                Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
                t.setToNow(); // 取得系统时间。
                int year = t.year;
                int month = t.month;
                int date1 = t.monthDay;
                int hour = t.hour; // 0-23
                int minute = t.minute;
                int second = t.second;
                date = year + "-" + month + "-" + date1 + " " + hour + ":" + minute + ":" + second;
            }
            if (wd != 0.0) {
                addposition.setText("地理位置已添加");
//                pictureIma.setClickable(false);
//                addposition.setClickable(false);
                addPosition.num = 0;
            }
            lon= String.valueOf(jd);
            lat= String.valueOf(wd);
//            text.setText(date + "  time " + wd + "  纬度 " + jd + "  经度  ");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static float convertRationalLatLonToFloat(
            String rationalString, String ref) {

        String[] parts = rationalString.split(",");

        String[] pair;
        pair = parts[0].split("/");
        double degrees = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[1].split("/");
        double minutes = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[2].split("/");
        double seconds = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if ((ref.equals("S") || ref.equals("W"))) {
            return (float) -result;
        }
        return (float) result;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "upload Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.asdf.test/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "upload Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.asdf.test/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    private void refresh() {
                /*finish();
06.         Intent intent = new Intent(RefreshActivityTest.this, RefreshActivityTest.class);
07.         startActivity(intent);*/
        onCreate(null);
    }

}
