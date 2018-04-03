package ru.zuma.ikraqualifying.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import ru.zuma.ikraqualifying.R;
import ru.zuma.ikraqualifying.database.model.User;
import ru.zuma.ikraqualifying.database.tables.UserDbModel;
import ru.zuma.ikraqualifying.database.tables.UserDbModel_Table;

public class DbManager {
    private static final DbManager ourInstance = new DbManager();

    public static DbManager getInstance() {
        return ourInstance;
    }

    private DbManager() {
    }

    /**
     * Получает пользователей из базы данных.
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
     * Получает пользователя из базы данных.
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
     * Возрващает изображение пользователя.
     * Не работает, нужно переделать!
     * @param id
     * @return
     */
    @Deprecated
    public Bitmap getUserImage(final long id) {
        return null;
    }

    /**
     * Добавляет нового пользователя в базу.
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

        long newId = newDbUser.insert();

        return newId;
    }
}
