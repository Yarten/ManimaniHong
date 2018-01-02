package com.xiaoshq.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ivan on 2018/1/2.
 */

public class DataOperation {
    private Context context;
    private DatabaseHelper databaseHelper;

    public DataOperation(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        boolean hasTableUser = isExistTable("user");
        boolean hasTableCtrl = isExistTable("ctrl");
        if(!hasTableUser || !hasTableCtrl) initTable();
    }

    // 查询是否已经存在表名为tableName的表,true->存在，false->不存在
    public boolean isExistTable(String tableName) {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.rawQuery("select count(*)  from sqlite_master where type='table' and name = '?';", new String[]{tableName});
            if (cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return false;
    }

    // 初始化两张表
    public void initTable() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
    }

    /*
     *   增加
     */

    // 增加用户
    public int addUser(String name, String pwd) {
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getWritableDatabase();

            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("userName", name);
            cv.put("password", pwd);
            db.insertOrThrow("user",null,cv);
            db.setTransactionSuccessful();

            int id = getUserId(name);
            return id;
        } catch (SQLiteConstraintException e){
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Log.e("operate","", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;
    }

    // 增加控制
    public int addCtrl(int userId, String name, String detail) {
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getWritableDatabase();

            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("userId",userId);
            cv.put("ctrlName", name);
            cv.put("detail", detail);
            db.insertOrThrow("ctrl",null,cv);
            db.setTransactionSuccessful();
            return getCtrlId(userId, name);
        } catch (SQLiteConstraintException e){
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Log.e("operate","", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return 0;
    }

    /*
     *   删除
     */

    // 删除用户
    public boolean deleteUser(int userId) {
        SQLiteDatabase db = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete("user","userId = ?",new String[]{idStr});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("operate", "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    // 删除控制
    public boolean deleteCtrl(int ctrlId) {
        SQLiteDatabase db = null;
        String idStr = Integer.toString(ctrlId);
        try {
            db = databaseHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete("ctrl","ctrlId = ?",new String[]{idStr});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("operate", "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /*
     *   查询
     */

    // 根据userId返回用户名
    public String getuserNameById(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("user",null,"userId=?",new String[]{idStr},null,null,null);
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("userName"));
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据用户名返回userId
    public int getUserId(String name) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("user",null,"userName=?",new String[]{name},null,null,null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("userId"));
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return 0;
    }

    // 根据userId返回用户密码
    public String getPasswordById(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("user",null,"userId=?",new String[]{idStr},null,null,null);
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("password"));
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据用户名返回用户密码
    public String getPasswordByName(String name) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("user",null,"userName=?",new String[]{name},null,null,null);
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("password"));
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据userId返回ctrl列表
    public List<Ctrl> getCtrlList(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Ctrl> ctrlList = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("ctrl",null,"userId=?",new String[]{idStr},null,null,null);
            while (cursor.moveToFirst()) {
                int ctrlId = cursor.getInt(cursor.getColumnIndex("ctrlId"));
                String ctrlName = cursor.getString(cursor.getColumnIndex("ctrlName"));
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                Ctrl ctrl = new Ctrl(ctrlId, userId, ctrlName, detail);
                ctrlList.add(ctrl);
            }
            return ctrlList;
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据ctrlId返回拥有者Id
    public int getUserIdByCtrlId(int ctrlId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(ctrlId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("ctrl",null,"ctrlId=?",new String[]{idStr},null,null,null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("userId"));
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return 0;
    }

    // 根据ctrlId返回控制名称
    public String getCtrlNameById(int ctrlId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(ctrlId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("ctrl",null,"ctrlId=?",new String[]{idStr},null,null,null);
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("ctrlName"));
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据ctrlid返回控制内容
    public String getDetailById(int ctrlId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(ctrlId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("ctrl",null,"ctrlId=?",new String[]{idStr},null,null,null);
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("detail"));
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据控制名称和拥有者Id返回ctrlId
    public int getCtrlId(int userId, String name) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("ctrl",null,"userId=?,ctrlName=?",new String[]{idStr,name},null,null,null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("ctrlId"));
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return 0;
    }

    /*
     *   修改
     */

    // 根据userId修改用户信息
    public boolean updateUser(int userId, String name, String pwd) {
        SQLiteDatabase db = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getWritableDatabase();

            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("userName",name);
            cv.put("password",pwd);
            db.update("user",cv,"userId = ?",new String[]{idStr});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("update user","",e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    // 根据ctrlId修改控制信息
    public boolean updateCtrl(int ctrlId, String name, String ctx) {
        SQLiteDatabase db = null;
        String idStr = Integer.toString(ctrlId);
        try {
            db = databaseHelper.getWritableDatabase();

            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("ctrlName", name);
            cv.put("detail", ctx);
            db.update("ctrl",cv,"ctrlId = ?",new String[]{idStr});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("update ctrl","",e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
}
