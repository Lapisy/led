package shiyiliang.cn.basetool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

import shiyiliang.cn.basetool.R;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/3
 * Desc  :
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static DBHelper mDBHelper;
    private static String DB_NAME = "db_data";
    private static final int VERSION = 1;
    private Context mContext;
    private HashMap<String, Dao> daos = new HashMap<>();

    public static DBHelper getInstance(Context context) {
        if (mDBHelper == null) {
            synchronized (DBHelper.class) {
                mDBHelper = new DBHelper(context.getApplicationContext(), DB_NAME, null, VERSION);
            }
        }
        return mDBHelper;
    }

    public DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            String[] tableNames = mContext.getResources().getStringArray(R.array.table_name);
            for (String name : tableNames) {
                TableUtils.createTable(connectionSource, Class.forName(name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            String[] tableNames = mContext.getResources().getStringArray(R.array.table_name);
            for (String name : tableNames) {
                TableUtils.dropTable(connectionSource, Class.forName(name), false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Dao getDao(Class clazz) {
        Dao dao = null;
        try {
            if (daos.containsKey(clazz.getSimpleName())) {
                dao = daos.get(clazz.getSimpleName());
            } else {
                //注意这个地方，不能使用getDao
                dao = super.getDao(clazz);
                daos.put(clazz.getSimpleName(), dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        mContext = null;
        Set<String> strings = daos.keySet();
        for (String s : strings) {
            Dao dao = daos.get(s);
            dao = null;
        }
    }
}
