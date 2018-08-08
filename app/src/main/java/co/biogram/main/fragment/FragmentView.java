package co.biogram.main.fragment;

import android.content.Intent;
import android.view.View;

public abstract class FragmentView {
    public FragmentActivity Activity;
    public View ViewMain;
    String Tag;

    public abstract void OnCreate();

    public void OnResume() {

    }

    public void OnOpen() {
    }

    public void OnPause() {
    }

    public void OnDestroy() {
    }

    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent) {
    }
}
