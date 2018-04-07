package ru.zuma.ikraqualifying.database.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import ru.zuma.ikraqualifying.App;
import ru.zuma.ikraqualifying.utils.ImageDecoder;

/**
 * Вспомогательный класс для создания
 * объектов пользователей
 */
public class UserFactory {

    private static final String TAG = UserFactory.class.getName();

    /**
     * Создает пользователя и копирует
     * его изображение из ресурсов в
     * файловое хранилище.
     *
     * @param name Имя пользователя
     * @param secondName Фамилия пользователя
     * @param thirdName Отчество пользователя
     * @param group Учебная группа пользователя
     * @param about Информация о пользователе
     * @param imageId ID ресурса - фото пользователя
     * @param imageFileName Имя файла, в котором будет сохранено изображение
     * @return Объект пользователя
     */
    public static User createUser(String name, String secondName, String thirdName,
                            String group, String about, int imageId, String imageFileName) {

        User user = new User(name, secondName, thirdName, group, about);

        Bitmap bitmap = ImageDecoder.decodeSampledBitmapFromResource(
                App.getContext().getResources(), imageId);

        String image = null;
        try {
            File imageFile = ImageDecoder.saveBitmapToFile(bitmap,
                    ImageDecoder.DEFAULT_COMPRESS_QUALITY, imageFileName);
            image = imageFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        user.setImage(image);

        return user;
    }

    public static boolean deleteUserData(User user) {
        if (user.getImage() != null) {
            File imageFile = new File(user.getImage());
            return imageFile.delete();
        } else {
            return true;
        }
    }
}
