package ru.zuma.ikraqualifying.utils;

/**
 * Created by Stephan on 03.04.2018.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.zuma.ikraqualifying.App;

/**
 * Класс для работы с изображениями
 */
public class ImageDecoder {

    /** Качество сжатия по умолчанию */
    public static final int DEFAULT_COMPRESS_QUALITY = 60;

    /** Максимальная высота изображения в DPI */
    public static final int MAX_HEIGHT_DPI = 180;

    /** Максимальные размеры изображения в пикселах */
    public static final int MAX_HEIGHT;
    public static final int MAX_WIDTH;

    static {
        MAX_HEIGHT = (int) (MAX_HEIGHT_DPI * Resources.getSystem().getDisplayMetrics().density);
        MAX_WIDTH = MAX_HEIGHT;
    }

    /**
     * Читает данные изображения и изменяет размер изображения
     * под требуемый
     *
     * @param path Полный путь к файлу
     * @param reqWidth Требуемая ширина выходного изображения
     * @param reqHeight Требуемая высота выходного изображения
     * @return Выходное изображение с подогнанным размером
     */
    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {

        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Вычисляем inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;
        // Используем конфигурацию без прозрачности
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * Читает данные изображения и изменяет размер изображения
     * под требуемый
     *
     * @param res
     * @param id
     * @param reqWidth Требуемая ширина выходного изображения
     * @param reqHeight Требуемая высота выходного изображения
     * @return Выходное изображение с подогнанным размером
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int id,
                                                         int reqWidth, int reqHeight) {

        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);

        // Вычисляем inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;
        // Используем конфигурацию без прозрачности
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(res, id, options);
    }

    /**
     * Читает данные изображения и изменяет размер изображения
     * под специальный размер для отображения на экран
     *
     * @param res Ресурсы приложения
     * @param id ID ресурса
     * @return Выходное изображение с подогнанным размером
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int id) {
        return decodeSampledBitmapFromResource(res, id, MAX_WIDTH, MAX_HEIGHT);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Сохраняет изображение Bitmap в файл JPEG.
     *
     * @param bitmap Bitmap изображение
     * @param quality Качество (характеризует отсутствие потерь)
     * @param fileName Имя файла
     * @return Файл с изображением
     * @throws IOException
     */
    public static File saveBitmapToFile(Bitmap bitmap, int quality, String fileName) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

        FileOutputStream outputStream = App.getContext().openFileOutput(fileName,
                Context.MODE_PRIVATE);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.close();

        return new File(App.getContext().getFilesDir(), fileName);
    }
}
