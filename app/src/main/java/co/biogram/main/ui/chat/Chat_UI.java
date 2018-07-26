package co.biogram.main.ui.chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.*;
import android.net.Uri;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.*;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.*;
import co.biogram.main.service.NetworkService;
import co.biogram.main.ui.component.CircularProgressView;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.general.ImagePreviewUI;
import co.biogram.main.ui.general.VideoPreviewUI;
import co.biogram.main.ui.view.PermissionDialog;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.SeekBar.*;

/**
 * Created by soh_mil97
 */

public class Chat_UI extends FragmentView implements KeyboardHeightObserver
{

    public static final int MODE_SINGLE = 0;
    public static final int MODE_GROUP = 1;

    private static final String AUDIO_RECORDER_FILE_EXT_MP3 = ".mp3";

    private final int TEXT = 1;
    private final int IMAGE = 2;
    private final int VIDEO = 3;
    private final int AUDIO = 4;
    private final int FILE = 5;

    private RecyclerView ChatRecyclerView;
    private ChatAdapter ChatAdapter;
    private AudioManager AudioManager;

    private MediaRecorder Recorder;
    private SoundPool AudioPlayer;
    private EmojiPopup Emoji;
    private PermissionDialog PermissionRequest;
    private int CHAT_MODE;

    private String Filename;

    private boolean isSendIconGray = true;
    private boolean isAudioPlaying;

    private KeyboardHeightProvider keyboardHeightProvider;

