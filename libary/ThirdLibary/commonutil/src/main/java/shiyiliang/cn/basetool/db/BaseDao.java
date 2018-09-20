package shiyiliang.cn.basetool.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/3
 * Desc  :
 */

public abstract class BaseDao<Q> {
    private Class<Q> clazz;
    protected DBHelper helper;
    protected Dao<Q, Integer> dao;

    public BaseDao(Context context) {
        Class clazz = getClass();

        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.clazz = (Class<Q>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
        helper = DBHelper.getInstance(context);
        dao = helper.getDao(this.clazz);
    }

    public void add(Q q) {
        try {
            dao.createIfNotExists(q);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updata(Q q) {
        try {
            dao.update(q);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Q q) {
        try {
            dao.delete(q);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Q> getAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
