package com.appsbybirbeck.android.dashclock.ownerinfo.impl;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.appsbybirbeck.android.dashclock.ownerinfo.api.OwnerInfoDelegate;
import com.appsbybirbeck.android.dashclock.ownerinfo.beans.OwnerBean;

public class OwnerInfoCursorLoaderImpl implements LoaderManager.LoaderCallbacks<Cursor> {

    // Our log tag
    private static final String TAG = OwnerInfoCursorLoaderImpl.class.getName();

    // The context to load the cursor in
    private final Context context;

    // A delegate to handle retrieved owner information
    private final OwnerInfoDelegate ownerInfoDelegate;

    public OwnerInfoCursorLoaderImpl(final Context context, final OwnerInfoDelegate ownerInfoDelegate) {
        this.context = context;
        this.ownerInfoDelegate = ownerInfoDelegate;
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final String[] projection = {
                ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME
        };
        return new CursorLoader(context, ContactsContract.Profile.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        try {
            handleOwnerInfoCursor(data);
        } catch (final IllegalArgumentException e) {
            Log.e(TAG, "Failed to get owner info.", e);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        // do nothing
    }


    /**
     * Takes a {@link Cursor} containing the results of querying for the device owner profile sets the owner info in
     * the textbox.
     *
     * @param cursor Cursor from a contacts provider
     * @throws IllegalArgumentException if the cursor is null, empty or does not contain contact info.
     */
    private void handleOwnerInfoCursor(final Cursor cursor) throws IllegalArgumentException {
        Log.d(TAG, cursor.toString());

        // Make sure we have a valid cursor and it has rows
        if (cursor == null || cursor.getCount() < 1) {
            throw new IllegalArgumentException("Cursor was null or empty.");
        }

        // Get the column indexes for the values we're interested in, or throw an IllegalArgumentException
        final int idColumnIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
        final int nameColumnIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

        try {
            // Get the owner info from the first row in the cursor
            cursor.moveToFirst();

            final OwnerBean owner = new OwnerBean();
            owner.setId(cursor.getString(idColumnIndex));
            owner.setName(cursor.getString(nameColumnIndex));
            owner.setEmail(getEmail());

            ownerInfoDelegate.displayOwnerInfo(owner);
        } finally {
            // Don't leak the cursor
            cursor.close();
        }

    }

    /**
     * Get the email address of primary Google account for the logged in user from Account Manager.
     *
     * @return the current users Google account email address
     */
    private String getEmail() {
        final AccountManager accountManager = AccountManager.get(context);
        final Account[] accounts = accountManager.getAccountsByType("com.google");
        if (accounts.length > 0) {
            Log.d(TAG, accounts[0].toString());
            return accounts[0].name;
        }
        return null;
    }

}
