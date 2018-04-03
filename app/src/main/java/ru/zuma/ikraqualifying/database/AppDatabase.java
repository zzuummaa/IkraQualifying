package ru.zuma.ikraqualifying.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.zuma.ikraqualifying.App;
import ru.zuma.ikraqualifying.R;
import ru.zuma.ikraqualifying.database.model.User;
import ru.zuma.ikraqualifying.database.tables.UserDbModel;

/**
 * Конфигурации базы данных в
 * соответствии с требованиями
 * ORM DBFlow.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;

    /**
     * Миграция с нулевой версии на первую.
     * В контексте DBFlow, нулевая версия
     * означает отсутствие БД. Таким образом
     * реализуется начальное заполнение базы
     * при первом запуске приложений.
     */
    @Migration(version = 0, database = AppDatabase.class)
    public static class InitMigration extends BaseMigration {

        @Override
        public void migrate(@NonNull DatabaseWrapper database) {
            List<User> users = createDevelopers();
            for (User user : users) {
                UserDbModel dbUser = new UserDbModel(user);
                dbUser.insert(database);
            }
        }

        /**
         * Создает список разработчиков
         * (участников команды).
         *
         * @return Список пользователей-разработчиков
         */
        private List<User> createDevelopers() {
            User stepa = createUser("Степан", "Фоменко", "Отчество",
                    "СМ5-62", "О Степе", R.drawable.stepa, "strepa.jpg");
            User artem = createUser("Артем", "Ткаченко", "Алексеевич",
                    "ИУ7", "Об Артеме", R.drawable.artem, "artem.jpg");
            User timur = createUser("Тимур", "Ахтямов", "Ришадович",
                    "СМ5-61", "О Тимуре", R.drawable.timur, "timur.jpg");

            List<User> users = new ArrayList<>();
            users.add(stepa);
            users.add(artem);
            users.add(timur);

            return users;
        }

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
        private User createUser(String name, String secondName, String thirdName,
                                String group, String about, int imageId, String imageFileName) {
            User user = new User(name, secondName, thirdName, group, about);
            Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), imageId);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            String image = null;
            try {
                FileOutputStream outputStream = App.getContext().openFileOutput(imageFileName,
                        Context.MODE_PRIVATE);
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.close();
                image = new File(App.getContext().getFilesDir(), imageFileName).getAbsolutePath();
            } catch (FileNotFoundException e) {
                Log.d(AppDatabase.class.getName(), e.getMessage(), e);
            } catch (IOException e) {
                Log.d(AppDatabase.class.getName(), e.getMessage(), e);
            }
            user.setImage(image);

            return user;
        }
    }
}
