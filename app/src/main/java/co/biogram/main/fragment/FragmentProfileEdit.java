package co.biogram.main.fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

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

import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.activity.ActivityMain;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.PermissionHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;

public class FragmentProfileEdit extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment_profile_edit);
    }
}
        /*

            private TextView TextViewTryAgain;
    private LoadingView LoadingBounceEdit;
    private RelativeLayout RelativeLayoutLoading;

    private Uri ImageCaptureUri;
    private final int FROM_FILE = 1;
    private final int FROM_CAMERA = 2;

    private boolean IsBackGround = false;
    private ImageView ImageViewBackGround;
    private ImageViewCircle ImageViewCircleProfile;
    private AlertDialog DialogProfile, DialogBackGround;

    private String Position = "";
    private EditText EditTextUsername;
    private EditText EditTextDescription;
    private EditText EditTextLink;
    private EditText EditTextLocation;
    private EditText EditTextEmail;

    private File ImageProfile;
    private File ImageBackGround;

    private PermissionHandler PermissionObject;

        findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        final LoadingView LoadingBounceSave = (LoadingView) findViewById(R.id.LoadingBounceSave);
        final TextView TextViewSave = (TextView) findViewById(R.id.TextViewSave);

        TextViewSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextViewSave.setVisibility(View.GONE);
                LoadingBounceSave.Start();

                Map<String, File> UploadFile = new HashMap<>();

                if (ImageProfile != null || ImageBackGround != null)
                {
                    if (ImageProfile != null)
                        UploadFile.put("ImageProfile", ImageProfile);

                    if (ImageBackGround != null)
                        UploadFile.put("ImageBackGround", ImageBackGround);
                }
                else
                    UploadFile = null;

                final ProgressDialog Progress = new ProgressDialog(FragmentProfileEdit.this);
                Progress.setMessage(getString(R.string.ActivityProfileEditUpload));
                Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Progress.setIndeterminate(false);
                Progress.setMax(100);
                Progress.setProgress(0);

                if (UploadFile != null)
                    Progress.show();

                AndroidNetworking.upload(URLHandler.GetURL(URLHandler.URL.MISC_LAST_ONLINE))
                .addMultipartParameter("Username", EditTextUsername.getText().toString())
                .addMultipartParameter("Description", EditTextDescription.getText().toString())
                .addMultipartParameter("Link", EditTextLink.getText().toString())
                .addMultipartParameter("Position", Position)
                .addMultipartParameter("Location", EditTextLocation.getText().toString())
                .addMultipartParameter("Email", EditTextEmail.getText().toString())
                .addMultipartFile(UploadFile).addHeaders("TOKEN", SharedHandler.GetString("Token"))
                .setTag("FragmentProfileEdit").build().setUploadProgressListener(new UploadProgressListener()
                {
                    @Override
                    public void onProgress(long Uploaded, long Total)
                    {
                        Progress.setProgress((int) (100 * Uploaded / Total));
                    }
                })
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        TextViewSave.setVisibility(View.VISIBLE);
                        LoadingBounceSave.Stop();

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000)
                            {
                                Intent i = new Intent(FragmentProfileEdit.this, ActivityMain.class);
                                i.putExtra("Tab", 5);
                                startActivity(i);
                                finish();
                            }
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        TextViewSave.setVisibility(View.VISIBLE);
                        LoadingBounceSave.Stop();

                        Toast.makeText(App.GetContext(), getString(R.string.GeneralCheckInternet), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ImageViewBackGround = (ImageView) findViewById(R.id.ImageViewBackGround);
        ImageViewCircleProfile = (ImageViewCircle) findViewById(R.id.ImageViewCircleProfile);
        LoadingBounceEdit = (LoadingView) findViewById(R.id.LoadingBounceEdit);
        RelativeLayoutLoading = (RelativeLayout) findViewById(R.id.RelativeLayoutLoading);
        EditTextDescription = (EditText) findViewById(R.id.EditTextDescription);
        EditTextLink = (EditText) findViewById(R.id.EditTextLink);
        EditTextLocation = (EditText) findViewById(R.id.EditTextLocation);
        EditTextEmail = (EditText) findViewById(R.id.EditTextEmail);

        TextViewTryAgain = (TextView) findViewById(R.id.TextViewTryAgain);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RetrieveDataFromServer();
            }
        });

        findViewById(R.id.RelativeLayoutBackGround).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogBackGround.show();
            }
        });

        findViewById(R.id.RelativeLayoutProfile).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogProfile.show();
            }
        });

        EditTextLocation.setFocusable(false);
        EditTextLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment _Map = new FragmentMap();
                FragmentTransaction Trans = getFragmentManager().beginTransaction();
                Trans.add(R.id.FrameLayoutMap, _Map);
                Trans.addToBackStack("FragmentMap");
                Trans.commit();
            }
        });

        EditTextUsername = (EditText) findViewById(R.id.EditTextUsername);
        EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(App.GetContext(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);

        InitializationBackground();
        InitializationProfile();
        RetrieveDataFromServer();
    }

    public static class FragmentMap extends Fragment
    {
        private MapThreadClass MapThread;
        private MapView _MapView;
        private GoogleMap _GoogleMap;
        private TextView TextViewName;
        private TextView TextViewPosition;

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_main_fragment_profile_edit_fragment_map, Parent, false);

            TextViewName = (TextView) RootView.findViewById(R.id.TextViewName);
            TextViewPosition = (TextView) RootView.findViewById(R.id.TextViewPosition);

            _MapView = new MapView(App.GetContext())
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

            _MapView.onCreate(null);

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

                    double Lat = 0;
                    double Lon = 0;
                    String[] Position = ((FragmentProfileEdit) getActivity()).Position.split(":");

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

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getActivity().onBackPressed();
                }
            });

            RootView.findViewById(R.id.ImageViewSearch).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Fragment Search = new FragmentSearch();
                    FragmentTransaction Trans = getFragmentManager().beginTransaction();
                    Trans.add(R.id.FrameLayoutMap, Search);
                    Trans.addToBackStack("FragmentSearch");
                    Trans.commit();
                }
            });

            RootView.findViewById(R.id.ImageViewSend).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (_GoogleMap != null)
                    {
                        double Lat = _GoogleMap.getCameraPosition().target.latitude;
                        double Lon = _GoogleMap.getCameraPosition().target.longitude;

                        ((FragmentProfileEdit) getActivity()).Position = (float) Lat + ":" + (float) Lon;
                        ((FragmentProfileEdit) getActivity()).EditTextLocation.setText(TextViewName.getText().toString());
                    }

                    getActivity().onBackPressed();
                }
            });

            RelativeLayout.LayoutParams MapParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            MapParams.addRule(RelativeLayout.BELOW, R.id.ViewBlankLine);
            MapParams.addRule(RelativeLayout.ABOVE, R.id.ViewBlankLine2);
            _MapView.setLayoutParams(MapParams);

            ((RelativeLayout) RootView.findViewById(R.id.RelativeLayoutRoot)).addView(_MapView);

            RootView.findViewById(R.id.ImageViewPin).bringToFront();

            return RootView;
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

        public class MapThreadClass extends Thread
        {
            private double Lat;
            private double Lon;
            private Handler MapHandler;
            private Runnable MapRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String LocationName = "";
                        Geocoder geocoder = new Geocoder(App.GetContext());
                        List<Address> Address = geocoder.getFromLocation(Lat, Lon, 1);

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

            void FindLocationName(double _Lat, double _Lon)
            {
                Lat = _Lat;
                Lon = _Lon;

                if (MapHandler != null && MapRunnable != null)
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
        private SearchAdapter _SearchAdapter;
        private List<SearchStruct> SearchList = new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_main_fragment_profile_edit_fragment_search, Parent, false);

            final EditText EditTextHeader = (EditText) RootView.findViewById(R.id.EditTextHeader);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getActivity().onBackPressed();
                }
            });

            RootView.findViewById(R.id.ImageViewSearch).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String URL = "https://api.foursquare.com/v2/venues/search/?v=20170101&locale=en&limit=25&client_id=YXWKVYF1XFBW3XRC4I0EIVALENCVNVEX5ARMQA4TP5ZUAB41&client_secret=QX2MNDH4GGFYSQCLFE1NVXDFJBNTNWKT3IDX0OUJD54TNKJ2&near=" + EditTextHeader.getText().toString();

                    AndroidNetworking.get(URL).setTag("FragmentProfileEdit").build().getAsString(new StringRequestListener()
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
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            Toast.makeText(App.GetContext(), getString(R.string.GeneralCheckInternet), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            InputMethodManager IMG = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMG.showSoftInput(EditTextHeader, 0);
            IMG.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            EditTextHeader.requestFocus();

            _SearchAdapter = new SearchAdapter(getActivity().getApplicationContext(), SearchList);
            ListView ListViewSearch = (ListView) RootView.findViewById(R.id.ListViewSearch);
            ListViewSearch.setAdapter(_SearchAdapter);

            return RootView;
        }

        public class SearchStruct
        {
            String Name;
            double Lat;
            double Lon;

            SearchStruct(String Name, double Lat, double Lon)
            {
                this.Name = Name;
                this.Lat = Lat;
                this.Lon = Lon;
            }
        }

        public class SearchAdapter extends ArrayAdapter<SearchStruct>
        {
            SearchAdapter(Context context, List<SearchStruct> SearchList)
            {
                super(context, -1, SearchList);
            }

            @Override
            public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent)
            {
                SearchStruct Search = SearchList.get(position);

                RelativeLayout MainView = new RelativeLayout(App.GetContext());
                RelativeLayout.LayoutParams MainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56));
                MainView.setLayoutParams(MainParam);

                if (Search == null)
                    return MainView;

                ImageView Icon = new ImageView(App.GetContext());
                RelativeLayout.LayoutParams IconParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT);
                Icon.setPadding(MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12));
                Icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Icon.setLayoutParams(IconParam);
                Icon.setImageResource(R.drawable.ic_location_blue);
                Icon.setId(MiscHandler.GenerateViewID());

                MainView.addView(Icon);

                TextView Name = new TextView(App.GetContext());
                RelativeLayout.LayoutParams NameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                NameParam.addRule(RelativeLayout.RIGHT_OF, Icon.getId());
                NameParam.setMargins(0, MiscHandler.DpToPx(2), 0, 0);
                Name.setLayoutParams(NameParam);
                Name.setText(Search.Name);
                Name.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
                Name.setId(MiscHandler.GenerateViewID());

                MainView.addView(Name);

                TextView Position = new TextView(App.GetContext());
                RelativeLayout.LayoutParams PositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                PositionParam.addRule(RelativeLayout.RIGHT_OF, Icon.getId());
                PositionParam.addRule(RelativeLayout.BELOW, Name.getId());
                PositionParam.setMargins(0, MiscHandler.DpToPx(5), 0, 0);
                Position.setLayoutParams(PositionParam);
                Position.setText("(" + (float) Search.Lat + " , " + (float) Search.Lon + ")");

                MainView.addView(Position);

                return MainView;
            }
        }
    }

    protected void onActivityResult(int RequestCode, int ResultCode, Intent Data)
    {
        if (ResultCode != RESULT_OK)
            return;

        switch (RequestCode)
        {
            case FROM_FILE:
                ImageCaptureUri = Data.getData();
                DoCrop();
                break;
            case FROM_CAMERA:
                PermissionObject = new PermissionHandler(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101, FragmentProfileEdit.this, new PermissionHandler.PermissionEvent()
                {
                    @Override
                    public void OnGranted()
                    {
                        ContentResolver _ContentResolver = getContentResolver();
                        ContentValues _ContentValues = new ContentValues();

                        _ContentValues.put(MediaStore.Images.Media.DATA, ImageCaptureUri.getPath());
                        _ContentValues.put(MediaStore.Images.Media.IS_PRIVATE, 0);
                        _ContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, _ContentValues);

                        String[] SelectionArgs = { ImageCaptureUri.getPath() };

                        Cursor _Cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATA + " = ?", SelectionArgs, null);

                        if (_Cursor != null)
                        {
                            _Cursor.moveToFirst();
                            ImageCaptureUri = Uri.parse("content://media/external/images/media/" + _Cursor.getInt(_Cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                            _Cursor.close();
                        }

                        DoCrop();
                    }

                    @Override
                    public void OnFailed()
                    {
                        Toast.makeText(App.GetContext(), getString(R.string.GeneralPermissionStorage), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                Uri ResultURI = CropImage.getActivityResult(Data).getUri();

                if (IsBackGround)
                {
                    try
                    {
                        boolean IsCreated = true;
                        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram");

                        if (!Root.exists())
                            IsCreated = Root.mkdirs();

                        if (IsCreated)
                        {
                            ImageBackGround = new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                            FileOutputStream FileStream = new FileOutputStream(ImageBackGround);
                            ByteArrayOutputStream ImageByteArray = new ByteArrayOutputStream();

                            MediaStore.Images.Media.getBitmap(getContentResolver(), ResultURI).compress(Bitmap.CompressFormat.JPEG, 60, ImageByteArray);
                            ImageByteArray.writeTo(FileStream);
                            FileStream.close();

                            ImageViewBackGround.setImageURI(ResultURI);
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
                        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram");

                        if (!Root.exists())
                            IsCreated = Root.mkdirs();

                        if (IsCreated)
                        {
                            ImageProfile = new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                            FileOutputStream FileStream = new FileOutputStream(ImageProfile);
                            ByteArrayOutputStream ImageByteArray = new ByteArrayOutputStream();

                            MediaStore.Images.Media.getBitmap(getContentResolver(), ResultURI).compress(Bitmap.CompressFormat.JPEG, 60, ImageByteArray);
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

    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);

        if (PermissionObject != null)
            PermissionObject.GetRequestPermissionResult(RequestCode, Permissions, GrantResults);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        AndroidNetworking.cancel("FragmentProfileEdit");
    }

    private void InitializationProfile()
    {
        final String[] DialogItems = new String[] { getString(R.string.ActivityProfileEditDialogMessage1), getString(R.string.ActivityProfileEditDialogMessage2), getString(R.string.ActivityProfileEditDialogMessage3) };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, DialogItems);
        AlertDialog.Builder ProfileBuilder = new AlertDialog.Builder(this);
        ProfileBuilder.setTitle(getString(R.string.ActivityProfileEditDialogMessage4));
        ProfileBuilder.setAdapter(adapter, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int Position)
            {
                if (Position == 0)
                {
                    PermissionObject = new PermissionHandler(Manifest.permission.CAMERA, 100, FragmentProfileEdit.this, new PermissionHandler.PermissionEvent()
                    {
                        @Override
                        public void OnGranted()
                        {
                            boolean IsCreated = true;
                            File Root = new File(Environment.getExternalStorageDirectory(), "BioGram");

                            if (!Root.exists())
                                IsCreated = Root.mkdirs();

                            if (IsCreated)
                            {
                                IsBackGround = false;
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                ImageCaptureUri = Uri.fromFile(new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg")));
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageCaptureUri);
                                intent.putExtra("return-data", true);
                                startActivityForResult(intent, FROM_CAMERA);
                            }
                        }

                        @Override
                        public void OnFailed()
                        {
                            Toast.makeText(App.GetContext(), getString(R.string.GeneralPermissionCamera), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (Position == 1)
                {
                    IsBackGround = false;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.ActivityProfileEditCompleteAction)), FROM_FILE);
                }
                else if (Position == 2)
                {
                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.MISC_LAST_ONLINE))
                    .addHeaders("TOKEN", SharedHandler.GetString("Token"))
                    .setTag("FragmentProfileEdit")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            ImageViewCircleProfile.setImageResource(R.color.BlueLight);
                            Toast.makeText(App.GetContext(), getString(R.string.ActivityProfileEditImageProfileRemoved), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            Toast.makeText(App.GetContext(), getString(R.string.GeneralCheckInternet), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        DialogProfile = ProfileBuilder.create();
    }

    private void InitializationBackground()
    {
        final String[] DialogItems = new String[] { getString(R.string.ActivityProfileEditDialogMessage1), getString(R.string.ActivityProfileEditDialogMessage2), getString(R.string.ActivityProfileEditDialogMessage3) };

        AlertDialog.Builder BackGroundBuilder = new AlertDialog.Builder(this);
        BackGroundBuilder.setTitle(getString(R.string.ActivityProfileEditDialogMessage4));
        BackGroundBuilder.setAdapter(new ArrayAdapter<>(this, android.R.layout.select_dialog_item, DialogItems), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface d, int Position)
            {
                if (Position == 0)
                {
                    PermissionObject = new PermissionHandler(Manifest.permission.CAMERA, 100, FragmentProfileEdit.this, new PermissionHandler.PermissionEvent()
                    {
                        @Override
                        public void OnGranted()
                        {
                            boolean IsCreated = true;
                            File Root = new File(Environment.getExternalStorageDirectory(), "BioGram");

                            if (!Root.exists())
                                IsCreated = Root.mkdirs();

                            if (IsCreated)
                            {
                                IsBackGround = true;
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                ImageCaptureUri = Uri.fromFile(new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg")));
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageCaptureUri);
                                intent.putExtra("return-data", true);
                                startActivityForResult(intent, FROM_CAMERA);
                            }
                        }

                        @Override
                        public void OnFailed()
                        {
                            Toast.makeText(App.GetContext(), getString(R.string.GeneralPermissionCamera), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else if (Position == 1)
                {
                    IsBackGround = true;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.ActivityProfileEditCompleteAction)), FROM_FILE);
                }
                else if (Position == 2)
                {
                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.MISC_LAST_ONLINE))
                    .addHeaders("TOKEN", SharedHandler.GetString("Token"))
                    .setTag("FragmentProfileEdit")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            ImageViewBackGround.setImageResource(R.color.BlueLight);
                            Toast.makeText(App.GetContext(), getString(R.string.ActivityProfileEditImageBackGroundRemoved), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            Toast.makeText(App.GetContext(), getString(R.string.GeneralCheckInternet), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        DialogBackGround = BackGroundBuilder.create();
    }

    private void DoCrop()
    {
        if (IsBackGround)
        {
            try
            {
                String URL = null;
                String[] Pro = { MediaStore.Images.Media.DATA };
                Cursor _Cursor = getContentResolver().query(ImageCaptureUri, Pro, null, null, null);

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

                while ((o.outWidth / Scale / 2) >= (int)(MiscHandler.DpToPx(160) * 2.45f) && (o.outHeight / Scale / 2) >= MiscHandler.DpToPx(160))
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
                        .setActivityTitle(getString(R.string.ActivityProfileEditCrop))
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(2, 1)
                        .setFixAspectRatio(true)
                        .setRequestedSize((int)(MiscHandler.DpToPx(160) * 2.45f), MiscHandler.DpToPx(160), CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                        .start(this);
            }
            catch (Exception e)
            {
                // Leave Me Alone
            }
        }
        else
            CropImage.activity(ImageCaptureUri).setAllowRotation(true).setActivityTitle(getString(R.string.ActivityProfileEditCrop)).setGuidelines(CropImageView.Guidelines.ON_TOUCH).setFixAspectRatio(true).start(this);
    }

    private void RetrieveDataFromServer()
    {
        LoadingBounceEdit.Start();
        TextViewTryAgain.setVisibility(View.GONE);
        RelativeLayoutLoading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.MISC_LAST_ONLINE))
        .addHeaders("TOKEN", SharedHandler.GetString("Token"))
        .setTag("FragmentProfileEdit").build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
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

                        MiscHandler.LoadImage(ImageViewBackGround, "FragmentProfileEdit", Data.getString("BackGround"), 0, MiscHandler.DpToPx(150));
                        MiscHandler.LoadImage(ImageViewCircleProfile, "FragmentProfileEdit", Data.getString("Profile"), MiscHandler.DpToPx(90), MiscHandler.DpToPx(90));

                        LoadingBounceEdit.Stop();
                        TextViewTryAgain.setVisibility(View.GONE);
                        RelativeLayoutLoading.setVisibility(View.GONE);
                    }
                    else
                    {
                        LoadingBounceEdit.Stop();
                        TextViewTryAgain.setVisibility(View.VISIBLE);
                        RelativeLayoutLoading.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    LoadingBounceEdit.Stop();
                    TextViewTryAgain.setVisibility(View.VISIBLE);
                    RelativeLayoutLoading.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(ANError error)
            {
                LoadingBounceEdit.Stop();
                TextViewTryAgain.setVisibility(View.VISIBLE);
                RelativeLayoutLoading.setVisibility(View.VISIBLE);

                Toast.makeText(App.GetContext(), getString(R.string.GeneralCheckInternet), Toast.LENGTH_SHORT).show();
            }
        });
    }
}*/
