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
import led.lapisy.com.LedApplication;
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

public class MainMenuAdapter extends BaseAdapter<Menu> {
    private int mPower;
    private boolean[] mSelected;
    public MainMenuAdapter(Context context, List<Menu> data) {
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

        if (i != mData.size() - 1) {
            if (i == 0 && LedApplication.isSearchLight) {
                holder.iv.setImageResource(R.drawable.ic_search_light_selected);
            } else if (i == 1 && LedApplication.openType == 1) {
                holder.iv.setImageResource(R.drawable.ic_light_selected);
            } else if (i == 2 && LedApplication.openType == 2) {
                holder.iv.setImageResource(R.drawable.ic_reaction_mode_selected);
            } else {
                holder.iv.setImageResource(menu.icon);
            }
        } else {
            int resId = getDrawableResource(mPower);
            holder.iv.setImageResource(resId);
        }
        holder.tv.setText(menu.name);
        return view;
    }

    /**
     * 根据电量加载不同的图片
     *
     * @param mPower
     * @return
     */
    private int getDrawableResource(int mPower) {
        int resId = R.drawable.ic_battery_0;
        if (mPower >= 0 && mPower <= 10) {
            resId = R.drawable.ic_battery_0;
        } else if (mPower > 10 && mPower <= 40) {
            resId = R.drawable.ic_battery_25;
        } else if (mPower > 40 && mPower <= 60) {
            resId = R.drawable.ic_battery_50;
        } else if (mPower > 60 && mPower <= 80) {
            resId = R.drawable.ic_battery_75;
        } else if (mPower > 80 && mPower <= 100) {
            resId = R.drawable.ic_battery_100;
        }
        return resId;
    }

    /**
     * 更新电量
     *
     * @param powerPercent
     */
    public void updateBattery(int powerPercent) {
        mPower = powerPercent;
        this.notifyDataSetChanged();
    }

    /**
     * 更新选中的照片
     *
     * @param mSelected
     */
    public void updateSelectdDrawable(boolean[] mSelected) {
        this.mSelected = mSelected;
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
