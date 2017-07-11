package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.RecyclerViewScroll;

public class SearchFragment extends Fragment
{
    private final List<PeopleStruct> PeopleList = new ArrayList<>();
    private final List<TagStruct> TagList = new ArrayList<>();
    private AdapterPeople PeopleAdapter;
    private AdapterTag TagAdapter;
    private int Tab = 0;

    private RecyclerView RecyclerViewMain;
    private TextView TextViewTabPeople;
    private View ViewLinePeople;
    private TextView TextViewTabTag;
    private View ViewLineTag;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        LinearLayout LinearLayoutHeader = new LinearLayout(context);
        LinearLayoutHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        LinearLayoutHeader.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(LinearLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getActivity().onBackPressed(); } });

        LinearLayoutHeader.addView(ImageViewBack);

        EditText EditTextSearch = new EditText(context);
        EditTextSearch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        EditTextSearch.setHint(R.string.SearchFragmentEditTextSearchHint);
        EditTextSearch.setBackground(null);
        EditTextSearch.addTextChangedListener(new TextWatcher()
        {
            @Override public void afterTextChanged(Editable s) { }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 0)
                {
                    if (Tab == 1)
                        SearchPeople(context, s);
                    else
                        SearchTag(context, s);
                }
            }
        });

        LinearLayoutHeader.addView(EditTextSearch);

        RelativeLayout.LayoutParams LinearLayoutTabParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LinearLayoutTabParam.addRule(RelativeLayout.BELOW, LinearLayoutHeader.getId());

        LinearLayout LinearLayoutTab = new LinearLayout(context);
        LinearLayoutTab.setLayoutParams(LinearLayoutTabParam);
        LinearLayoutTab.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutTab.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(LinearLayoutTab);

        RelativeLayout RelativeLayoutTabPeople = new RelativeLayout(context);
        RelativeLayoutTabPeople.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56), 1.0f));

        LinearLayoutTab.addView(RelativeLayoutTabPeople);

        TextViewTabPeople = new TextView(context);
        TextViewTabPeople.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewTabPeople.setText(getString(R.string.SearchFragmentTabPeople));
        TextViewTabPeople.setGravity(Gravity.CENTER);
        TextViewTabPeople.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));
        TextViewTabPeople.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTabPeople.setTypeface(null, Typeface.BOLD);

        RelativeLayoutTabPeople.addView(TextViewTabPeople);

        RelativeLayout.LayoutParams ViewLinePeopleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 2));
        ViewLinePeopleParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        ViewLinePeople = new View(context);
        ViewLinePeople.setLayoutParams(ViewLinePeopleParam);
        ViewLinePeople.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabPeople.addView(ViewLinePeople);

        RelativeLayout RelativeLayoutTabTag = new RelativeLayout(context);
        RelativeLayoutTabTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56), 1.0f));

        LinearLayoutTab.addView(RelativeLayoutTabTag);

        TextViewTabTag = new TextView(context);
        TextViewTabTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewTabTag.setText(getString(R.string.SearchFragmentTabTag));
        TextViewTabTag.setGravity(Gravity.CENTER);
        TextViewTabTag.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));
        TextViewTabTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTabTag.setTypeface(null, Typeface.BOLD);

        RelativeLayoutTabTag.addView(TextViewTabTag);

        RelativeLayout.LayoutParams ViewLineTagParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 2));
        ViewLineTagParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        ViewLineTag = new View(context);
        ViewLineTag.setLayoutParams(ViewLineTagParam);
        ViewLineTag.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabTag.addView(ViewLineTag);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, LinearLayoutTab.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(context);

        RecyclerViewMain = new RecyclerView(context);
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.addOnScrollListener(new RecyclerViewScroll(LinearLayoutManagerMain)
        {
            @Override
            public void OnLoadMore()
            {

            }
        });

        RelativeLayoutMain.addView(RecyclerViewMain);

        ChangeTab(context, 1);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("SearchFragment");
    }

    private void SearchTag(Context context, CharSequence text)
    {
        if (text.length() == 0)
            return;

        AndroidNetworking.post(MiscHandler.GetRandomServer("SearchTag"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Tag", text.toString())
        .setTag("SearchFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {MiscHandler.Debug(Response);
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));
                        PeopleList.clear();

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            JSONObject Comment = ResultList.getJSONObject(I);

                            PeopleStruct People = new PeopleStruct();
                            People.Username = Comment.getString("Username");
                            People.Avatar = Comment.getString("Avatar");
                            People.Follower = Comment.getInt("Follower");

                            PeopleList.add(People);
                        }

                        PeopleAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("SearchFragment-SearchPeople: " + e.toString());
                }
            }

            @Override
            public void onError(ANError anError)
            {

            }
        });
    }

    private void SearchPeople(Context context, CharSequence text)
    {
        if (text.length() == 0)
            return;

        AndroidNetworking.post(MiscHandler.GetRandomServer("SearchPeople"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Name", text.toString())
        .setTag("SearchFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {MiscHandler.Debug(Response);
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));
                        PeopleList.clear();

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            JSONObject Comment = ResultList.getJSONObject(I);

                            PeopleStruct People = new PeopleStruct();
                            People.Username = Comment.getString("Username");
                            People.Avatar = Comment.getString("Avatar");
                            People.Follower = Comment.getInt("Follower");

                            PeopleList.add(People);
                        }

                        PeopleAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("SearchFragment-SearchPeople: " + e.toString());
                }
            }

            @Override
            public void onError(ANError anError)
            {

            }
        });
    }

    private void ChangeTab(Context context, int tab)
    {
        TextViewTabPeople.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewLinePeople.setBackgroundResource(R.color.White);
        TextViewTabTag.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewLineTag.setBackgroundResource(R.color.White);

        Tab = tab;
        PeopleList.clear();
        PeopleAdapter = new AdapterPeople(context);

        switch (tab)
        {
            case 1:
                RecyclerViewMain.setAdapter(PeopleAdapter);
                TextViewTabPeople.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewLinePeople.setBackgroundResource(R.color.BlueLight);
            break;
            case 2:
                RecyclerViewMain.setAdapter(PeopleAdapter);
                TextViewTabTag.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewLineTag.setBackgroundResource(R.color.BlueLight);
            break;
        }
    }

    private class AdapterPeople extends RecyclerView.Adapter<AdapterPeople.ViewHolderComment>
    {
        private final int ID_Main = MiscHandler.GenerateViewID();
        private final int ID_Profile = MiscHandler.GenerateViewID();
        private final int ID_Username = MiscHandler.GenerateViewID();
        private final int ID_Follower = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterPeople(Context c)
        {
            context = c;
        }

        class ViewHolderComment extends RecyclerView.ViewHolder
        {
            private RelativeLayout RelativeLayoutMain;
            private ImageViewCircle ImageViewCircleProfile;
            private TextView TextViewUsername;
            private TextView TextViewFollower;

            ViewHolderComment(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    RelativeLayoutMain = (RelativeLayout) view.findViewById(ID_Main);
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(ID_Profile);
                    TextViewUsername = (TextView) view.findViewById(ID_Username);
                    TextViewFollower = (TextView) view.findViewById(ID_Follower);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            if (getItemViewType(position) != 0)
                return;

            final int Position = Holder.getAdapterPosition();

            Holder.RelativeLayoutMain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", PeopleList.get(Position).Username);

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Glide.with(context)
                    .load(PeopleList.get(Position).Avatar)
                    .placeholder(R.color.BlueGray)
                    .dontAnimate()
                    .into(Holder.ImageViewCircleProfile);

            Holder.ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", PeopleList.get(Position).Username);

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Holder.TextViewUsername.setText(PeopleList.get(Position).Username);
            Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", PeopleList.get(Position).Username);

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            String Follower;

            if (PeopleList.get(Position).Follower == 0)
                Follower = context.getString(R.string.SearchFragmentNoFollower);
            else
                Follower = String.valueOf(PeopleList.get(Position).Follower);

            Follower += " " + context.getString(R.string.SearchFragmentFollowers);

            Holder.TextViewFollower.setText(Follower);
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayoutMain.setId(ID_Main);

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(ID_Profile);

                RelativeLayoutMain.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID_Profile);

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(TextViewUsernameParam);
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTypeface(null, Typeface.BOLD);
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(ID_Username);

                RelativeLayoutMain.addView(TextViewUsername);

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.addRule(RelativeLayout.BELOW, ID_Username);
                TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID_Profile);

                TextView TextViewFollow = new TextView(context);
                TextViewFollow.setLayoutParams(TextViewMessageParam);
                TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                TextViewFollow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewFollow.setId(ID_Follower);

                RelativeLayoutMain.addView(TextViewFollow);

                return new ViewHolderComment(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new LoadingView.LayoutParams(LoadingView.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            LoadingViewMain.Start();

            return new ViewHolderComment(LoadingViewMain, false);
        }

        @Override
        public int getItemCount()
        {
            return PeopleList.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            return PeopleList.get(position) == null ? 1 : 0;
        }
    }

    private class AdapterTag extends RecyclerView.Adapter<AdapterTag.ViewHolderComment>
    {
        private final int ID_MAIN = MiscHandler.GenerateViewID();
        private final int ID_SHARP = MiscHandler.GenerateViewID();
        private final int ID_TAG = MiscHandler.GenerateViewID();
        private final int ID_POST = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterTag(Context c)
        {
            context = c;
        }

        class ViewHolderComment extends RecyclerView.ViewHolder
        {
            private RelativeLayout RelativeLayoutMain;
            private TextView TextViewSharp;
            private TextView TextViewTag;
            private TextView TextViewPost;

            ViewHolderComment(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    RelativeLayoutMain = (RelativeLayout) view.findViewById(ID_MAIN);
                    TextViewSharp = (TextView) view.findViewById(ID_SHARP);
                    TextViewTag = (TextView) view.findViewById(ID_TAG);
                    TextViewPost = (TextView) view.findViewById(ID_POST);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            if (getItemViewType(position) != 0)
                return;

            final int Position = Holder.getAdapterPosition();

            if (new Bidi(TagList.get(Position).Tag, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
            {
                // English
            }
            else
            {
                // Farsi
            }
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayoutMain.setId(ID_MAIN);

                RelativeLayout.LayoutParams TextViewSharpParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
                TextViewSharpParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                TextView TextViewSharp = new TextView(context);
                TextViewSharp.setLayoutParams(TextViewSharpParam);
                TextViewSharp.setText("#");
                TextViewSharp.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                TextViewSharp.setId(ID_SHARP);
                TextViewSharp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

                RelativeLayoutMain.addView(TextViewSharp);

                RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID_SHARP);

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(TextViewUsernameParam);
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTypeface(null, Typeface.BOLD);
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(ID_TAG);

                RelativeLayoutMain.addView(TextViewUsername);

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.addRule(RelativeLayout.BELOW, ID_TAG);
                TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID_SHARP);

                TextView TextViewFollow = new TextView(context);
                TextViewFollow.setLayoutParams(TextViewMessageParam);
                TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                TextViewFollow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewFollow.setId(ID_POST);

                RelativeLayoutMain.addView(TextViewFollow);

                return new ViewHolderComment(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new LoadingView.LayoutParams(LoadingView.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            LoadingViewMain.Start();

            return new ViewHolderComment(LoadingViewMain, false);
        }

        @Override
        public int getItemCount()
        {
            return PeopleList.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            return PeopleList.get(position) == null ? 1 : 0;
        }
    }

    private class TagStruct
    {
        String Tag;
        int Post;
    }

    private class PeopleStruct
    {
        String Avatar;
        String Username;
        int Follower;
    }
}
