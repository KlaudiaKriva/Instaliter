package com.example.instaliter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, "instaliter.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table User (id integer primary key autoincrement," +
                "name text, instaName text, email text, password text, cTime text)");
        db.execSQL("create table UserInfo (idU integer primary key," +
                "profilePhoto text, profileDescription text)");
        db.execSQL("create table Follow (id integer primary key autoincrement," +
                "followerID integer, followedID integer)");
        db.execSQL("create table Comment (id integer primary key autoincrement," +
                "idI integer, idU integer, commentText text, cTime text)");
        db.execSQL("create table UserLike (id integer primary key autoincrement," +
                "idU integer, idI integer)");
        db.execSQL("create table ImageTag (id integer primary key autoincrement," +
                "idT integer, idI integer)");
        db.execSQL("create table Tag (id integer primary key autoincrement," +
                "tagText text)");
        db.execSQL("create table Image (id integer primary key autoincrement," +
                " idU integer, path text, cTime text, decsriptipn text)"); //path ma byt text alebo blob?

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists UserInfo");
        db.execSQL("drop table if exists Follow");
        db.execSQL("drop table if exists Comment");
        db.execSQL("drop table if exists UserLike");
        db.execSQL("drop table if exists ImageTag");
        db.execSQL("drop table if exists Tag");
        db.execSQL("drop table if exists Image");
        onCreate(db);
    }

    public boolean insertNewPost(long idU, String path, String description) {
//        if (path.equals("")) return false;

        System.out.println("spon som sa tu dostal");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idU", RegisterActivity.userID);
        contentValues.put("path", path);
        contentValues.put("cTime", new Date().getTime()); //alebo iny time
        contentValues.put("decsriptipn", description);

        System.out.println("date je: " + new Date().getTime());
        long result = db.insert("Image", null, contentValues);

        if (result == -1) return false;
        else
            return true;

    }


    public long insertNewUser(String name, String userName, String email, String password ){
        if (name.equals("") || userName.equals("") || email.equals("") || password.equals("")) return -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("instaName", userName);
        contentValues.put("email", email);
        contentValues.put("password", password); //ctime nie je potrebny

        long result = db.insert("User", null, contentValues);

        return result;

    }


    public boolean selectUserInfo(long userid){

        return false;

    } //na zaklade id
    public boolean selectAllPosts(){return false;}

    public boolean loginUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT id FROM User where email = ? and password = ?", new String[]{email, password});

            int userid = 0;
            while (cursor.moveToNext()) {
                userid = cursor.getInt(0);

            }

            if(userid != 0){
                RegisterActivity.userID = userid;
                return true;
            } else {
                return false;
            }
        } catch ( SQLiteException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    public ArrayList<Post> selectMyPosts(){
        ArrayList<Post> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Image where idU = " + RegisterActivity.userID, null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String path = cursor.getString(2);
            String ctime = cursor.getString(3);
            String description = cursor.getString(4);

            Post post = new Post(id, RegisterActivity.userName, path, ctime, description);
            arrayList.add(post);

        }
        return arrayList;
    }



}
