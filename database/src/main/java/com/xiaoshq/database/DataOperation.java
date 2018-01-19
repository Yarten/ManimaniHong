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
        boolean hasTableCtrl = isExistTable("solution");
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

    // 将查找到的数据转换成User类
    public User toUser(Cursor cursor) {
        User user = new User(0,"","");
        user.userId = (cursor.getInt(cursor.getColumnIndex("userId")));
        user.userName = (cursor.getString(cursor.getColumnIndex("userName")));
        user.password = (cursor.getString(cursor.getColumnIndex("password")));
        return user;
    }

    // 将查找到的数据转换成Solutioin类
    public Solution toSolution(Cursor cursor) {
        Solution solution = new Solution(0,0,"","");
        solution.solId = (cursor.getInt(cursor.getColumnIndex("solId")));
        solution.userId = (cursor.getInt(cursor.getColumnIndex("userId")));
        solution.solName = (cursor.getString(cursor.getColumnIndex("solName")));
        solution.detail = (cursor.getString(cursor.getColumnIndex("detail")));
        return solution;
    }

    // region ** 增加 **

    // 增加用户 返回：user的userId
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

            int id = getUser(name).userId;
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

    // 增加solution 返回：solution的Id
    public int addSol(int userId, String name, String detail) {
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getWritableDatabase();

            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("userId",userId);
            cv.put("solName", name);
            cv.put("detail", detail);
            db.insertOrThrow("solution",null,cv);
            db.setTransactionSuccessful();
            return getSolution(userId, name).solId;
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
    // endregion 增加

    // region ** 删除 **

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

    // 删除solution
    public boolean deleteSol(int solId) {
        SQLiteDatabase db = null;
        String idStr = Integer.toString(solId);
        try {
            db = databaseHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete("solution","solId = ?",new String[]{idStr});
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
    // endregion 删除

    // region ** 查询 **

    // 根据用户ID返回User信息
    public User getUser(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("user",null,"userId=?",new String[]{idStr},null,null,null);
            if (cursor.moveToFirst()) {
                return toUser(cursor);
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据用户名返回User
    public User getUser(String name) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("user",null,"userName=?",new String[]{name},null,null,null);
            if (cursor.moveToFirst()) {
                return toUser(cursor);
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据用户ID返回列表List<Solution>
    public List<Solution> getSolList(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Solution> solList = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("solutioin",null,"userId=?",new String[]{idStr},null,null,null);
            while (cursor.moveToFirst()) {
                Solution sol = toSolution(cursor);
                solList.add(sol);
            }
            return solList;
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据solId返回拥有者User信息
    public User getUserBySolId(int solId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(solId);
        int userId = 0;
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("solution",null,"solId=?",new String[]{idStr},null,null,null);
            if (cursor.moveToFirst()) {
                userId = toSolution(cursor).userId;
            }
            return getUser(userId);
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据solId返回Solution
    public Solution getSolution(int solId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(solId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("solution",null,"solId=?",new String[]{idStr},null,null,null);
            if (cursor.moveToFirst()) {
                return toSolution(cursor);
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }

    // 根据solution名称和拥有者Id返回Solution
    public Solution getSolution(int userId, String name) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String idStr = Integer.toString(userId);
        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("solution",null,"userId=?,solName=?",new String[]{idStr,name},null,null,null);
            if (cursor.moveToFirst()) {
                return toSolution(cursor);
            }
        } catch (Exception e) {
            Log.e("operate","",e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return null;
    }
    // endregion 查询

    // region ** 修改 **

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
    public boolean updateCtrl(int solId, String name, String ctx) {
        SQLiteDatabase db = null;
        String idStr = Integer.toString(solId);
        try {
            db = databaseHelper.getWritableDatabase();

            db.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put("solName", name);
            cv.put("detail", ctx);
            db.update("solution",cv,"solId = ?",new String[]{idStr});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("update sol","",e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }
    // endregion 修改
}
