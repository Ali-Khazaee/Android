package co.biogram.main.misc;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.biogram.main.handler.CacheHandler;

public class TextCrawler
{
    private URLContent UrlContent;

    private final String URL;
    private final String Tag;
    private final Context context;
    private final TextCrawlerCallBack CallBackListener;

    public TextCrawler(Context c, String url, String tag, TextCrawlerCallBack CallBack)
    {
        context = c;
        URL = url;
        Tag = tag;
        CallBackListener = CallBack;
    }

    public void Start()
    {
        UrlContent = new URLContent();

        if (CacheHandler.LinkIsCache(URL))
        {
            String[] Data = CacheHandler.FindLink(URL);

            if (Data != null && Data.length > 1)
            {
                UrlContent.Title = Data[0];
                UrlContent.Description = Data[1];
                UrlContent.Image = Data[2];

                CallBackListener.OnCompleted(UrlContent);
                return;
            }
        }

        AndroidNetworking.get(URL).setTag(Tag).build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    String Title = new URI(URL).getHost();
                    Title = Title.substring(0, 1).toUpperCase() + Title.substring(1);

                    if (Title.length() > 25)
                        Title = Title.substring(0, 25) + "...";

                    UrlContent.Title = Title;

                    String HTML = ExtendedTrim(Response);

                    HashMap<String, String> MetaTags = GetMetaTags(HTML);

                    UrlContent.Description = MetaTags.get("description");

                    if (UrlContent.Description == null || UrlContent.Description.equals(""))
                        UrlContent.Description = "No Description ...";

                    if (!MetaTags.get("image").startsWith("http://") && !MetaTags.get("image").startsWith("https://"))
                        UrlContent.Image = URL + MetaTags.get("image");
                    else
                        UrlContent.Image = MetaTags.get("image");

                    CallBackListener.OnCompleted(UrlContent);

                    CacheHandler.StoreLink(context, URL, UrlContent.Title, UrlContent.Description, UrlContent.Image);
                }
                catch (Exception e)
                {
                    CallBackListener.OnFailed();
                }
            }

            @Override
            public void onError(ANError anError)
            {
                CallBackListener.OnFailed();
            }
        });
    }

    public interface TextCrawlerCallBack
    {
        void OnCompleted(URLContent UrlContent);
        void OnFailed();
    }

    public class URLContent
    {
        public String Image;
        public String Title;
        public String Description;
    }

    private String ExtendedTrim(String Content)
    {
        return Content.replaceAll("\\s+", " ").replace("\n", " ").replace("\r", " ").trim();
    }

    private HashMap<String, String> GetMetaTags(String Content)
    {
        HashMap<String, String> MetaTags = new HashMap<>();
        MetaTags.put("description", "");
        MetaTags.put("image", "");

        List<String> MatchList = PregMatchAll(Content, "<meta(.*?)>", 1);

        for (String Match : MatchList)
        {
            if (Match.toLowerCase().contains("property=\"og:description\"") ||
                Match.toLowerCase().contains("property='og:description'") ||
                Match.toLowerCase().contains("property=og:description") ||
                Match.toLowerCase().contains("property=\"twitter:description\"") ||
                Match.toLowerCase().contains("property='twitter:description'") ||
                Match.toLowerCase().contains("property=twitter:description") ||
                Match.toLowerCase().contains("name=\"og:description\"") ||
                Match.toLowerCase().contains("name='og:description'") ||
                Match.toLowerCase().contains("name=og:description") ||
                Match.toLowerCase().contains("name=\"twitter:description\"") ||
                Match.toLowerCase().contains("name='twitter:description'") ||
                Match.toLowerCase().contains("name=twitter:description") ||
                Match.toLowerCase().contains("name=\"description\"") ||
                Match.toLowerCase().contains("name='description'") ||
                Match.toLowerCase().contains("name=description") ||
                Match.toLowerCase().contains("property=\"og:title\"") ||
                Match.toLowerCase().contains("property='og:title'") ||
                Match.toLowerCase().contains("property=og:title") ||
                Match.toLowerCase().contains("property=\"twitter:title\"") ||
                Match.toLowerCase().contains("property='twitter:title'") ||
                Match.toLowerCase().contains("property=twitter:title") ||
                Match.toLowerCase().contains("name=\"og:title\"") ||
                Match.toLowerCase().contains("name='og:title'") ||
                Match.toLowerCase().contains("name=og:title") ||
                Match.toLowerCase().contains("name=\"twitter:title\"") ||
                Match.toLowerCase().contains("name='twitter:title'") ||
                Match.toLowerCase().contains("name=twitter:title"))
            {
                String Result = PregMatch(Match, "content=\"(.*?)\"", 1);

                if (Result.equals(""))
                    Result = PregMatch(Match, "content='(.*?)'", 1);

                MetaTags.put("description", Result);
                break;
            }
        }

        List<String> MatchList2 = PregMatchAll(Content, "<meta(.*?)>", 1);

        for (String Match : MatchList2)
        {
            if (Match.toLowerCase().contains("rel=\"fluid-icon\"") ||
                Match.toLowerCase().contains("rel='fluid-icon'") ||
                Match.toLowerCase().contains("rel=fluid-icon") ||
                Match.toLowerCase().contains("rel=\"mask-icon\"") ||
                Match.toLowerCase().contains("rel='mask-icon'") ||
                Match.toLowerCase().contains("rel=mask-icon") ||
                Match.toLowerCase().contains("property=\"og:image\"") ||
                Match.toLowerCase().contains("property='og:image'") ||
                Match.toLowerCase().contains("property=\"twitter:image\"") ||
                Match.toLowerCase().contains("property='twitter:image'") ||
                Match.toLowerCase().contains("name=\"og:image\"") ||
                Match.toLowerCase().contains("name='og:image'") ||
                Match.toLowerCase().contains("name=\"twitter:image\"") ||
                Match.toLowerCase().contains("name='twitter:image'") ||
                Match.toLowerCase().contains("rel=\"icon\" sizes=\"192x192\"") ||
                Match.toLowerCase().contains("rel='icon' sizes='192x192'") ||
                Match.toLowerCase().contains("rel=icon sizes=192x192") ||
                Match.toLowerCase().contains("rel=\"icon\" sizes=\"180x180\"") ||
                Match.toLowerCase().contains("rel='icon' sizes='180x180'") ||
                Match.toLowerCase().contains("rel=icon sizes=180x180") ||
                Match.toLowerCase().contains("rel=\"icon\" sizes=\"160x160\"") ||
                Match.toLowerCase().contains("rel='icon' sizes='160x160'") ||
                Match.toLowerCase().contains("rel=icon sizes=160x160") ||
                Match.toLowerCase().contains("rel=\"shortcut icon\" sizes=\"192x192\"") ||
                Match.toLowerCase().contains("rel='shortcut icon' sizes='192x192'") ||
                Match.toLowerCase().contains("rel=\"shortcut icon\" sizes=\"180x180\"") ||
                Match.toLowerCase().contains("rel='shortcut icon' sizes='180x180'") ||
                Match.toLowerCase().contains("rel=\"shortcut icon\" sizes=\"160x160\"") ||
                Match.toLowerCase().contains("rel='shortcut icon' sizes='160x160'"))
            {
                String Result = PregMatch(Match, "content=\"(.*?)\"", 1);

                if (Result.equals(""))
                    Result = PregMatch(Match, "content='(.*?)'", 1);

                MetaTags.put("image", Result);
                break;
            }
        }

        return MetaTags;
    }

    private String PregMatch(String Content, String pattern, int Index)
    {
        String Match = "";
        Matcher matcher = Pattern.compile(pattern).matcher(Content);

        if (matcher.find())
            Match = matcher.group(Index);

        return ExtendedTrim(Match);
    }

    private List<String> PregMatchAll(String Content, String pattern, int Index)
    {
        List<String> MatchList = new ArrayList<>();
        Matcher matcher = Pattern.compile(pattern).matcher(Content);

        while (matcher.find())
        {
            MatchList.add(ExtendedTrim(matcher.group(Index)));
        }

        return MatchList;
    }
}
