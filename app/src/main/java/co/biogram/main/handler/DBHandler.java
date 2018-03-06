package co.biogram.main.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper
{
    DBHandler(Context context)
    {
        super(context, "Bio.db" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB)
    {
        DB.execSQL("CREATE TABLE `inbox_post` (`ID` VARCHAR(50) PRIMARY KEY NOT NULL, `Owner` VARCHAR(50), `Profile` VARCHAR(50), `Name` VARCHAR(50), `Medal` VARCHAR(50), `Message` VARCHAR(300), `Username` VARCHAR(50), `Time` INT(10), `Type` TINYINT(3), `Data` VARCHAR(500), `View` INT(10), `Category` TINYINT(3), `LikeCount` INT(10), `CommentCount` INT(10), `IsLike` TINYINT(3), `IsFollow` TINYINT(3), `IsComment` TINYINT(3), `IsBookmark` TINYINT(3), `I1` VARCHAR(50), `I1P` VARCHAR(50), `I2` VARCHAR(50), `I2P` VARCHAR(50), `I3` VARCHAR(50), `I3P` VARCHAR(50), `I4` VARCHAR(50), `I4P` VARCHAR(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int Old, int New)
    {
        onCreate(DB);
    }

    static final String INBOX_POST_ID = "ID";
    static final String INBOX_POST_OWNER = "Owner";
    static final String INBOX_POST_PROFILE = "Profile";
    static final String INBOX_POST_NAME = "Name";
    static final String INBOX_POST_MEDAL = "Medal";
    static final String INBOX_POST_MESSAGE = "Message";
    static final String INBOX_POST_USERNAME = "Username";
    static final String INBOX_POST_TIME = "Time";
    static final String INBOX_POST_TYPE = "Type";
    static final String INBOX_POST_DATA = "Data";
    static final String INBOX_POST_VIEW = "View";
    static final String INBOX_POST_CATEGORY = "Category";
    static final String INBOX_POST_LIKECOUNT = "LikeCount";
    static final String INBOX_POST_COMMENTCOUNT = "CommentCount";
    static final String INBOX_POST_LIKE = "IsLike";
    static final String INBOX_POST_FOLLOW = "IsFollow";
    static final String INBOX_POST_BOOKMARK = "IsComment";
    static final String INBOX_POST_COMMENT = "IsBookmark";
    static final String INBOX_POST_I1 = "I1";
    static final String INBOX_POST_I1P = "I1P";
    static final String INBOX_POST_I2 = "I2";
    static final String INBOX_POST_I2P = "I2P";
    static final String INBOX_POST_I3 = "I3";
    static final String INBOX_POST_I3P = "I3P";
    static final String INBOX_POST_I4 = "I4";
    static final String INBOX_POST_I4P = "I4P";

    Cursor InboxPost(int Skip)
    {
        return getReadableDatabase().rawQuery("SELECT * FROM `inbox_post` ORDER BY  `Time` DESC LIMIT " + Skip + ",8", null);
    }

    void InboxUpdate(PostAdapter.PostStruct P)
    {
        ContentValues Value = new ContentValues();
        Value.put(INBOX_POST_ID, P.ID);

        if (P.Profile != null)
            Value.put(INBOX_POST_PROFILE, P.Profile);

        Value.put(INBOX_POST_NAME, P.Name);

        if (P.Medal != null)
            Value.put(INBOX_POST_MEDAL, P.Medal);

        Value.put(INBOX_POST_USERNAME, P.Username);
        Value.put(INBOX_POST_TIME, P.Time);

        if (P.Message != null)
            Value.put(INBOX_POST_MESSAGE, P.Message);

        Value.put(INBOX_POST_TYPE, P.Type);

        if (P.Data != null)
            Value.put(INBOX_POST_DATA, P.Data);

        Value.put(INBOX_POST_OWNER, P.Owner);
        Value.put(INBOX_POST_VIEW, P.View);
        Value.put(INBOX_POST_CATEGORY, P.Category);
        Value.put(INBOX_POST_LIKECOUNT, P.LikeCount);
        Value.put(INBOX_POST_COMMENTCOUNT, P.CommentCount);
        Value.put(INBOX_POST_LIKE, P.IsLike);
        Value.put(INBOX_POST_FOLLOW, P.IsFollow);
        Value.put(INBOX_POST_COMMENT, P.IsComment);
        Value.put(INBOX_POST_BOOKMARK, P.IsBookmark);

        if (P.Person1ID != null)
        {
            Value.put(INBOX_POST_I1, P.Person1ID);
            Value.put(INBOX_POST_I1P, P.Person1Avatar);
        }

        if (P.Person2ID != null)
        {
            Value.put(INBOX_POST_I2, P.Person2ID);
            Value.put(INBOX_POST_I2P, P.Person2Avatar);
        }

        if (P.Person3ID != null)
        {
            Value.put(INBOX_POST_I3, P.Person3ID);
            Value.put(INBOX_POST_I3P, P.Person3Avatar);
        }

        if (P.Person4ID != null)
        {
            Value.put(INBOX_POST_I4, P.Person4ID);
            Value.put(INBOX_POST_I4P, P.Person4Avatar);
        }

        getWritableDatabase().replace("inbox_post", null, Value);
    }

    void InboxLike(String ID, boolean Ins)
    {
        String SQL = "UPDATE `inbox_post` SET `" + INBOX_POST_LIKECOUNT + "` = `" + INBOX_POST_LIKECOUNT + "`" + (Ins ? " + " : " - ") + "1 WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    void InboxComment(String ID, boolean Ins)
    {
        String SQL = "UPDATE `inbox_post` SET `" + INBOX_POST_COMMENTCOUNT + "` = `" + INBOX_POST_COMMENTCOUNT + "`" + (Ins ? " + " : " - ") + "1 WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    void InboxDelete(String ID)
    {
        getWritableDatabase().execSQL("DELETE FROM `inbox_post` WHERE `ID` = '" + ID + "'");
    }
}
