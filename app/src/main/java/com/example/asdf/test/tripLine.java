package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.example.asdf.httpClient.httpImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class tripLine extends Activity implements LocationSource, AMapLocationListener {
    private MapView mapView;
    private AMap aMap;
    private ImageView leftDrawer;
    private Handler handler1;
//    private TextView Map;//地图
    private TextView picture;//查看照片
    private LocationManagerProxy mLocationManagerProxy;
    private OnLocationChangedListener mListener;
    private static final String TAG = "LocationActivity";
    private ImageView friendHead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripline);
        mapView = (MapView) findViewById(R.id.map3);
        mapView.onCreate(savedInstanceState);
        init();
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
//        Map= (TextView) findViewById(R.id.Map);
        friendHead= (ImageView) findViewById(R.id.friendHead);
        picture= (TextView) findViewById(R.id.picture);
//        Map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(tripLine.this,mainView.class));
//            }
//        });
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
                        Bitmap tmpBitmap = null;
                        tmpBitmap = tmpHttpImage.getBitmap("http://120.27.7.115:1010/api/image?name=" + lookTrip.tripictureNames.get(i), handler1);
                        LatLng latLng1 = new LatLng(Double.valueOf(lookTrip.lattt.get(i)), Double.valueOf(lookTrip.longgg.get(i)));
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
        friendHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        httpImage tmpHttpImage = new httpImage();

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
                tripLine.this.finish();
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
        PolylineOptions polylineOptions = new PolylineOptions();
        //设置线的宽度
        polylineOptions.width(3);
        //设置线的颜色
        polylineOptions.color(0xFFA020F0);
        //设置线是否可见
        polylineOptions.visible(true);
        List<LatLng> points = new ArrayList<LatLng>();
        for(int i=0;i<lookTrip.lattt.size();i++){
            LatLng latLng = new LatLng(Double.valueOf(lookTrip.lattt.get(i)),Double.valueOf( lookTrip.longgg.get(i)));
            points.add(latLng);
        }
        for(int  i = 0;i<points.size();i++){
            polylineOptions.add(points.get(i));
        }
        mapView.getMap().addPolyline(polylineOptions);
    }
}