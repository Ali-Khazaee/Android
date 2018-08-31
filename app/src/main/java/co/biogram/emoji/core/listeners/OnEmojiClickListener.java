package co.biogram.emoji.core.listeners;

import android.support.annotation.NonNull;

import co.biogram.emoji.core.EmojiImageView;
import co.biogram.emoji.core.emoji.Emoji;

public interface OnEmojiClickListener
{
    void onEmojiClick(@NonNull EmojiImageView emoji, @NonNull Emoji imageView);
}
