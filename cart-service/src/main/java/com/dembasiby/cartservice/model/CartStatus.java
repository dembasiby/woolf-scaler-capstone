package com.dembasiby.cartservice.model;

public enum CartStatus {
    ACTIVE,
    ORDERED,
    ABANDONED;

    public static final CartStatus DEFAULT = ACTIVE;
}
