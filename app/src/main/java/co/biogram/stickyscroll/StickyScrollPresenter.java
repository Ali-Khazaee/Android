package co.biogram.stickyscroll;

class StickyScrollPresenter
{
    private final IResourceProvider mTypedArrayResourceProvider;
    private IStickyScrollPresentation mStickyScrollPresentation;

    private int mDeviceHeight;

    private int mStickyFooterHeight;
    private int mStickyFooterInitialTranslation;
    private int mStickyFooterInitialLocation;

    private int mStickyHeaderInitialLocation;
    boolean mScrolled;

    StickyScrollPresenter(IStickyScrollPresentation stickyScrollPresentation, IScreenInfoProvider screenInfoProvider, IResourceProvider typedArrayResourceProvider)
    {
        mDeviceHeight = screenInfoProvider.getScreenHeight();
        mTypedArrayResourceProvider = typedArrayResourceProvider;
        mStickyScrollPresentation = stickyScrollPresentation;
    }

    void onGlobalLayoutChange(int headerRes, int footerRes)
    {
        int headerId = mTypedArrayResourceProvider.getResourceId(headerRes);

        if(headerId != 0)
        {
            mStickyScrollPresentation.initHeaderView(headerId);
        }

        int footerId = mTypedArrayResourceProvider.getResourceId(footerRes);

        if(footerId != 0)
        {
            mStickyScrollPresentation.initFooterView(footerId);
        }

        mTypedArrayResourceProvider.recycle();
    }

    void initStickyFooter(int measuredHeight, int initialStickyFooterLocation)
    {
        mStickyFooterHeight = measuredHeight;
        mStickyFooterInitialLocation = initialStickyFooterLocation;
        mStickyFooterInitialTranslation = mDeviceHeight - initialStickyFooterLocation - mStickyFooterHeight;

        if (mStickyFooterInitialLocation > mDeviceHeight - mStickyFooterHeight)
        {
            mStickyScrollPresentation.stickFooter(mStickyFooterInitialTranslation);
        }
    }

    void initStickyHeader(int headerTop)
    {
        mStickyHeaderInitialLocation = headerTop;
    }

    void onScroll(int scrollY)
    {
        mScrolled = true;
        handleFooterStickiness(scrollY);
        handleHeaderStickiness(scrollY);
    }

    private void handleFooterStickiness(int scrollY)
    {
        if (scrollY > mStickyFooterInitialLocation - mDeviceHeight + mStickyFooterHeight)
        {
            mStickyScrollPresentation.freeFooter();
        }
        else
        {
            mStickyScrollPresentation.stickFooter(mStickyFooterInitialTranslation + scrollY);
        }
    }

    private void handleHeaderStickiness(int scrollY)
    {
        if (scrollY > mStickyHeaderInitialLocation)
        {
            mStickyScrollPresentation.stickHeader(scrollY - mStickyHeaderInitialLocation);
        }
        else
        {
            mStickyScrollPresentation.freeHeader();
        }
    }

    void recomputeFooterLocation(int footerTop, int footerLocation)
    {
        if (mScrolled)
        {
            mStickyFooterInitialTranslation = mDeviceHeight - footerTop - mStickyFooterHeight;
            mStickyFooterInitialLocation = footerTop;

            if (footerLocation > mDeviceHeight - mStickyFooterHeight)
            {
                mStickyScrollPresentation.stickFooter(mStickyFooterInitialTranslation);
            }
            else
            {
                mStickyScrollPresentation.freeFooter();
            }

        }
        else
        {
            initStickyFooter(mStickyFooterHeight, footerTop);
        }
    }
}
