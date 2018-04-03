package ru.zuma.ikraqualifying.database.model;

/**
 * Created by sibirsky on 02.04.18.
 */

public class User {

    private long id = 0;
    private String name;
    private String secondName;
    private String thirdName;
    private String group;
    private String about;

    public User()
    {
    }

    public User(long id, String name, String secondName, String thirdName, String group, String about) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.group = group;
        this.about = about;
    }

    public User(String name, String secondName, String thirdName, String group, String about) {
        this.name = name;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.group = group;
        this.about = about;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

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