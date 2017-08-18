package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    private String Search = "";

    private RecyclerViewScroll RecyclerViewScrollMain;

    private TextView TextViewTabPeople;
    private View ViewLinePeople;
    private TextView TextViewTabTag;
    private View ViewLineTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();
        final RecyclerView RecyclerViewMain = new RecyclerView(context);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewMainParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        final LoadingView LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);
        LoadingViewMain.setId(MiscHandler.GenerateViewID());
        LoadingViewMain.SetColor(R.color.BlueLight);
        LoadingViewMain.SetSize(5);

        RelativeLayoutHeader.addView(LoadingViewMain);

        RelativeLayout.LayoutParams EditTextSearchParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        EditTextSearchParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        EditTextSearchParam.addRule(RelativeLayout.LEFT_OF, LoadingViewMain.getId());

        final EditText EditTextSearch = new EditText(context);
        EditTextSearch.setLayoutParams(EditTextSearchParam);
        EditTextSearch.setHint(R.string.SearchFragmentEditTextSearchHint);
        EditTextSearch.setBackground(null);
        EditTextSearch.requestFocus();
        EditTextSearch.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
        EditTextSearch.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        EditTextSearch.setHintTextColor(ContextCompat.getColor(context, R.color.Gray2));
        EditTextSearch.addTextChangedListener(new TextWatcher()
        {
            @Override public void afterTextChanged(Editable s) { }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                TagList.clear();
                PeopleList.clear();
                AndroidNetworking.forceCancel("SearchFragment");

                if (s.length() <= 0)
                    return;

                Search = s.toString();
                LoadingViewMain.Start();
                RecyclerViewScrollMain.ResetLoading(true);

                switch (Tab)
                {
                    case 1: SearchPeople(context, s, LoadingViewMain); break;
                    case 2: SearchTag(context, s, LoadingViewMain);    break;
                }
            }
        });

        RelativeLayoutHeader.addView(EditTextSearch);

        RelativeLayout.LayoutParams LinearLayoutTabParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LinearLayoutTabParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        LinearLayout LinearLayoutTab = new LinearLayout(context);
        LinearLayoutTab.setLayoutParams(LinearLayoutTabParam);
        LinearLayoutTab.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutTab.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(LinearLayoutTab);

        RelativeLayout RelativeLayoutTabPeople = new RelativeLayout(context);
        RelativeLayoutTabPeople.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56), 1.0f));
        RelativeLayoutTabPeople.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditTextSearch.setText("");
                ChangeTab(context, 1, RecyclerViewMain);
            }
        });

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

        RelativeLayoutTabPeople.addView(ViewLinePeople);

        RelativeLayout RelativeLayoutTabTag = new RelativeLayout(context);
        RelativeLayoutTabTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56), 1.0f));
        RelativeLayoutTabTag.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditTextSearch.setText("");
                ChangeTab(context, 2, RecyclerViewMain);
            }
        });

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

        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.addOnScrollListener(RecyclerViewScrollMain = new RecyclerViewScroll(LinearLayoutManagerMain)
        {
            @Override
            public void OnLoadMore()
            {
                LoadingViewMain.Start();

                switch (Tab)
                {
                    case 1: SearchPeople(context, Search, LoadingViewMain); break;
                    case 2: SearchTag(context, Search, LoadingViewMain);    break;
                }
            }
        });

        RelativeLayoutMain.addView(RecyclerViewMain);

        ChangeTab(context, 1, RecyclerViewMain);

        return RelativeLayoutMain;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        MiscHandler.HideSoftKey(getActivity());
        AndroidNetworking.forceCancel("SearchFragment");
    }

    private void SearchPeople(final Context context, CharSequence text, final LoadingView LoadingViewMain)
    {
        AndroidNetworking.post(MiscHandler.GetRandomServer("SearchPeople"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Skip", String.valueOf(PeopleList.size()))
        .addBodyParameter("Name", text.toString())
        .setTag("SearchFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            JSONObject PeopleObject = ResultList.getJSONObject(I);

                            PeopleStruct People = new PeopleStruct();
                            People.Username = PeopleObject.getString("Username");
                            People.Avatar = PeopleObject.getString("Avatar");
                            People.Follower = PeopleObject.getInt("Follower");

                            PeopleList.add(People);
                        }

                        PeopleAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    RecyclerViewScrollMain.ResetLoading(false);
                    MiscHandler.Debug("SearchFragment-SearchPeople: " + e.toString());
                }

                LoadingViewMain.Stop();
            }

            @Override
            public void onError(ANError anError)
            {
                LoadingViewMain.Stop();
                RecyclerViewScrollMain.ResetLoading(false);
                MiscHandler.Toast(context, getString(R.string.NoInternet));
            }
        });
    }

    private void SearchTag(final Context context, CharSequence text, final LoadingView LoadingViewMain)
    {
        AndroidNetworking.post(MiscHandler.GetRandomServer("SearchTag"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Skip", String.valueOf(TagList.size() - 1))
        .addBodyParameter("Tag", text.toString())
        .setTag("SearchFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            JSONObject TagObject = ResultList.getJSONObject(I);

                            TagStruct tag = new TagStruct();
                            tag.Tag = TagObject.getString("Tag");
                            tag.Count = TagObject.getInt("Count");

                            TagList.add(tag);
                        }

                        TagAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    RecyclerViewScrollMain.ResetLoading(false);
                    MiscHandler.Debug("SearchFragment-SearchTag: " + e.toString());
                }

                LoadingViewMain.Stop();
            }

            @Override
            public void onError(ANError anError)
            {
                LoadingViewMain.Stop();
                RecyclerViewScrollMain.ResetLoading(false);
                MiscHandler.Toast(context, getString(R.string.NoInternet));
            }
        });
    }

    private void ChangeTab(Context context, int tab, RecyclerView RecyclerViewMain)
    {
        TextViewTabPeople.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewLinePeople.setBackgroundResource(R.color.White);
        TextViewTabTag.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewLineTag.setBackgroundResource(R.color.White);

        Tab = tab;
        TagList.clear();
        PeopleList.clear();

        switch (tab)
        {
            case 1:
                PeopleAdapter = new AdapterPeople(context);
                RecyclerViewMain.setAdapter(PeopleAdapter);
                TextViewTabPeople.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewLinePeople.setBackgroundResource(R.color.BlueLight);
            break;
            case 2:
                TagAdapter = new AdapterTag(context);
                RecyclerViewMain.setAdapter(TagAdapter);
                TextViewTabTag.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewLineTag.setBackgroundResource(R.color.BlueLight);
            break;
        }
    }

    private class AdapterPeople extends RecyclerView.Adapter<AdapterPeople.ViewHolderComment>
    {
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
            private final ImageViewCircle ImageViewCircleProfile;
            private final TextView TextViewUsername;
            private final TextView TextViewFollower;

            ViewHolderComment(View view)
            {
                super(view);
                ImageViewCircleProfile = (ImageViewCircle) view.findViewById(ID_Profile);
                TextViewUsername = (TextView) view.findViewById(ID_Username);
                TextViewFollower = (TextView) view.findViewById(ID_Follower);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            final int Position = Holder.getAdapterPosition();

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

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("FragmentProfile").commit();
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

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            String Follower;

            if (PeopleList.get(Position).Follower == 0)
                Follower = context.getString(R.string.SearchFragmentNo);
            else
                Follower = String.valueOf(PeopleList.get(Position).Follower);

            Follower += " " + context.getString(R.string.SearchFragmentFollowers);

            Holder.TextViewFollower.setText(Follower);
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 40), MiscHandler.ToDimension(context, 40));
            ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

            ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
            ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
            ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
            ImageViewCircleProfile.setImageResource(R.color.BlueGray);
            ImageViewCircleProfile.setId(ID_Profile);

            RelativeLayoutMain.addView(ImageViewCircleProfile);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 8), 0, 0);
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

            return new ViewHolderComment(RelativeLayoutMain);
        }

        @Override
        public int getItemCount()
        {
            return PeopleList.size();
        }
    }

    private class AdapterTag extends RecyclerView.Adapter<AdapterTag.ViewHolderComment>
    {
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
            private final TextView TextViewSharp;
            private final TextView TextViewTag;
            private final TextView TextViewPost;

            ViewHolderComment(View view)
            {
                super(view);
                TextViewSharp = (TextView) view.findViewById(ID_SHARP);
                TextViewTag = (TextView) view.findViewById(ID_TAG);
                TextViewPost = (TextView) view.findViewById(ID_POST);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            final int Position = Holder.getAdapterPosition();

            Holder.TextViewSharp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Tag", TagList.get(Position).Tag);

                    Fragment fragment = new TagFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("TagFragment").commit();
                }
            });

            Holder.TextViewTag.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Tag", TagList.get(Position).Tag);

                    Fragment fragment = new TagFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("TagFragment").commit();
                }
            });

            Holder.TextViewTag.setText(TagList.get(Position).Tag);

            String Post;

            if (TagList.get(Position).Count == 0)
                Post = context.getString(R.string.SearchFragmentNo);
            else
                Post = String.valueOf(TagList.get(Position).Count);

            Post += " " + context.getString(R.string.SearchFragmentPosts);

            Holder.TextViewPost.setText(Post);
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            RelativeLayout.LayoutParams TextViewSharpParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,  MiscHandler.ToDimension(context, 50));
            TextViewSharpParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));

            TextView TextViewSharp = new TextView(context);
            TextViewSharp.setLayoutParams(TextViewSharpParam);
            TextViewSharp.setPadding(MiscHandler.ToDimension(context, 20), 0, MiscHandler.ToDimension(context, 20), 0);
            TextViewSharp.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewSharp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            TextViewSharp.setId(ID_SHARP);
            TextViewSharp.setText("#");

            RelativeLayoutMain.addView(TextViewSharp);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 20), 0, 0);
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

            TextView TextViewPost = new TextView(context);
            TextViewPost.setLayoutParams(TextViewMessageParam);
            TextViewPost.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPost.setId(ID_POST);

            RelativeLayoutMain.addView(TextViewPost);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, ID_SHARP);

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray);

            RelativeLayoutMain.addView(ViewLine);

            return new ViewHolderComment(RelativeLayoutMain);
        }

        @Override
        public int getItemCount()
        {
            return TagList.size();
        }
    }

    private class TagStruct
    {
        String Tag;
        int Count;
    }

    private class PeopleStruct
    {
        String Avatar;
        String Username;
        int Follower;
    }
}
