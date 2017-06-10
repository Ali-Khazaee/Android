package co.biogram.stickyscroll;

interface IResourceProvider
{
    int getResourceId(int styleResId);
    void recycle();
}
