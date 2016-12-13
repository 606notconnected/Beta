package com.example.asdf.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class mainView extends baseActivity implements LocationSource, AMapLocationListener {
    public static double[] locala;
    private MapView mapView;
    private AMap aMap;
    private LocationManagerProxy mLocationManagerProxy;
    private static final String TAG = "LocationActivity";
    private OnLocationChangedListener mListener;
    private double geoLat;
    private double geoLng;
    private ImageView leftDrawer;//点击后出现左滑菜单
    private TextView Map;//地图
    private TextView username;
    private TextView picture;//查看照片
    private ImageView myhead;//头像
    private Button photo;//点击后调用系统相机
    private LinearLayout upload;//上传照片
    private LinearLayout care;//关注
    private LinearLayout looktrip;//查看行程
    private LinearLayout delete;//回收站
    private LinearLayout set;//设置
    private LinearLayout lookpicture;//查看照片
    private Button start;//开始行程
    private int num = 0;
    private Bitmap head;// 头像Bitmap
    private Handler handler;
    private Handler handler1;
    private android.os.Handler handler2;
    private Handler handler3;
    private DrawerLayout drawerLayout;
    private PopupWindow mPopWindow;
    private TextView introduce;
    httpImage tmp = new httpImage();
    public static String tripName;
    public static List<String> tripNa=null;
    public static String tripId=null;
    int i=0;
    private Button btn_picture, btn_photo, btn_cancle;
    private static String path = "/sdcard/myHeadOfData/";// sd路径
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    httpClient tmp2=new httpClient();
    httpClient tmp1 = new httpClient();
    int j=0;
    JSONObject object = new JSONObject();
    public  static  List<trip.ttrip> uuu=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (TextView) findViewById(R.id.username);
        photo = (Button) findViewById(R.id.photo);
        start = (Button) findViewById(R.id.start);
        leftDrawer = (ImageView) findViewById(R.id.leftdrawer);
        Map = (TextView) findViewById(R.id.Map);
        username= (TextView) findViewById(R.id.username1);
        introduce= (TextView) findViewById(R.id.introduce1);
        drawerLayout = (DrawerLayout) findViewById(R.id.layout);
        picture = (TextView) findViewById(R.id.picture);
        upload = (LinearLayout) findViewById(R.id.upload);
        looktrip = (LinearLayout) findViewById(R.id.looktrip);
        lookpicture = (LinearLayout) findViewById(R.id.lookpicture);
        set = (LinearLayout) findViewById(R.id.set);
        delete = (LinearLayout) findViewById(R.id.delete);
        care = (LinearLayout) findViewById(R.id.care);
        myhead = (ImageView) findViewById(R.id.head);
        Map= (TextView) findViewById(R.id.Map);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });

        handler1 = new android.os.Handler() {
            public void handleMessage(Message msg) {
              Log.i("23333",msg.obj.toString());
            }
        };
        handler2 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
//                Toast.makeText(mainView.this, tmp, Toast.LENGTH_LONG).show();
                if (tmp!="error") {
                    tripId=tmp;
                    Toast.makeText(mainView.this, "可以上传图片啦", Toast.LENGTH_SHORT).show();
                } else if (tmp.equals("error")) {
                    Toast.makeText(mainView.this, "服务器出错", Toast.LENGTH_LONG).show();
                }
            }

        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0) {
                    start.setBackgroundResource(R.drawable.end);
                    final EditText et = new EditText(mainView.this);
                    new AlertDialog.Builder(mainView.this).setTitle("请输入行程名")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String input = et.getText().toString();
                                    if (input.equals("")) {
                                        Toast.makeText(getApplicationContext(), "行程名不能为空！" + input, Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        tripName=input;
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                object.put("roadName", tripName);
                                                object.put("account", login.account);
                                                object.put("introduction", "000000000");
                                                tmp2.postParamsJson("http://120.27.7.115:1010/api/road", object, handler2);
                                            }
                                            }.start();
//                                        Intent intent = new Intent();
//                                        intent.putExtra("content", input);
//                                        intent.setClass(mainView.this, SearchActivity.class);
//                                        startActivity(intent);
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    num = 1;
                } else if (num == 1) {
                    tripName=null;
                    tripId=null;
                    start.setBackgroundResource(R.drawable.start);
                    num = 0;
                }
            }
        });
        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread()
                {
                    public void run()
                    {
                        httpImage tmpHttpImage = new httpImage();
                        for(int j=0;j<login.latt.size();j++) {
                            LatLng latLng1 = new LatLng(Double.valueOf(login.latt.get(j)), Double.valueOf(login.longg.get(j)));
                            Bitmap tmpBitmap = null;
                            tmpBitmap = tmpHttpImage.getBitmap("http://120.27.7.115:1010/api/image?name=" + login.picName.get(j), handler1);
//                               MarkerOptions otMarkerOptions = new MarkerOptions();
//                               otMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(tmpBitmap));
//                               otMarkerOptions.position(latLng1);
//                               aMap.addMarker(otMarkerOptions);
//                               aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng1));

                            //获取这个图片的宽和高
                            int width = tmpBitmap.getWidth();
                            int height = tmpBitmap.getHeight();
                            int newWidth = 80;
                            int newHeight = 120;
                            //int newWidth=200;
                            //   int newHeight=120;
                            //计算缩放率，新尺寸除原始尺寸
                            float scaleWidth = ((float) newWidth) / width;
                            float scaleHeight = ((float) newHeight) / height;
                            // 创建操作图片用的matrix对象
                            Matrix matrix = new Matrix();
                            matrix.postScale(scaleWidth, scaleHeight);
                            // 创建新的图片
                            Bitmap resizedBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, width,
                                    height, matrix, true);
                            resizedBitmap=Bitmap.createBitmap(resizedBitmap, 0, 10, 80, 80, null, false);
                            aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)//设置锚点
                                    .position(latLng1).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));
                        }
                    }
                }.start();

            }
        });
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
//                    startActivity(new Intent(upload.this, mainView.class));
                    tmp = "{" + tmp + "}";
                    Gson gson = new Gson();
                    trip tri = gson.fromJson(tmp, trip.class);
                    if (tri!=null) {
                    uuu = tri.gettripList();
                    lookTrip.tName.clear();
                    lookTrip.tId.clear();
                    for(i = 0;i<uuu.size();i++)
                    {
                        lookTrip.tId.add(uuu.get(i).getIId());
                        lookTrip.tName.add(uuu.get(i).getName());
                        System.out.println(uuu.get(i).getIId()+"  99   "+uuu.get(i).getName());
                    }
//                        Toast.makeText(mainView.this, "获取行程列表成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mainView.this, "获取行程列表失败", Toast.LENGTH_LONG).show();
                    }
            }

        };
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainView.this,information.class));
            }
        });
        leftDrawer.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              username.setText(login.uuername);
                                              introduce.setText(login.introduce);
                                              drawerLayout.openDrawer(GravityCompat.START);
                                              new Thread(){
                                                  @Override
                                                  public void run() {
                                                      tmp1.getParamTest("http://120.27.7.115:1010/api/road?account=" + login.account, handler);
                                                  }
                                              }.start();
                                      }
            });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainView.this, upload.class));
            }
        });
        care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainView.this, watchFriend.class));
            }
        });//点击进入关注好友界面
        looktrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainView.this, lookTrip.class));
