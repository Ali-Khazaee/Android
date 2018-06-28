package co.biogram.main.ui.messenger;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.component.CircleImageView;

import java.util.ArrayList;

public class Chat_UI extends FragmentView implements View.OnClickListener
{
    private ChatAdapter AdapterMain;
    private RecyclerView RecyclerViewMain;

    private EditText EditTextMessage;
    private ImageView ImageViewSend;

    private String Target;

    private MediaPlayer MediaPlayerSend;

    Chat_UI(String target)
    {
        Target = target;
    }

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.messenger_chat, null);

        view.findViewById(R.id.ImageViewBack).setOnClickListener(this);
        view.findViewById(R.id.ImageViewOption).setOnClickListener(this);

        CircleImageView CircleImageViewProfile = view.findViewById(R.id.CircleImageViewProfile);
        CircleImageViewProfile.setOnClickListener(this);

        TextView TextViewUsername = view.findViewById(R.id.TextViewUsername);
        TextView TextViewSeen = view.findViewById(R.id.TextViewSeen);

        TextViewUsername.setText("Ali QQ <3");
        TextViewSeen.setText("10 days ago");

        RecyclerViewMain = view.findViewById(R.id.RecyclerViewMain);
        RecyclerViewMain.setLayoutManager(new LinearLayoutManager(Activity));
        RecyclerViewMain.setAdapter(AdapterMain = new ChatAdapter());
        RecyclerViewMain.scrollToPosition(AdapterMain.getItemCount() - 1);

        ImageViewSend = view.findViewById(R.id.ImageViewSend);
        ImageViewSend.setOnClickListener(this);

        EditTextMessage = view.findViewById(R.id.EditTextMessage);
        EditTextMessage.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ImageViewSend.setImageResource(s.length() == 0 ? R.drawable.xxx_messenger_chat_send_gray : R.drawable.xxx_messenger_chat_send);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        ViewMain = view;
    }

    @Override
    public void OnResume()
    {
        MediaPlayerSend = MediaPlayer.create(Activity, R.raw.message_send);
    }

    @Override
    public void OnPause()
    {
        MediaPlayerSend.release();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ImageViewBack:
            {
                Activity.onBackPressed();
                break;
            }
            case R.id.ImageViewOption:
            {
                // TODO Add Me
                break;
            }
            case R.id.ImageViewSend:
            {
                if (!EditTextMessage.getText().toString().equals(""))
                {
                    AdapterMain.AddText(EditTextMessage.getText().toString());
                    RecyclerViewMain.scrollToPosition(AdapterMain.getItemCount() - 1);
                    EditTextMessage.setText("");
                    MediaPlayerSend.start();
                }
                break;
            }
        }
    }

    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private ArrayList<ChatEntity> ChatList = new ArrayList<>();

        private static final int TYPE_TEXT = 0;
        private static final int TYPE_IMAGE = 1;
        private static final int TYPE_VIDEO = 2;
        private static final int TYPE_VOICE = 3;
        private static final int TYPE_FILE = 4;
        // private static final int TYPE_GIF = 5;
        // private static final int TYPE_STICKER = 6;
        // private static final int TYPE_VOTE = 7;
        // private static final int TYPE_QUERY = 8;


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup Parent, int ViewType)
        {
            switch (ViewType)
            {
                case TYPE_IMAGE:
                    return new ViewHolderText(View.inflate(Activity, R.layout.messenger_chat_list_row, null));
                case TYPE_VIDEO:
                    return new ViewHolderText(View.inflate(Activity, R.layout.messenger_chat_list_row, null));
                case TYPE_VOICE:
                    return new ViewHolderText(View.inflate(Activity, R.layout.messenger_chat_list_row, null));
                case TYPE_FILE:
                    return new ViewHolderText(View.inflate(Activity, R.layout.messenger_chat_list_row, null));
                default:
                    return new ViewHolderText(View.inflate(Activity, R.layout.messenger_chat_list_row, null));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder Holder, int Position)
        {
            switch (Holder.getItemViewType())
            {
                case TYPE_TEXT:
                    ((ViewHolderText) Holder).BindView(Position);
                    break;
                case TYPE_IMAGE:
                    ((ViewHolderText) Holder).BindView(Position);
                    break;
                case TYPE_VIDEO:
                    ((ViewHolderText) Holder).BindView(Position);
                    break;
                case TYPE_VOICE:
                    ((ViewHolderText) Holder).BindView(Position);
                    break;
                case TYPE_FILE:
                    ((ViewHolderText) Holder).BindView(Position);
                    break;
            }
        }

        @Override
        public int getItemViewType(int Position)
        {
            return ChatList.get(Position).Type;
        }

        @Override
        public int getItemCount()
        {
            return ChatList.size();
        }

        void AddText(String Message)
        {
            ChatList.add(new ChatEntity(0, TYPE_TEXT, Misc.GetString("ID"), Message, "", Misc.Time()));
        }

        class ViewHolderText extends RecyclerView.ViewHolder
        {
            ViewHolderText(View view)
            {
                super(view);
            }

            void BindView(int Position)
            {

            }
        }

        class ChatEntity
        {
            int ID;
            int Type;
            String Sender;
            String Message;
            String Url;
            long Time;

            ChatEntity(int id, int type, String sender, String message, String url, long time)
            {
                ID = id;
                Type = type;
                Sender = sender;
                Message = message;
                Url = url;
                Time = time;
            }
        }
    }



    /*

        Emoji = EmojiPopup.Builder.fromRootView(view).build((EmojiEditText) EditTextMessage);

        keyboardHeightProvider = new KeyboardHeightProvider(Activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            AudioPlayer = new SoundPool.Builder().setMaxStreams(4).build();
        else
            AudioPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 20);

        view.post(new Runnable()
        {
            public void run()
            {
                keyboardHeightProvider.start();
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




    public static final int MODE_SINGLE = 0;
    public static final int MODE_GROUP = 1;

    private static final String TAQ = "CHAT_UI";
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP3 = ".mp3";

    private final int TEXT = 1;
    private final int IMAGE = 2;
    private final int VIDEO = 3;
    private final int AUDIO = 4;
    private final int FILE = 5;

    private EditText EditTextMessage;
    private FloatingActionButton FABAudio;
    private ImageButton ImageButtonAudio;
    private ImageButton ImageButtonImage;
    private ImageButton ImageButtonVideo;
    private ImageButton ImageButtonAttach;
    private ImageButton ImageButtonSend;
    private ImageButton ImageButtonEmoji;
    private RecyclerView ChatRecyclerView;
    private ChatAdapter ChatAdapter;

    private MediaRecorder Recorder;
    private SoundPool AudioPlayer;
    private EmojiPopup Emoji;
    private PermissionDialog PermissionRequest;
    private int CHAT_MODE;

    private int[] output_formats = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
    private String[] file_types = { AUDIO_RECORDER_FILE_EXT_MP3, AUDIO_RECORDER_FILE_EXT_3GP };
    private String Filename;

    private boolean isSendIconGray = true;
    private boolean isAudioPlaying;

    private KeyboardHeightProvider keyboardHeightProvider;

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

    @Override
    public void OnDestroy()
    {
        super.OnDestroy();
    }



    private void setData()
    {
        ChatAdapter.addChat(new TextChatModel("Hello World"));
        ChatAdapter.addChat(new TextChatModel("Building The Message System"));
        ChatAdapter.addChat(new TextChatModel("User Message is Like This"));
    }

    private void startRecord()
    {
        Recorder = new MediaRecorder();
        Filename = Misc.createFile(Misc.DIR_AUDIO, "Upload", file_types[ 0 ]);

        Recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        Recorder.setOutputFormat(output_formats[ 0 ]);
        Recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        Recorder.setOutputFile(Filename);
        Recorder.setOnErrorListener(errorListener);
        Recorder.setOnInfoListener(infoListener);

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
    public boolean onTouch(View v, MotionEvent event)
    {

        Misc.closeKeyboard(Activity);

        if (!Misc.CheckPermission(Manifest.permission.RECORD_AUDIO))
        {
            if (PermissionRequest == null)
                PermissionRequest = new PermissionDialog(Activity);
            if (!PermissionRequest.isShowing())
                PermissionRequest.SetContentView(R.drawable.xxx_messenger_chat_voice, R.string.PermissionMic, Manifest.permission.RECORD_AUDIO, new PermissionDialog.OnChoiceListener()
                {
                    @Override
                    public void OnChoice(boolean Result)
                    {
                    }
                });
        }

        //        if (!Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
        //            if (PermissionRequest == null)
        //                PermissionRequest = new PermissionDialog(Activity);
        //            if (!PermissionRequest.isShowing())
        //                PermissionRequest.SetContentView(R.drawable.xxx_messenger_chat_voice, R.string.PermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener() {
        //                    @Override
        //                    public void OnChoice(boolean Result) {
        //                    }
        //                });
        //        }
        else if (v.getId() == R.id.ImageButtonAudio)
        {

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                {

                    FABAudio.setVisibility(View.VISIBLE);

                    AnimationSet anim = new AnimationSet(true);

                    ScaleAnimation scaleAnim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);

                    anim.addAnimation(scaleAnim);
                    anim.addAnimation(alphaAnim);
                    anim.setDuration(100);
                    anim.setInterpolator(new AccelerateDecelerateInterpolator());

                    anim.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            FABAudio.clearAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {

                        }

                    });

                    FABAudio.startAnimation(anim);

                    startRecord();
                    AudioPlayer.load(Activity.getBaseContext(), R.raw.auido_hold, 1);

                    break;
                }
                case MotionEvent.ACTION_UP:
                {

                    AnimationSet anim = new AnimationSet(true);

                    ScaleAnimation scaleAnim = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);

                    anim.addAnimation(scaleAnim);
                    anim.addAnimation(alphaAnim);
                    anim.setDuration(100);
                    anim.setInterpolator(new AccelerateDecelerateInterpolator());

                    anim.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            FABAudio.setVisibility(View.GONE);
                            FABAudio.clearAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {

                        }

                    });

                    FABAudio.startAnimation(anim);

                    pauseRecord();

                    AudioChatModel model = new AudioChatModel(Filename);

                    AudioPlayer.load(Activity.getBaseContext(), R.raw.audio_release, 1);

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
    public void onKeyboardHeightChanged(int height, int orientation)
    {

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ViewMain.findViewById(R.id.MessageControls).getLayoutParams();
        params.bottomMargin = height;

        ViewMain.findViewById(R.id.MessageControls).setLayoutParams(params);

        ChatRecyclerView.scrollToPosition(ChatAdapter.getSizeOfChats() - 1);
        Log.d(TAQ, String.valueOf(height));
    }

    private class ChatAdapter extends RecyclerView.AdapterMain<Chat_UI.ChatAdapter.CustomViewHolder>
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
            return MessageList.get(position).getChatType();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolderMain
        {
            protected TextView TextViewTime;
            protected ImageView ImageViewSeen;

            public CustomViewHolder(View itemView)
            {
                super(itemView);
                TextViewTime = itemView.findViewById(R.id.TextViewTime);
                ImageViewSeen = itemView.findViewById(R.id.ImageViewSeen);
            }

            public void bind(int position)
            {
                TextViewTime.setText(MessageList.get(position).getCurrentTime());
                ImageViewSeen.setVisibility((MessageList.get(position).isSeen() ? View.VISIBLE : View.GONE));

                if (position > 0 && MessageList.get(position).isFromUser() == MessageList.get(position - 1).isFromUser())
                {
                    MessageList.get(position - 1).setSecond(true);
                }

                MessageList.get(position).setLayout(itemView);

            }
        }

        private class TextViewHolder extends CustomViewHolder
        {

            private TextView TextViewChat;

            public TextViewHolder(View itemView)
            {
                super(itemView);
                TextViewChat = itemView.findViewById(R.id.TextViewMessage);
            }

            @Override
            public void bind(int position)
            {
                super.bind(position);

                TextViewChat.setText(((TextChatModel) MessageList.get(position)).getTextMessage());

            }

        }

        private class AudioViewHolder extends CustomViewHolder implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnPreparedListener
        {
            private Button ButtonPlay;
            private TextView TextViewLength;
            private SeekBar SeekBarVoice;

            private AudioHandler Player;
            private boolean isPlaying;

            private Handler SeekBarHandler = new Handler();
            private Runnable SeekBarRunnable = null;

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

                ButtonPlay.setOnClickListener(this);

                TextViewLength.setText(((AudioChatModel) MessageList.get(position)).getLength());

                Player.getPlayer().setOnPreparedListener(this);
                SeekBarVoice.setOnSeekBarChangeListener(this);

                Player.setData(((AudioChatModel) MessageList.get(position)).getFile().getAbsolutePath());

            }

            @Override
            public void onClick(View v)
            {

                if (!isPlaying && !isAudioPlaying)
                {
                    ButtonPlay.setBackgroundResource(R.drawable.ic_pause_white_256dp);

                    Player.seekTo(SeekBarVoice.getProgress());

                    Player.play();

                    Log.d(TAQ, String.valueOf(Player.getPlayer().getDuration()));

                    SeekBarRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            SeekBarVoice.setProgress(Player.getPlayer().getCurrentPosition());
                            SeekBarHandler.postDelayed(this, 50);
                        }
                    };
                    SeekBarHandler.postDelayed(SeekBarRunnable, 0);
                    isPlaying = true;
                    isAudioPlaying = true;
                }
                else
                {
                    ButtonPlay.setBackgroundResource(R.drawable.ic_play_arrow_white_256dp);
                    Player.pause();
                    isPlaying = false;
                    isAudioPlaying = false;
                }

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                    Player.seekTo(progress);
                else if (progress == seekBar.getMax())
                {
                    ButtonPlay.setBackgroundResource(R.drawable.ic_play_arrow_white_256dp);
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

            @Override
            public void onPrepared(MediaPlayer mp)
            {
                Player.setState(AudioHandler.MP_STATES.MPS_PREPARED);
                SeekBarVoice.setMax(Player.getPlayer().getDuration());
            }

        }

        private class ImageViewHolder extends CustomViewHolder implements View.OnClickListener
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

                Bitmap bitmap = ((ImageChatModel) MessageList.get(position)).buildBitmap();
                ConstraintLayout.LayoutParams layoutParams;
                if (bitmap != null)
                    layoutParams = new ConstraintLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
                else
                    layoutParams = new ConstraintLayout.LayoutParams(400, 400);

                ImageViewMain.setLayoutParams(layoutParams);
                ImageViewMain.setBackground(new BitmapDrawable(bitmap));
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new ImagePreviewUI(((ImageChatModel) MessageList.get(getAdapterPosition())).getFile().getAbsolutePath(), false), "ImagePreviewUI", false);

            }
        }

        private class VideoViewHolder extends CustomViewHolder implements View.OnClickListener
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

                TextViewLength.setText(((VideoChatModel) MessageList.get(position)).getLength());
                TextViewSize.setText(((AudioChatModel) MessageList.get(position)).getSize());

                Bitmap bitmap = Misc.scale(((VideoChatModel) MessageList.get(position)).buildBitmap());

                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());

                ImageViewMain.setLayoutParams(layoutParams);
                ImageViewMain.setBackground(new BitmapDrawable(bitmap));
                itemView.setOnClickListener(this);
                ImageButtonPlay.setOnClickListener(this);
            }

            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new VideoPreviewUI(((VideoChatModel) MessageList.get(getAdapterPosition())).getFile().getAbsolutePath(), true, false), "VideoPreviewUI", true);
            }
        }

        private class FileViewHolder extends CustomViewHolder implements View.OnClickListener
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

                TextViewName.setText((((FileChatModel) MessageList.get(position)).getFileName()));
                TextViewDetail.setText((((FileChatModel) MessageList.get(position)).getFileDetail()));
                ImageButtonDownload.setOnClickListener(this);

            }

            @Override
            public void onClick(View v)
            {
                if (!((BaseFileChatModel) MessageList.get(getAdapterPosition())).isDownloaded())
                {
                    ((FileChatModel) MessageList.get(getAdapterPosition())).saveFile();
                }

                MimeTypeMap map = MimeTypeMap.getSingleton();
                String ext = MimeTypeMap.getFileExtensionFromUrl(((FileChatModel) MessageList.get(getAdapterPosition())).getFile().getName());
                String type = map.getMimeTypeFromExtension(ext);

                if (type == null)
                    type = "/";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.fromFile(((FileChatModel) MessageList.get(getAdapterPosition())).getFile());

                intent.setDataAndType(data, type);

                Activity.startActivity(Intent.createChooser(intent, "Select An App"));
            }
        }

    }

    private abstract class ChatModel
    {

        private String UserID;
        private String CurrentTime;
        private boolean IsFromUser;
        private boolean IsSeen;
        private boolean IsSecond;
        private int ChatType;

        public ChatModel(boolean isFromUser, boolean isSeen, int chatType)
        {
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            CurrentTime = today.format("%k:%M");
            IsFromUser = isFromUser;
            IsSeen = isSeen;
            ChatType = chatType;
        }

        public String getCurrentTime()
        {
            return CurrentTime;
        }

        public void setCurrentTime(String currentTime)
        {
            CurrentTime = currentTime;
        }

        public boolean isFromUser()
        {
            return IsFromUser;
        }

        public void setFromUser(boolean fromUser)
        {
            IsFromUser = fromUser;
        }

        public boolean isSeen()
        {
            return IsSeen;
        }

        public void setSeen(boolean seen)
        {
            IsSeen = seen;
        }

        public int getChatType()
        {
            return ChatType;
        }

        public void setChatType(int chatType)
        {
            ChatType = chatType;
        }

        public void setLayout(View view)
        {
            View chatModel = view.findViewById(R.id.ConstraintLayoutChat);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chatModel.getLayoutParams();

            if (isFromUser())
            {
                if (getChatType() != IMAGE || getChatType() != VIDEO)
                    chatModel.setBackgroundResource(isSecond() ? R.drawable.z_blue_chat_background_round : R.drawable.z_blue_chat_background);

                ((TextView) view.findViewById(R.id.TextViewTime)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
                params.setMarginStart(Misc.ToDP(40));
                params.setMarginEnd(Misc.ToDP((getChatType() != IMAGE || getChatType() != VIDEO) ? 16 : 8));
                ((LinearLayout) view.getRootView()).setGravity(Gravity.END);

                if (isSeen())
                    view.findViewById(R.id.ImageViewSeen).setVisibility(View.VISIBLE);

            }
            else
            {
                if (getChatType() == IMAGE || getChatType() == VIDEO)
                {
                    ((TextView) view.findViewById(R.id.TextViewTime)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
                }
                else
                {
                    chatModel.setBackgroundResource(isSecond() ? R.drawable.z_white_chat_background_round : R.drawable.z_white_chat_background);
                    ((TextView) view.findViewById(R.id.TextViewTime)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
                }

                params.setMarginStart(Misc.ToDP((getChatType() != IMAGE || getChatType() != VIDEO) ? 16 : 8));
                params.setMarginEnd(Misc.ToDP(40));
                ((LinearLayout) view.getRootView()).setGravity(Gravity.START);

                view.findViewById(R.id.ImageViewSeen).setVisibility(View.GONE);
            }
            chatModel.setLayoutParams(params);

        }

        public boolean isSecond()
        {
            return IsSecond;
        }

        public void setSecond(boolean second)
        {
            IsSecond = second;
        }

        public String getUserID()
        {
            return UserID;
        }

        public void setUserID(String userID)
        {
            UserID = userID;
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

            if (isFromUser())
                ((TextView) view.findViewById(R.id.TextViewMessage)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
            else
                ((TextView) view.findViewById(R.id.TextViewMessage)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));

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
        private File File;
        private boolean IsDownloaded;

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
            String output = Misc.createFile(getChatType());
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

        public boolean isDownloaded()
        {
            return IsDownloaded;
        }

        public void setDownloaded(boolean downloaded)
        {
            IsDownloaded = downloaded;
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

            if (isFromUser())
            {
                ((TextView) view.findViewById(R.id.TextViewFileName)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
                ((TextView) view.findViewById(R.id.TextViewFileDetail)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
                view.findViewById(R.id.ImageButtonDownload).setBackgroundResource(R.drawable.z_white_chat_file_bg);
                if (isDownloaded())
                    ((ImageButton) view.findViewById(R.id.ImageButtonDownload)).setImageResource(R.drawable.__gallery_file);
                //                else
                //                    ((ImageButton) view.findViewById(R.id.ImageButtonDownload)).setImageResource(R.drawable._general_download);

            }
            else
            {
                ((TextView) view.findViewById(R.id.TextViewFileName)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
                ((TextView) view.findViewById(R.id.TextViewFileDetail)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
                view.findViewById(R.id.ImageButtonDownload).setBackgroundResource(R.drawable.z_blue_chat_file_bg);
                if (isDownloaded())
                    ((ImageButton) view.findViewById(R.id.ImageButtonDownload)).setImageResource(R.drawable.__gallery_folder);
                //                else
                //                    ((ImageButton) view.findViewById(R.id.ImageButtonDownload)).setImageResource(R.drawable._general_download);
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
    }

    public class AudioChatModel extends BaseFileChatModel
    {

        public AudioChatModel(String audioPath)
        {
            super(audioPath, true, false, AUDIO);
        }

        public AudioChatModel(String audioPath, int type)
        {
            super(audioPath, true, false, type);
        }

        @Override
        public void setLayout(View view)
        {

            super.setLayout(view);

            if (isFromUser())
            {
                // TODO Change Colors to Attrs
                ((TextView) view.findViewById(R.id.TextViewTime)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));

            }
            else
            {
                // TODO Change Colors to Attrs
                ((TextView) view.findViewById(R.id.TextViewTime)).setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
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

    public class VideoChatModel extends AudioChatModel
    {

        public VideoChatModel(String videoPath)
        {
            super(videoPath, VIDEO);
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

        }
    }     */
}
