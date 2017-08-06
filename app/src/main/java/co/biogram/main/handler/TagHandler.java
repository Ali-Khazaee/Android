package co.biogram.main.handler;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;

import android.widget.TextView;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import co.biogram.main.R;
import co.biogram.main.fragment.ProfileFragment;
import co.biogram.main.fragment.TagFragment;

public class TagHandler
{
    public TagHandler(TextView textView, FragmentActivity activity)
    {
        if (textView.getText().length() <= 2)
            return;

        textView.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);

        int Index = 0;
        CharSequence Text = textView.getText();

        while (Index < Text.length() - 1)
        {
            char Sign = Text.charAt(Index);
            char NextSign = Text.charAt(Index + 1);
            int NextNotLetterDigitCharIndex = Index + 1;

            if ((Sign == '#' || Sign == '@') && (NextSign != '#' && NextSign != '@'))
            {
                int TagType = 1;

                if (Sign == '@')
                    TagType = 2;

                NextNotLetterDigitCharIndex = FindNextValidTagChar(Text, Index);

                Spannable Span = (Spannable) textView.getText();
                CharacterStyle TagChar = new ClickableForegroundColorSpan(TagType, ContextCompat.getColor(textView.getContext(), R.color.BlueLight), activity);

                Span.setSpan(TagChar, Index, NextNotLetterDigitCharIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            Index = NextNotLetterDigitCharIndex;
        }
    }

    private int FindNextValidTagChar(CharSequence Text, int Start)
    {
        int NonLetterDigitCharIndex = -1;

        for (int Index = Start + 1; Index < Text.length(); Index++)
        {
            char Sign = Text.charAt(Index);
            boolean IsValidSign = Character.isLetterOrDigit(Sign) || Sign == '_' || Sign == '.';

            if (!IsValidSign)
            {
                NonLetterDigitCharIndex = Index;
                break;
            }
        }

        if (NonLetterDigitCharIndex == -1)
            NonLetterDigitCharIndex = Text.length();

        return NonLetterDigitCharIndex;
    }

    private class ClickableForegroundColorSpan extends ClickableSpan
    {
        private final FragmentActivity Activity;
        private final int TagColor;
        private final int TagType;

        ClickableForegroundColorSpan(int Type, int Color, FragmentActivity activity)
        {
            TagType = Type;
            TagColor = Color;
            Activity = activity;
        }

        @Override
        public void updateDrawState(TextPaint textpaint)
        {
            textpaint.setColor(TagColor);
        }

        @Override
        public void onClick(View Widget)
        {
            CharSequence Text = ((TextView) Widget).getText();
            Spanned Span = (Spanned) Text;
            int Start = Span.getSpanStart(this);
            int End = Span.getSpanEnd(this);
            String Message = Text.subSequence(Start + 1, End).toString();

            if (TagType == 1)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Tag", Message);

                Fragment fragment = new TagFragment();
                fragment.setArguments(bundle);

                Activity.getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("TagFragment").commit();
            }
            else if (TagType == 2)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Username", Message);

                Fragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);

                Activity.getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
            }

            MiscHandler.HideSoftKey(Activity);
        }
    }
}
