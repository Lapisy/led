package led.lapisy.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import led.lapisy.com.bean.Menu;
import led.lapisy.com.led.R;
import shiyiliang.cn.basetool.base.adapter.BaseAdapter;
import shiyiliang.cn.basetool.util.LogUtil;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/4/12
 * Desc  :
 */

public class LightMenuAdapter extends BaseAdapter<Menu> {
    private int[] mSelectedIon = new int[]{
            R.drawable.ic_light_up_selected,
            R.drawable.ic_flash_selected,
            R.drawable.ic_multi_light_selected,
            R.drawable.ic_candle_selected
    };

    public LightMenuAdapter(Context context, List<Menu> data) {
        super(context, data);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.main_menu_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Menu menu = mData.get(i);
        if (menu.isSelected) {
            holder.iv.setImageResource(mSelectedIon[i]);
        } else {
            holder.iv.setImageResource(menu.icon);
        }
        holder.tv.setText(menu.name);
        return view;
    }

    public void updateSelectedDrawable(List<Menu> data) {
        this.mData=data;
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.iv_menu_icon)
        public ImageView iv;
        @BindView(R.id.tv_menu_name)
        public TextView tv;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
