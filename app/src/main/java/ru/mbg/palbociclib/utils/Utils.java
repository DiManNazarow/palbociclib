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

    public static int calculateGrade(int leukocytesCount, int neutrophilsCount){
        int grade = -1;
        double ln = leukocytesCount * ( neutrophilsCount / 100.0 );
        if (ln < 500) {
            grade = 4;
        } else if (ln <= 1000) {
            grade = 3;
        } else if (ln < 1500) {
            grade = 2;
        } else {
            grade = 1;
        }
        return grade;
    }
}
