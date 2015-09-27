package com.diemen.olaoff.utilities;

import com.diemen.olaoff.R;

import java.util.ArrayList;

/**
 * Created by vikoo on 27/09/15.
 */
public class CabItem {
    private String time;
    private String cabItem;
    private int iconRes;

    public CabItem(String time, String cabItem, int iconRes){
        this.cabItem = cabItem;
        this.iconRes = iconRes;
        this.time = time;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCabItem() {
        return cabItem;
    }

    public void setCabItem(String cabItem) {
        this.cabItem = cabItem;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }


    public static ArrayList<CabItem> getCabList(){
        ArrayList<CabItem> cabItems = new ArrayList<>();
        cabItems.add(new CabItem("", "Mini", R.drawable.ic_kp_car));
        cabItems.add(new CabItem("", "Sedan", R.drawable.ic_cab_selection_luxury));
        cabItems.add(new CabItem("", "Shuttle", R.drawable.ic_cab_selection_shuttle));
        cabItems.add(new CabItem("", "Ola Store", R.drawable.olastore));
        cabItems.add(new CabItem("", "Go Green", R.drawable.olaenv));
        return cabItems;
    }
}
