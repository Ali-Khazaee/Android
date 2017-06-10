package co.biogram.stickyscroll;

interface IStickyScrollPresentation
{
    void freeHeader();
    void freeFooter();
    void stickHeader(int translationY);
    void stickFooter(int translationY);
    void initHeaderView(int id);
    void initFooterView(int id);
}
