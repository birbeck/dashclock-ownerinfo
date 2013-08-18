package com.appsbybirbeck.android.dashclock.ownerinfo.api;

/**
 * Interface for handling owner info
 */
public interface OwnerInfoDelegate {

    /**
     * Display the owner info to the view.
     *
     * @param owner Owner bean with populated owner information.
     */
    void displayOwnerInfo(Owner owner);

}
