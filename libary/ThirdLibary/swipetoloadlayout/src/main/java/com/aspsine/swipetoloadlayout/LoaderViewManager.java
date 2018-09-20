package com.aspsine.swipetoloadlayout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lange on 2017/6/17.
 */

public class LoaderViewManager {
    public static enum Type {
        GoogleStyle,
        ClassicStyle
    }

    public static View createHeaderView(Activity context, Type type, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View mView = null;
        switch (type) {
            case GoogleStyle:
                mView = layoutInflater.inflate(R.layout.layout_google_hook_header, parent, false);
                break;
            case ClassicStyle:
                mView = layoutInflater.inflate(R.layout.layout_twitter_header, parent, false);
                break;
        }
        return mView;
    }

    public static View createFooterView(Activity context, Type type, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View mView = null;
        switch (type) {
            case GoogleStyle:
                mView = layoutInflater.inflate(R.layout.layout_google_hook_footer, parent, false);
                break;
            case ClassicStyle:
                mView = layoutInflater.inflate(R.layout.layout_classic_footer, parent, false);
                break;
        }
        return mView;
    }
}
