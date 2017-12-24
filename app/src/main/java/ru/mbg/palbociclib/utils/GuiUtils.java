package ru.mbg.palbociclib.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import ru.mbg.palbociclib.R;

public class GuiUtils {

    public static void displayOkDialog(Context context, final String title, final String message, @Nullable DialogInterface.OnClickListener listener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!Utils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setPositiveButton(R.string.app_ok, listener);
        builder.setCancelable(cancelable);
        try {
            builder.show();
        } catch (WindowManager.BadTokenException ignored) {
        }
    }

    public static void displayOkDialog(Context context, final int title, final int message, @Nullable DialogInterface.OnClickListener listener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!Utils.isEmpty(context.getString(title))) {
            builder.setTitle(title);
        }
        builder.setMessage(context.getString(message));
        builder.setPositiveButton(R.string.app_ok, listener);
        builder.setCancelable(cancelable);
        try {
            builder.show();
        } catch (WindowManager.BadTokenException ignored) {
        }
    }

    public static void showSelectDialog(Context context, String title, String[] elements, DialogInterface.OnClickListener onClickListener, boolean cancelable){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(elements, onClickListener);
        builder.setCancelable(cancelable);
        try {
            builder.show();
        } catch (WindowManager.BadTokenException ignored) {
        }
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
