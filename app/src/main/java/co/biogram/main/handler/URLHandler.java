package co.biogram.main.handler;

import java.util.Random;

public class URLHandler
{
    public enum URL
    {
        USERNAME_IS_AVAILABLE,
        SIGN_UP,
        SIGN_IN,
        RESET_PASSWORD,
        SIGN_IN_GOOGLE,

        POST_WRITE,
        POST_LIST,
        POST_REPORT,
        POST_DELETE,
        POST_TURN_COMMENT,
        POST_LIKE,
        POST_LIKE_LIST,
        POST_DETAILS,
        POST_COMMENT,
        POST_COMMENT_LIST,
        POST_COMMENT_LIKE,
        POST_COMMENT_DELETE,
        POST_BOOKMARK,

        PROFILE_GET,
        PROFILE_GET_POST,
        PROFILE_GET_COMMENT,
        PROFILE_GET_LIKE,
        PROFILE_EDIT_GET,
        PROFILE_EDIT_SET,
        PROFILE_EDIT_COVER_DELETE,
        PROFILE_EDIT_AVATAR_DELETE,
        PROFILE_POST_GET,
        PROFILE_COMMENT_GET,
        PROFILE_LIKE_GET,

        FOLLOW,
        FOLLOWING_GET,
        FOLLOWERS_GET,

        MISC_LAST_ONLINE
    }

    public static String GetURL(URL Code)
    {
        String URL = "";

        switch (Code)
        {
            case USERNAME_IS_AVAILABLE: URL = "UsernameIsAvailable"; break;
            case SIGN_UP:               URL = "SignUp";              break;
            case SIGN_IN:               URL = "SignIn";              break;
            case RESET_PASSWORD:        URL = "ResetPassword";       break;
            case SIGN_IN_GOOGLE:        URL = "SignInGoogle";        break;

            case POST_WRITE:          URL = "PostWrite";         break;
            case POST_LIST:           URL = "PostList";          break;
            case POST_REPORT:         URL = "PostReport";        break;
            case POST_DELETE:         URL = "PostDelete";        break;
            case POST_TURN_COMMENT:   URL = "PostTurnComment";   break;
            case POST_LIKE:           URL = "PostLike";          break;
            case POST_LIKE_LIST:      URL = "PostLikeList";      break;
            case POST_DETAILS:        URL = "PostDetails";       break;
            case POST_COMMENT:        URL = "PostComment";       break;
            case POST_COMMENT_LIST:   URL = "PostCommentList";   break;
            case POST_COMMENT_LIKE:   URL = "PostCommentLike";   break;
            case POST_COMMENT_DELETE: URL = "PostCommentDelete"; break;
            case POST_BOOKMARK:       URL = "PostBookMark";      break;

            case PROFILE_GET:                URL = "ProfileGet";          break;
            case PROFILE_GET_POST:           URL = "ProfileGetPost";      break;
            case PROFILE_GET_COMMENT:        URL = "ProfileGetComment";   break;
            case PROFILE_GET_LIKE:           URL = "ProfileGetLike";      break;
            case PROFILE_EDIT_SET:           URL = "ProfileSetEdit";      break;
            case PROFILE_EDIT_GET:           URL = "ProfileGetEdit";      break;
            case PROFILE_EDIT_COVER_DELETE:  URL = "ProfileCoverDelete";  break;
            case PROFILE_EDIT_AVATAR_DELETE: URL = "ProfileAvatarDelete"; break;
            case PROFILE_POST_GET:           URL = "ProfilePostGet";      break;
            case PROFILE_COMMENT_GET:        URL = "ProfileCommentGet";   break;
            case PROFILE_LIKE_GET:           URL = "ProfileLikeGet";      break;

            case FOLLOW:        URL = "Follow";       break;
            case FOLLOWING_GET: URL = "FollowingGet"; break;
            case FOLLOWERS_GET: URL = "FollowersGet"; break;

            case MISC_LAST_ONLINE: URL = "LastOnline"; break;
        }

        return GetRandomServer() + URL;
    }

    public static String GetURL(String Name)
    {
        String URL = "";

        switch (Name)
        {
            case "Notification": URL = "NotificationList"; break;
        }

        return GetRandomServer() + URL;
    }

    private static String GetRandomServer()
    {
        Random random = new Random();

        switch (random.nextInt(5) + 1)
        {
            case 1:  return "http://10.48.9.85/";
            case 2:  return "http://10.48.9.85/";
            case 3:  return "http://10.48.9.85/";
            case 4:  return "http://10.48.9.85/";
            case 5:  return "http://10.48.9.85/";
            default: return "http://10.48.9.85/";
        }
    }
}
