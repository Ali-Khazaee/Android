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
        return inflater.inflate(R.layout.fooo, container, false);
    }
}
