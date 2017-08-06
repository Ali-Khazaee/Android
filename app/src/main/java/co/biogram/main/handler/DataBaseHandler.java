package co.biogram.main.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler
{
    private DatabaseHelper DBHelper;

    public DataBaseHandler(Context context)
    {
        DBHelper = new DatabaseHelper(context);
    }

    public long Insert(String table, ContentValues values)
    {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        return db.insert(table, null, values);
    }

    public int Delete(String table, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        return db.delete(table, selection, selectionArgs);
    }

    public int Update(String table, ContentValues values, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        return db.update(table, values, selection, selectionArgs);
    }

    public Cursor Find(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        return db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
    }

    private class DatabaseHelper extends SQLiteOpenHelper
    {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "BioGram.db";

        private DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS `POST` (`PostID`, `OwnerID`, `Type`, `Category`, `Time`, `Comment`, `Message`, `Data`, `Username`, `Avatar`, `Like`, `LikeCount`, `CommentCount`, `BookMark`, `Follow`);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS `POST`;");
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
