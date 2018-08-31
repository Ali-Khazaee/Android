package co.biogram.emoji.emojibio;

import android.support.annotation.NonNull;

import co.biogram.emoji.core.EmojiProvider;
import co.biogram.emoji.core.emoji.EmojiCategory;
import co.biogram.emoji.emojibio.category.ActivitiesCategory;
import co.biogram.emoji.emojibio.category.AnimalsAndNatureCategory;
import co.biogram.emoji.emojibio.category.FlagsCategory;
import co.biogram.emoji.emojibio.category.FoodAndDrinkCategory;
import co.biogram.emoji.emojibio.category.ObjectsCategory;
import co.biogram.emoji.emojibio.category.SmileysAndPeopleCategory;
import co.biogram.emoji.emojibio.category.SymbolsCategory;
import co.biogram.emoji.emojibio.category.TravelAndPlacesCategory;

public final class IosEmojiProvider implements EmojiProvider
{
    @Override
    @NonNull
    public EmojiCategory[] getCategories()
    {
        return new EmojiCategory[] { new SmileysAndPeopleCategory(), new AnimalsAndNatureCategory(), new FoodAndDrinkCategory(), new ActivitiesCategory(), new TravelAndPlacesCategory(), new ObjectsCategory(), new SymbolsCategory(), new FlagsCategory() };
    }
}
