package com.ipl.auction.model;

/**
 * Another interface for MULTIPLE INHERITANCE.
 * Represents entities that can participate in the auction.
 */
public interface Auctionable {
    PlayerStatus getAuctionStatus();
    void setAuctionStatus(PlayerStatus status);
}
