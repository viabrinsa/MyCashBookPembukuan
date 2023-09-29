package com.example.mycashbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mycashbook.DetailsModels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "Cash_Book.DB";
    static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL )");
        sqLiteDatabase.execSQL("CREATE TABLE incomes(id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE NOT NULL, nominal DOUBLE NOT NULL, description TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE expenses(id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE NOT NULL, nominal DOUBLE NOT NULL, description TEXT)");


        ContentValues contentValues = new ContentValues();
        contentValues.put("username", "admin");
        contentValues.put("password", "password");
        long result = sqLiteDatabase.insert("users", null, contentValues);

        if (result==-1){
            Log.d("test_create_insert", "false");
        }
        else{
            Log.d("test_create_insert", "true");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS incomes");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS expenses");

    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[] {username, password});

        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Cursor getAllPemasukan(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM incomes", null);
        return cursor;
    }

    public Cursor getNewestPemasukanByUser(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT SUM(nominal) as nominals FROM incomes", null);
        return cursor;
    }

    public Cursor getNewestPengeluaranByUser(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT SUM(nominal) as nominals FROM expenses", null);
        return cursor;
    }

    public boolean insertPemasukan(String date, double nominal, String description){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("nominal", nominal);
        contentValues.put("description", description);
        long result = MyDB.insert("incomes", null, contentValues);

        if (result==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertPengeluaran(String date, double nominal, String description){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("nominal", nominal);
        contentValues.put("description", description);
        long result = MyDB.insert("expenses", null, contentValues);

        if (result==-1){
            return false;
        }else{
            return true;
        }
    }

    public List<DetailsModels> getDetails(){

        List<DetailsModels> returnList = new ArrayList<>();

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor pemasukanCursor = MyDB.rawQuery("SELECT * FROM incomes", null);

        Cursor pengeluaranCursor = MyDB.rawQuery("SELECT * FROM expenses", null);

        if (pemasukanCursor.moveToFirst()){
            do {
                double nominal = pemasukanCursor.getInt(2);
                String description = pemasukanCursor.getString(3);
                String date = pemasukanCursor.getString(1);
                String type = "pemasukan";

                Date date1 = null;

                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DetailsModels newDetails = new DetailsModels(nominal, description, date1, type);

                returnList.add(newDetails);

//            DetailsModels data = returnList.get(0);

            }while (pemasukanCursor.moveToNext());
        }else {

        }

        if (pengeluaranCursor.moveToFirst()){
            do {
                double nominal = pengeluaranCursor.getInt(2);
                String description = pengeluaranCursor.getString(3);
                String date = pengeluaranCursor.getString(1);
                String type = "pengeluaran";

                Date date1 = null;

                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DetailsModels newDetails = new DetailsModels(nominal, description, date1, type);

                returnList.add(newDetails);

            }while (pengeluaranCursor.moveToNext());
        }else {

        }
        pemasukanCursor.close();
        pengeluaranCursor.close();
        MyDB.close();
    return returnList;
    }

    public boolean changePassword(String oldPassword, String newPassword){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        String username = "admin";

        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[] {username, oldPassword});

        if (cursor.getCount()>0){
            long result = MyDB.update("users", contentValues,"username=?", new String[] {username});
            if (result==-1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }



}
