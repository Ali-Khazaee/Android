package co.biogram.main.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    public static String PROFILE_PRIVATE_ID = "ID";
    public static String PROFILE_PRIVATE_NAME = "Name";
    public static String PROFILE_PRIVATE_USERNAME = "Username";
    public static String PROFILE_PRIVATE_PROFILE = "Profile";
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
    public static String PROFILE_PRIVATE_LATITUDE = "Latitude";
    public static String PROFILE_PRIVATE_LONGITUDE = "Longitude";
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
    private static String INBOX_POST = "inbox_post";
    private static String PROFILE_PRIVATE = "profile_private";

    public DBHandler(Context context) {
        super(context, "Bio.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        CreateDB(DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int Old, int New) {
        CreateDB(DB);
    }

    private void CreateDB(SQLiteDatabase DB) {
        DB.execSQL("DROP TABLE IF EXISTS `" + INBOX_POST + "`");
        DB.execSQL("DROP TABLE IF EXISTS `" + PROFILE_PRIVATE + "`");
        DB.execSQL("CREATE TABLE `" + INBOX_POST + "` (`ID` VARCHAR(50) PRIMARY KEY NOT NULL, `Owner` VARCHAR(50), `Profile` VARCHAR(50), `Name` VARCHAR(50), `Medal` VARCHAR(50), `Message` VARCHAR(300), `Username` VARCHAR(50), `Time` INT(10), `Type` TINYINT(3), `Data` VARCHAR(500), `View` INT(10), `Category` TINYINT(3), `LikeCount` INT(10), `CommentCount` INT(10), `IsLike` TINYINT(3), `IsFollow` TINYINT(3), `IsComment` TINYINT(3), `IsBookmark` TINYINT(3), `I1` VARCHAR(50), `I1P` VARCHAR(50), `I2` VARCHAR(50), `I2P` VARCHAR(50), `I3` VARCHAR(50), `I3P` VARCHAR(50), `I4` VARCHAR(50), `I4P` VARCHAR(50))");
        DB.execSQL("CREATE TABLE `" + PROFILE_PRIVATE + "` (`ID` VARCHAR(50) PRIMARY KEY NOT NULL, `Name` VARCHAR(50) NOT NULL, `Profile` VARCHAR(50) NOT NULL, `Username` VARCHAR(50) NOT NULL, `Type` VARCHAR(50) NOT NULL, `ProfileCount` INT(11) NOT NULL, `PostCount` INT(11) NOT NULL, `FollowingCount` INT(11) NOT NULL, `FollowerCount` INT(11) NOT NULL, `RatingCount` INT(11) NOT NULL, `Level` INT(11) NOT NULL, `Cash` INT(11) NOT NULL, `Rating` FLOAT NOT NULL, `Badge` VARCHAR(50) NOT NULL, `AboutMe` VARCHAR(50) NOT NULL, `Link` VARCHAR(50) NOT NULL, `Location` VARCHAR(50) NOT NULL, `Latitude` VARCHAR(50) NOT NULL, `Longitude` VARCHAR(50) NOT NULL)");
    }

    Cursor InboxPost(int Skip) {
        return getReadableDatabase().rawQuery("SELECT * FROM `" + INBOX_POST + "` ORDER BY `Time` DESC LIMIT " + Skip + ",8", null);
    }

    public void InboxUpdate(PostAdapter.PostStruct P) {
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

        getWritableDatabase().replace(INBOX_POST, null, Value);
    }

    void InboxLike(String ID, boolean Ins) {
        String SQL = "UPDATE `" + INBOX_POST + "` SET `" + INBOX_POST_LIKECOUNT + "` = '" + INBOX_POST_LIKECOUNT + "'" + (Ins ? " + " : " - ") + "1 WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    public void InboxMessage(String ID, String Message) {
        String SQL = "UPDATE `" + INBOX_POST + "` SET `" + INBOX_POST_MESSAGE + "` = '" + Message + "' WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    void InboxComment(String ID, boolean Ins) {
        String SQL = "UPDATE `" + INBOX_POST + "` SET `" + INBOX_POST_COMMENTCOUNT + "` = '" + INBOX_POST_COMMENTCOUNT + "'" + (Ins ? " + " : " - ") + "1 WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    public void InboxDelete(String ID) {
        getWritableDatabase().execSQL("DELETE FROM `" + INBOX_POST + "` WHERE `ID` = '" + ID + "'");
    }

    public void ProfilePrivate(ContentValues Value) {
        getWritableDatabase().replace(PROFILE_PRIVATE, null, Value);
    }

    public Cursor ProfilePrivate(String ID, boolean IsUsername) {
        if (IsUsername)
            return getReadableDatabase().rawQuery("SELECT * FROM `" + PROFILE_PRIVATE + "` WHERE `Username` = '" + ID + "'", null);

        return getReadableDatabase().rawQuery("SELECT * FROM `" + PROFILE_PRIVATE + "` WHERE `ID` = '" + ID + "'", null);
    }

    public void ProfilePrivateAbout(String ID, String Message) {
        String SQL = "UPDATE `" + PROFILE_PRIVATE + "` SET `" + PROFILE_PRIVATE_ABOUTME + "` = '" + Message + "' WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    public void ProfilePrivateLink(String ID, String Message) {
        String SQL = "UPDATE `" + PROFILE_PRIVATE + "` SET `" + PROFILE_PRIVATE_LINK + "` = '" + Message + "' WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    public void ProfilePrivateLocation(String ID, String Name, String Lati, String Long) {
        String SQL = "UPDATE `" + PROFILE_PRIVATE + "` SET `" + PROFILE_PRIVATE_LOCATION + "` = '" + Name + "', `" + PROFILE_PRIVATE_LATITUDE + "` = '" + Lati + "', `" + PROFILE_PRIVATE_LONGITUDE + "` = '" + Long + "' WHERE `ID` = '" + ID + "'";

        getWritableDatabase().execSQL(SQL);
    }

    public Cursor PostDetail(String ID) {
        return getReadableDatabase().rawQuery("SELECT * FROM `" + INBOX_POST + "`" + " WHERE `ID` = '" + ID + "' ORDER BY `Time` DESC LIMIT 1", null);
    }
}
