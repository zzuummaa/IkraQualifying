package ru.zuma.ikraqualifying.database;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import ru.zuma.ikraqualifying.database.model.User;
import ru.zuma.ikraqualifying.database.tables.UserDbModel;

class DbManager {
    private static final DbManager ourInstance = new DbManager();

    static DbManager getInstance() {
        return ourInstance;
    }

    private DbManager() {
    }

    public List<User> getUsers() {
        List<UserDbModel> dbUsers = SQLite.select()
                                            .from(UserDbModel.class)
                                            .queryList();

        List<User> users = new ArrayList<>();
        for (UserDbModel dbUser : dbUsers) {
            users.add(dbUser.toUser());
        }

        return users;
    }

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
