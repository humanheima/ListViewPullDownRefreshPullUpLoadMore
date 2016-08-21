package com.example.listviewpulldownrefreshpulluploadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainActivity extends AppCompatActivity {
    private List<String> list;
    MyListView listView;
    PtrClassicFrameLayout frameLayout;
    ListAdapter adapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
        listView = (MyListView) findViewById(R.id.list_view);
        list = new ArrayList<>();
        adapter = new ListAdapter(this, list);
        listView.setAdapter(adapter);

        frameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;//每次下拉刷新要把page重置为1
                list.clear();
                listView.setNoMoreItem(false);//下拉刷新的时候认为，有更多的数据加载
                for (int i = 0; i < 23; i++) {
                    list.add("string");
                }
                adapter.notifyDataSetChanged();
                listView.removeFootView();//加载完成后要移除掉footView
                frameLayout.refreshComplete();//结束下拉刷新操作
            }
        });

        //ListView 添加加载更多的回调监听
        listView.setLoadMoreListener(new MyListView.LoadMoreListener() {
            @Override
            public void loadMore() {
                listView.setPullUpLoading(true);//正在加载更多
                if (page < 3) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 30; i++) {
                                list.add("string" + i);
                            }
                            adapter.notifyDataSetChanged();
                            Log.e("tag", "listview size=" + list.size());
                            page++;
                            listView.removeFootView();
                        }
                    }, 2000);

                } else {
                    //表示没有更多的数据可以加载了
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.haveLoadAll();
                        }
                    }, 2000);

                }

            }
        });

        frameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                frameLayout.autoRefresh();
            }
        }, 500);
    }

}
