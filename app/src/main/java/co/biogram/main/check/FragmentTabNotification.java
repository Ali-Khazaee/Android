package co.biogram.main.check;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.biogram.main.R;

public class FragmentTabNotification extends Fragment
{
    public FragmentTabNotification() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.fragment_tab_notification, container, false);



        return RootView;
    }
}
