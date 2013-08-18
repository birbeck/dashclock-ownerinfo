package com.appsbybirbeck.android.dashclock.ownerinfo.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.appsbybirbeck.android.dashclock.ownerinfo.R;
import com.appsbybirbeck.android.dashclock.ownerinfo.api.Constants;
import com.appsbybirbeck.android.dashclock.ownerinfo.api.Owner;
import com.appsbybirbeck.android.dashclock.ownerinfo.api.OwnerInfoDelegate;
import com.appsbybirbeck.android.dashclock.ownerinfo.impl.OwnerInfoCursorLoaderImpl;

public class MyActivity extends Activity implements OwnerInfoDelegate
{

    // Our log tag
    private static final String TAG = MyActivity.class.getName();

    // The id of the contacts cursor loader
    private static final int CONTACTS_LOADER = 0;

    // Shared preference for this application. Used to read/write owner info to persistent storage.
    private SharedPreferences preferences;

    // The EditText which displays owner info to the user
    private EditText ownerInfoEditText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Display a chevron in the actionbar to indicate that the user can go back
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the default shared preferences for this application
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Find the owner info text box in the current view
        ownerInfoEditText = (EditText) findViewById(R.id.owner_info);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the current owner info from preferences or null if no value exists
        final String ownerInfo = preferences.getString(Constants.PREFS_OWNER_INFO_KEY, null);

        // If we were able to retrieve a value from preferences, display it in the owner info text box
        if (ownerInfo != null) {
            ownerInfoEditText.setText(ownerInfo);
        }

        // Otherwise, retrieve the owner profile from the contacts database
        else {
            getLoaderManager().initLoader(CONTACTS_LOADER, null, new OwnerInfoCursorLoaderImpl(this, this));
        }
    }

    @Override
    protected void onPause() {
        // If owner info text is provided, save it to the preferences
        final String ownerInfo = ownerInfoEditText.getText().toString();
        if (!TextUtils.isEmpty(ownerInfo)) {
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.PREFS_OWNER_INFO_KEY, ownerInfo);
            editor.apply();
        }

        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // The activity icon in the action bar
                // Exit the activity
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayOwnerInfo(final Owner owner) {
        Log.d(TAG, owner.toString());

        // Set the new owner info in the text box
        ownerInfoEditText.setText(getString(R.string.default_info,
                TextUtils.isEmpty(owner.getName()) ? getString(R.string.default_name) : owner.getName(),
                TextUtils.isEmpty(owner.getEmail()) ? getString(R.string.default_email) : owner.getEmail()));

        // Save the owners email address
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREFS_OWNER_EMAIL_KEY,
                preferences.getString(Constants.PREFS_OWNER_EMAIL_KEY, owner.getEmail()));
        editor.apply();
    }

}
