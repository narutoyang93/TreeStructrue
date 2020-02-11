package com.naruto.treestructrue;

import android.os.Bundle;
import android.os.Handler;

import com.naruto.treestructrue.base.BaseTreeAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private TreeAdapter adapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        //构造数据源
        String[] array = {"A", "B", "C", "D"};
        List<TestBean> dataList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            dataList.add(new TestBean("", i, 0, true, array[i]));
        }
        adapter = new TreeAdapter(dataList, new BaseTreeAdapter.IGetChildData<TestBean>() {
            @Override
            public void getData(final TestBean testBean) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int count = 1 + (int) (Math.random() * 4);
                        final List<TestBean> list = new ArrayList<>();
                        final String parentId = testBean.makeChildParentId();
                        int level = testBean.level + 1;
                        for (int i = 0; i < count; i++) {
                            list.add(new TestBean(parentId, i, level, (int) (Math.random() * 2) > 0, testBean.name + "-" + i));
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.showChildData(parentId, list);
                            }
                        }, 500);
                    }
                }).start();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
    }
}
