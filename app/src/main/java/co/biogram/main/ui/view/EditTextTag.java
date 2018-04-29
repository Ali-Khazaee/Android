package co.biogram.main.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

public class EditTextTag extends FrameLayout implements View.OnClickListener, TextView.OnEditorActionListener, View.OnKeyListener
{
    private Drawable DrawableTag;
    private EditText EditTextMain;
    private FlowLayout FlowLayoutMain;
    private TextView LastSelectTagView;
    private boolean IsDelAction = false;
    private List<String> TagList = new ArrayList<>();

    public EditTextTag(Context context)
    {
        this(context, null);
    }

    public EditTextTag(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public EditTextTag(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        FlowLayoutMain = new FlowLayout(getContext());
        FlowLayoutMain.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        addView(FlowLayoutMain);

        EditTextMain = new TagEditText(getContext());
        EditTextMain.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        EditTextMain.setPadding(Misc.ToDP(8), 0, Misc.ToDP(8), Misc.ToDP(14));
        EditTextMain.setHint(Misc.String(R.string.ProfileUIFeatureTag));
        EditTextMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextMain.setImeOptions(EditorInfo.IME_ACTION_DONE);
        EditTextMain.setOnEditorActionListener(this);
        EditTextMain.setOnClickListener(this);
        EditTextMain.setOnKeyListener(this);
        EditTextMain.setTag(new Object());
        EditTextMain.setSingleLine(true);
        EditTextMain.setMaxEms(30);

        Misc.SetCursorColor(EditTextMain, R.color.Primary);
        ViewCompat.setBackgroundTintList(EditTextMain, ColorStateList.valueOf(Misc.Color(R.color.Primary)));

        FlowLayoutMain.addView(EditTextMain);
    }

    private TextView CreateTag(String Message)
    {
        MarginLayoutParams TagParam = new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TagParam.setMargins(Misc.ToDP(5), 0, Misc.ToDP(5), Misc.ToDP(10));

        TextView Tag = new TextView(getContext());
        Tag.setPadding(Misc.ToDP(10), Misc.ToDP(5), Misc.ToDP(10), Misc.ToDP(5));
        Tag.setBackgroundResource(R.drawable.___profile_tag_border);
        Tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        Tag.setTextColor(Misc.Color(R.color.Primary));
        Tag.setGravity(Gravity.CENTER);
        Tag.setLayoutParams(TagParam);
        Tag.setText(Message);

        return Tag;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent e)
    {
        boolean Handle = false;

        if (keyCode == KeyEvent.KEYCODE_DEL && e.getAction() == KeyEvent.ACTION_DOWN)
        {
            String Tag = EditTextMain.getText().toString();

            if (TextUtils.isEmpty(Tag))
            {
                int Count = FlowLayoutMain.getChildCount();

                if (LastSelectTagView == null && Count > 1)
                {
                    if (IsDelAction)
                    {
                        FlowLayoutMain.removeViewAt(Count - 2);
                        TagList.remove(Count - 2);
                        Handle = true;
                    }
                    else
                    {
                        LastSelectTagView = (TextView) FlowLayoutMain.getChildAt(Count - 2);
                        IsDelAction = true;
                    }
                }
                else
                {
                    if (TagList.size() > 0 && LastSelectTagView != null)
                    {
                        IsDelAction = false;
                        FlowLayoutMain.removeView(LastSelectTagView);
                        TagList.remove(FlowLayoutMain.indexOfChild(LastSelectTagView));
                        LastSelectTagView = null;
                    }
                }
            }
            else
            {
                int Length = Tag.length();
                EditTextMain.getText().delete(Length, Length);
            }
        }

        return Handle;
    }

    @Override
    public boolean onEditorAction(TextView v, int ActionID, KeyEvent e)
    {
        boolean Handle = false;

        if (ActionID == EditorInfo.IME_ACTION_DONE)
        {
            String Tag = EditTextMain.getText().toString();

            if (!TextUtils.isEmpty(Tag))
            {
                TextView TagView = CreateTag(Tag);
                TagView.setOnClickListener(EditTextTag.this);

                FlowLayoutMain.addView(TagView, FlowLayoutMain.getChildCount() - 1);
                EditTextMain.getText().clear();
                EditTextMain.performClick();
                IsDelAction = false;
                TagList.add(Tag);
                Handle = true;

                if (DrawableTag == null)
                    DrawableTag = TagView.getBackground();
            }
        }

        return Handle;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getTag() == null)
        {
            if (LastSelectTagView == null)
            {
                LastSelectTagView = (TextView) v;
                v.setBackgroundResource(R.color.Red);
            }
            else
            {
                if (LastSelectTagView.equals(v))
                {
                    v.setBackground(DrawableTag);
                    LastSelectTagView = null;
                }
                else
                {
                    v.setBackgroundResource(R.color.Red);
                    LastSelectTagView = (TextView) v;
                }
            }
        }
        else
        {
            if (LastSelectTagView != null)
            {
                LastSelectTagView.setBackground(DrawableTag);
                LastSelectTagView = null;
            }
        }
    }

    public void AddTag(List<String> list)
    {
        for (int I = 0; I < list.size(); I++)
        {
            String Tag = list.get(I);

            if (TextUtils.isEmpty(Tag))
                return;

            TextView TagView = CreateTag(Tag);
            TagView.setOnClickListener(EditTextTag.this);

            if (DrawableTag == null)
                DrawableTag = TagView.getBackground();

            FlowLayoutMain.addView(TagView, FlowLayoutMain.getChildCount() - 1);

            TagList.add(Tag);
            EditTextMain.getText().clear();
            EditTextMain.performClick();
            IsDelAction = false;
        }
    }

    public List<String> GetTagList()
    {
        return TagList;
    }

    private class TagEditText extends AppCompatEditText
    {
        public TagEditText(Context context)
        {
            super(context);
        }

        @Override
        public InputConnection onCreateInputConnection(EditorInfo outAttrs)
        {
            return new TagInputConnection(super.onCreateInputConnection(outAttrs), true);
        }

        private class TagInputConnection extends InputConnectionWrapper
        {
            TagInputConnection(InputConnection target, boolean mutable)
            {
                super(target, mutable);
            }

            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength)
            {
                if (beforeLength == 1 && afterLength == 0)
                    return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));

                return super.deleteSurroundingText(beforeLength, afterLength);
            }
        }
    }
}
