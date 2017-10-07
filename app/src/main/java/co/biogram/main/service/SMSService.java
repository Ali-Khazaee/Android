package co.biogram.main.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.biogram.main.handler.MiscHandler;

public class SMSService extends BroadcastReceiver
{
    private boolean IsWaiting = false;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (!IsWaiting)
            return;

        if (intent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle = intent.getExtras();

            if (bundle != null)
            {
                try
                {
                    SmsMessage[] SMS;
                    String Message = "";

                    if (Build.VERSION.SDK_INT > 18)
                    {
                        SMS = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                        for (SmsMessage sms : SMS)
                            Message += sms.getMessageBody();
                    }
                    else
                    {
                        Object[] Data = (Object[]) bundle.get("pdus");

                        if (Data != null)
                        {
                            SMS = new SmsMessage[Data.length];

                            for (int i = 0; i < SMS.length; i++)
                            {
                                // noinspection all
                                SMS[i] = SmsMessage.createFromPdu((byte[]) Data[i]);
                                Message += SMS[i].getMessageBody();
                            }
                        }
                    }

                    Matcher matcher = Pattern.compile("[0-9]+").matcher(Message);

                    if (matcher.find())
                    {
                        MiscHandler.Debug(matcher.group(0));
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("SMSService: " + e.toString());
                }
            }
        }
    }

    public void SetWaiting(boolean Value)
    {
        IsWaiting = Value;
    }
}
