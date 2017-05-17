package co.biogram.main.check;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.biogram.main.R;

public class ActivityProfileFragmentLike extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View RootView = new View(getActivity());
        RootView.setBackgroundResource(R.color.BlueLight);

        return RootView;
    }
}
