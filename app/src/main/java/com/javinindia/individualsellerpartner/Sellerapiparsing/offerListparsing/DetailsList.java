package com.javinindia.individualsellerpartner.Sellerapiparsing.offerListparsing;

/**
 * Created by Ashish on 22-10-2016.
 */
public class DetailsList {
    private String favCount;
    private String offerViewCount;
    private OfferDetails offerDetails;
    private OfferMallDetails offerMallDetails;
    private OfferShopDetails offerShopDetails;
    private OfferBrandDetails offerBrandDetails;

    public String getFavCount() {
        return favCount;
    }

    public void setFavCount(String favCount) {
        this.favCount = favCount;
    }

    public String getOfferViewCount() {
        return offerViewCount;
    }

    public void setOfferViewCount(String offerViewCount) {
        this.offerViewCount = offerViewCount;
    }

    public OfferShopDetails getOfferShopDetails() {
        return offerShopDetails;
    }

    public void setOfferShopDetails(OfferShopDetails offerShopDetails) {
        this.offerShopDetails = offerShopDetails;
    }

    public OfferMallDetails getOfferMallDetails() {
        return offerMallDetails;
    }

    public void setOfferMallDetails(OfferMallDetails offerMallDetails) {
        this.offerMallDetails = offerMallDetails;
    }

    public OfferDetails getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(OfferDetails offerDetails) {
        this.offerDetails = offerDetails;
    }

    public OfferBrandDetails getOfferBrandDetails() {
        return offerBrandDetails;
    }

    public void setOfferBrandDetails(OfferBrandDetails offerBrandDetails) {
        this.offerBrandDetails = offerBrandDetails;
    }
}