    public Chat_UI(int chatMode)
    {
        this.CHAT_MODE = chatMode;
    }

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.social_chat, null);

        ImageView buttonBack = view.findViewById(R.id.ImageButtonBack);
        ChatRecyclerView = view.findViewById(R.id.RecycelerViewChat);
        final EditText EditTextMessage = view.findViewById(R.id.EditTextMessage);
        final ImageButton ImageButtonSend = view.findViewById(R.id.ImageButtonSend);
        ImageButton ImageButtonAttach = view.findViewById(R.id.ImageButtonAttach);
        ImageButton ImageButtonAudio = view.findViewById(R.id.ImageButtonAudio);
        ImageButton ImageButtonEmoji = view.findViewById(R.id.ImageButtonEmoji);
        ImageButton ImageButtonImage = view.findViewById(R.id.ImageButtonImage);
        ImageButton ImageButtonVideo = view.findViewById(R.id.ImageButtonVideo);
        final ImageView FABAudio = view.findViewById(R.id.ButtonAudio);
        AudioManager = (AudioManager) Activity.getSystemService(Context.AUDIO_SERVICE);
        Emoji = EmojiPopup.Builder.fromRootView(view).build((EmojiEditText) EditTextMessage);

        keyboardHeightProvider = new KeyboardHeightProvider(Activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            AudioPlayer = new SoundPool.Builder().setMaxStreams(4).build();
        else
            AudioPlayer = new SoundPool(4, android.media.AudioManager.STREAM_MUSIC, 20);

        view.post(new Runnable()
        {
            public void run()
            {
                keyboardHeightProvider.start();
            }
        });

        ChatRecyclerView.setLayoutManager(new LinearLayoutManager(Activity));
        ChatAdapter = new ChatAdapter();
        ChatRecyclerView.setAdapter(ChatAdapter);

        FABAudio.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        EditTextMessage.setTypeface(Misc.GetTypeface());

        EditTextMessage.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (EditTextMessage.getText().toString().trim().length() > 0)
                {
                    if (isSendIconGray)
                    {
                        ImageButtonSend.setImageResource(R.drawable.ic_back112323_blue_fa);
                        isSendIconGray = false;

                    }
                }
                else
                {
                    if (!isSendIconGray)
                    {
                        ImageButtonSend.setImageResource(R.drawable.ic_back123_bl123ue_fa);
                        isSendIconGray = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        ImageButtonSend.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String textMessage = EditTextMessage.getText().toString();

                if (!textMessage.equals(""))
                {

                    ChatAdapter.addChat(new TextChatModel(textMessage));
                    EditTextMessage.setText("");
                    ChatRecyclerView.scrollToPosition(ChatAdapter.getSizeOfChats() - 1);

                    AsyncTask.execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            JSONObject uploadObject = NetworkService.createUploadData("5b464e90a2ef1ca5e3a659f8", textMessage, null);
                            Log.d("DATAFROMSERVER", uploadObject.toString());

                            NetworkService.Emit("SendMessage", uploadObject.toString(), new SocketHandler.Callback()
                            {
                                @Override
                                public void call(Object data)
                                {
                                    Log.d("DATAFROMSERVER", data.toString());
                                    try
                                    {
                                        JSONObject result = new JSONObject(data.toString());

                                        if ((int) result.get("Result") == 0)

                                            Misc.UIThread(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    if (AudioManager.getRingerMode() == android.media.AudioManager.RINGER_MODE_NORMAL)
                                                        AudioPlayer.load(Activity.getBaseContext(), R.raw.sound_out, 1);
                                                }
                                            }, 0);

                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                    });

                    //                    new Thread(new Runnable()
                    //                    {
                    //                        @Override
                    //                        public void run()
                    //                        {
                    //                            JSONObject uploadObject = new JSONObject();
                    //                            try
                    //                            {
                    //                                uploadObject.put("To", "5b464e90a2ef1ca5e3a659f8");
                    //                                uploadObject.put("Message", textMessage);
                    //                                uploadObject.put("ReplyID", null);
                    //
                    //                                Log.d("DATAFROMSERVER", uploadObject.toString());
                    //                            }
                    //                            catch (JSONException e)
                    //                            {
                    //                                e.printStackTrace();
                    //                            }
                    //
                    //                            NetworkService.Emit("SendMessage", uploadObject.toString(), new SocketHandler.Callback()
                    //                            {
                    //                                @Override
                    //                                public void call(Object data)
                    //                                {
                    //                                    Log.d("DATAFROMSERVER", data.toString());
                    //                                    try
                    //                                    {
                    //                                        JSONObject result = new JSONObject(data.toString());
                    //
                    //                                        if ((int) result.get("Result") == 0)
                    //
                    //                                            if (AudioManager.getRingerMode() == android.media.AudioManager.RINGER_MODE_NORMAL)
                    //                                                AudioPlayer.load(Activity.getBaseContext(), R.raw.sound_out, 1);
                    //                                    }
                    //                                    catch (JSONException e)
                    //                                    {
                    //                                        e.printStackTrace();
                    //                                    }
                    //
                    //                                }
                    //                            });
                    //                        }
                    //                    }).start();

                }
            }
        });

        ImageButtonAttach.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Misc.closeKeyboard(Activity);

                final GalleryViewUI.GalleryListener L = new GalleryViewUI.GalleryListener()
                {

                    @Override
                    public void OnSelection(final String URL)
                    {
                        if (URL != null && !URL.equals(""))
                        {

                            ChatAdapter.addChat(new FileChatModel(URL));
                            EditTextMessage.setText("");
                            ChatRecyclerView.scrollToPosition(ChatAdapter.getSizeOfChats() - 1);

                            AsyncTask.execute(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    try
                                    {

                                        //                                    Map<String,File> uploadFile = new HashMap<>() ;
                                        //                                    uploadFile.put("Message",new File(URL));
                                        //
                                        //                                    AndroidNetworking.upload(NetworkService.GetBestServer())
                                        //                                            .addMultipartParameter("ID", "5b464e90a2ef1ca5e3a659f8")
                                        //                                            .addMultipartParameter(uploadFile)
                                        //                                            .build()
                                        //                                            .setUploadProgressListener(new UploadProgressListener() {
                                        //                                                @Override
                                        //                                                public void onProgress(long bytesUploaded, long totalBytes) {
                                        //                                                    Log.d("TEST", String.valueOf((int) (100 * bytesUploaded / totalBytes)));
                                        //                                                }
                                        //                                            })
                                        //                                            .getAsString(new StringRequestListener() {
                                        //                                                @Override
                                        //                                                public void onResponse(String Response) {
                                        //
                                        //                                                    try {
                                        //                                                        JSONObject Result = new JSONObject(Response);
                                        //
                                        //                                                        Log.d("TEST", Response);
                                        //                                                    } catch (Exception e) {
                                        //                                                        Misc.Debug("WriteUI-RequestPost: " + e.toString());
                                        //                                                    }
                                        //                                                }
                                        //
                                        //                                                @Override
                                        //                                                public void onError(ANError e) {
                                        //                                                    Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                                        //                                                }
                                        //                                            });

                                        byte[] file = Misc.ReadFile(URL);

                                        JSONObject uploadObject = NetworkService.createUploadData("5b464e90a2ef1ca5e3a659f8", new String(file));
                                        Log.d("DATAFROMSERVER", uploadObject.toString());

                                        NetworkService.Emit("SendMessage", file, new SocketHandler.Callback()
                                        {
                                            @Override
                                            public void call(Object data)
                                            {
                                                Log.d("DATAFROMSERVER", data.toString());
                                                try
                                                {
                                                    JSONObject result = new JSONObject(data.toString());

                                                    if ((int) result.get("Result") == 0)

                                                        Misc.UIThread(new Runnable()
                                                        {
                                                            @Override
                                                            public void run()
                                                            {
                                                                if (AudioManager.getRingerMode() == android.media.AudioManager.RINGER_MODE_NORMAL)
                                                                    AudioPlayer.load(Activity.getBaseContext(), R.raw.sound_out, 1);
                                                            }
                                                        }, 0);

                                                }
                                                catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                    }

                    @Override
                    public void OnRemove(String URL)
                    {
                    }

                    @Override
                    public void OnSave()
                    {

                    }
                };

                if (!Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    if (PermissionRequest == null)
                        PermissionRequest = new PermissionDialog(Activity);
                    if (!PermissionRequest.isShowing())
                    {
                        PermissionRequest.SetContentView(R.drawable.z_general_permission_storage, R.string.WriteUIPermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Allow)
                            {
                                if (!Allow)
                                {
                                    Misc.ToastOld(Misc.String(R.string.PermissionStorage));
                                    return;
                                }
                            }
                        });
                        return;
                    }
                }
                Activity.GetManager().OpenView(new GalleryViewUI(Integer.MAX_VALUE, GalleryViewUI.TYPE_FILE, L), "GalleryViewUI", true);

            }
        });

        ImageButtonEmoji.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Emoji.toggle();
            }
        });

        ImageButtonImage.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Misc.closeKeyboard(Activity);

                final GalleryViewUI.GalleryListener L = new GalleryViewUI.GalleryListener()
                {
                    List<String> ImageURL = new ArrayList<>();

                    @Override
                    public void OnSelection(String URL)
                    {
                        ImageURL.add(URL);
                    }

                    @Override
                    public void OnRemove(String URL)
                    {
                        ImageURL.remove(URL);
                    }

                    @Override
                    public void OnSave()
                    {
                        if (ImageURL.size() <= 0)
                            return;

                        for (final String path : ImageURL)
                        {

                            AsyncTask.execute(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    try
                                    {
                                        int Size = Misc.ToDP(320);
                                        BitmapFactory.Options O = new BitmapFactory.Options();
                                        O.inJustDecodeBounds = true;

                                        BitmapFactory.decodeFile(path, O);

                                        O.inSampleSize = Misc.SampleSize(O, Size, Size);
                                        O.inJustDecodeBounds = false;

                                        Bitmap bitmap = BitmapFactory.decodeFile(path, O);

                                        Bitmap finalBitmap = Misc.getRoundedCornerBitmap(Misc.scale(bitmap), 100);

                                        File selectedFilePath = new File(Misc.Temp(), System.currentTimeMillis() + ".jpg");
                                        selectedFilePath.createNewFile();

                                        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                                        FileOutputStream FOS = new FileOutputStream(selectedFilePath);
                                        FOS.write(BAOS.toByteArray());
                                        FOS.flush();
                                        FOS.close();

                                        final ImageChatModel chatModel = new ImageChatModel(selectedFilePath.getAbsolutePath());

                                        Misc.UIThread(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                ChatAdapter.addChat(chatModel);
                                                ChatRecyclerView.scrollToPosition(ChatAdapter.getSizeOfChats() - 1);

                                                if (AudioManager.getRingerMode() == android.media.AudioManager.RINGER_MODE_NORMAL)
                                                    AudioPlayer.load(Activity.getBaseContext(), R.raw.sound_out, 1);

                                            }
                                        }, 0);

                                    }
                                    catch (Exception e)
                                    {
                                        Misc.Debug("WriteUI-Compress: " + e.toString());
                                    }
                                }
                            });

                        }
                    }
                };

                if (!Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    if (PermissionRequest == null)
                        PermissionRequest = new PermissionDialog(Activity);
                    if (!PermissionRequest.isShowing())
                    {
                        PermissionRequest.SetContentView(R.drawable.z_general_permission_storage, R.string.WriteUIPermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Allow)
                            {
                                if (!Allow)
                                {
                                    Misc.ToastOld(Misc.String(R.string.PermissionStorage));
                                    return;
                                }
                            }
                        });
                        return;
                    }
                }
                Activity.GetManager().OpenView(new GalleryViewUI(Integer.MAX_VALUE, GalleryViewUI.TYPE_IMAGE, L), "GalleryViewUI", true);

            }
        });

        ImageButtonVideo.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Misc.closeKeyboard(Activity);

                final GalleryViewUI.GalleryListener L = new GalleryViewUI.GalleryListener()
                {
                    String VideoURL;

                    @Override
                    public void OnSelection(String URL)
                    {
                        VideoURL = URL;
                    }

                    @Override
                    public void OnRemove(String URL)
                    {
                        VideoURL = "";
                    }

                    @Override
                    public void OnSave()
                    {
                        if (VideoURL != null && !VideoURL.equals(""))
                        {

                            // if (Build.VERSION.SDK_INT <= 17) {
                            //       Misc.ToastOld(Misc.String(R.string.WriteUICantCompress));

                            ChatAdapter.addChat(new VideoChatModel(VideoURL));
                            ChatRecyclerView.scrollToPosition(ChatAdapter.getSizeOfChats() - 1);
                            if (AudioManager.getRingerMode() == android.media.AudioManager.RINGER_MODE_NORMAL)
                                AudioPlayer.load(Activity.getBaseContext(), R.raw.sound_out, 1);
                            return;
                            //     }

                            //                            final String OldPath = VideoURL;
                            //
                            //                            final File SelectVideo = new File(Misc.Temp(), "video." + String.valueOf(System.currentTimeMillis()) + ".mp4");
                            //
                            //                            final ProgressDialog Progress = new ProgressDialog(Activity);
                            //                            Progress.setMessage(Misc.String(R.string.WriteUICompress));
                            //                            Progress.setIndeterminate(false);
                            //                            Progress.setCancelable(false);
                            //                            Progress.setMax(100);
                            //                            Progress.setProgress(0);
                            //                            Progress.show();
                            //
                            //                            MediaTransCoder.Start(OldPath, SelectVideo.getAbsolutePath(), new MediaTransCoder.MediaStrategy() {
                            //                                        @Override
                            //                                        public MediaFormat CreateVideo(MediaFormat Format) {
                            //                                            int Frame = 30;
                            //                                            int BitRate = 500000;
                            //                                            int Width = Format.getInteger(MediaFormat.KEY_WIDTH);
                            //                                            int Height = Format.getInteger(MediaFormat.KEY_HEIGHT);
                            //
                            //                                            if (Width > 640 || Height > 640) {
                            //                                                Width = Width / audio_start;
                            //                                                Height = Height / audio_start;
                            //                                            }
                            //
                            //                                            try {
                            //                                                Frame = Format.getInteger(MediaFormat.KEY_FRAME_RATE);
                            //                                            } catch (Exception e) { /* */ }
                            //
                            //                                            MediaFormat format = MediaFormat.createVideoFormat("video/avc", Width, Height);
                            //                                            format.setInteger(MediaFormat.KEY_BIT_RATE, BitRate);
                            //                                            format.setInteger(MediaFormat.KEY_FRAME_RATE, Frame);
                            //                                            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
                            //                                            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, 0x7F000789);
                            //                                            return format;
                            //                                        }
                            //
                            //                                        @Override
                            //                                        public MediaFormat CreateAudio(MediaFormat Format) {
                            //                                            int Sample = 44100;
                            //                                            int Channel = 1;
                            //
                            //                                            try {
                            //                                                Sample = Format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                            //                                            } catch (Exception e) { /* */ }
                            //                                            try {
                            //                                                Channel = Format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                            //                                            } catch (Exception e) { /* */ }
                            //
                            //                                            int Bitrate = Sample * Channel;
                            //
                            //                                            MediaFormat format = MediaFormat.createAudioFormat("audio/mp4a-latm", Sample, Channel);
                            //                                            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
                            //                                            format.setInteger(MediaFormat.KEY_BIT_RATE, Bitrate);
                            //                                            return format;
                            //                                        }
                            //                                    },
                            //                                    new MediaTransCoder.CallBack() {
                            //                                        @Override
                            //                                        public void OnProgress(double progress) {
                            //                                            Progress.setProgress((int) (((progress + 0.001) * 100) % 100));
                            //                                        }
                            //
                            //                                        @Override
                            //                                        public void OnCompleted() {
                            //                                            Progress.cancel();
                            //                                            VideoChatModel chatModel = new VideoChatModel(SelectVideo.getAbsolutePath());
                            //                                            ChatAdapter.addChat(chatModel);
                            //                                        }
                            //
                            //                                        @Override
                            //                                        public void OnFailed(Exception e) {
                            //                                            Progress.cancel();
                            //                                            Misc.Debug("WriteUI-VideoCompress: " + e.toString());
                            //
                            //                                            VideoChatModel chatModel = new VideoChatModel(OldPath);
                            //                                            ChatAdapter.addChat(chatModel);
                            //                                        }
                            //                                    });

                        }
                    }
                };

                if (!Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    if (PermissionRequest == null)
                        PermissionRequest = new PermissionDialog(Activity);
                    if (!PermissionRequest.isShowing())
                    {
                        PermissionRequest.SetContentView(R.drawable.z_general_permission_storage, R.string.WriteUIPermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Allow)
                            {
                                if (!Allow)
                                {
                                    Misc.ToastOld(Misc.String(R.string.PermissionStorage));
                                }
                            }
                        });
                        return;
                    }
                }
                Activity.GetManager().OpenView(new GalleryViewUI(Integer.MAX_VALUE, GalleryViewUI.TYPE_VIDEO, L), "GalleryViewUI", true);

            }
        });

        buttonBack.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new Message_UI(), "Chat_ListUI", false);

            }
        });

        ImageButtonAudio.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Misc.closeKeyboard(Activity);

                if (!Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    if (PermissionRequest == null)
                        PermissionRequest = new PermissionDialog(Activity);
                    if (!PermissionRequest.isShowing())
                        PermissionRequest.SetContentView(R.drawable.ic_profile123_black, R.string.WriteUIPermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Result)
                            {
                                if (!Result)
                                {
                                    Misc.ToastOld(Misc.String(R.string.PermissionStorage));
                                }
                            }
                        });
                }

                if (!Misc.CheckPermission(Manifest.permission.RECORD_AUDIO))
                {
                    if (PermissionRequest == null)
                        PermissionRequest = new PermissionDialog(Activity);
                    if (!PermissionRequest.isShowing())
                        PermissionRequest.SetContentView(R.drawable.ic_profile123_black, R.string.WriteUIPermissionMic, Manifest.permission.RECORD_AUDIO, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Result)
                            {
                                if (!Result)
                                {
                                    Misc.ToastOld(Misc.String(R.string.PermissionMic));
                                }
                            }
                        });
                }
                else
                {

                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                        {

                            if (AudioManager.getRingerMode() == android.media.AudioManager.RINGER_MODE_NORMAL)
                                AudioPlayer.load(Activity.getBaseContext(), R.raw.auido_hold, 1);
                            else
                            {
                                Vibrator vib = (Vibrator) Activity.getSystemService(Context.VIBRATOR_SERVICE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                {
                                    vib.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE));
                                }
                                else
                                {
                                    vib.vibrate(40);
                                }
                            }

                            FABAudio.setVisibility(View.VISIBLE);
                            FABAudio.animate().scaleX(1).scaleY(1).alpha(1).setDuration(100).start();

                            startRecord();

                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        {

                            FABAudio.animate().scaleX(0).scaleY(0).alpha(0).setDuration(100).start();
                            FABAudio.setVisibility(View.GONE);

                            pauseRecord();
                            if (AudioManager.getRingerMode() == android.media.AudioManager.RINGER_MODE_NORMAL)
                                AudioPlayer.load(Activity.getBaseContext(), R.raw.audio_release, 1);
                            AudioChatModel model = new AudioChatModel(Filename);

                            EditTextMessage.setText("");
                            if (!model.getLength().equals("00:00"))
                            {
                                ChatAdapter.addChat(model);
                                ChatRecyclerView.scrollToPosition(ChatAdapter.getSizeOfChats() - 1);
                            }
                        }
                    }

                }

                return true;
            }
        });

        AudioPlayer.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
        {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
            {
                AudioPlayer.play(sampleId, 1f, 1f, 10, 0, 1f);
            }
        });

        ChatRecyclerView.scrollToPosition(ChatAdapter.getSizeOfChats() - 1);

        ViewMain = view;

    }

    @Override
    public void OnPause()
    {
        keyboardHeightProvider.setKeyboardHeightObserver(null);
        keyboardHeightProvider.close();
        super.OnPause();
    }

    @Override
    public void OnResume()
    {
        super.OnResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
    }

    private void startRecord()
    {
        Recorder = new MediaRecorder();
        Filename = Misc.createFile(Misc.DIR_AUDIO, "Upload", AUDIO_RECORDER_FILE_EXT_MP3);

        Recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        Recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        Recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        Recorder.setOutputFile(Filename);
        //        Recorder.setOnErrorListener(errorListener);
        //        Recorder.setOnInfoListener(infoListener);

        try
        {
            Recorder.prepare();
            Recorder.start();
        }
        catch (IllegalStateException | IOException e)
        {
            e.printStackTrace();
        }

    }

    private void pauseRecord()
    {
        if (null != Recorder)
        {
            Recorder.reset();
            Recorder.release();
            Recorder = null;
        }

    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation)
    {

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ViewMain.findViewById(R.id.MessageControls).getLayoutParams();
        params.bottomMargin = height;

        ViewMain.findViewById(R.id.MessageControls).setLayoutParams(params);

        ChatRecyclerView.scrollToPosition(ChatAdapter.getSizeOfChats() - 1);
    }

    private class ChatAdapter extends RecyclerView.Adapter<Chat_UI.ChatAdapter.CustomViewHolder>
    {

        private ArrayList<ChatModel> MessageList = new ArrayList<>();
        private int lastPosition = -1;

        private ChatAdapter()
        {
        }

        void addChat(ChatModel chatModel)
        {
            MessageList.add(chatModel);
            notifyDataSetChanged();
        }

        int getSizeOfChats()
        {
            return MessageList.size();
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {

            switch (viewType)
            {
                case TEXT:
                {
                    return new TextViewHolder(LayoutInflater.from(Activity).inflate(R.layout.chat_text_model, parent, false));
                }

                case AUDIO:
                {
                    return new AudioViewHolder(LayoutInflater.from(Activity).inflate(R.layout.chat_audio_model, parent, false));
                }

                case IMAGE:
                {
                    return new ImageViewHolder(LayoutInflater.from(Activity).inflate(R.layout.chat_image_model, parent, false));
                }

                case VIDEO:
                {
                    return new VideoViewHolder(LayoutInflater.from(Activity).inflate(R.layout.chat_video_model, parent, false));
                }

                case FILE:
                {
                    return new FileViewHolder(LayoutInflater.from(Activity).inflate(R.layout.chat_file_model, parent, false));
                }

                default:
                    return null;
            }

        }

        @Override
        public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position)
        {
            holder.bind(position);
            setAnimation(holder.itemView, position);
        }

        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(Activity, android.R.anim.fade_in);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        @Override
        public void onViewDetachedFromWindow(final CustomViewHolder holder)
        {
            (holder).itemView.clearAnimation();
        }

        @Override
        public int getItemCount()
        {
            return (MessageList != null) ? MessageList.size() : 0;
        }

        @Override
        public int getItemViewType(int position)
        {
            return MessageList.get(position).ChatType;
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder
        {
            protected TextView TextViewTime;
            protected ImageView ImageViewSeen;
            protected TextView TextViewUserName;

            public CustomViewHolder(View itemView)
            {
                super(itemView);
                TextViewTime = itemView.findViewById(R.id.TextViewTime);
                ImageViewSeen = itemView.findViewById(R.id.ImageViewSeen);
                if (CHAT_MODE == MODE_GROUP)
                    TextViewUserName = itemView.findViewById(R.id.TextViewUserName);
            }

            public void bind(int position)
            {
                ChatModel chatModel = MessageList.get(position);
                TextViewTime.setText(chatModel.CurrentTime);
                ImageViewSeen.setVisibility((chatModel.IsSeen ? VISIBLE : GONE));
                if (CHAT_MODE == MODE_GROUP)
                    TextViewUserName.setText(chatModel.UserID);

                if (position > 0 && chatModel.UserID.equals(MessageList.get(position - 1).UserID))
                {
                    MessageList.get(position - 1).IsSecond = true;
                }

                chatModel.setLayout(itemView);
            }
        }

        private class TextViewHolder extends CustomViewHolder
        {

            private EmojiTextView TextViewChat;

            public TextViewHolder(View itemView)
            {
                super(itemView);
                TextViewChat = itemView.findViewById(R.id.TextViewMessage);

            }

            @Override
            public void bind(int position)
            {
                super.bind(position);

                String textMessage = ((TextChatModel) MessageList.get(position)).getTextMessage();
                TextViewChat.setEmojiSize(Misc.ToSP(22));
                TextViewTime.setVisibility(VISIBLE);

                if (CHAT_MODE == MODE_SINGLE)
                {
                    try
                    {
                        String regexPattern = "^[\uD83C-\uDBFF\uDC00-\uDFFF]+$";
                        byte[] utf8 = textMessage.getBytes("UTF-8");

                        String text = new String(utf8, "UTF-8");

                        Pattern pattern = Pattern.compile(regexPattern);
                        Matcher matcher = pattern.matcher(text);

                        if (matcher.find())
                        {
                            itemView.findViewById(R.id.ConstraintLayoutChat).setBackground(null);
                            TextViewTime.setVisibility(GONE);

                            switch (textMessage.length())
                            {
                                case 2:
                                    TextViewChat.setEmojiSize(Misc.ToSP(68));
                                    break;
                                case 4:
                                    TextViewChat.setEmojiSize(Misc.ToSP(50));
                                    break;
                                case 6:
                                    TextViewChat.setEmojiSize(Misc.ToSP(38));
                                    break;
                                case 8:
                                    TextViewChat.setEmojiSize(Misc.ToSP(28));
                                    break;

                            }
                        }

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }
                TextViewChat.setText(textMessage);

            }

        }

        private class AudioViewHolder extends CustomViewHolder
        {
            private ImageView ButtonPlay;
            private TextView TextViewLength;
            private SeekBar SeekBarVoice;

            private AudioHandler Player;
            private boolean isPlaying;

            public AudioViewHolder(View itemView)
            {
                super(itemView);
                Player = new AudioHandler();

                ButtonPlay = itemView.findViewById(R.id.ButtonPlay);
                TextViewLength = itemView.findViewById(R.id.TextViewLength);
                SeekBarVoice = itemView.findViewById(R.id.AudioSeekBar);
            }

            @Override
            public void bind(int position)
            {

                super.bind(position);
                final Handler SeekBarHandler = new Handler();
                final Runnable SeekBarRunnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SeekBarVoice.setProgress(Player.getPlayer().getCurrentPosition());
                        SeekBarHandler.postDelayed(this, 50);
                    }
                };

                ButtonPlay.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        if (!isPlaying && !isAudioPlaying)
                        {
                            ButtonPlay.setImageResource(R.drawable.ic_pause_white_256dp);

                            Player.seekTo(SeekBarVoice.getProgress());

                            Player.play();

                            SeekBarHandler.postDelayed(SeekBarRunnable, 0);
                            isPlaying = true;
                            isAudioPlaying = true;
                        }
                        else
                        {
                            ButtonPlay.setImageResource(R.drawable.ic_play_arrow_white_256dp);
                            Player.pause();
                            isPlaying = false;
                            isAudioPlaying = false;
                        }

                    }
                });

                AudioChatModel audioChatModel = (AudioChatModel) MessageList.get(position);
                TextViewLength.setText(audioChatModel.getLength());

                Player.getPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener()
                {
                    @Override
                    public void onPrepared(MediaPlayer mp)
                    {
                        Player.setState(AudioHandler.MP_STATES.MPS_PREPARED);
                        SeekBarVoice.setMax(Player.getPlayer().getDuration());
                    }

                });

                Player.getPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        ButtonPlay.setImageResource(R.drawable.ic_play_arrow_white_256dp);
                        SeekBarHandler.removeCallbacks(SeekBarRunnable);
                        SeekBarVoice.setProgress(0);
                        isPlaying = false;
                        isAudioPlaying = false;
                    }
                });
                SeekBarVoice.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        if (fromUser)
                            Player.seekTo(progress);
                        else if (progress == seekBar.getMax())
                        {
                            ButtonPlay.setImageResource(R.drawable.ic_play_arrow_white_256dp);
                            SeekBarHandler.removeCallbacks(SeekBarRunnable);
                            SeekBarVoice.setProgress(0);
                            isPlaying = false;
                            isAudioPlaying = false;
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar)
                    {
                        Player.pause();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar)
                    {
                        //  play();
                    }
                });

                Player.setData(audioChatModel.getFile().getAbsolutePath());

            }

        }

        private class ImageViewHolder extends CustomViewHolder
        {
            private ImageView ImageViewMain;

            public ImageViewHolder(View itemView)
            {
                super(itemView);
                ImageViewMain = itemView.findViewById(R.id.ImageViewMainImage);
            }

            @Override
            public void bind(final int position)
            {

                super.bind(position);

                ImageChatModel imageChatModel = (ImageChatModel) MessageList.get(position);
                Bitmap bitmap = imageChatModel.buildBitmap();
                ImageViewMain.setImageDrawable(new BitmapDrawable(Misc.Blurry(Misc.ChangeBrightness(bitmap, Misc.DARKEN_BITMAP))));
                itemView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Activity.GetManager().OpenView(new ImagePreviewUI(((ImageChatModel) MessageList.get(getAdapterPosition())).getFile().getAbsolutePath(), false), "ImagePreviewUI", true);

                    }
                });

            }

        }

        private class VideoViewHolder extends CustomViewHolder
        {
            private ImageButton ImageButtonPlay;
            private TextView TextViewSize;
            private ImageView ImageViewMain;
            private TextView TextViewLength;

            public VideoViewHolder(View itemView)
            {
                super(itemView);
                TextViewLength = itemView.findViewById(R.id.TextViewLength);
                TextViewSize = itemView.findViewById(R.id.TextViewSize);
                ImageViewMain = itemView.findViewById(R.id.ImageViewMainImage);
                ImageButtonPlay = itemView.findViewById(R.id.ImageButtonPlay);
            }

            @Override
            public void bind(final int position)
            {

                super.bind(position);

                final VideoChatModel videoChatModel = (VideoChatModel) MessageList.get(position);
                TextViewLength.setText(videoChatModel.getLength());
                TextViewSize.setText(((VideoChatModel) MessageList.get(position)).getSize());

                Bitmap bitmap = Misc.getRoundedCornerBitmap(Misc.scale(videoChatModel.buildBitmap()), 20);

                ImageViewMain.setImageDrawable(new BitmapDrawable(Misc.Blurry(Misc.ChangeBrightness(bitmap, Misc.DARKEN_BITMAP))));

                itemView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Activity.GetManager().OpenView(new VideoPreviewUI(videoChatModel.getFile().getAbsolutePath(), true, true), "VideoPreviewUI", true);

                    }
                });
                ImageButtonPlay.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Activity.GetManager().OpenView(new VideoPreviewUI(videoChatModel.getFile().getAbsolutePath(), true, true), "VideoPreviewUI", true);

                    }
                });
            }

        }

        private class FileViewHolder extends CustomViewHolder
        {

            private TextView TextViewName;
            private TextView TextViewDetail;
            private ImageButton ImageButtonDownload;

            public FileViewHolder(View itemView)
            {
                super(itemView);
                TextViewName = itemView.findViewById(R.id.TextViewFileName);
                TextViewDetail = itemView.findViewById(R.id.TextViewFileDetail);
                ImageButtonDownload = itemView.findViewById(R.id.ImageButtonDownload);

            }

            @Override
            public void bind(final int position)
            {

                super.bind(position);

                final FileChatModel fileChatModel = (FileChatModel) MessageList.get(position);
                TextViewName.setText((fileChatModel.getFileName()));
                TextViewDetail.setText((fileChatModel.getFileDetail()));
                ImageButtonDownload.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!fileChatModel.IsDownloaded)
                        {
                            fileChatModel.saveFile();
                        }

                        MimeTypeMap map = MimeTypeMap.getSingleton();
                        String ext = MimeTypeMap.getFileExtensionFromUrl(((FileChatModel) MessageList.get(getAdapterPosition())).getFile().getName());
                        String type = map.getMimeTypeFromExtension(ext);

                        if (type == null)
                            type = "*/*";

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri data = Uri.fromFile(((FileChatModel) MessageList.get(getAdapterPosition())).getFile());

                        intent.setDataAndType(data, type);

                        Activity.startActivity(Intent.createChooser(intent, "Select An App"));
                    }
                });

            }
        }

    }

    private abstract class ChatModel
    {

        String UserID;
        String CurrentTime;
        boolean IsFromUser;
        boolean IsSeen;
        boolean IsSecond;
        int ChatType;

        public ChatModel(boolean isFromUser, boolean isSeen, int chatType)
        {
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            UserID = "SOH_MIL";
            CurrentTime = today.format("%k:%M");
            IsFromUser = false;
            IsSeen = isSeen;
            ChatType = chatType;
        }

        public void setLayout(View view)
        {
            View chatModel = view.findViewById(R.id.ConstraintLayoutChat);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chatModel.getLayoutParams();

            TextView timeTextView = view.findViewById(R.id.TextViewTime);
            timeTextView.setTypeface(Misc.GetTypeface());
            LinearLayout rootView = (LinearLayout) view.getRootView();
            TextView UserNameTextView = null;

            if (CHAT_MODE == MODE_GROUP)
            {
                UserNameTextView = view.findViewById(R.id.TextViewUserName);
                UserNameTextView.setVisibility(VISIBLE);
                UserNameTextView.setTypeface(Misc.GetTypeface());
            }

            if (IsFromUser)
            {
                if (ChatType != IMAGE || ChatType != VIDEO)
                    chatModel.setBackgroundResource(IsSecond ? R.drawable.z_blue_chat_background_round : R.drawable.z_blue_chat_background);

                timeTextView.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));
                params.setMarginStart(Misc.ToDP(40));
                params.setMarginEnd(Misc.ToDP((ChatType != IMAGE || ChatType != VIDEO) ? 16 : 8));
                rootView.setGravity(Gravity.END);

                if (IsSeen)
                    view.findViewById(R.id.ImageViewSeen).setVisibility(View.VISIBLE);

                if (CHAT_MODE == MODE_GROUP)
                {
                    UserNameTextView.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));
                }

            }
            else
            {
                if (ChatType == IMAGE || ChatType == VIDEO)
                {
                    timeTextView.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));
                }
                else
                {
                    chatModel.setBackgroundResource(IsSecond ? R.drawable.z_white_chat_background_round : R.drawable.z_white_chat_background);
                    timeTextView.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.TextWhite, null));
                }

                params.setMarginStart(Misc.ToDP((ChatType != IMAGE || ChatType != VIDEO) ? 16 : 8));
                params.setMarginEnd(Misc.ToDP(40));
                rootView.setGravity(Gravity.START);

                view.findViewById(R.id.ImageViewSeen).setVisibility(View.GONE);

                if (CHAT_MODE == MODE_GROUP)
                {
                    UserNameTextView.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.TextWhite, null));
                }
            }
            chatModel.setLayoutParams(params);

        }

    }

    public class TextChatModel extends ChatModel
    {

        private String TextMessage;

        public TextChatModel(String textMessage)
        {
            super(true, false, TEXT);
            TextMessage = textMessage;
        }

        @Override
        public void setLayout(View view)
        {
            super.setLayout(view);

            TextView textView = view.findViewById(R.id.TextViewMessage);
            textView.setTypeface(Misc.GetTypeface());
            if (IsFromUser)
                textView.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));
            else
                textView.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.TextWhite, null));

        }

        public String getTextMessage()
        {
            return TextMessage;
        }

        public void setTextMessage(String textMessage)
        {
            TextMessage = textMessage;
        }

    }

    public class BaseFileChatModel extends ChatModel
    {
        File File;
        boolean IsDownloaded;

        public BaseFileChatModel(String filePath)
        {
            super(true, false, FILE);
            this.File = new File(filePath);
            IsDownloaded = false;
        }

        public BaseFileChatModel(String filePath, boolean isFromUser, boolean isSeen, int chatType)
        {
            super(isFromUser, isSeen, chatType);
            if (filePath == null)
            {
                switch (chatType)
                {
                    case IMAGE:
                    {
                        filePath = "";
                    }
                }
            }
            this.File = new File(filePath);
            if (isFromUser)
                this.IsDownloaded = true;
            else
                this.IsDownloaded = this.File.exists();

        }

        public File getFile()
        {
            return File;
        }

        public void setFile(File file)
        {
            File = file;
        }

        public String getFileName(boolean fullName)
        {
            if (fullName)
                return File.getName();
            else
                return File.getName().length() <= 18 ? File.getName() : File.getName().substring(0, Math.min(File.getName().length(), 18)) + "...";
        }

        public String getFileName()
        {
            return this.getFileName(false);
        }

        public String getFileDetail()
        {
            return (new DecimalFormat("#.##").format((double) File.length() / 1048576.0) + " " + Misc.String(R.string.WriteUIMB) + " / " + getExtension());
        }

        public String getExtension()
        {
            return File.getName().substring(File.getName().lastIndexOf(".")).substring(1).toUpperCase();
        }

        public boolean saveFile()
        {
            String output = Misc.createFile(ChatType);
            if (!File.exists())
            {
                try
                {
                    File file = new File(output + this.getFileName(true));
                    file.createNewFile();

                    // TODO Write File Content

                    this.File = file;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;

        }

    }

    public class FileChatModel extends BaseFileChatModel
    {

        public FileChatModel(String filePath)
        {
            super(filePath);
        }

        @Override
        public void setLayout(View view)
        {
            super.setLayout(view);

            LinearLayout.LayoutParams params;
            if (CHAT_MODE == MODE_GROUP)
                params = new LinearLayout.LayoutParams(Misc.ToDP(260), Misc.ToDP(100));
            else
                params = new LinearLayout.LayoutParams(Misc.ToDP(260), Misc.ToDP(80));

            if (IsFromUser)
                params.setMargins(Misc.ToDP(40), Misc.ToDP(8), Misc.ToDP(12), Misc.ToDP(16));
            else
                params.setMargins(Misc.ToDP(16), Misc.ToDP(8), Misc.ToDP(12), Misc.ToDP(40));

            view.findViewById(R.id.ConstraintLayoutChat).setLayoutParams(params);

            CircularProgressView progressView = view.findViewById(R.id.ProgressBar);
            TextView fileName = view.findViewById(R.id.TextViewFileName);
            TextView fileDetail = view.findViewById(R.id.TextViewFileDetail);

            ImageButton downloadIcon = view.findViewById(R.id.ImageButtonDownload);
            if (IsDownloaded)
                downloadIcon.setImageResource(R.drawable.__gallery_file);

            fileName.setTypeface(Misc.GetTypeface());
            fileDetail.setTypeface(Misc.GetTypeface());

            if (IsFromUser)
            {
                fileName.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));
                fileDetail.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));
                view.findViewById(R.id.ImageButtonDownload).setBackgroundResource(R.drawable.z_white_chat_file_bg);
                progressView.setColor(Color.parseColor("#000000"));

                //                else
                //                    ((ImageButton) view.findViewById(R.id.ImageButtonDownload)).setImageResource(R.drawable._general_download);

            }
            else
            {
                fileName.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.TextWhite, null));
                fileDetail.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.TextWhite, null));
                view.findViewById(R.id.ImageButtonDownload).setBackgroundResource(R.drawable.z_blue_chat_file_bg);
                downloadIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                progressView.setColor(Color.parseColor("#848484"));

                //                else
                //                    ((ImageButton) view.findViewById(R.id.ImageButtonDownload)).setImageResource(R.drawable._general_download);a
            }

        }
    }

    public class ImageChatModel extends BaseFileChatModel
    {

        public ImageChatModel(String filePath)
        {
            super(filePath, true, false, IMAGE);

        }

        public Bitmap buildBitmap()
        {
            return BitmapFactory.decodeFile(getFile().getAbsolutePath());
        }

        @Override
        public void setLayout(View view)
        {
            super.setLayout(view);

            if (CHAT_MODE == MODE_GROUP)
                ((TextView) view.findViewById(R.id.TextViewUserName)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));

        }
    }

    public class AudioChatModel extends BaseFileChatModel
    {

        public AudioChatModel(String audioPath)
        {
            super(audioPath, true, false, AUDIO);
        }

        @Override
        public void setLayout(View view)
        {

            super.setLayout(view);

            TextView lenghtText = view.findViewById(R.id.TextViewLength);
            lenghtText.setTypeface(Misc.GetTypeface());

            if (IsFromUser)
            {
                lenghtText.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));
                ((SeekBar) view.findViewById(R.id.AudioSeekBar)).getThumb().setColorFilter(Color.parseColor("#fff"), PorterDuff.Mode.SRC_ATOP);
                ((ImageView) view.findViewById(R.id.ButtonPlay)).setColorFilter(Color.parseColor("#fff"), PorterDuff.Mode.SRC_IN);

            }
            else
            {
                lenghtText.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.TextWhite, null));
                ((SeekBar) view.findViewById(R.id.AudioSeekBar)).getThumb().setColorFilter(Color.parseColor("#49a4ff"), PorterDuff.Mode.SRC_ATOP);
                ((ImageView) view.findViewById(R.id.ButtonPlay)).setColorFilter(Color.parseColor("#49a4ff"), PorterDuff.Mode.SRC_IN);

            }

        }

        public String getLength()
        {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getFile().getPath());
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(time) / 1000;
            long minutes = duration / 60;
            long seconds = duration % 60;

            return ((minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0" + seconds : seconds);
        }

        public String getSize()
        {
            return new DecimalFormat("#.##").format((double) getFile().length() / 1048576.0) + "MB";
        }

    }

    public class VideoChatModel extends BaseFileChatModel
    {

        public VideoChatModel(String videoPath)
        {
            super(videoPath, true, false, VIDEO);
        }

        public String getLength()
        {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getFile().getPath());
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(time) / 1000;
            long minutes = duration / 60;
            long seconds = duration % 60;

            return ((minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0" + seconds : seconds);
        }

        public String getSize()
        {
            return new DecimalFormat("#.##").format((double) getFile().length() / 1048576.0) + "MB";
        }

        public Bitmap buildBitmap()
        {

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getFile().getAbsolutePath());

            return retriever.getFrameAtTime(50);
        }

        @Override
        public void setLayout(View view)
        {
            super.setLayout(view);

            ((TextView) view.findViewById(R.id.TextViewLength)).setTypeface(Misc.GetTypeface());
            ((TextView) view.findViewById(R.id.TextViewSize)).setTypeface(Misc.GetTypeface());

            if (CHAT_MODE == MODE_GROUP)
                ((TextView) view.findViewById(R.id.TextViewUserName)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.ActionBarWhite, null));

        }
    }
}

