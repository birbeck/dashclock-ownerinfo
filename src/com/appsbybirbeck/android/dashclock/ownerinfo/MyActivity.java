package com.appsbybirbeck.android.dashclock.ownerinfo;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.EditText;

public class MyActivity extends Activity {

    public static final String PREFS_OWNER_INFO_KEY = "ownerInfo";

    private SharedPreferences preferences;
    private EditText ownerInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ownerInfoText = (EditText)findViewById(R.id.owner_info);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String id = null;
        String display_name = null;

        final Cursor c = getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        try {
            c.moveToFirst();
            id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            display_name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
        } finally {
            c.close();
        }

        final EditText ownerInfoText = (EditText)findViewById(R.id.owner_info);
        ownerInfoText.setText(preferences.getString(PREFS_OWNER_INFO_KEY, getString(R.string.default_info, display_name)));
    }

    @Override
    protected void onPause() {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_OWNER_INFO_KEY, ownerInfoText.getText().toString());
        editor.apply();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
