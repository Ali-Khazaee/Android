package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

public class CategoryFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        TextViewTitleParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(getString(R.string.CategoryFragment));
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitle.setTypeface(null, Typeface.BOLD);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewBookMarkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewBookMarkParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewBookMark = new ImageView(context);
        ImageViewBookMark.setLayoutParams(ImageViewBookMarkParam);
        ImageViewBookMark.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBookMark.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
        ImageViewBookMark.setImageResource(R.drawable.ic_bookmark_blue);
        ImageViewBookMark.setId(MiscHandler.GenerateViewID());
        ImageViewBookMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, new BookmarkFragment()).addToBackStack("BookmarkFragment").commit();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBookMark);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewSearchParam.addRule(RelativeLayout.LEFT_OF, ImageViewBookMark.getId());

        ImageView ImageViewSearch = new ImageView(context);
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSearch.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
        ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
        ImageViewSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, new SearchFragment()).addToBackStack("SearchFragment").commit();
            }
        });

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewCategoryParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewCategory = new RecyclerView(context);
        RecyclerViewCategory.setLayoutParams(RecyclerViewCategoryParam);
        RecyclerViewCategory.setLayoutManager(new GridLayoutManager(context, 2));
        RecyclerViewCategory.addItemDecoration(new GridSpacingItemDecoration(2, MiscHandler.ToDimension(context, 15)));
        RecyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewCategory.setClipToPadding(false);
        RecyclerViewCategory.setAdapter(new AdapterCategory(context));

        RelativeLayoutMain.addView(RecyclerViewCategory);

        return RelativeLayoutMain;
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
    {
        private final int spanCount;
        private final int spacing;

        GridSpacingItemDecoration(int spanCount, int spacing)
        {
            this.spanCount = spanCount;
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
        {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;

            if (position < spanCount)
                outRect.top = spacing;

            outRect.bottom = spacing;
        }
    }

    private class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolderMain>
    {
        private final int ID_Main = MiscHandler.GenerateViewID();
        private final int ID_Icon = MiscHandler.GenerateViewID();
        private final int ID_Name = MiscHandler.GenerateViewID();

        private final Context context;
        private final List<Struct> CategoryList = new ArrayList<>();

        AdapterCategory(Context c)
        {
            context = c;
            CategoryList.add(new Struct(R.drawable.ic_category_news, getString(R.string.CategoryFragmentNews), 1));
            CategoryList.add(new Struct(R.drawable.ic_category_fun, getString(R.string.CategoryFragmentFun), 2));
            CategoryList.add(new Struct(R.drawable.ic_category_music, getString(R.string.CategoryFragmentMusic), 3));
            CategoryList.add(new Struct(R.drawable.ic_category_sport, getString(R.string.CategoryFragmentSport), 4));
            CategoryList.add(new Struct(R.drawable.ic_category_fashion, getString(R.string.CategoryFragmentFashion), 5));
            CategoryList.add(new Struct(R.drawable.ic_category_food, getString(R.string.CategoryFragmentFood), 6));
            CategoryList.add(new Struct(R.drawable.ic_category_technology, getString(R.string.CategoryFragmentTechnology), 7));
            CategoryList.add(new Struct(R.drawable.ic_category_art, getString(R.string.CategoryFragmentArt), 8));
            CategoryList.add(new Struct(R.drawable.ic_category_artist, getString(R.string.CategoryFragmentArtist), 9));
            CategoryList.add(new Struct(R.drawable.ic_category_media, getString(R.string.CategoryFragmentMedia), 10));
            CategoryList.add(new Struct(R.drawable.ic_category_business, getString(R.string.CategoryFragmentBusiness), 11));
            CategoryList.add(new Struct(R.drawable.ic_category_echonomy, getString(R.string.CategoryFragmentEconomy), 12));
            CategoryList.add(new Struct(R.drawable.ic_category_lilterature, getString(R.string.CategoryFragmentLiterature), 13));
            CategoryList.add(new Struct(R.drawable.ic_category_travel, getString(R.string.CategoryFragmentTravel), 14));
            CategoryList.add(new Struct(R.drawable.ic_category_politics, getString(R.string.CategoryFragmentPolitics), 15));
            CategoryList.add(new Struct(R.drawable.ic_category_health, getString(R.string.CategoryFragmentHealth), 16));
            CategoryList.add(new Struct(R.drawable.ic_category_religious, getString(R.string.CategoryFragmentReligious), 18));
            CategoryList.add(new Struct(R.drawable.ic_category_other, getString(R.string.CategoryFragmentOther), 17));
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            final LinearLayout LinearLayoutMain;
            final ImageView ImageViewIcon;
            final TextView TextViewName;

            ViewHolderMain(View view)
            {
                super(view);
                LinearLayoutMain = (LinearLayout) view.findViewById(ID_Main);
                ImageViewIcon = (ImageView) view.findViewById(ID_Icon);
                TextViewName = (TextView) view.findViewById(ID_Name);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderMain Holder, int position)
        {
            final int Position = Holder.getAdapterPosition();

            Holder.LinearLayoutMain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("CatName", CategoryList.get(Position).Name);
                    bundle.putInt("CatType", CategoryList.get(Position).Type);

                    Fragment fragment = new SubCategoryFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("SubCategoryFragment").commit();
                }
            });
            Holder.ImageViewIcon.setImageResource(CategoryList.get(Position).Icon);
            Holder.TextViewName.setText(CategoryList.get(Position).Name);
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            LinearLayout LinearLayoutMain = new LinearLayout(context);
            LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayoutMain.setId(ID_Main);

            CardView.LayoutParams CardViewMainParam = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            CardViewMainParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
            CardViewMainParam.gravity = Gravity.CENTER;

            CardView CardViewMain = new CardView(context);
            CardViewMain.setLayoutParams(CardViewMainParam);
            CardViewMain.setCardBackgroundColor(ContextCompat.getColor(context, R.color.White));

            LinearLayoutMain.addView(CardViewMain);

            LinearLayout LinearLayoutCard = new LinearLayout(context);
            LinearLayoutCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayoutCard.setOrientation(LinearLayout.VERTICAL);

            CardViewMain.addView(LinearLayoutCard);

            LinearLayout.LayoutParams ImageViewIconParam = new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 45), MiscHandler.ToDimension(context, 45));
            ImageViewIconParam.gravity = Gravity.CENTER;

            ImageView ImageViewIcon = new ImageView(context);
            ImageViewIcon.setLayoutParams(ImageViewIconParam);
            ImageViewIcon.setId(ID_Icon);
            ImageViewIcon.setPadding(0, MiscHandler.ToDimension(context, 10), 0, 0);

            LinearLayoutCard.addView(ImageViewIcon);

            TextView ImageViewName = new TextView(context);
            ImageViewName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            ImageViewName.setId(ID_Name);
            ImageViewName.setGravity(Gravity.CENTER);
            ImageViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
            ImageViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ImageViewName.setPadding(0, MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10));

            LinearLayoutCard.addView(ImageViewName);

            return new ViewHolderMain(LinearLayoutMain);
        }

        @Override
        public int getItemCount()
        {
            return CategoryList.size();
        }
    }

    private class Struct
    {
        final int Icon;
        final String Name;
        final int Type;

        Struct(int icon, String name, int type)
        {
            Icon = icon;
            Name = name;
            Type = type;
        }
    }
}
