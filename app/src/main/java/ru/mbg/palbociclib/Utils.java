package ru.mbg.palbociclib;

import android.content.res.Resources;
import android.util.TypedValue;

public class Utils {
    public static int fromDP(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
