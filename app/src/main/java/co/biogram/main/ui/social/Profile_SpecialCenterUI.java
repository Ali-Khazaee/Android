package co.biogram.main.ui.social;

import android.app.Dialog;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.RecyclerViewOnScroll;

public class Profile_SpecialCenterUI extends FragmentView {
    private TextView TextViewCredit;
    private TextView TextViewBadge;
    private TextView TextViewSpecialPost;
    private FrameLayout FrameLayoutMain;

    @Override
    public void OnCreate() {
        View view = View.inflate(Activity, R.layout.social_profile_specialcenter, null);

        ImageView ImageViewClose = view.findViewById(R.id.ImageViewClose);
        ImageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();
            }
        });

        TextViewCredit = view.findViewById(R.id.TextViewCredit);
        TextViewCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTab(1);
            }
        });
        TextViewCredit.setTypeface(Misc.GetTypeface(), Typeface.BOLD);

        TextViewBadge = view.findViewById(R.id.TextViewBadge);
        TextViewBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTab(2);
            }
        });
        TextViewBadge.setTypeface(Misc.GetTypeface(), Typeface.BOLD);

        TextViewSpecialPost = view.findViewById(R.id.TextViewSpecialPost);
        TextViewSpecialPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTab(3);
            }
        });
        TextViewSpecialPost.setTypeface(Misc.GetTypeface(), Typeface.BOLD);

        FrameLayoutMain = view.findViewById(R.id.FrameLayoutMain);

        ChangeTab(1);

        ViewMain = view;
    }

    private void ChangeTab(int Tab) {
        TypedValue Value = new TypedValue();
        Activity.getTheme().resolveAttribute(R.attr.TextColor, Value, true);

        TextViewCredit.setTextColor(Value.data);
        TextViewBadge.setTextColor(Value.data);
        TextViewSpecialPost.setTextColor(Value.data);

        View view;

        switch (Tab) {
            default:
                view = Credit();
                TextViewCredit.setTextColor(Misc.Color(R.color.Primary));
                break;
            case 2:
                view = Badge();
                TextViewBadge.setTextColor(Misc.Color(R.color.Primary));
                break;
            case 3:
                view = SpecialPost();
                TextViewSpecialPost.setTextColor(Misc.Color(R.color.Primary));
                break;
        }

        FrameLayoutMain.removeAllViews();
        FrameLayoutMain.addView(view);
    }

    private View Credit() {
        View view = View.inflate(Activity, R.layout.social_profile_specialcenter_credit, null);

        TextView TextViewAddCredit = view.findViewById(R.id.TextViewAddCredit);
        TextViewAddCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Add Billing
            }
        });

        RecyclerView RecyclerViewMain = view.findViewById(R.id.RecyclerViewMain);
        RecyclerViewMain.setAdapter(new CreditAdapter());
        RecyclerViewMain.setLayoutManager(new LinearLayoutManager(Activity));
        RecyclerViewMain.setNestedScrollingEnabled(false);
        RecyclerViewMain.setHasFixedSize(false);
        RecyclerViewMain.addOnScrollListener(new RecyclerViewOnScroll() {
            @Override
            public void OnLoadMore() {
                Misc.Debug("On Load More Called");
            }
        });

        EditText EditTextAmount = view.findViewById(R.id.EditTextAmount);
        Misc.SetCursorColor(EditTextAmount, R.color.Primary);

        // TODO Request

        return view;
    }

    private View Badge() {
        return null;
    }

    private View SpecialPost() {
        return null;
    }

    private class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolderMain> {
        private ArrayList<Credit> MainList = new ArrayList<>();

        private CreditAdapter() {
            MainList.add(new Credit(150000, 1524768367, 1, "1050", 1, ""));
            MainList.add(new Credit(100000, 1524668367, 2, "1050", 2, ""));
            MainList.add(new Credit(191000, 1524868367, 3, "1050", 3, ""));
            MainList.add(new Credit(150000, 1524768367, 4, "1050", 1, ""));
            MainList.add(new Credit(100000, 1524668367, 5, "1050", 2, ""));

            MainList.add(new Credit(150000, 1524768367, 1, "1050", 1, ""));
            MainList.add(new Credit(100000, 1524668367, 2, "1050", 2, ""));
            MainList.add(new Credit(191000, 1524868367, 3, "1050", 3, ""));
            MainList.add(new Credit(150000, 1524768367, 4, "1050", 1, ""));
            MainList.add(new Credit(100000, 1524668367, 5, "1050", 2, ""));

            MainList.add(new Credit(150000, 1524768367, 1, "1050", 1, ""));
            MainList.add(new Credit(100000, 1524668367, 2, "1050", 2, ""));
            MainList.add(new Credit(191000, 1524868367, 3, "1050", 3, ""));
            MainList.add(new Credit(150000, 1524768367, 4, "1050", 1, ""));
            MainList.add(new Credit(100000, 1524668367, 5, "1050", 2, ""));

            MainList.add(new Credit(150000, 1524768367, 1, "1050", 1, ""));
            MainList.add(new Credit(100000, 1524668367, 2, "1050", 2, ""));
            MainList.add(new Credit(191000, 1524868367, 3, "1050", 3, ""));
            MainList.add(new Credit(150000, 1524768367, 4, "1050", 1, ""));
            MainList.add(new Credit(100000, 1524668367, 5, "1050", 2, ""));
        }

        @NonNull
        public CreditAdapter.ViewHolderMain onCreateViewHolder(@NonNull ViewGroup vg, int type) {
            return new ViewHolderMain(View.inflate(Activity, R.layout.social_profile_specialcenter_credit_row, null));
        }

        @Override
        public void onBindViewHolder(@NonNull CreditAdapter.ViewHolderMain Holder, int p) {
            int Pos = Holder.getAdapterPosition();

            Holder.RelativeLayoutMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(Activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);

                    View dialogView = View.inflate(Activity, R.layout.social_profile_specialcenter_credit_dialog, null);

                    ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                    ImageViewClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setContentView(dialogView);
                    dialog.show();
                }
            });

            Holder.TextViewAmount.setText(String.valueOf(MainList.get(Pos).Amount));
            Holder.TextViewDate.setText(new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.getDefault()).format(new Date(MainList.get(Pos).Date * 1000L)));

            switch (MainList.get(Pos).Type) {
                case Credit.TYPE_CREDIT: {
                    Holder.ImageViewIcon.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
                    Holder.ImageViewIcon.setImageResource(R.drawable.z_social_profile_specialcenter_credit);
                    Holder.TextViewMessage.setText(Misc.String(R.string.SocialProfileSpecialCenterUICreditRowIns));
                    break;
                }
                case Credit.TYPE_BADGE: {
                    Holder.ImageViewIcon.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
                    Holder.ImageViewIcon.setImageResource(R.drawable.z_social_profile_specialcenter_badge);
                    Holder.TextViewMessage.setText(Misc.String(R.string.SocialProfileSpecialCenterUICreditRowBadge));
                    break;
                }
                case Credit.TYPE_PURCHASE: {
                    Holder.ImageViewIcon.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                    Holder.ImageViewIcon.setImageResource(R.drawable.z_social_profile_specialcenter_purchase);
                    Holder.TextViewMessage.setText(Misc.String(R.string.SocialProfileSpecialCenterUICreditRowPurchase));
                    break;
                }
                case Credit.TYPE_REQUEST: {
                    Holder.ImageViewIcon.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
                    Holder.ImageViewIcon.setImageResource(R.drawable.z_social_profile_specialcenter_request);
                    Holder.TextViewMessage.setText(Misc.String(R.string.SocialProfileSpecialCenterUICreditRowRequest));
                    break;
                }
                case Credit.TYPE_SPONSERED: {
                    Holder.ImageViewIcon.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
                    Holder.ImageViewIcon.setImageResource(R.drawable.z_social_profile_specialcenter_sponsered);
                    Holder.TextViewMessage.setText(Misc.String(R.string.SocialProfileSpecialCenterUICreditRowSponsored));
                    break;
                }
            }

            Holder.ViewLine.setVisibility(Pos == (MainList.size() - 1) ? View.GONE : View.VISIBLE);
        }

        @Override
        public int getItemViewType(int Position) {
            return MainList.get(Position).Type;
        }

        @Override
        public int getItemCount() {
            return MainList.size();
        }

        class ViewHolderMain extends RecyclerView.ViewHolder {
            private RelativeLayout RelativeLayoutMain;
            private ImageView ImageViewIcon;
            private TextView TextViewMessage;
            private TextView TextViewAmount;
            private TextView TextViewDate;
            private View ViewLine;

            ViewHolderMain(View v) {
                super(v);
                RelativeLayoutMain = v.findViewById(R.id.RelativeLayoutMain);
                ImageViewIcon = v.findViewById(R.id.ImageViewIcon);
                TextViewMessage = v.findViewById(R.id.TextViewMessage);
                TextViewAmount = v.findViewById(R.id.TextViewAmount2);
                TextViewDate = v.findViewById(R.id.TextViewDate);
                ViewLine = v.findViewById(R.id.ViewLine);
            }
        }

        class Credit {
            private final static int TYPE_CREDIT = 1;
            private final static int TYPE_BADGE = 2;
            private final static int TYPE_PURCHASE = 3;
            private final static int TYPE_REQUEST = 4;
            private final static int TYPE_SPONSERED = 5;

            public int Amount;
            public int Date;
            public int Type;
            public String Issue;
            public int Status;
            public String Detail;

            Credit(int amount, int date, int type, String issue, int status, String detail) {
                Amount = amount;
                Date = date;
                Type = type;
                Issue = issue;
                Status = status;
                Detail = detail;
            }
        }
    }
}
