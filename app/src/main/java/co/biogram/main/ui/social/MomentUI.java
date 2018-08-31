package co.biogram.main.ui.social;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;

public class MomentUI extends FragmentView {
    @Override
    public void OnCreate() {
        View view = View.inflate(Activity, R.layout.aaa, null);

        Button button2 = view.findViewById(R.id.button2);
        final EditText editText = view.findViewById(R.id.editText);
        final TextView textView = view.findViewById(R.id.textView);

        //        button2.setOnClickListener(new View.OnClickListener()
        //        {
        //            @Override
        //            public void onClick(View v)
        //            {
        //                NetworkService.Emit("SendMessage", editText.getText().toString(), new Emitter.Listener()
        //                {
        //                    @Override
        //                    public void call(Object... args)
        //                    {
        //                        Analyze.Debug("SendMessage", "" + args[0]);
        //
        //                    }
        //                });
        //            }
        //        });

        ViewMain = view;
    }
}
