package ru.mbg.palbociclib.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
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

}
