package com.naruto.treestructrue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naruto.treestructrue.base.BaseTreeAdapter;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2020/2/10 0010
 * @Note
 */
public class TreeAdapter extends BaseTreeAdapter<TestBean, TreeAdapter.MyViewHolder> {

    public TreeAdapter(List<TestBean> dataList, IGetChildData<TestBean> iGetChildData) {
        super(dataList, iGetChildData);
    }

    @Override
    protected MyViewHolder onCreateViewHolder2(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    protected int getIndent(int level) {
        return level * 50;
    }



    /**
     * @Purpose
     * @Author Naruto Yang
     * @CreateDate 2020/2/11 0011
     * @Note
     */
    class MyViewHolder extends BaseTreeAdapter.BaseViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView, R.id.checkBox, R.id.text);
        }
    }
}
