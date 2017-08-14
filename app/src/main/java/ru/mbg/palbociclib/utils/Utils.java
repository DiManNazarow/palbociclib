package ru.mbg.palbociclib.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;

public class Utils {
    public static int fromDP(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static boolean isEmpty(String target) {
        return TextUtils.isEmpty(target) || "null".equalsIgnoreCase(target);
    }
}
