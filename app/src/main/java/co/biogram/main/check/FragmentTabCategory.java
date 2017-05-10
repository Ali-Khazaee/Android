package co.biogram.main.check;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.misc.LoadingView;

public class FragmentTabCategory extends Fragment
{
    public FragmentTabCategory() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout Root = new RelativeLayout(getContext());
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.White));

        RelativeLayout.LayoutParams LoadingParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(100), MiscHandler.ToDimension(100));
        LoadingParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingView Loading = new LoadingView(getContext());
        Loading.setLayoutParams(LoadingParam);
        Loading.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.Black));
        Loading.SetColor(R.color.BlueLight);
        //Loading.SetSize(5);
        Loading.Start();

        Root.addView(Loading);

        return Root;
    }
}
