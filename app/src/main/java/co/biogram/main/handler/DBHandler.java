package co.biogram.main.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper
{
    public DBHandler(Context context)
    {
        super(context, "Bio.db" , null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase DB)
    {
        CreateDB(DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int Old, int New)
    {
        CreateDB(DB);
    }

    private void CreateDB(SQLiteDatabase DB)
    {
        DB.execSQL("CREATE TABLE `inbox_post` (`ID` VARCHAR(50) PRIMARY KEY NOT NULL, `Owner` VARCHAR(50), `Profile` VARCHAR(50), `Name` VARCHAR(50), `Medal` VARCHAR(50), `Message` VARCHAR(300), `Username` VARCHAR(50), `Time` INT(10), `Type` TINYINT(3), `Data` VARCHAR(500), `View` INT(10), `Category` TINYINT(3), `LikeCount` INT(10), `CommentCount` INT(10), `IsLike` TINYINT(3), `IsFollow` TINYINT(3), `IsComment` TINYINT(3), `IsBookmark` TINYINT(3), `I1` VARCHAR(50), `I1P` VARCHAR(50), `I2` VARCHAR(50), `I2P` VARCHAR(50), `I3` VARCHAR(50), `I3P` VARCHAR(50), `I4` VARCHAR(50), `I4P` VARCHAR(50))");
        DB.execSQL("CREATE TABLE `private_profile` (`ID` VARCHAR(50) PRIMARY KEY NOT NULL, `Name 2` VARCHAR(50) NOT NULL, `Username` VARCHAR(50) NOT NULL, `Type` VARCHAR(50) NOT NULL, `ProfileCount` INT(11) NOT NULL, `PostCount` INT(11) NOT NULL, `FollowingCount` INT(11) NOT NULL, `FollowerCount` INT(11) NOT NULL, `RatingCount` INT(11) NOT NULL, `Level` INT(11) NOT NULL, `Cash` INT(11) NOT NULL, `Rating` FLOAT NOT NULL, `Badge` VARCHAR(50) NOT NULL, `AboutMe` VARCHAR(50) NOT NULL, `Link` VARCHAR(50) NOT NULL, `Location` VARCHAR(50) NOT NULL)");
    }

    static String INBOX_POST_ID = "ID";
    static String INBOX_POST_OWNER = "Owner";
    static String INBOX_POST_PROFILE = "Profile";
    static String INBOX_POST_NAME = "Name";
    static String INBOX_POST_MEDAL = "Medal";
    static String INBOX_POST_MESSAGE = "Message";
    static String INBOX_POST_USERNAME = "Username";
    static String INBOX_POST_TIME = "Time";
    static String INBOX_POST_TYPE = "Type";
    static String INBOX_POST_DATA = "Data";
    static String INBOX_POST_VIEW = "View";
    static String INBOX_POST_CATEGORY = "Category";
    static String INBOX_POST_LIKECOUNT = "LikeCount";
    static String INBOX_POST_COMMENTCOUNT = "CommentCount";
    static String INBOX_POST_LIKE = "IsLike";
    static String INBOX_POST_FOLLOW = "IsFollow";
    static String INBOX_POST_BOOKMARK = "IsComment";
    static String INBOX_POST_COMMENT = "IsBookmark";
    static String INBOX_POST_I1 = "I1";
    static String INBOX_POST_I1P = "I1P";
    static String INBOX_POST_I2 = "I2";
    static String INBOX_POST_I2P = "I2P";
    static String INBOX_POST_I3 = "I3";
    static String INBOX_POST_I3P = "I3P";
    static String INBOX_POST_I4 = "I4";
    static String INBOX_POST_I4P = "I4P";

    Cursor InboxPost(int Skip)
    {
        return getReadableDatabase().rawQuery("SELECT * FROM `inbox_post` ORDER BY `Time` DESC LIMIT " + Skip + ",8", null);
    }

    void InboxUpdate(PostAdapter.PostStruct P)
    {
        ContentValues Value = new ContentValues();
        Value.put(INBOX_POST_ID, P.ID);
        Value.put(INBOX_POST_PROFILE, P.Profile);
        Value.put(INBOX_POST_NAME, P.Name);
        Value.put(INBOX_POST_MEDAL, P.Medal);
        Value.put(INBOX_POST_USERNAME, P.Username);
        Value.put(INBOX_POST_TIME, P.Time);
        Value.put(INBOX_POST_MESSAGE, P.Message);
        Value.put(INBOX_POST_TYPE, P.Type);
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
        Value.put(INBOX_POST_I1, P.Person1ID);
        Value.put(INBOX_POST_I1P, P.Person1Avatar);
        Value.put(INBOX_POST_I2, P.Person2ID);
        Value.put(INBOX_POST_I2P, P.Person2Avatar);
        Value.put(INBOX_POST_I3, P.Person3ID);
        Value.put(INBOX_POST_I3P, P.Person3Avatar);
        Value.put(INBOX_POST_I4, P.Person4ID);
        Value.put(INBOX_POST_I4P, P.Person4Avatar);

        getWritableDatabase().replace("inbox_post", null, Value);
    }

    void InboxLike(String ID, boolean Ins)
    {
        String SQL = "UPDATE `inbox_post` SET `" + INBOX_POST_LIKECOUNT + "` = `" + INBOX_POST_LIKECOUNT + "`" + (Ins ? " + " : " - ") + "1 WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    void InboxMessage(String ID, String Message)
    {
        String SQL = "UPDATE `inbox_post` SET `" + INBOX_POST_MESSAGE + "` = '" + Message + "' WHERE `ID` = '" + ID + "'";

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

    public static String PROFILE_PRIVATE_ID = "ID";
    public static String PROFILE_PRIVATE_NAME = "Name";
    public static String PROFILE_PRIVATE_USERNAME = "Username";
    public static String PROFILE_PRIVATE_TYPE = "Type";
    public static String PROFILE_PRIVATE_PROFILE_COUNT = "ProfileCount";
    public static String PROFILE_PRIVATE_POST_COUNT = "PostCount";
    public static String PROFILE_PRIVATE_FOLLOWING_COUNT = "FollowingCount";
    public static String PROFILE_PRIVATE_FOLLOWER_COUNT = "FollowerCount";
    public static String PROFILE_PRIVATE_RATING_COUNT = "RatingCount";
    public static String PROFILE_PRIVATE_LEVEL = "Level";
    public static String PROFILE_PRIVATE_CASH = "Cash";
    public static String PROFILE_PRIVATE_RATING = "Rating";
    public static String PROFILE_PRIVATE_BADGE = "Badge";
    public static String PROFILE_PRIVATE_ABOUTME = "AboutMe";
    public static String PROFILE_PRIVATE_LINK = "Link";
    public static String PROFILE_PRIVATE_LOCATION = "Location";

    public void ProfilePrivate(ContentValues Value)
    {
        getWritableDatabase().replace("private_profile", null, Value);
    }

    public Cursor ProfilePrivate(String ID)
    {
        return getReadableDatabase().rawQuery("SELECT * FROM `private_profile` WHERE `ID` = '" + ID + "'", null);
    }
}
