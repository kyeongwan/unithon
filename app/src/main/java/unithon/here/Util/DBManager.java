package unithon.here.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

    static DBManager g_this;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        g_this = this;
    }

    public static DBManager getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        if(g_this == null)
            g_this = new DBManager(context, name, factory, version);
        return g_this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS friend(" +
                        "phoneNumber TEXT, " +
                        "username TEXT);";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS chat_info(" +
                "phoneNumber TEXT, " +
                "lot double, " +
                "lat double, " +
                "message TEXT, " +
                "toUser TEXT, " +
                "fromUser TEXT);";

        db.execSQL(sql);

        Log.e("DBManager", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void write(String _query) {
        Log.e("DBManager", _query);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void select(String query, OnSelect cb) {
        SQLiteDatabase db = getReadableDatabase();
        Log.e("DBManager SELECT query", query);
        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                cb.onSelect(cursor);
            }
            cb.onComplete(cursor.getCount());
        }catch (Exception e){
            cb.onErrorHandler(e);
        }
    }

    public static interface OnSelect {
        public void onSelect(Cursor cursor);
        public void onComplete(int cnt);
        public void onErrorHandler(Exception e);
    }
}