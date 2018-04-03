package ru.zuma.ikraqualifying.database.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import ru.zuma.ikraqualifying.database.AppDatabase;
import ru.zuma.ikraqualifying.database.model.User;

@Table(database = AppDatabase.class)
public class UserDbModel extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String name;

    @Column
    private String secondName;

    @Column
    private String thirdName;

    @Column
    private String group;

    @Column
    private String about;

    public UserDbModel()
    {
    }

    public UserDbModel(User user)
    {
        this.id = user.getId();
        this.name = user.getName();
        this.secondName = user.getSecondName();
        this.thirdName = user.getThirdName();
        this.group = user.getGroup();
        this.about = user.getAbout();
    }

    public User toUser()
    {
        return new User(id, name, secondName, thirdName, group, about);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
