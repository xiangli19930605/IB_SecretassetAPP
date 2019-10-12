package me.jessyan.armscomponent.commonsdk.data.prefs;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.core.Constants;

/**
 * @author flymegoc
 * @date 2018/3/4
 */

@Singleton
public class PreferenceHelperImpl implements PreferenceHelper {

    private static final String TAG = PreferenceHelperImpl.class.getSimpleName();

    private Gson gson;

    private SharedPreferences mPreferences;

    @Inject
    public PreferenceHelperImpl() {
        mPreferences = MyApplication.getApplication().getSharedPreferences(Constants.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }


    @Override
    public boolean getNightModeState() {
        return mPreferences.getBoolean(Constants.NIGHT_MODE_STATE, false);
    }

    @Override
    public void setNightModeState(boolean b) {
        mPreferences.edit().putBoolean(Constants.NIGHT_MODE_STATE, b).apply();
    }

    @Override
    public void setLocation(String location) {
        mPreferences.edit().putString(Constants.LOCATION, location).apply();
    }

    @Override
    public String getLocation() {
        return mPreferences.getString(Constants.LOCATION, "");
    }

    @Override
    public void setUsbState(boolean b) {
        mPreferences.edit().putBoolean(Constants.USB_STATE, b).apply();
    }

    @Override
    public boolean getUsbState() {
        return mPreferences.getBoolean(Constants.USB_STATE, false);
    }
}
