package com.appsbybirbeck.android.dashclock.ownerinfo.services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import com.appsbybirbeck.android.dashclock.ownerinfo.R;
import com.appsbybirbeck.android.dashclock.ownerinfo.api.Constants;

public class OwnerInfoWidgetService extends DashClockExtension {

    // Shared preference for this application. Used to read/write owner info to persistent storage.
    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        // Get the default shared preferences for this application
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onUpdateData(int i) {
        final String ownerInfo = preferences.getString(Constants.PREFS_OWNER_INFO_KEY,
                getString(R.string.default_widget_text));
        final String ownerEmail = preferences.getString(Constants.PREFS_OWNER_EMAIL_KEY,
                getString(R.string.default_email));

        // Publish the extension data update.
        publishUpdate(new ExtensionData()
                .visible(true)
                .icon(R.drawable.app_icon_widget)
                .status(ownerEmail)
                .expandedTitle(getString(R.string.app_name_short))
                .expandedBody(ownerInfo)
        );
    }

}
