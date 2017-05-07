package co.biogram.main.handler;

import android.graphics.Color;
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

public class TagHandler
{
    public TagHandler(TextView textView, OnTagClickListener Listener)
    {
        if (textView.getText().length() <= 2)
            return;

        textView.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);

        int Index = 0;
        int StartIndexOfNextTagSign;
        CharSequence Text = textView.getText();

        while (Index < Text.length() - 1)
        {
            char Sign = Text.charAt(Index);
            char NextSign = Text.charAt(Index + 1);
            int NextNotLetterDigitCharIndex = Index + 1;

            if ((Sign == '#' || Sign == '@') && (NextSign != '#' && NextSign != '@'))
            {
                int TagType = 0;

                if (Sign == '#')
                    TagType = 1;

                if (Sign == '@')
                    TagType = 2;

                StartIndexOfNextTagSign = Index;
                NextNotLetterDigitCharIndex = FindNextValidTagChar(Text, StartIndexOfNextTagSign);

                CharacterStyle TagChar;
                Spannable Span = (Spannable) textView.getText();

                TagChar = new ClickableForegroundColorSpan(TagType, ContextCompat.getColor(textView.getContext(), R.color.BlueLight), Listener);

                Span.setSpan(TagChar, StartIndexOfNextTagSign, NextNotLetterDigitCharIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            boolean IsValidSign = Character.isLetterOrDigit(Sign) || Sign == '_';

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
        private int TagType;
        private int TagColor;
        private OnTagClickListener _OnTagClickListener;

        ClickableForegroundColorSpan(int Type, int Color, OnTagClickListener Listener)
        {
            TagType = Type;
            TagColor = Color;
            _OnTagClickListener = Listener;
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

            if (_OnTagClickListener == null)
            {
                if (TagType == 1)
                {

                }

                if (TagType == 2)
                {

                }

                return;
            }

           _OnTagClickListener.OnTagClicked(Text.subSequence(Start + 1, End).toString(), TagType);
        }
    }

    public interface OnTagClickListener
    {
        void OnTagClicked(String Tag, int Type);
    }
}
