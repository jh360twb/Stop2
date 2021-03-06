package com.example.tuao.stop.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.example.tuao.stop.PropertyAnimation;
import com.example.tuao.stop.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private MapView mapView;
    private LocationClient mLocationClient;
    private BaiduMap baiduMap;
    private boolean isFirstLocate=true;
    private TextView load;//屏幕下方点击加载
    private RelativeLayout mHiddenLayout;
    private PropertyAnimation propertyAnimation;
    private ImageView location;//点击定位按钮
    private ImageView service;
    private ImageView share;
    private ImageView setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
       // 在setcontentView之前用
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //自己定义的属性动画类
        propertyAnimation=new PropertyAnimation(this);
        load=(TextView)findViewById(R.id.load);
        load.setOnClickListener(this);
        mHiddenLayout=(RelativeLayout)this.findViewById(R.id.showhideView);
        location=(ImageView)findViewById(R.id.location);
        location.setOnClickListener(this);
        mapView=(MapView)findViewById(R.id.bmapView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //获取BaiduMap实例
        baiduMap=mapView.getMap();

//悬浮按钮点击事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//访问权限
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else{
            requestLocation();
        }

        service = (ImageView)findViewById(R.id.service);
        service.setOnClickListener(this);
        share = (ImageView)findViewById(R.id.share);
        share.setOnClickListener(this);
        setting = (ImageView)findViewById(R.id.setting);
        setting.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
/* 点击左侧menu 的点击事件*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.menu的点击事件
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i_1=new Intent(MainActivity.this,CarManager.class);
            startActivity(i_1);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i_2=new Intent(MainActivity.this,ParkRecord.class);
            startActivity(i_2);

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,"懒得写",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this,"暂未开放",Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void requestLocation(){
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:if(grantResults.length>0){
                for(int result:grantResults){
                    if(result!=PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"必须同意所有权限才能运行此程序",Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                requestLocation();
            }else{
                Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    private void navigateTo(BDLocation location){
        if(isFirstLocate){
            LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update=MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            isFirstLocate=false;
        }
    }
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation||bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                navigateTo(bdLocation);
            }
        }
    }
//屏幕上的一些点击事件
    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.load:
        if (mHiddenLayout.getVisibility() == View.GONE) {
            propertyAnimation.animateOpen(mHiddenLayout);
            propertyAnimation.animationIvOpen(load);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    load.setText("关闭");
                }
            });
        } else {
            propertyAnimation.animateClose(mHiddenLayout);
            propertyAnimation.animationIvClose(load);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    load.setText("点击展开更多");
                }
            });
        }
        break;
    case R.id.location: //requestLocation();//点击定位待写
        break;
    case R.id.service:
        Intent intent = new Intent(MainActivity.this,Call_Phone.class);
        startActivity(intent);
        break;
    case R.id.share:
        Intent intent1 = new Intent(MainActivity.this,Share.class);
        startActivity(intent1);
        break;
    case R.id.setting:
        Intent intent2 = new Intent(MainActivity.this,Setting.class);
        startActivity(intent2);
        break;
        default:
    }
   }
}
