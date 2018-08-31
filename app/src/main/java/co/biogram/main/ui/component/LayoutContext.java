package co.biogram.main.ui.component;

import co.biogram.main.ui.helper.FlowLayoutHelper;

/**
 * Created by xhan on 4/11/16.
 */
public class LayoutContext
{
    public FlowLayoutHelper layoutOptions;
    public int currentLineItemCount;

    public static LayoutContext clone(LayoutContext layoutContext)
    {
        LayoutContext resultContext = new LayoutContext();
        resultContext.currentLineItemCount = layoutContext.currentLineItemCount;
        resultContext.layoutOptions = FlowLayoutHelper.clone(layoutContext.layoutOptions);
        return resultContext;
    }

    public static LayoutContext fromLayoutOptions(FlowLayoutHelper layoutOptions)
    {
        LayoutContext layoutContext = new LayoutContext();
        layoutContext.layoutOptions = layoutOptions;
        return layoutContext;
    }
}
