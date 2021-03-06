package co.biogram.emoji.emojibio.category;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import co.biogram.emoji.core.emoji.EmojiCategory;
import co.biogram.emoji.emojibio.IosEmoji;
import co.biogram.main.R;

@SuppressWarnings("PMD.MethodReturnsInternalArray")
public final class AnimalsAndNatureCategory implements EmojiCategory
{
    private static final IosEmoji[] DATA = new IosEmoji[] { new IosEmoji(0x1F435, 13, 31), new IosEmoji(0x1F412, 12, 48), new IosEmoji(0x1F98D, 42, 37), new IosEmoji(0x1F436, 13, 32), new IosEmoji(0x1F415, 12, 51), new IosEmoji(0x1F429, 13, 19), new IosEmoji(0x1F43A, 13, 36), new IosEmoji(0x1F98A, 42, 34), new IosEmoji(0x1F431, 13, 27), new IosEmoji(0x1F408, 12, 38), new IosEmoji(0x1F981, 42, 25), new IosEmoji(0x1F42F, 13, 25), new IosEmoji(0x1F405, 12, 35), new IosEmoji(0x1F406, 12, 36), new IosEmoji(0x1F434, 13, 30), new IosEmoji(0x1F40E, 12, 44), new IosEmoji(0x1F984, 42, 28), new IosEmoji(0x1F993, 42, 43), new IosEmoji(0x1F98C, 42, 36), new IosEmoji(0x1F42E, 13, 24), new IosEmoji(0x1F402, 12, 32), new IosEmoji(0x1F403, 12, 33), new IosEmoji(0x1F404, 12, 34), new IosEmoji(0x1F437, 13, 33), new IosEmoji(0x1F416, 13, 0), new IosEmoji(0x1F417, 13, 1), new IosEmoji(0x1F43D, 13, 39), new IosEmoji(0x1F40F, 12, 45), new IosEmoji(0x1F411, 12, 47), new IosEmoji(0x1F410, 12, 46), new IosEmoji(0x1F42A, 13, 20), new IosEmoji(0x1F42B, 13, 21), new IosEmoji(0x1F992, 42, 42), new IosEmoji(0x1F418, 13, 2), new IosEmoji(0x1F98F, 42, 39), new IosEmoji(0x1F42D, 13, 23), new IosEmoji(0x1F401, 12, 31), new IosEmoji(0x1F400, 12, 30), new IosEmoji(0x1F439, 13, 35), new IosEmoji(0x1F430, 13, 26), new IosEmoji(0x1F407, 12, 37), new IosEmoji(new int[] { 0x1F43F, 0xFE0F }, 13, 41), new IosEmoji(0x1F994, 42, 44), new IosEmoji(0x1F987, 42, 31), new IosEmoji(0x1F43B, 13, 37), new IosEmoji(0x1F428, 13, 18), new IosEmoji(0x1F43C, 13, 38), new IosEmoji(0x1F43E, 13, 40), new IosEmoji(0x1F983, 42, 27), new IosEmoji(0x1F414, 12, 50), new IosEmoji(0x1F413, 12, 49), new IosEmoji(0x1F423, 13, 13), new IosEmoji(0x1F424, 13, 14), new IosEmoji(0x1F425, 13, 15), new IosEmoji(0x1F426, 13, 16), new IosEmoji(0x1F427, 13, 17), new IosEmoji(new int[] { 0x1F54A, 0xFE0F }, 28, 13), new IosEmoji(0x1F985, 42, 29), new IosEmoji(0x1F986, 42, 30), new IosEmoji(0x1F989, 42, 33), new IosEmoji(0x1F438, 13, 34), new IosEmoji(0x1F40A, 12, 40), new IosEmoji(0x1F422, 13, 12), new IosEmoji(0x1F98E, 42, 38), new IosEmoji(0x1F40D, 12, 43), new IosEmoji(0x1F432, 13, 28), new IosEmoji(0x1F409, 12, 39), new IosEmoji(0x1F995, 42, 45), new IosEmoji(0x1F996, 42, 46), new IosEmoji(0x1F433, 13, 29), new IosEmoji(0x1F40B, 12, 41), new IosEmoji(0x1F42C, 13, 22), new IosEmoji(0x1F41F, 13, 9), new IosEmoji(0x1F420, 13, 10), new IosEmoji(0x1F421, 13, 11), new IosEmoji(0x1F988, 42, 32), new IosEmoji(0x1F419, 13, 3), new IosEmoji(0x1F41A, 13, 4), new IosEmoji(0x1F980, 42, 24), new IosEmoji(0x1F990, 42, 40), new IosEmoji(0x1F991, 42, 41), new IosEmoji(0x1F40C, 12, 42), new IosEmoji(0x1F98B, 42, 35), new IosEmoji(0x1F41B, 13, 5), new IosEmoji(0x1F41C, 13, 6), new IosEmoji(0x1F41D, 13, 7), new IosEmoji(0x1F41E, 13, 8), new IosEmoji(0x1F997, 42, 47), new IosEmoji(new int[] { 0x1F577, 0xFE0F }, 29, 18), new IosEmoji(new int[] { 0x1F578, 0xFE0F }, 29, 19), new IosEmoji(0x1F982, 42, 26), new IosEmoji(0x1F490, 24, 42), new IosEmoji(0x1F338, 6, 46), new IosEmoji(0x1F4AE, 25, 25), new IosEmoji(new int[] { 0x1F3F5, 0xFE0F }, 12, 20), new IosEmoji(0x1F339, 6, 47), new IosEmoji(0x1F940, 41, 36), new IosEmoji(0x1F33A, 6, 48), new IosEmoji(0x1F33B, 6, 49), new IosEmoji(0x1F33C, 6, 50), new IosEmoji(0x1F337, 6, 45), new IosEmoji(0x1F331, 6, 39), new IosEmoji(0x1F332, 6, 40), new IosEmoji(0x1F333, 6, 41), new IosEmoji(0x1F334, 6, 42), new IosEmoji(0x1F335, 6, 43), new IosEmoji(0x1F33E, 7, 0), new IosEmoji(0x1F33F, 7, 1), new IosEmoji(new int[] { 0x2618, 0xFE0F }, 47, 25), new IosEmoji(0x1F340, 7, 2), new IosEmoji(0x1F341, 7, 3), new IosEmoji(0x1F342, 7, 4), new IosEmoji(0x1F343, 7, 5) };

    @Override
    @NonNull
    public IosEmoji[] getEmojis()
    {
        return DATA;
    }

    @Override
    @DrawableRes
    public int getIcon()
    {
        return R.drawable.emoji_ios_category_animalsandnature;
    }
}
