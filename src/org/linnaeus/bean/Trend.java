package org.linnaeus.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 19/07/11
 * Time: 17:18
 * To change this template use File | Settings | File Templates.
 */
public class Trend implements Parcelable {

       public static final Parcelable.Creator<Trend> CREATOR = new
            Parcelable.Creator<Trend>() {
                public Trend createFromParcel(Parcel in) {
                    return new Trend(in);
                }
                    public Trend[] newArray(int size) {
                        return new Trend[size];
                    }
                };

    private String trend;
    private int mentions;

    public Trend() {
    }

    public Trend(Parcel parcel) {
        trend = parcel.readString();
        mentions = parcel.readInt();
    }

    public int getMentions() {
        return mentions;
    }

    public void setMentions(int mentions) {
        this.mentions = mentions;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public int describeContents() {
        return this.hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trend);
        parcel.writeInt(mentions);
    }

    @Override
    public String toString() {
        if (mentions != 0){
            return trend + " " + mentions;
        } else {
            return trend;
        }
    }
}
