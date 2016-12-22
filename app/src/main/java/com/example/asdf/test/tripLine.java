package com.example.asdf.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.example.asdf.httpClient.httpClient;
import com.example.asdf.httpClient.httpImage;
import com.example.asdf.test.adapter.tmpAdapter;
import com.example.asdf.test.attached.commen;
import com.example.asdf.test.attached.tmpBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class tripLine extends Activity implements LocationSource, AMapLocationListener,View.OnTouchListener {
    private MapView mapView;
    private AMap aMap;
    private ImageView leftDrawer;
    private Button openButton;
    private Handler handler1;
    private Handler handler2;
    private Handler handler3;
    private ListView rightList;
    private List<tmpBean> listDatas;
    private tmpAdapter lAdapter;
    private TextView picture;//查看照片
    httpClient tmp1 = new httpClient();
    httpImage tmp2 = new httpImage();
    httpClient tmp4 = new httpClient();
    private Handler handler;
    public static String intro;
    public static String na;
    public static Bitmap tmpBitmap;
    public static  List<commen> comlist;
    private LocationManagerProxy mLocationManagerProxy;
    private OnLocationChangedListener mListener;
    private static final String TAG = "LocationActivity";
    /**
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 80;
    /**
     * 屏幕宽度值。
     */
    private int screenWidth;
    /**
     * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
     */
    private int leftEdge;
    /**
     * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
     */
    private int rightEdge = 0;
    /**
     * menu完全显示时，留给content的宽度值。
     */
    private int menuPadding = 210;
    /**
     * 主内容的布局。
     */
    private View layout_left;
    /**
     * menu的布局。
     */
    private View layout_right;
    /**
     * menu布局的参数，通过此参数来更改leftMargin的值。
     */
    private LinearLayout.LayoutParams menuParams;
    /**
     * 记录手指按下时的横坐标。
     */
    private float xDown;
    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;
    /**
     * 记录手机抬起时的横坐标。
     */
    private float xUp;
    /**
     * menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
     */
    private boolean isMenuVisible;
    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripline);
        mapView = (MapView) findViewById(R.id.map3);
        openButton= (Button) findViewById(R.id.open);
        mapView.onCreate(savedInstanceState);
        rightList= (ListView) findViewById(R.id.rightList);
        init();
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        layout_left=  findViewById(R.id.layout_left);
        layout_right= findViewById(R.id.layout_right);
        picture= (TextView) findViewById(R.id.picture);
        listDatas = new ArrayList<>();
        initData();
        lAdapter = new tmpAdapter(listDatas, this);
        rightList.setAdapter(lAdapter);
        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = window.getDefaultDisplay().getWidth();
        menuParams = (LinearLayout.LayoutParams) layout_right.getLayoutParams();
        // 将menu的宽度设置为屏幕宽度减去menuPadding
        menuParams.width = screenWidth - menuPadding;
        // 左边缘的值赋值为menu宽度的负数
        leftEdge = -menuParams.width;
        // menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
        menuParams.leftMargin = leftEdge;
        // 将content的宽度设置为屏幕宽度
        layout_left.getLayoutParams().width = screenWidth;
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToMenu();
            }
        });
        handler3= new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                System.out.println(tmp+"获取评论");
                Log.d("json", tmp);
                Gson gson = new Gson();
                comment comli = gson.fromJson(tmp, comment.class);
                comlist=comli.getcoList();
                String a=comli.getresult();
                if(a.equals("true"))
                {
                    System.out.println("获取评论成功");
                    startActivity(new Intent(tripLine.this, mapLook.class));
//                    Toast.makeText(picture.this,"获取照片详情成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    System.out.println("获取评论失败");
                    Toast.makeText(tripLine.this,"获取评论失败",Toast.LENGTH_SHORT).show();
                }
            }

        };
        handler2 = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                System.out.println(tmp+"single");
                if(tmp.equals("true")) {
                    new Thread() {
                        @Override
                        public void run() {
                            System.out.println(na+"照片名");
                            tmp4.getParamTest("http://120.27.7.115:1010/api/Comment?imageName=" + na, handler3);
                        }
                    }.start();
//                    startActivity(new Intent(picture.this, singleDetail.class));
                }
                else{
                    System.out.println("获取照片详情失败");
                }
            }

        };
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                tmp = "{" + tmp + "}";
                Log.d("json", tmp);
                Gson gson = new Gson();
                pictureinfor peopl = gson.fromJson(tmp, pictureinfor.class);
                na = peopl.getImageName();
                intro = peopl.getIntroduction();
                if(intro!=null)
                {
                    System.out.println(intro + "  0 " + na);
                    new Thread()
                    {
                        public void run()
                        {
                            if(na!=null)
                                tmpBitmap =tmp2.getBitmap("http://120.27.7.115:1010/api/image?name="+na,handler2);
                        }
                    }.start();
//                    Toast.makeText(picture.this,"获取照片详情成功",Toast.LENGTH_SHORT).show();
                }
            }

        };
        handler1 = new android.os.Handler() {
            public void handleMessage(Message msg) {
                String tmp = msg.obj.toString();
                if(tmp.equals("true"))
                {
//                    Toast.makeText(tripLine.this, "成功", Toast.LENGTH_SHORT).show();
                  System.out.println("tripline获取成功");
                }
                else
                {
                    Toast.makeText(tripLine.this,"失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                    httpImage tmpHttpImage = new httpImage();
                    for (int i = 0; i < lookTrip.tripictureNames.size(); i++) {
                        System.out.println(lookTrip.tripictureNames.get(i)+"图片名");
                        Bitmap tmpBitmap = null;
                        tmpBitmap = tmpHttpImage.getBitmap("http://120.27.7.115:1010/api/image?name=" + lookTrip.tripictureNames.get(i), handler1);
                        LatLng latLng1 = new LatLng(Double.valueOf(lookTrip.lattt.get(i)), Double.valueOf(lookTrip.longgg.get(i)));
                        //获取这个图片的宽和高
                        int width = tmpBitmap.getWidth();
                        int height = tmpBitmap.getHeight();
                        int newWidth = screenWidth/6;
                        int newHeight = screenWidth/6;
                        //int newWidth=200;
                        //   int newHeight=120;
                        //计算缩放率，新尺寸除原始尺寸
                        float scaleWidth = ((float) newWidth) / width;
                        float scaleHeight = ((float) newHeight) / height;
                        // 创建操作图片用的matrix对象
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleHeight);
                        Bitmap resizedBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, width,
                                height, matrix, true);
                       resizedBitmap=Bitmap.createBitmap(resizedBitmap, 0, 10, 80, 80, null, false);
                        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)//设置锚点
                                .position(latLng1).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));
                        // 自定义的聚合类MyMarkerCluster
                    }
                LatLng latLng1 = new LatLng(Double.valueOf(lookTrip.lattt.get(0)), Double.valueOf(lookTrip.longgg.get(0)));
                aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)//设置锚点
                        .position(latLng1));
                }

        }.start();
        rightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                new Thread() {
                    @Override
                    public void run() {
                        tmp1.getParamTest("http://120.27.7.115:1010/api/imagemessage?imagename=" + lookTrip.tripictureNames.get(arg2), handler);
                    }
                }.start();
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tripLine.this, picture.class));
            }
        });
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isMenuVisible==false)
                tripLine.this.finish();
                else
                    scrollToContent();
            }
        });
        aMap.clear();
        draw();
    }

    /**
     * 初始化AMap对象
     */
    private void init(){
        if(aMap == null ){
            aMap = mapView.getMap();
        }
        //initLocation();
        setUpMap();
    }


    /**
     * 初始化定位
     */
    private void initLocation(){

        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 15, this);

        mLocationManagerProxy.setGpsEnable(false);
    }

    private void setUpMap(){
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
        if(aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0){
            //获取位置信息
            Double geoLat = aMapLocation.getLatitude();
            Double geoLng = aMapLocation.getLongitude();
            Log.d(TAG, "Latitude = " + geoLat.doubleValue() + ", Longitude = " + geoLng.doubleValue());

            // 通过 AMapLocation.getExtras() 方法获取位置的描述信息，包括省、市、区以及街道信息，并以空格分隔。
            String desc = "";
            Bundle locBundle = aMapLocation.getExtras();
            if (locBundle != null) {
                desc = locBundle.getString("desc");
                Log.d(TAG, "desc = " + desc);
            }
            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
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
    public void draw() {
        aMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        //设置线的宽度
        polylineOptions.width(3);
        //设置线的颜色
        polylineOptions.color(0xFFA020F0);
        //设置线是否可见
        polylineOptions.visible(true);
        List<LatLng> points = new ArrayList<LatLng>();
        for(int i=0;i<lookTrip.lattt.size();i++){
            System.out.println(lookTrip.lattt.get(i)+" "+i+"照片地址");
            LatLng latLng = new LatLng(Double.valueOf(lookTrip.lattt.get(i)),Double.valueOf( lookTrip.longgg.get(i)));
            points.add(latLng);
        }
        for(int  i = 0;i<points.size();i++){
            polylineOptions.add(points.get(i));
        }
        mapView.getMap().addPolyline(polylineOptions);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，记录按下时的横坐标
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown);
                if (isMenuVisible) {
                    menuParams.leftMargin = distanceX;
                } else {
                    menuParams.leftMargin = leftEdge + distanceX;
                }
                if (menuParams.leftMargin < leftEdge) {
                    menuParams.leftMargin = leftEdge;
                } else if (menuParams.leftMargin > rightEdge) {
                    menuParams.leftMargin = rightEdge;
                }
                layout_right.setLayoutParams(menuParams);
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
                xUp = event.getRawX();
                if (wantToShowMenu()) {
                    if (shouldScrollToMenu()) {
                        scrollToMenu();
                    } else {
                        scrollToContent();
                    }
                } else if (wantToShowContent()) {
                    if (shouldScrollToContent()) {
                        scrollToContent();
                    } else {
                        scrollToMenu();
                    }
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
     *
     * @return 当前手势想显示content返回true，否则返回false。
     */
    private boolean wantToShowContent() {
        return xUp - xDown < 0 && isMenuVisible;
    }

    /**
     * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。
     *
     * @return 当前手势想显示menu返回true，否则返回false。
     */
    private boolean wantToShowMenu() {
        return xUp - xDown > 0 && !isMenuVisible;
    }

    /**
     * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
     * 就认为应该滚动将menu展示出来。
     *
     * @return 如果应该滚动将menu展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToMenu() {
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
     * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。
     *
     * @return 如果应该滚动将content展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToContent() {
        return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 将屏幕滚动到menu界面，滚动速度设定为30.
     */
    private void scrollToMenu() {
        new ScrollTask().execute(30);
    }

    /**
     * 将屏幕滚动到content界面，滚动速度设定为-30.
     */
    private void scrollToContent() {
        new ScrollTask().execute(-30);
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *            content界面的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int leftMargin = menuParams.leftMargin;
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            while (true) {
                leftMargin = leftMargin + speed[0];
                if (leftMargin > rightEdge) {
                    leftMargin = rightEdge;
                    break;
                }
                if (leftMargin < leftEdge) {
                    leftMargin = leftEdge;
                    break;
                }
                publishProgress(leftMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
                sleep(20);
            }
            if (speed[0] > 0) {
                isMenuVisible = true;
            } else {
                isMenuVisible = false;
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... leftMargin) {
            menuParams.leftMargin = leftMargin[0];
            layout_right.setLayoutParams(menuParams);
        }

        @Override
        protected void onPostExecute(Integer leftMargin) {
            menuParams.leftMargin = leftMargin;
            layout_right.setLayoutParams(menuParams);
        }
    }

    /**
     * 使当前线程睡眠指定的毫秒数。
     *
     * @param millis
     *            指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void initData() {
        List<tmpBean> sun1 = new ArrayList<>() ;
        for(int i=0;i<lookTrip.tripictureNames.size();i++) {
            sun1.add(new tmpBean());
            sun1.get(i).setPicUrl("http://120.27.7.115:1010/api/image" + "?name=" + lookTrip.tripictureNames.get(i));
            listDatas.add(sun1.get(i));
        }
    }
    class pictureinfor {
        private String imageName;
        private String dateTime;  //属性都定义成String类型，并且属性名要和Json数据中的键值对的键名完全一样
        private String longitude;
        private String latitude;
        private String introduction;
        public String getImageName() {
            return imageName;
        }
        public String getDateTime() {
            return dateTime;
        }
        public String getLongitude() {
            return longitude;
        }
        public String getLatitude() {
            return latitude;
        }
        public String getIntroduction() {
            return introduction;
        }
    }
    class comment
    {
        private String result;
        private List<commen>commentBackList;
        public String getresult(){
            return result;
        }
        public List<commen> getcoList()
        {
            return commentBackList;
        }
    }
}