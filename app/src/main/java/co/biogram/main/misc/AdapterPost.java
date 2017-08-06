package co.biogram.main.misc;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
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
import co.biogram.main.fragment.FragmentComment;
import co.biogram.main.fragment.FragmentImagePreview;
import co.biogram.main.fragment.FragmentLike;
import co.biogram.main.fragment.FragmentPostDetails;
import co.biogram.main.fragment.ProfileFragment;
import co.biogram.main.fragment.VideoPreviewFragment;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.TagHandler;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.ViewHolderPost>
{
    private final String Tag;
    private final FragmentActivity Activity;
    private List<Struct> PostList = new ArrayList<>();

    public AdapterPost(FragmentActivity activity, List<Struct> list, String tag)
    {
        Activity = activity;
        PostList = list;
        Tag = tag;
    }

    class ViewHolderPost extends RecyclerView.ViewHolder
    {
        RelativeLayout RelativeLayoutRoot;
        ImageViewCircle ImageViewCircleProfile;
        TextView TextViewUsername;
        TextView TextViewTime;
        ImageView ImageViewOption;
        RelativeLayout RelativeLayoutMessage;
        TextView TextViewMessage;
        LinearLayout LinearLayoutImageContent1;
        ImageView ImageViewSingle;
        LinearLayout LinearLayoutImageContent2;
        ImageView ImageViewDouble1;
        ImageView ImageViewDouble2;
        LinearLayout LinearLayoutImageContent3;
        ImageView ImageViewTriple1;
        ImageView ImageViewTriple2;
        ImageView ImageViewTriple3;
        RelativeLayout RelativeLayoutLink;
        LoadingView LoadingViewLink;
        ImageView ImageViewFav;
        TextView TextViewTitle;
        TextView TextViewDescription;
        TextView TextViewTry;
        FrameLayout FrameLayoutVideo;
        ImageView ImageViewVideo;
        ImageView ImageViewLike;
        TextView TextViewLikeCount;
        ImageView ImageViewComment;
        TextView TextViewCommentCount;
        ImageView ImageViewShare;
        View ViewLine;

        ViewHolderPost(View view, boolean Content)
        {
            super(view);

            if (Content)
            {
                RelativeLayoutRoot = (RelativeLayout) view.findViewById(R.id.RelativeLayoutRoot);
                ImageViewCircleProfile = (ImageViewCircle) view.findViewById(R.id.ImageViewCircleProfile);
                TextViewUsername = (TextView) view.findViewById(R.id.TextViewUsername);
                TextViewTime = (TextView) view.findViewById(R.id.TextViewTime);
                ImageViewOption = (ImageView) view.findViewById(R.id.ImageViewOption);
                RelativeLayoutMessage = (RelativeLayout) view.findViewById(R.id.RelativeLayoutMessage);
                TextViewMessage = (TextView) view.findViewById(R.id.TextViewMessage);
                LinearLayoutImageContent1 = (LinearLayout) view.findViewById(R.id.LinearLayoutImageContent1);
                ImageViewSingle = (ImageView) view.findViewById(R.id.ImageViewSingle);
                LinearLayoutImageContent2 = (LinearLayout) view.findViewById(R.id.LinearLayoutImageContent2);
                ImageViewDouble1 = (ImageView) view.findViewById(R.id.ImageViewDouble1);
                ImageViewDouble2 = (ImageView) view.findViewById(R.id.ImageViewDouble2);
                LinearLayoutImageContent3 = (LinearLayout) view.findViewById(R.id.LinearLayoutImageContent3);
                ImageViewTriple1 = (ImageView) view.findViewById(R.id.ImageViewTriple1);
                ImageViewTriple2 = (ImageView) view.findViewById(R.id.ImageViewTriple2);
                ImageViewTriple3 = (ImageView) view.findViewById(R.id.ImageViewTriple3);
                RelativeLayoutLink = (RelativeLayout) view.findViewById(R.id.RelativeLayoutLink);
                LoadingViewLink = (LoadingView) view.findViewById(R.id.LoadingViewLink);
                ImageViewFav = (ImageView) view.findViewById(R.id.ImageViewFav);
                TextViewTitle = (TextView) view.findViewById(R.id.TextViewTitle);
                TextViewDescription = (TextView) view.findViewById(R.id.TextViewDescription);
                TextViewTry = (TextView) view.findViewById(R.id.TextViewTry);
                FrameLayoutVideo = (FrameLayout) view.findViewById(R.id.FrameLayoutVideo);
                ImageViewVideo = (ImageView) view.findViewById(R.id.ImageViewVideo);
                ImageViewLike = (ImageView) view.findViewById(R.id.ImageViewLike);
                TextViewLikeCount = (TextView) view.findViewById(R.id.TextViewLikeCount);
                ImageViewComment = (ImageView) view.findViewById(R.id.ImageViewComment);
                TextViewCommentCount = (TextView) view.findViewById(R.id.TextViewCommentCount);
                ImageViewShare = (ImageView) view.findViewById(R.id.ImageViewShare);
                ViewLine = view.findViewById(R.id.ViewBlankLine);
            }
        }
    }

    @Override
    public ViewHolderPost onCreateViewHolder(ViewGroup Parent, int ViewType)
    {
        Context context = Parent.getContext();

        if (ViewType == 0)
        {
            View ItemView = LayoutInflater.from(context).inflate(R.layout.general_adapter_post_row, Parent, false);
            return new ViewHolderPost(ItemView, true);
        }
        else if (ViewType == 1)
        {
            LoadingView Root = new LoadingView(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            Root.Start();

            return new ViewHolderPost(Root, false);
        }
        else
        {
            TextView Root = new TextView(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            Root.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
            Root.setText(context.getString(R.string.AdapterPostNoContent));
            Root.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            Root.setTypeface(null, Typeface.BOLD);
            Root.setGravity(Gravity.CENTER);

            return new ViewHolderPost(Root, false);
        }
    }

    @Override
    public int getItemCount()
    {
        return PostList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return PostList.get(position) != null ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(final ViewHolderPost Holder, int position)
    {
        if (getItemViewType(position) != 0)
            return;

        final int Position = Holder.getAdapterPosition();

        Holder.RelativeLayoutRoot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("PostID", PostList.get(Position).PostID);

                Fragment fragment = new FragmentPostDetails();
                fragment.setArguments(bundle);

                Activity.getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentPostDetails").commit();
            }
        });

        Glide.with(Activity)
        .load(PostList.get(Position).Avatar)
        .placeholder(R.color.BlueGray)
        .override(MiscHandler.ToDimension(Activity, 55), MiscHandler.ToDimension(Activity, 55))
        .dontAnimate()
        .into(Holder.ImageViewCircleProfile);

        Holder.ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Username", PostList.get(Position).Username);

                Fragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);

                Activity.getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
            }
        });

        Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Username", PostList.get(Position).Username);

                Fragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);

                Activity.getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
            }
        });

        Holder.TextViewUsername.setText(PostList.get(Position).Username);
        Holder.TextViewTime.setText(MiscHandler.GetTimeName(PostList.get(Position).Time));
        Holder.ImageViewOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog DialogOption = new Dialog(Activity);
                DialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogOption.setCancelable(true);

                LinearLayout Root = new LinearLayout(Activity);
                Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Root.setBackgroundResource(R.color.White);
                Root.setOrientation(LinearLayout.VERTICAL);

                final TextView Follow = new TextView(Activity);
                Follow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Follow.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                Follow.setPadding(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15));
                Follow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Follow.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("Follow"))
                        .addBodyParameter("Username", PostList.get(Position).Username)
                        .addHeaders("TOKEN", SharedHandler.GetString(Activity, "TOKEN"))
                        .setTag("FragmentFollowers")
                        .build().getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    JSONObject Result = new JSONObject(Response);

                                    if (Result.getInt("Message") == 1000)
                                    {
                                        if (Result.getBoolean("Follow"))
                                        {
                                            PostList.get(Position).FollowReverse();
                                            Follow.setText(Activity.getString(R.string.AdapterPostUnFollow));
                                        }
                                        else
                                        {
                                            PostList.get(Position).FollowReverse();
                                            Follow.setText(Activity.getString(R.string.AdapterPostFollow));
                                        }

                                        MiscHandler.Toast(Activity, Activity.getString(R.string.AdapterPostFollowSent));
                                    }
                                }
                                catch (Exception e)
                                {
                                    // Leave Me Alone
                                }
                            }

                            @Override
                            public void onError(ANError anError)
                            {
                                MiscHandler.Toast(Activity, Activity.getString(R.string.NoInternet));
                            }
                        });

                        DialogOption.dismiss();
                    }
                });

                if (PostList.get(Position).Follow)
                    Follow.setText(Activity.getString(R.string.AdapterPostUnFollow));
                else
                    Follow.setText(Activity.getString(R.string.AdapterPostFollow));

                Root.addView(Follow);

                View FollowLine = new View(Activity);
                FollowLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 1)));
                FollowLine.setBackgroundResource(R.color.Gray1);

                Root.addView(FollowLine);

                final TextView Turn = new TextView(Activity);
                Turn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Turn.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                Turn.setVisibility(View.GONE);
                Turn.setPadding(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15));
                Turn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Turn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("PostTurnComment"))
                        .addBodyParameter("PostID", PostList.get(Position).PostID)
                        .addHeaders("TOKEN", SharedHandler.GetString(Activity, "TOKEN"))
                        .setTag(Tag).build().getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    if (new JSONObject(Response).getInt("Message") == 1000)
                                    {
                                        PostList.get(Position).CommentReverse();

                                        if (PostList.get(Position).Comment)
                                            Turn.setText(Activity.getString(R.string.AdapterPostTurnOff));
                                        else
                                            Turn.setText(Activity.getString(R.string.AdapterPostTurnOn));

                                        MiscHandler.Toast(Activity, Activity.getString(R.string.AdapterPostCommentSent));
                                    }
                                }
                                catch (Exception e)
                                {
                                    // Leave Me Alone
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                if (PostList.get(Position).Comment)
                    Turn.setText(Activity.getString(R.string.AdapterPostTurnOff));
                else
                    Turn.setText(Activity.getString(R.string.AdapterPostTurnOn));

                Root.addView(Turn);

                View TurnLine = new View(Activity);
                TurnLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 1)));
                TurnLine.setBackgroundResource(R.color.Gray1);
                TurnLine.setVisibility(View.GONE);

                Root.addView(TurnLine);

                TextView Copy = new TextView(Activity);
                Copy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Copy.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                Copy.setText(Activity.getString(R.string.AdapterPostCopy));
                Copy.setPadding(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15));
                Copy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Copy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager ClipBoard = (ClipboardManager) Activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData Clip = ClipData.newPlainText(PostList.get(Position).PostID, PostList.get(Position).Message);
                        ClipBoard.setPrimaryClip(Clip);

                        MiscHandler.Toast(Activity, Activity.getString(R.string.AdapterPostClipboard));
                        DialogOption.dismiss();
                    }
                });

                Root.addView(Copy);

                View CopyLine = new View(Activity);
                CopyLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 1)));
                CopyLine.setBackgroundResource(R.color.Gray1);

                Root.addView(CopyLine);

                final TextView BookMark = new TextView(Activity);
                BookMark.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                BookMark.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                BookMark.setPadding(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15));
                BookMark.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                BookMark.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("PostBookMark"))
                        .addBodyParameter("PostID", PostList.get(Position).PostID)
                        .addHeaders("TOKEN", SharedHandler.GetString(Activity, "TOKEN"))
                        .setTag(Tag).build().getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    if (new JSONObject(Response).getInt("Message") == 1000)
                                    {
                                        PostList.get(Position).BookMarkReverse();

                                        if (PostList.get(Position).BookMark)
                                            BookMark.setText(Activity.getString(R.string.AdapterPostUnBookMark));
                                        else
                                            BookMark.setText(Activity.getString(R.string.AdapterPostBookMark));

                                        MiscHandler.Toast(Activity, Activity.getString(R.string.AdapterPostBookMarkSent));
                                    }
                                }
                                catch (Exception e)
                                {
                                    // Leave Me Alone
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                if (PostList.get(Position).BookMark)
                    BookMark.setText(Activity.getString(R.string.AdapterPostUnBookMark));
                else
                    BookMark.setText(Activity.getString(R.string.AdapterPostBookMark));

                Root.addView(BookMark);

                View BookMarkLine = new View(Activity);
                BookMarkLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 1)));
                BookMarkLine.setBackgroundResource(R.color.Gray1);

                Root.addView(BookMarkLine);

                TextView Delete = new TextView(Activity);
                Delete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Delete.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                Delete.setText(Activity.getString(R.string.AdapterPostDelete));
                Delete.setVisibility(View.GONE);
                Delete.setPadding(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15));
                Delete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("PostDelete"))
                        .addBodyParameter("PostID", PostList.get(Position).PostID)
                        .addHeaders("TOKEN", SharedHandler.GetString(Activity, "TOKEN"))
                        .setTag(Tag).build().getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    if (new JSONObject(Response).getInt("Message") == 1000)
                                    {
                                        PostList.remove(Position);
                                        notifyDataSetChanged();
                                    }
                                }
                                catch (Exception e)
                                {
                                    // Leave Me Alone
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                Root.addView(Delete);

                View DeleteLine = new View(Activity);
                DeleteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 1)));
                DeleteLine.setBackgroundResource(R.color.Gray1);
                DeleteLine.setVisibility(View.GONE);

                Root.addView(DeleteLine);

                TextView Report = new TextView(Activity);
                Report.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Report.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                Report.setText(Activity.getString(R.string.AdapterPostReport));
                Report.setPadding(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 15));
                Report.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Report.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("PostReport"))
                        .addBodyParameter("PostID", PostList.get(Position).PostID)
                        .addHeaders("TOKEN", SharedHandler.GetString(Activity, "TOKEN"))
                        .setTag(Tag).build().getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    if (new JSONObject(Response).getInt("Message") == 1000)
                                        MiscHandler.Toast(Activity, Activity.getString(R.string.AdapterPostReportSent));
                                }
                                catch (Exception e)
                                {
                                    // Leave Me Alone
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                Root.addView(Report);

                if (PostList.get(Position).OwnerID.equals(SharedHandler.GetString(Activity, "ID")))
                {
                    Follow.setVisibility(View.GONE);
                    FollowLine.setVisibility(View.GONE);

                    Turn.setVisibility(View.VISIBLE);
                    TurnLine.setVisibility(View.VISIBLE);

                    Delete.setVisibility(View.VISIBLE);
                    DeleteLine.setVisibility(View.VISIBLE);

                    Report.setVisibility(View.GONE);
                }

                DialogOption.setContentView(Root);
                DialogOption.show();
            }
        });

        Holder.RelativeLayoutMessage.setVisibility(View.GONE);

        if (PostList.get(Position).Message != null && !PostList.get(Position).Message.equals(""))
        {
            Holder.RelativeLayoutMessage.setVisibility(View.VISIBLE);
            Holder.TextViewMessage.setText(PostList.get(Position).Message);

            new TagHandler(Holder.TextViewMessage, Activity);

            if (new Bidi(PostList.get(Position).Message, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
            {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Holder.TextViewMessage.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                Holder.TextViewMessage.setLayoutParams(params);
            }
            else
            {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Holder.TextViewMessage.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                Holder.TextViewMessage.setLayoutParams(params);
            }
        }

        Holder.LinearLayoutImageContent1.setVisibility(View.GONE);
        Holder.LinearLayoutImageContent2.setVisibility(View.GONE);
        Holder.LinearLayoutImageContent3.setVisibility(View.GONE);
        Holder.RelativeLayoutLink.setVisibility(View.GONE);
        Holder.FrameLayoutVideo.setVisibility(View.GONE);
        Holder.ImageViewVideo.setImageResource(R.color.BlueGray2);

        if (PostList.get(Position).Type == 1)
        {
            try
            {
                final JSONArray URL = new JSONArray(PostList.get(Position).Data);

                switch (URL.length())
                {
                    case 1:
                        Holder.LinearLayoutImageContent1.setVisibility(View.VISIBLE);
                        Holder.ImageViewSingle.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), null, null); } catch (Exception e) { /* Leave Me Alone */ } } });
                        Glide.with(Activity).load(URL.get(0).toString()).placeholder(R.color.BlueGray).dontAnimate().into(Holder.ImageViewSingle);
                        break;
                    case 2:
                        Holder.LinearLayoutImageContent2.setVisibility(View.VISIBLE);
                        Holder.ImageViewDouble1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), URL.get(1).toString(), null); } catch (Exception e) { /* Leave Me Alone */ } } });
                        Holder.ImageViewDouble2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(1).toString(), URL.get(0).toString(), null); } catch (Exception e) { /* Leave Me Alone */ } } });
                        Glide.with(Activity).load(URL.get(0).toString()).placeholder(R.color.BlueGray).dontAnimate().into(Holder.ImageViewDouble1);
                        Glide.with(Activity).load(URL.get(1).toString()).placeholder(R.color.BlueGray).dontAnimate().into(Holder.ImageViewDouble2);
                        break;
                    case 3:
                        Holder.LinearLayoutImageContent3.setVisibility(View.VISIBLE);
                        Holder.ImageViewTriple1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), URL.get(1).toString(), URL.get(2).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                        Holder.ImageViewTriple2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(1).toString(), URL.get(2).toString(), URL.get(0).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                        Holder.ImageViewTriple3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(2).toString(), URL.get(0).toString(), URL.get(1).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                        Glide.with(Activity).load(URL.get(0).toString()).placeholder(R.color.BlueGray).dontAnimate().into(Holder.ImageViewTriple1);
                        Glide.with(Activity).load(URL.get(1).toString()).placeholder(R.color.BlueGray).dontAnimate().into(Holder.ImageViewTriple2);
                        Glide.with(Activity).load(URL.get(2).toString()).placeholder(R.color.BlueGray).dontAnimate().into(Holder.ImageViewTriple3);
                        break;
                }
            }
            catch (Exception e)
            {
                // Leave Me Alone
            }
        }
        else if (PostList.get(Position).Type == 2)
        {
            try
            {
                JSONArray URL = new JSONArray(PostList.get(Position).Data);

                final String VideoUrl = URL.get(0).toString();

                Holder.FrameLayoutVideo.setVisibility(View.VISIBLE);
                Holder.ImageViewVideo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("VideoURL", VideoUrl);

                        Fragment fragment = new VideoPreviewFragment();
                        fragment.setArguments(bundle);

                        Activity.getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("VideoPreviewFragment").commit();
                    }
                });

                MiscHandler.CreateVideoThumbnail(URL.get(0).toString(), Activity, Holder.ImageViewVideo);
            }
            catch (Exception e)
            {
                // Leave Me Alone
            }
        }
        else if (PostList.get(Position).Type == 3)
        {
            GradientDrawable ShapeLink = new GradientDrawable();
            ShapeLink.setStroke(MiscHandler.ToDimension(Activity, 1), ContextCompat.getColor(Activity, R.color.BlueGray));

            Holder.RelativeLayoutLink.setBackground(ShapeLink);
            Holder.RelativeLayoutLink.setVisibility(View.VISIBLE);
            Holder.TextViewTitle.setText("");
            Holder.TextViewDescription.setText("");
            Holder.ImageViewFav.setImageDrawable(null);
            Holder.TextViewTry.setVisibility(View.GONE);
            Holder.LoadingViewLink.Start();

            try
            {
                final JSONArray URL = new JSONArray(PostList.get(Position).Data);

                Holder.RelativeLayoutLink.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL.get(0).toString()));
                            Activity.startActivity(i);
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("AdapterPost-Link: " + e.toString());
                        }
                    }
                });

                final TextCrawler Request = new TextCrawler(URL.get(0).toString(), Tag, new TextCrawler.TextCrawlerCallBack()
                {
                    @Override
                    public void OnCompleted(TextCrawler.URLContent Content)
                    {
                        if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                        {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Holder.TextViewTitle.getLayoutParams();
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            Holder.TextViewTitle.setLayoutParams(params);
                        }
                        else
                        {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Holder.TextViewTitle.getLayoutParams();
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            Holder.TextViewTitle.setLayoutParams(params);
                        }

                        if (new Bidi(Content.Description, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                        {
                            RelativeLayout.LayoutParams LeftParams = (RelativeLayout.LayoutParams) Holder.TextViewDescription.getLayoutParams();
                            LeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            Holder.TextViewDescription.setLayoutParams(LeftParams);
                        }
                        else
                        {
                            RelativeLayout.LayoutParams RightParams = (RelativeLayout.LayoutParams) Holder.TextViewDescription.getLayoutParams();
                            RightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            Holder.TextViewDescription.setLayoutParams(RightParams);
                        }

                        Holder.TextViewTitle.setText(Content.Title);
                        Holder.TextViewDescription.setText(Content.Description);
                        Holder.LoadingViewLink.Stop();

                        Holder.ImageViewFav.setVisibility(View.GONE);

                        if (Content.Image.endsWith(".jpg") || Content.Image.endsWith(".png"))
                        {
                            Holder.ImageViewFav.setVisibility(View.VISIBLE);
                            Glide.with(Activity).load(Content.Image).placeholder(R.color.BlueGray).dontAnimate().into(Holder.ImageViewFav);
                        }
                    }

                    @Override
                    public void OnFailed()
                    {
                        Holder.TextViewTitle.setText("");
                        Holder.TextViewDescription.setText("");
                        Holder.ImageViewFav.setImageURI(null);
                        Holder.LoadingViewLink.Stop();
                        Holder.TextViewTry.setVisibility(View.VISIBLE);
                    }
                });

                Holder.TextViewTry.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Request.Start();
                        Holder.LoadingViewLink.Start();
                        Holder.TextViewTry.setVisibility(View.GONE);
                    }
                });

                Request.Start();
            }
            catch (Exception e)
            {
                // Leave Me Alone
            }
        }

        if (PostList.get(Position).Like)
        {
            Holder.TextViewLikeCount.setTextColor(ContextCompat.getColor(Activity, R.color.RedLike));
            Holder.ImageViewLike.setImageResource(R.drawable.ic_like_red);
        }
        else
        {
            Holder.TextViewLikeCount.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray));
            Holder.ImageViewLike.setImageResource(R.drawable.ic_like);
        }

        Holder.ImageViewLike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (PostList.get(Position).Like)
                {
                    Holder.TextViewLikeCount.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray));
                    Holder.ImageViewLike.setImageResource(R.drawable.ic_like);

                    ObjectAnimator Fade = ObjectAnimator.ofFloat(Holder.ImageViewLike, "alpha",  0.1f, 1f);
                    Fade.setDuration(200);

                    AnimatorSet AnimationSet = new AnimatorSet();
                    AnimationSet.play(Fade);
                    AnimationSet.start();

                    PostList.get(Position).LikeDecrease();
                    PostList.get(Position).LikeReverse();

                    Holder.TextViewLikeCount.setText(String.valueOf(PostList.get(Position).LikeCount));
                }
                else
                {
                    Holder.TextViewLikeCount.setTextColor(ContextCompat.getColor(Activity, R.color.RedLike));
                    Holder.ImageViewLike.setImageResource(R.drawable.ic_like_red);

                    ObjectAnimator SizeX = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleX", 1.5f);
                    SizeX.setDuration(200);

                    ObjectAnimator SizeY = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleY", 1.5f);
                    SizeY.setDuration(200);

                    ObjectAnimator Fade = ObjectAnimator.ofFloat(Holder.ImageViewLike, "alpha",  0.1f, 1f);
                    Fade.setDuration(400);

                    ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleX", 1f);
                    SizeX2.setDuration(200);
                    SizeX2.setStartDelay(200);

                    ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleY", 1f);
                    SizeY2.setDuration(200);
                    SizeY2.setStartDelay(200);

                    AnimatorSet AnimationSet = new AnimatorSet();
                    AnimationSet.playTogether(SizeX, SizeY, Fade, SizeX2, SizeY2);
                    AnimationSet.start();

                    PostList.get(Position).LikeIncrease();
                    PostList.get(Position).LikeReverse();

                    Holder.TextViewLikeCount.setText(String.valueOf(PostList.get(Position).LikeCount));
                }

                AndroidNetworking.post(MiscHandler.GetRandomServer("PostLike"))
                .addBodyParameter("PostID", PostList.get(Position).PostID)
                .addHeaders("TOKEN", SharedHandler.GetString(Activity, "TOKEN"))
                .setTag(Tag).build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000)
                            {
                                if (Result.getBoolean("Like"))
                                    Holder.ImageViewLike.setImageResource(R.drawable.ic_like_red);
                                else
                                    Holder.ImageViewLike.setImageResource(R.drawable.ic_like);
                            }
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }
                    }

                    @Override
                    public void onError(ANError anError) { }
                });
            }
        });

        Holder.TextViewLikeCount.setText(String.valueOf(PostList.get(Position).LikeCount));
        Holder.TextViewLikeCount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("PostID", PostList.get(Position).PostID);

                Fragment fragment = new FragmentLike();
                fragment.setArguments(bundle);

                Activity.getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentLike").commit();
            }
        });

        Holder.TextViewCommentCount.setText(String.valueOf(PostList.get(Position).CommentCount));
        Holder.ImageViewComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (PostList.get(Position).Comment)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", PostList.get(Position).PostID);
                    bundle.putString("OwnerID", PostList.get(Position).OwnerID);

                    Fragment fragment = new FragmentComment();
                    fragment.setArguments(bundle);

                    Activity.getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentComment").commit();
                    return;
                }

                MiscHandler.Toast(Activity, Activity.getString(R.string.AdapterPostComment));
            }
        });

        Holder.ImageViewShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent SendIntent = new Intent();
                SendIntent.setAction(Intent.ACTION_SEND);
                SendIntent.putExtra(Intent.EXTRA_TEXT, PostList.get(Position).Message + "\n http://BioGram.Co/");
                SendIntent.setType("text/plain");
                Activity.startActivity(Intent.createChooser(SendIntent, Activity.getString(R.string.AdapterPostChoice)));
            }
        });

        if (Position == PostList.size() - 1)
            Holder.ViewLine.setVisibility(View.GONE);
        else
            Holder.ViewLine.setVisibility(View.VISIBLE);
    }

    public static class Struct
    {
        public String PostID;
        public String OwnerID;
        public int Type;
        public int Category;
        public long Time;
        public boolean Comment;
        public String Message;
        public String Data;
        public String Username;
        public String Avatar;
        public boolean Like;
        public int LikeCount;
        public int CommentCount;
        public boolean BookMark;
        public boolean Follow;

        void LikeIncrease() { LikeCount = LikeCount + 1; }
        void LikeDecrease() { LikeCount = LikeCount - 1; }
        void LikeReverse() { Like = !Like; }
        void CommentReverse() { Comment = !Comment; }
        void BookMarkReverse() { BookMark = !BookMark; }
        void FollowReverse() { Follow = !Follow; }
    }

    private void OpenPreviewImage(String URL, String URL2, String URL3)
    {
        Bundle bundle = new Bundle();
        bundle.putString("URL", URL);

        if (URL2 != null && !URL2.equals(""))
            bundle.putString("URL2", URL2);

        if (URL3 != null && !URL3.equals(""))
            bundle.putString("URL3", URL3);

        Fragment fragment = new FragmentImagePreview();
        fragment.setArguments(bundle);

        Activity.getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentImagePreview").commit();
    }
}
