package co.biogram.main.handler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.biogram.main.App;

public class DataBaseHandler
{
    private static SQLiteDatabase DB;
    private static SQLiteOpenHelper Helper;

    private static synchronized void Prepare()
    {
        if (Helper == null)
            Helper = new DataBaseHelper();
    }

    public synchronized static void AddOrUpdate(String Table, String[] Columns, String Where, String[] Args, ContentValues Content)
    {
        Prepare();

        SQLiteDatabase DataBase = Helper.getWritableDatabase();
        Cursor cursor = DataBase.query(Table, Columns, Where, Args, null, null, null);

        if (cursor.getCount() > 0)
        {
            DataBase.update(Table, Content, Where, Args);
        }
        else
        {
            DataBase.beginTransaction();
            DataBase.insert(Table, null, Content);
            DataBase.setTransactionSuccessful();
            DataBase.endTransaction();
        }

        DataBase.close();
        cursor.close();
    }

    public synchronized static void Add(String Table, ContentValues Content)
    {
        Prepare();

        SQLiteDatabase DataBase = Helper.getWritableDatabase();
        DataBase.beginTransaction();
        DataBase.insert(Table, null, Content);
        DataBase.setTransactionSuccessful();
        DataBase.endTransaction();
        DataBase.close();
    }

    public synchronized static void Update(String Table, ContentValues Content, String Where, String[] Args)
    {
        Prepare();

        SQLiteDatabase DataBase = Helper.getWritableDatabase();
        DataBase.update(Table, Content, Where, Args);
        DataBase.close();
    }

    public synchronized static void Remove(String Table, String Where, String[] Args)
    {
        Prepare();

        SQLiteDatabase DataBase = Helper.getWritableDatabase();
        DataBase.delete(Table, Where, Args);
        DataBase.close();
    }

    public synchronized static Cursor Find(String Table, String[] Columns, String Selection, String[] Args, String Order, String Limit)
    {
        Prepare();

        DB = Helper.getReadableDatabase();
        return DB.query(Table, Columns, Selection, Args, null, null, Order, Limit);
    }

    public synchronized static void Close(Cursor cursor)
    {
        if (cursor != null)
            cursor.close();

        if (DB != null)
            DB.close();
    }

    public synchronized static void SetUp()
    {
        Prepare();

        SQLiteDatabase DataBase = Helper.getWritableDatabase();
        DataBase.execSQL("CREATE TABLE IF NOT EXISTS `POST` (`PostID`, `OwnerID`, `Type`, `Category`, `Time`, `Comment`, `Message`, `Data`, `Username`, `Avatar`, `Like`, `LikeCount`, `CommentCount`, `BookMark`);");
        DataBase.close();
    }

    private static class DataBaseHelper extends SQLiteOpenHelper
    {
        DataBaseHelper()
        {
            super(App.GetContext(), "BioGram.db", null, 1);
        }

        public void onCreate(SQLiteDatabase db)
        {

        }

        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
        {

        }
    }
}
