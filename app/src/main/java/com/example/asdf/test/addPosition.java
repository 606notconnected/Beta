package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

public class addPosition extends Activity implements AMap.OnMapClickListener,LocationSource, AMapLocationListener {

    public static double longitude;
    public static double latitude;
    public static int num;
    private MapView mapView;
    private AMap aMap;
    private LocationManagerProxy mLocationManagerProxy;
    private OnLocationChangedListener mListener;
    double geoLat;
    double geoLng;
    private ImageView leftDrawer;//点击后出现左滑菜单
    private TextView Map;//地图
    private TextView picture;//查看照片
    private static final String TAG = "LocationActivity";
    private Button sure;//确认定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        num=0;
        setContentView(R.layout.addposition);
        mapView = (MapView) findViewById(R.id.map2);
        mapView.onCreate(savedInstanceState);
        init();
        sure= (Button) findViewById(R.id.sure);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        Map= (TextView) findViewById(R.id.Map);
        picture= (TextView) findViewById(R.id.picture);
        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addPosition.this,mainView.class));
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=1;
               upload.addposition.setText("地理位置已添加");
//                    addposition.setClickable(false);
                upload.pictureIma.setClickable(false);
               addPosition.this.finish();
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addPosition.this, picture.class));
            }
        });
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPosition.this.finish();
            }
        });
    }

    /**
     * 初始化AMap对象
     */
    private void init(){
        if(aMap == null ){
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
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
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
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
            longitude=geoLng;
            latitude=geoLat;
            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//            MarkerOptions otMarkerOptions = new MarkerOptions();
//            LatLng latLng = new LatLng(geoLat,geoLng);
//            otMarkerOptions.position(latLng);
//            otMarkerOptions.draggable(true);
//            otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.set));
//            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//            aMap.addMarker(otMarkerOptions);

        }
    }

    /**
     　　* 对单击地图事件回调
     　　*/
    @Override
    public void onMapClick(LatLng latLng) {
        aMap.clear();
        geoLat = latLng.latitude;
        geoLng = latLng.longitude;
        longitude=geoLng;
        latitude=geoLat;
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.local));
        otMarkerOptions.position(latLng);
        aMap.addMarker(otMarkerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
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

}