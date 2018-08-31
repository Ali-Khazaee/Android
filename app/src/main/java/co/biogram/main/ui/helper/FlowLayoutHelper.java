package co.biogram.main.ui.helper;

import co.biogram.main.ui.component.FlowLayoutManager;

/**
 * Created by xhan on 4/11/16.
 */
public class FlowLayoutHelper
{
    public static final int ITEM_PER_LINE_NO_LIMIT = 0;
    public FlowLayoutManager.Alignment alignment = FlowLayoutManager.Alignment.LEFT;
    public int itemsPerLine = ITEM_PER_LINE_NO_LIMIT;

    public static FlowLayoutHelper clone(FlowLayoutHelper layoutOptions)
    {
        FlowLayoutHelper result = new FlowLayoutHelper();
        result.alignment = layoutOptions.alignment;
        result.itemsPerLine = layoutOptions.itemsPerLine;
        return result;
    }
}
