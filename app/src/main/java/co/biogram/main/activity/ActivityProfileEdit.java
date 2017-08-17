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
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private ImageView ImageViewCover;
    private ImageViewCircle ImageViewCircleProfile;







    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final Context context = this;

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setFocusableInTouchMode(true);



        setContentView(Root);
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);

        if (_PermissionHandler != null)
            _PermissionHandler.GetRequestPermissionResult(RequestCode, Permissions, GrantResults);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        AndroidNetworking.forceCancel("ActivityProfileEdit");
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent Data)
    {
        if (ResultCode != RESULT_OK)
            return;

        switch (RequestCode)
        {
            case FromFile:
                CaptureUri = Data.getData();
                DoCrop();
                break;
            case FromCamera:
                _PermissionHandler = new PermissionHandler(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101, ActivityProfileEdit.this, new PermissionHandler.PermissionEvent()
                {
                    @Override
                    public void OnGranted()
                    {
                        ContentResolver _ContentResolver = getContentResolver();
                        ContentValues _ContentValues = new ContentValues();

                        _ContentValues.put(MediaStore.Images.Media.DATA, CaptureUri.getPath());
                        _ContentValues.put(MediaStore.Images.Media.IS_PRIVATE, 0);
                        _ContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, _ContentValues);

                        String[] SelectionArgs = { CaptureUri.getPath() };

                        Cursor _Cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATA + " = ?", SelectionArgs, null);

                        if (_Cursor != null)
                        {
                            _Cursor.moveToFirst();
                            CaptureUri = Uri.parse("content://media/external/images/media/" + _Cursor.getInt(_Cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                            _Cursor.close();
                        }

                        DoCrop();
                    }

                    @Override
                    public void OnFailed()
                    {
                        MiscHandler.Toast(ActivityProfileEdit.this, getString(R.string.PermissionStorage));
                    }
                });
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                Uri ResultURI = CropImage.getActivityResult(Data).getUri();

                if (IsCover)
                {
                    try
                    {
                        boolean IsCreated = true;
                        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Temp/Picture");

                        if (!Root.exists())
                            IsCreated = Root.mkdirs();

                        if (IsCreated)
                        {
                            FileCover = new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                            FileOutputStream FileStream = new FileOutputStream(FileCover);
                            ByteArrayOutputStream ImageByteArray = new ByteArrayOutputStream();
                            MediaStore.Images.Media.getBitmap(getContentResolver(), ResultURI).compress(Bitmap.CompressFormat.JPEG, 70, ImageByteArray);
                            ImageByteArray.writeTo(FileStream);
                            FileStream.close();

                            ImageViewCover.setImageURI(ResultURI);
                        }
                    }
                    catch (Exception e)
                    {
                        // Leave Me Alone
                    }
                }
                else
                {
                    try
                    {
                        boolean IsCreated = true;
                        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Temp/Picture");

                        if (!Root.exists())
                            IsCreated = Root.mkdirs();

                        if (IsCreated)
                        {
                            FileProfile = new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                            FileOutputStream FileStream = new FileOutputStream(FileProfile);
                            ByteArrayOutputStream ImageByteArray = new ByteArrayOutputStream();
                            MediaStore.Images.Media.getBitmap(getContentResolver(), ResultURI).compress(Bitmap.CompressFormat.JPEG, 70, ImageByteArray);
                            ImageByteArray.writeTo(FileStream);
                            FileStream.close();

                            ImageViewCircleProfile.setImageURI(ResultURI);
                        }
                    }
                    catch (Exception e)
                    {
                        // Leave Me Alone
                    }
                }
                break;
        }
    }

    public static class FragmentMap extends Fragment
    {
        private TextView TextViewName;
        private TextView TextViewPosition;

        private MapView _MapView;
        private GoogleMap _GoogleMap;
        private MapThreadClass MapThread;

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
            ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getActivity().onBackPressed(); } });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewHeader.setText(getString(R.string.ActivityProfileLocation));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewSearchParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewSearch = new ImageView(context);
            ImageViewSearch.setLayoutParams(ImageViewSearchParam);
            ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewSearch.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
            ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
            ImageViewSearch.setId(MiscHandler.GenerateViewID());
            ImageViewSearch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getFragmentManager().beginTransaction().add(R.id.ActivityProfileContainer, new FragmentSearch()).addToBackStack("FragmentSearch").commit(); } });

            RelativeLayoutHeader.addView(ImageViewSearch);

            RelativeLayout.LayoutParams ViewBlankLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewBlankLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewBlankLine = new View(context);
            ViewBlankLine.setLayoutParams(ViewBlankLineParam);
            ViewBlankLine.setBackgroundResource(R.color.Gray2);
            ViewBlankLine.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewBlankLine);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
            RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);
            RelativeLayoutBottom.setBackgroundResource(R.color.White5);
            RelativeLayoutBottom.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutBottom);

            ImageView ImageViewSend = new ImageView(context);
            ImageViewSend.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewSend.setPadding(MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13));
            ImageViewSend.setImageResource(R.drawable.ic_location_blue);
            ImageViewSend.setId(MiscHandler.GenerateViewID());
            ImageViewSend.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ActivityProfileEdit Parent = (ActivityProfileEdit) getActivity();

                    if (_GoogleMap != null)
                    {
                        double Lat = _GoogleMap.getCameraPosition().target.latitude;
                        double Lon = _GoogleMap.getCameraPosition().target.longitude;

                        String Name = TextViewName.getText().toString();

                        if (Name.length() > 25)
                            Name = Name.substring(0, 25) + " ...";

                        Parent.Position = (float) Lat + ":" + (float) Lon;
                        Parent.EditTextLocation.setText(Name);
                    }

                    Parent.onBackPressed();
                }
            });

            RelativeLayoutBottom.addView(ImageViewSend);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewSend.getId());
            TextViewNameParam.setMargins(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));

            TextViewName = new TextView(context);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewName.setId(MiscHandler.GenerateViewID());

            RelativeLayoutBottom.addView(TextViewName);

            RelativeLayout.LayoutParams TextViewPositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPositionParam.addRule(RelativeLayout.RIGHT_OF, ImageViewSend.getId());
            TextViewPositionParam.addRule(RelativeLayout.BELOW, TextViewName.getId());
            TextViewPositionParam.setMargins(MiscHandler.ToDimension(context, 5),0, 0, 0);

            TextViewPosition = new TextView(context);
            TextViewPosition.setLayoutParams(TextViewPositionParam);
            TextViewPosition.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            RelativeLayoutBottom.addView(TextViewPosition);

            RelativeLayout.LayoutParams ViewBlankLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewBlankLine2Param.addRule(RelativeLayout.ABOVE, RelativeLayoutBottom.getId());

            View ViewBlankLine2 = new View(context);
            ViewBlankLine2.setLayoutParams(ViewBlankLine2Param);
            ViewBlankLine2.setBackgroundResource(R.color.Gray2);
            ViewBlankLine2.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewBlankLine2);

            RelativeLayout.LayoutParams ViewCenterParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
            ViewCenterParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            View ViewCenter = new View(context);
            ViewCenter.setLayoutParams(ViewCenterParam);
            ViewCenter.setBackgroundResource(R.color.Gray2);
            ViewCenter.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewCenter);

            RelativeLayout.LayoutParams ImageViewPinParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 24), MiscHandler.ToDimension(context, 42));
            ImageViewPinParam.addRule(RelativeLayout.ABOVE, ViewCenter.getId());
            ImageViewPinParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            ImageViewPinParam.setMargins(0, MiscHandler.ToDimension(context, 21), 0, 0);

            ImageView ImageViewPin = new ImageView(context);
            ImageViewPin.setLayoutParams(ImageViewPinParam);
            ImageViewPin.setImageResource(R.drawable.ic_location_pin);

            Root.addView(ImageViewPin);

            _MapView = new MapView(context)
            {
                @Override
                public boolean onInterceptTouchEvent(MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_MOVE)
                    {
                        if (_GoogleMap != null)
                        {
                            double Lat = _GoogleMap.getCameraPosition().target.latitude;
                            double Lon = _GoogleMap.getCameraPosition().target.longitude;

                            TextViewPosition.setText("(" + (float) Lat + " , " + (float) Lon + ")");
                            SetLocationName(Lat, Lon);
                        }
                    }

                    return super.onInterceptTouchEvent(m);
                }
            };
            _MapView.onCreate(savedInstanceState);
            _MapView.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    _MapView.onResume();
                    _GoogleMap = googleMap;
                    _GoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    _GoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    _GoogleMap.getUiSettings().setCompassEnabled(true);

                    double Lat = 0, Lon = 0;
                    String[] Position = ((ActivityProfileEdit) getActivity()).Position.split(":");

                    if (Position.length > 1 && !Position[0].equals("") && !Position[1].equals(""))
                    {
                        Lat = Double.parseDouble(Position[0]);
                        Lon = Double.parseDouble(Position[1]);

                        CameraUpdate MyPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Lon), _GoogleMap.getMaxZoomLevel() - 15);
                        _GoogleMap.moveCamera(MyPosition);
                        _GoogleMap.animateCamera(MyPosition);
                    }
                    else if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        _GoogleMap.setMyLocationEnabled(true);

                        LocationManager Manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        Location MyLocation = Manager.getLastKnownLocation(Manager.getBestProvider(new Criteria(), true));

                        if (MyLocation != null)
                        {
                            Lat = MyLocation.getLatitude();
                            Lon = MyLocation.getLongitude();

                            CameraUpdate MyPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Lon), _GoogleMap.getMaxZoomLevel() - 15);
                            _GoogleMap.moveCamera(MyPosition);
                            _GoogleMap.animateCamera(MyPosition);
                        }
                    }

                    String Name = "Unknown Place";
                    TextViewName.setText(Name);
                    TextViewPosition.setText("(" + (float) Lat + " , " + (float) Lon + ")");
                    SetLocationName(Lat, Lon);
                }
            });

            RelativeLayout.LayoutParams MapParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            MapParams.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());
            MapParams.addRule(RelativeLayout.ABOVE, ViewBlankLine2.getId());
            _MapView.setLayoutParams(MapParams);

            Root.addView(_MapView);
            ImageViewPin.bringToFront();

            return Root;
        }

        @Override
        public void onStop()
        {
            super.onStop();
            MapThread = null;
        }

        private void SetLocationName(double Lat, double Lon)
        {
            if (MapThread == null)
            {
                MapThread = new MapThreadClass();
                MapThread.start();
            }

            MapThread.FindLocationName(Lat, Lon);
        }

        private class MapThreadClass extends Thread
        {
            private double Latitude, Longitude;
            private Handler MapHandler;
            private final Runnable MapRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String LocationName = "";
                        Geocoder geocoder = new Geocoder(getActivity());
                        List<Address> Address = geocoder.getFromLocation(Latitude, Longitude, 1);

                        if (Address.size() > 0)
                        {
                            String AddressLine = Address.get(0).getAddressLine(0);
                            String City = Address.get(0).getLocality();
                            String KnownName = Address.get(0).getFeatureName();

                            if (City != null && !City.equals(""))
                                LocationName += City;
                            else if (AddressLine != null && !AddressLine.equals(""))
                                LocationName += AddressLine;

                            if (KnownName != null && !KnownName.equals(""))
                                LocationName += " - " + KnownName;

                            if (LocationName.length() > 32)
                                LocationName = LocationName.substring(0, 32) + " ...";
                        }

                        if (LocationName.equals(""))
                            LocationName = "Unknown Place";

                        final String LocationName2 = LocationName;

                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                TextViewName.setText(LocationName2);
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        // Leave Me Alone
                    }
                }
            };

            void FindLocationName(double latitude, double longitude)
            {
                Latitude = latitude;
                Longitude = longitude;

                if (MapHandler != null)
                {
                    MapHandler.removeCallbacks(MapRunnable);
                    MapHandler.post(MapRunnable);
                }
            }

            @Override
            public void run()
            {
                Looper.prepare();

                if (MapHandler == null)
                    MapHandler = new Handler();

                MapHandler.post(MapRunnable);

                Looper.loop();
            }
        }
    }

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

    private void RetrieveDataFromServer()
    {
        final Context context = this;
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileGetEdit"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("ActivityProfileEdit")
        .build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                RelativeLayoutLoading.setVisibility(View.GONE);
                TextViewTry.setVisibility(View.GONE);
                LoadingViewData.Stop();

                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONObject Data = new JSONObject(Result.getString("Result"));

                        EditTextUsername.setText(Data.getString("Username"));
                        EditTextDescription.setText(Data.getString("Description"));
                        EditTextLink.setText(Data.getString("Link"));
                        EditTextLocation.setText(Data.getString("Location"));
                        EditTextEmail.setText(Data.getString("Email"));
                        Position = Data.getString("Position");

                        Glide.with(context).load(Data.getString("Cover")).dontAnimate().into(ImageViewCover);
                        Glide.with(context).load(Data.getString("Avatar")).override(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90)).dontAnimate().into(ImageViewCircleProfile);
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
                TextViewTry.setVisibility(View.VISIBLE);
                LoadingViewData.Stop();
            }
        });
    }

    private void DoCrop()
    {
        Context context = this;

        if (IsCover)
        {
            try
            {
                String URL = null;
                String[] Pro = { MediaStore.Images.Media.DATA };
                Cursor _Cursor = getContentResolver().query(CaptureUri, Pro, null, null, null);

                if (_Cursor != null && _Cursor.moveToFirst())
                {
                    URL = _Cursor.getString(_Cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    _Cursor.close();
                }

                if (URL == null)
                    return;

                File IMG = new File(URL);

                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(IMG), null, o);

                int Scale = 1;

                while ((o.outWidth / Scale / 2) >= (int) (MiscHandler.ToDimension(context, 160) * 2.45f) && (o.outHeight / Scale / 2) >= MiscHandler.ToDimension(context, 160))
                {
                    Scale *= 2;
                }

                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = Scale;
                Bitmap ResizeBitmap = BitmapFactory.decodeStream(new FileInputStream(IMG), null, o2);

                Matrix matrix = new Matrix();
                int Orientation = new ExifInterface(URL).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                if (Orientation == 6)
                    matrix.postRotate(90);
                else if (Orientation == 3)
                    matrix.postRotate(180);
                else if (Orientation == 8)
                    matrix.postRotate(270);

                ResizeBitmap = Bitmap.createBitmap(ResizeBitmap, 0, 0, ResizeBitmap.getWidth(), ResizeBitmap.getHeight(), matrix, true);
                String ResizeBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), ResizeBitmap, "BioGram Crop Image", null);

                CropImage.activity(Uri.parse(ResizeBitmapPath))
                .setAllowRotation(true)
                .setActivityTitle(getString(R.string.ActivityProfileCrop))
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(2, 1)
                .setFixAspectRatio(true)
                .setRequestedSize((int)(MiscHandler.ToDimension(context, 160) * 2.45f), MiscHandler.ToDimension(context, 160), CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .start(this);
            }
            catch (Exception e)
            {
                // Leave Me Alone
            }

            return;
        }

        CropImage.activity(CaptureUri).setAllowRotation(true).setActivityTitle(getString(R.string.ActivityProfileCrop)).setGuidelines(CropImageView.Guidelines.ON_TOUCH).setFixAspectRatio(true).start(this);
    }
}
