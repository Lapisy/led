package shiyiliang.cn.basetool.base.adapter;

import android.content.Context;

import java.util.List;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/9/6
 * Desc  :
 */

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected Context mContext;
    protected List<T> mData;

    public BaseAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
