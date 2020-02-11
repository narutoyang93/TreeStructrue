package com.naruto.treestructrue.base;

import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2020/2/10 0010
 * @Note
 */
public abstract class BaseTreeAdapter<T extends BaseTreeBean, V extends BaseTreeAdapter.BaseViewHolder> extends RecyclerView.Adapter<V> {
    private List<T> dataList;
    private IGetChildData<T> iGetChildData;
    private Map<String, List<T>> cacheData = new HashMap<>();//缓存数据
    private Pair<String, Integer> waitingDataKey;//记录待展开的数据的key和位置，异步获取数据时避免混乱

    public BaseTreeAdapter(List<T> dataList, IGetChildData<T> iGetChildData) {
        this.dataList = dataList;
        this.iGetChildData = iGetChildData;
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final V v = onCreateViewHolder2(parent, viewType);
        //设置收展监听
        v.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = v.getAdapterPosition();
                T t = dataList.get(position);
                t.isFold = !t.isFold;
                notifyItemChanged(position);
                if (t.isFold) {//折叠
                    int count = 0;
                    T t0;
                    int p = position + 1;
                    String parentId = t.makeChildParentId();
                    //根据parentId遍历查找需要折叠（移除）的数据
                    for (; p < dataList.size(); ) {
                        t0 = dataList.get(p);
                        if (t0.parentId.startsWith(parentId)) {
                            count++;
                            dataList.remove(p);
                        } else {
                            break;
                        }
                    }
                    notifyItemRangeRemoved(p, count);//刷新
                } else {//展开
                    String newParentId = t.makeChildParentId();
                    List<T> list = cacheData.get(newParentId);
                    waitingDataKey = new Pair<>(newParentId, position);
                    if (list == null) {//没有缓存数据，需要获取数据
                        iGetChildData.getData(t);
                    } else {//有缓存数据，无需再次获取
                        //重置状态
                        for (T t0 : list) {
                            t0.isFold = true;
                        }
                        showChildData(newParentId, list);
                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        T t = dataList.get(position);
        holder.textView.setText(t.getText());
        holder.itemView.setClickable(t.hasChildren);
        ImageView icon = holder.icon;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) icon.getLayoutParams();
        lp.leftMargin = getIndent(t.level);
        if (t.hasChildren) {//有子项，显示按钮
            icon.setSelected(!t.isFold);
            icon.setVisibility(View.VISIBLE);
        } else {//无子项，隐藏按钮
            icon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


    /**
     * 展开子项数据
     *
     * @param key
     * @param childData
     */
    public void showChildData(String key, List<T> childData) {
        if (!cacheData.containsKey(key)) cacheData.put(key, childData);//缓存
        if (waitingDataKey.first.equals(key)) {
            int position = waitingDataKey.second + 1;
            dataList.addAll(position, childData);
            notifyItemRangeInserted(position, childData.size());
        }
    }

    protected abstract V onCreateViewHolder2(@NonNull ViewGroup parent, int viewType);

    /**
     * 设置不同级别数据的缩进宽度
     *
     * @param level
     * @return
     */
    protected abstract int getIndent(int level);


    /**
     * @Purpose ViewHolder
     * @Author Naruto Yang
     * @CreateDate 2020/2/10 0010
     * @Note
     */
    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        protected ImageView icon;
        protected TextView textView;

        public BaseViewHolder(@NonNull View itemView, @IdRes int iconId, @IdRes int textViewId) {
            super(itemView);
            icon = itemView.findViewById(iconId);
            textView = itemView.findViewById(textViewId);
        }
    }

    /**
     * @Purpose 获取数据的接口
     * @Author Naruto Yang
     * @CreateDate 2020/2/10 0010
     * @Note
     */
    public interface IGetChildData<H extends BaseTreeBean> {
        void getData(H h);
    }
}
