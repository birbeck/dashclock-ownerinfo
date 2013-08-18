package com.appsbybirbeck.android.dashclock.ownerinfo.beans;

import com.appsbybirbeck.android.dashclock.ownerinfo.api.Owner;

public class OwnerBean implements Owner {

    private String id;
    private String name;
    private String email;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("OwnerBean {id=%s, name=%s, email=%s}", id, name, email);
    }
}
