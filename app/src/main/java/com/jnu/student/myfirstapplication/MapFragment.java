package com.jnu.student.myfirstapplication;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.jnu.student.myfirstapplication.data.ShopLoader;
import com.jnu.student.myfirstapplication.data.model.Shop;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    private MapView mMapView = null;

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //获取地图控件引用
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        BaiduMap mBaiduMap = mMapView.getMap();

        //设定中心点坐标
        LatLng cenpt =  new LatLng(22.2559, 113.541112);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(cenpt)
                .zoom(17)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        downloadAndDrawShops(mBaiduMap);

        //对 marker 添加点击相应事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                Toast.makeText(getActivity().getApplicationContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return view;
    }

    public void downloadAndDrawShops (final BaiduMap mBaiduMap) {
        // 下载并保存数据
        final ShopLoader shopLoader = new ShopLoader();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                for(int i=0;i<shopLoader.getShops().size();i++) {
                    //构建Marker图标
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.marker);
                    LatLng pos =  new LatLng(shopLoader.getShops().get(i).getLatitude(),shopLoader.getShops().get(i).getLongitude());
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions()
                            .position(pos)
                            .icon(bitmap);
                    // 显示图标标记
                    mBaiduMap.addOverlay(option);
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFFFF).fontSize(50)
                            .fontColor(0xFFFFFFFF).text(shopLoader.getShops().get(i).getName()).rotate(0).position(pos);
                    // 显示文本标记
                    mBaiduMap.addOverlay(textOption);
                }
            }
        };
        shopLoader.load(handler ,"http://file.nidama.net/class/mobile_develop/data/bookstore.json");
    }
}
