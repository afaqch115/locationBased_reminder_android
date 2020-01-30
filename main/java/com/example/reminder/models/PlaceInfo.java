package com.example.reminder.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo implements Parcelable {
    private String name;
    private String address;
    private String phoneNumber;
    private String Id;
    private Uri websiteUri;
    private LatLng latLng;
    private float ratting;
    private String attributatiosn;

    public PlaceInfo(String name, String address, String phoneNumber, String id, Uri websiteUri, LatLng latLng, float ratting, String attributatiosn)
    {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        Id = id;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.ratting = ratting;
        this.attributatiosn = attributatiosn;
    }

    public PlaceInfo()
    {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getRatting() {
        return ratting;
    }

    public void setRatting(float ratting) {
        this.ratting = ratting;
    }

    public String getAttributatiosn() {
        return attributatiosn;
    }

    public void setAttributatiosn(String attributatiosn) {
        this.attributatiosn = attributatiosn;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", Id='" + Id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng=" + latLng +
                ", ratting=" + ratting +
                ", attributatiosn='" + attributatiosn + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.Id);
        dest.writeParcelable(this.websiteUri, flags);
        dest.writeParcelable(this.latLng, flags);
        dest.writeFloat(this.ratting);
        dest.writeString(this.attributatiosn);
    }

    protected PlaceInfo(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.Id = in.readString();
        this.websiteUri = in.readParcelable(Uri.class.getClassLoader());
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.ratting = in.readFloat();
        this.attributatiosn = in.readString();
    }

    public static final Parcelable.Creator<PlaceInfo> CREATOR = new Parcelable.Creator<PlaceInfo>() {
        @Override
        public PlaceInfo createFromParcel(Parcel source) {
            return new PlaceInfo(source);
        }

        @Override
        public PlaceInfo[] newArray(int size) {
            return new PlaceInfo[size];
        }
    };
}
