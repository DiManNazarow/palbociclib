package ru.mbg.palbociclib.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;

import com.amulyakhare.textdrawable.TextDrawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.utils.Utils;
import ru.mbg.palbociclib.models.Patient;

public class AvatarHelper {

    private static LruCache<String, Drawable> cache;

    public static void initCache() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        if (maxMemory > Integer.MAX_VALUE) {
            maxMemory = Integer.MAX_VALUE;
        }
        final int cacheSize = (int)maxMemory / 8;
        cache = new LruCache<>(cacheSize);
    }

    // Создать изображение пользователя из переданного имени
    public static Drawable createAvatarForName(String name, Context context) {
        StringBuilder initials = new StringBuilder();
        for (String s : name.split(" ")) {
            if (s.length() > 0) {
                initials.append(s.charAt(0));
            }
        }

        return TextDrawable.builder()
                .beginConfig()
                    .width(Utils.fromDP(120, context.getResources()))
                    .height(Utils.fromDP(120, context.getResources()))
                    .textColor(-1)
                    .fontSize(Utils.fromDP(40, context.getResources()))
                    .toUpperCase()
                .endConfig()
                .buildRound(initials.substring(0, Math.min(3, initials.length())), ContextCompat.getColor(context, R.color.avatar_background));
    }

    // Загрузить сохраненное изображения пользователя или вернуть автогенерированное по инициалам
    public static Drawable getAvatarForPatient(Patient patient, Context context) {
        Drawable cached = cache.get(patient.getId());
        if (cached != null) {
            return cached;
        }

        // Попробовать открыть изображение на диске
        String avatarPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator  + "avatars" + File.separator + patient.getId();
        File avatar = new File(avatarPath);
        Drawable drawable;
        if (avatar.exists()) {
            drawable = Drawable.createFromPath(avatarPath);
        } else {
            // Сгенерировать изображение из инициалов
            drawable = createAvatarForName(patient.getName(), context);
        }

        cache.put(patient.getId(), drawable);
        return drawable;
    }

    // Сохранить изображение пользователя
    public static void saveAvatarForPatient(Bitmap avatar, Patient patient, Context context) throws IOException {
        String avatarsDirPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator  + "avatars";
        File avatarsDir = new File(avatarsDirPath);
        if (!avatarsDir.exists()) {
            if (!avatarsDir.mkdirs()) {
                throw new IOException("Avatar file not created");
            }
        }
        File avatarFile = new File(avatarsDir, patient.getId());
        if (avatarFile.createNewFile()) {
            FileOutputStream out = new FileOutputStream(avatarFile);
            avatar.compress(Bitmap.CompressFormat.PNG, 1, out);
            cache.put(patient.getId(), new BitmapDrawable(context.getResources(), avatar));
        } else {
            throw new IOException("Avatar file not created");
        }
    }

    public static void clearCachedAvatarForPatient(Patient patient) {
        cache.remove(patient.getId());
    }

    // Удалить сохраненное ранее изображение пользователя
    public static void deleteAvatarForPatient(Patient patient, Context context) {
        String avatarPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator  + "avatars" + File.separator + patient.getId();
        //noinspection ResultOfMethodCallIgnored
        new File(avatarPath).delete();
        cache.remove(patient.getId());
    }
}
