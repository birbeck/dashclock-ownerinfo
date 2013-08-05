package com.appsbybirbeck.android.dashclock.ownerinfo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class OwnerInfoWidgetService extends DashClockExtension {

    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onUpdateData(int i) {
        final String ownerInfo = preferences.getString(MyActivity.PREFS_OWNER_INFO_KEY,
                getString(R.string.default_info, "unnamed"));

        // Publish the extension data update.
        publishUpdate(new ExtensionData()
                .visible(true)
                .icon(R.drawable.perm_group_personal_info)
                .status(getString(R.string.app_name_short))
                .expandedTitle(getString(R.string.app_name_short))
                .expandedBody(ownerInfo)
        );
    }

}
