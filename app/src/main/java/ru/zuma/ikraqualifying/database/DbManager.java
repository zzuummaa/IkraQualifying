package ru.zuma.ikraqualifying.database;

import android.graphics.Bitmap;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.zuma.ikraqualifying.database.model.User;
import ru.zuma.ikraqualifying.database.tables.UserDbModel;
import ru.zuma.ikraqualifying.database.tables.UserDbModel_Table;
import ru.zuma.ikraqualifying.utils.ImageDecoder;

/**
 * Класс, отвечающий за взаимодействие
 * с базой данных.
 */
public class DbManager {
    final String TAG = getClass().getSimpleName();

    private static final DbManager ourInstance = new DbManager();

    public static DbManager getInstance() {
        return ourInstance;
    }

    private DbManager() {
    }

    /**
     * Получает пользователей (участника) из базы данных.
     *
     * @return Список пользователей, отсортированный
     * по именам
     */
    public List<User> getUsers() {
        List<UserDbModel> dbUsers = SQLite.select()
                                            .from(UserDbModel.class)
                                            .orderBy(UserDbModel_Table.name, true)
                                            .queryList();

        List<User> users = new ArrayList<>();
        for (UserDbModel dbUser : dbUsers) {
            users.add(dbUser.toUser());
        }

        return users;
    }


    /**
     * Получает пользователя (участника) из базы данных.
     *
     * @param id ID пользователя
     * @return Найденный пользователь, либо null, если
     * пользователь не найден
     */
    public User getUser(final long id) {
        UserDbModel dbUser = SQLite.select()
                                        .from(UserDbModel.class)
                                        .where(UserDbModel_Table.id.eq(id))
                                        .querySingle();

        if (dbUser == null)
            return null;
        return dbUser.toUser();
    }

    /**
     * Добавляет нового пользователя (участника) в базу.
     *
     * @param user Пользователь для добавления
     * @return ID новой записи в базе. ID пользователя, переданного в метод, игнорируется.
     */
    public long addUser(final User user) {
        UserDbModel newDbUser = new UserDbModel();

        newDbUser.setName(user.getName());
        newDbUser.setSecondName(user.getSecondName());
        newDbUser.setThirdName(user.getThirdName());
        newDbUser.setGroup(user.getGroup());
        newDbUser.setAbout(user.getAbout());
        newDbUser.setImage(user.getImage());

        long newId = newDbUser.insert();

        return newId;
    }

    /**
     * Добавляет нового пользователя (участника) в базу.
     *
     * @param user Пользователь для добавления
     * @return ID новой записи в базе. ID и путь к фото пользователя,
     * переданного в метод, игнорируется.
     */
    public long addUser(final User user, Bitmap bitmap) {
        UserDbModel newDbUser = new UserDbModel();

        newDbUser.setName(user.getName());
        newDbUser.setSecondName(user.getSecondName());
        newDbUser.setThirdName(user.getThirdName());
        newDbUser.setGroup(user.getGroup());
        newDbUser.setAbout(user.getAbout());
        newDbUser.setImage(user.getImage());

        long newId = newDbUser.insert();

        String image = null;
        try {
            String fileName = String.valueOf(newId) + ".jpg";
            File imageFile = ImageDecoder.saveBitmapToFile(bitmap, 60, fileName);
            image = imageFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            newDbUser.delete();
        }

        newDbUser.setImage(image);
        newDbUser.update();

        return newId;
    }
}
