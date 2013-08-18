package com.appsbybirbeck.android.dashclock.ownerinfo.api;

/**
 * An owner info bean.
 */
public interface Owner {

    /**
     * The id of the owner in the contacts database.
     *
     * @return the owners id
     */
    String getId();

    /**
     * The display name of the owner in the contacts database.
     *
     * @return the owners name
     */
    String getName();


    /** The email address of the owner in the contacts database.
     *
     * @return the owners email address
     */
    String getEmail();

}
