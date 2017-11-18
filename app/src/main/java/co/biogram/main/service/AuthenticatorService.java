package co.biogram.main.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import co.biogram.main.handler.MiscHandler;

public class AuthenticatorService extends Service
{
    private Authenticator authenticator;

    @Override
    public void onCreate()
    {
        MiscHandler.Debug("AuthenticatorService onCreate Called");
        authenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        MiscHandler.Debug("AuthenticatorService onBind Called");
        return authenticator.getIBinder();
    }

    private class Authenticator extends AbstractAccountAuthenticator
    {
        Authenticator(Context context)
        {
            super(context);
            MiscHandler.Debug("Authenticator Called");
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse r, String s)
        {
            MiscHandler.Debug("editProperties Called");
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse r, String s, String s2, String[] strings, Bundle bundle) throws NetworkErrorException
        {
            MiscHandler.Debug("addAccount Called");
            return null;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse r, Account account, Bundle bundle) throws NetworkErrorException
        {
            MiscHandler.Debug("confirmCredentials Called");
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse r,Account account,String s,Bundle bundle) throws NetworkErrorException
        {
            MiscHandler.Debug("getAuthToken Called");
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthTokenLabel(String s)
        {
            MiscHandler.Debug("getAuthTokenLabel Called");
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse r, Account account, String s, Bundle bundle) throws NetworkErrorException
        {
            MiscHandler.Debug("updateCredentials Called");
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse r, Account account, String[] strings) throws NetworkErrorException
        {
            MiscHandler.Debug("hasFeatures Called");
            throw new UnsupportedOperationException();
        }
    }
}
