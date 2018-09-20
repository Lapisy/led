package com.example.smallhttp.cookie;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/28
 * Desc  : 持久化存储Cookie
 */

public class CookieManager implements CookieJar {
    private static CookieManager cookieManager;

    private final PersistentCookieStore persistentCookieStore;

    public static CookieManager getInstance(Context context) {
        if (cookieManager == null) {
            synchronized (CookieManager.class) {
                if (cookieManager == null) {
                    cookieManager = new CookieManager(context.getApplicationContext());
                }
            }
        }
        return cookieManager;
    }

    private CookieManager(Context context) {
        persistentCookieStore = new PersistentCookieStore(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        persistentCookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return persistentCookieStore.get(url);
    }
}
