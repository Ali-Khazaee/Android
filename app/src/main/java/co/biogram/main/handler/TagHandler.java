package co.biogram.main.handler;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;

import android.widget.TextView;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import co.biogram.main.R;

class TagHandler
{
    static void Show(TextView tv)
    {
        if (tv.getText().length() <= 2)
            return;

        tv.setText(tv.getText(), TextView.BufferType.SPANNABLE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setHighlightColor(Color.TRANSPARENT);

        int Index = 0;
        CharSequence Text = tv.getText();

        while (Index < Text.length() - 1)
        {
            char Sign = Text.charAt(Index);
            char NextSign = Text.charAt(Index + 1);
            int NextChar = Index + 1;

            if ((Sign == '#' || Sign == '@') && (NextSign != '#' && NextSign != '@'))
            {
                NextChar = NextValidChar(Text, Index);

                Spannable Span = (Spannable) Text;
                CharacterStyle TagChar = new HashTagSpan(Sign == '@' ? 2 : 1, tv.getContext());

                Span.setSpan(TagChar, Index, NextChar, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            Index = NextChar;
        }
    }

    private static int NextValidChar(CharSequence Text, int Start)
    {
        int CharIndex = -1;

        for (int I = Start + 1; I < Text.length(); I++)
        {
            char Sign = Text.charAt(I);
            boolean IsValidSign = Character.isLetterOrDigit(Sign) || Sign == '_' || Sign == '.';

            if (!IsValidSign)
            {
                CharIndex = I;
                break;
            }
        }

        if (CharIndex == -1)
            CharIndex = Text.length();

        return CharIndex;
    }

    private static class HashTagSpan extends ClickableSpan
    {
        private Context context;
        private int Type;

        HashTagSpan(int t, Context c)
        {
            Type = t;
            context = c;
        }

        @Override
        public void updateDrawState(TextPaint tp)
        {
            tp.setColor(Misc.Color(R.color.HashTag));
        }

        @Override
        public void onClick(View v)
        {
            CharSequence Text = ((TextView) v).getText();
            Spanned Span = (Spanned) Text;
            int Start = Span.getSpanStart(this);
            int End = Span.getSpanEnd(this);
            String Message = Text.subSequence(Start + 1, End).toString();

            if (Type == 1)
            {
                Misc.Toast(Message + " - HashTag Clicked");

                // TODO Open View
            }
            else if (Type == 2)
            {
                if (SharedHandler.GetString(context, "Username").equalsIgnoreCase(Message))
                    return;

                Misc.Toast(Message + " - ID Clicked");

                // TODO Open View
            }
        }
    }
}
