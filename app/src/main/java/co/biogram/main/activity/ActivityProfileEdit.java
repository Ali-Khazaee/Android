package co.biogram.main.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import com.bumptech.glide.Glide;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.PermissionHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;

public class ActivityProfileEdit extends FragmentActivity
{






    public static class FragmentSearch extends Fragment
    {
        private EditText EditTextSearch;
        private SearchAdapter _SearchAdapter;
        private final List<SearchStruct> SearchList = new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            Context context = getActivity();

            RelativeLayout Root = new RelativeLayout(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setClickable(true);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.White5);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setImageResource(R.drawable.ic_back_blue);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { MiscHandler.HideSoftKey(getActivity()); getActivity().onBackPressed(); } });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewSearchParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewSearch = new ImageView(context);
            ImageViewSearch.setLayoutParams(ImageViewSearchParam);
            ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewSearch.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
            ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
            ImageViewSearch.setId(MiscHandler.GenerateViewID());
            ImageViewSearch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String URL = "https://api.foursquare.com/v2/venues/search/?v=20170101&locale=en&limit=25&client_id=YXWKVYF1XFBW3XRC4I0EIVALENCVNVEX5ARMQA4TP5ZUAB41&client_secret=QX2MNDH4GGFYSQCLFE1NVXDFJBNTNWKT3IDX0OUJD54TNKJ2&near=" + EditTextSearch.getText().toString();

                    AndroidNetworking.get(URL).setTag("FragmentSearch").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                SearchList.clear();

                                JSONArray Result = new JSONObject(Response).getJSONObject("response").getJSONArray("venues");

                                for (int A = 0; A < Result.length(); A++)
                                {
                                    try
                                    {
                                        JSONObject object = Result.getJSONObject(A);
                                        JSONObject location = object.getJSONObject("location");

                                        String Name;
                                        double Lat = location.getDouble("lat");
                                        double Lon = location.getDouble("lng");

                                        if (object.has("name"))
                                            Name = object.getString("name");
                                        else if (location.has("address"))
                                            Name = location.getString("address");
                                        else if (location.has("city"))
                                            Name = location.getString("city");
                                        else if (location.has("state"))
                                            Name = location.getString("state");
                                        else if (location.has("country"))
                                            Name = location.getString("country");
                                        else
                                            continue;

                                        SearchList.add(new SearchStruct(Name, Lat, Lon));
                                    }
                                    catch (Exception e)
                                    {
                                        // Leave Me Alone
                                    }
                                }

                                _SearchAdapter.notifyDataSetChanged();
                                MiscHandler.HideSoftKey(getActivity());
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

            RelativeLayoutHeader.addView(ImageViewSearch);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.LEFT_OF, ImageViewSearch.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            EditTextSearch = new EditText(context);
            EditTextSearch.setLayoutParams(TextViewHeaderParam);
            EditTextSearch.setBackgroundResource(R.color.White5);
            EditTextSearch.setTextColor(ContextCompat.getColor(context, R.color.Black));
            EditTextSearch.setHint("Search");
            EditTextSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextSearch.requestFocus();

            RelativeLayoutHeader.addView(EditTextSearch);

            RelativeLayout.LayoutParams ViewBlankLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewBlankLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewBlankLine = new View(context);
            ViewBlankLine.setLayoutParams(ViewBlankLineParam);
            ViewBlankLine.setBackgroundResource(R.color.Gray2);
            ViewBlankLine.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewBlankLine);

            RelativeLayout.LayoutParams ListViewSearchParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ListViewSearchParam.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());

            _SearchAdapter = new SearchAdapter(context, SearchList);

            ListView ListViewSearch = new ListView(context);
            ListViewSearch.setLayoutParams(ListViewSearchParam);
            ListViewSearch.setAdapter(_SearchAdapter);
            ListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
                {
                    SearchStruct Search = SearchList.get(position);

                    if (Search == null)
                        return;

                    String Name = Search.Name;

                    if (Name.length() > 25)
                        Name = Name.substring(0, 25) + " ...";

                    ActivityProfileEdit Parent = (ActivityProfileEdit) getActivity();
                    Parent.Position = (float) Search.Latitude + ":" + (float) Search.Longitude;
                    Parent.EditTextLocation.setText(Name);
                    Parent.onBackPressed();
                }
            });

            Root.addView(ListViewSearch);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return Root;
        }

        private class SearchStruct
        {
            final String Name;
            final double Latitude;
            final double Longitude;

            SearchStruct(String name, double latitude, double longitude)
            {
                Name = name;
                Latitude = latitude;
                Longitude = longitude;
            }
        }

        private class SearchAdapter extends ArrayAdapter<SearchStruct>
        {
            final Context context;

            SearchAdapter(Context c, List<SearchStruct> SearchList)
            {
                super(c, -1, SearchList);
                context = c;
            }

            @Override
            public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent)
            {
                SearchStruct Search = SearchList.get(position);

                RelativeLayout MainView = new RelativeLayout(context);
                MainView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));

                if (Search == null)
                    return MainView;

                ImageView ImageViewIcon = new ImageView(context);
                ImageViewIcon.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
                ImageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewIcon.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
                ImageViewIcon.setImageResource(R.drawable.ic_location_blue);
                ImageViewIcon.setId(MiscHandler.GenerateViewID());

                MainView.addView(ImageViewIcon);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewIcon.getId());
                TextViewNameParam.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

                TextView TextViewName = new TextView(context);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setText(Search.Name);
                TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewName.setId(MiscHandler.GenerateViewID());

                MainView.addView(TextViewName);

                RelativeLayout.LayoutParams TextViewPositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewPositionParam.addRule(RelativeLayout.RIGHT_OF, ImageViewIcon.getId());
                TextViewPositionParam.addRule(RelativeLayout.BELOW, TextViewName.getId());
                TextViewPositionParam.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

                TextView TextViewPosition = new TextView(context);
                TextViewPosition.setLayoutParams(TextViewPositionParam);
                TextViewPosition.setText("(" + (float) Search.Latitude + " , " + (float) Search.Longitude + ")");

                MainView.addView(TextViewPosition);

                return MainView;
            }
        }
    }
}