//
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainView.this, delete.class));
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainView.this, set.class));
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainView.this, picture.class));
            }
        });
        lookpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainView.this, picture.class));
            }
        });

        myhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
//                ;
            }
        });
        Bitmap bt = BitmapFactory.decodeFile(path + "myhead.jpg");// 从Sd中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(toRoundBitmap(bt));// 转换成drawable
            myhead.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void showDialog() {
        View contentView = LayoutInflater.from(mainView.this).inflate(R.layout.popup, null);
        mPopWindow = new PopupWindow(contentView,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        btn_picture = (Button) contentView.findViewById(R.id.btn_picture);
        btn_photo = (Button) contentView.findViewById(R.id.btn_photo);
        btn_cancle = (Button) contentView.findViewById(R.id.btn_cancle);

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                mPopWindow.dismiss();
            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "myhead.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                mPopWindow.dismiss();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        View rootview = LayoutInflater.from(mainView.this).inflate(R.layout.left, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/myhead.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        //发送
                        setPicToView(head);// 保存在SD卡中
                        myhead.setImageBitmap(toRoundBitmap(head));// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "myhead.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 把bitmap转成圆形
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        // 取最短边做边长
        if (width < height) {
            r = width;
        } else {
            r = height;
        }
        // 构建一个bitmap
        Bitmap backgroundBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBm);
        Paint p = new Paint();
        // 设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        // 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        // 且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, p);
        // 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        //initLocation();
        aMap.getUiSettings().setScaleControlsEnabled(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        aMap.setMapType(1);

//     2D   myLocationStyle.strokeColor(Color.argb(0, 255, 255, 255));// 设置圆形的边框颜色
//      2D  myLocationStyle.radiusFillColor(Color.argb(0, 255, 255, 255));// 设置圆形的填充颜色
        setUpMap();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 15, this);
        mLocationManagerProxy.setGpsEnable(true);
    }

    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色,并没有什么用
        myLocationStyle.radiusFillColor(Color.argb(0, 255, 255, 250));// 设置圆形的填充颜色，并没有什么用
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        // TODO Auto-generated method stub
        mListener = onLocationChangedListener;
        if (mLocationManagerProxy == null) {
            mLocationManagerProxy = LocationManagerProxy.getInstance(this);
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法
            //其中如果间隔时间为-1，则定位只定一次
            mLocationManagerProxy.requestLocationData(
                    LocationProviderProxy.AMapNetwork, -1, 10, this);
        }
    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        // TODO Auto-generated method stub
        mListener = null;
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destroy();
        }
        mLocationManagerProxy = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // TODO Auto-generated method stub
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            //获取位置信息
            geoLat = aMapLocation.getLatitude();
            geoLng = aMapLocation.getLongitude();
            Log.d(TAG, "Latitude = " + geoLat + ", Longitude = " + geoLng);
            // 通过 AMapLocation.getExtras() 方法获取位置的描述信息，包括省、市、区以及街道信息，并以空格分隔。
            String desc = "";
            Bundle locBundle = aMapLocation.getExtras();
            if (locBundle != null) {
                desc = locBundle.getString("desc");
                Log.d(TAG, "desc = " + desc);
            }
            //mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            LatLng latLng = new LatLng(geoLat, geoLng);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(new LatLng(geoLat,geoLng));
//            markerOptions.title("当前位置");
//            markerOptions.visible(true);
//            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wud));
//            markerOptions.icon(bitmapDescriptor);
//            aMap.addMarker(markerOptions);


        }
    }

    /**
     * 此方法需存在
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 此方法需存在
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 此方法需存在
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "mainView Page", // TODO: Define a title for the content shown.
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
                "mainView Page", // TODO: Define a title for the content shown.
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
    class trip
    {
        private List<ttrip>returnMessage;
        public class ttrip{
            private String RoadID;
            private String RoadName;
            private String Introduction;
            public String getIntroduction(){
                return Introduction;
            }
            public String getIId(){
                return RoadID;
            }
            public String getName(){
                return RoadName;
            }
        }
        public List<ttrip> gettripList()
        {
            return returnMessage;
        }
    }
}