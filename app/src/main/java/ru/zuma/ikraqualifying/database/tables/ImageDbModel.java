package ru.zuma.ikraqualifying.database.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

import ru.zuma.ikraqualifying.database.AppDatabase;

@Table(database = AppDatabase.class)
public class ImageDbModel extends BaseModel {

    @PrimaryKey
    private long id;

    @Column
    private long userId;

    @Column
    private Blob image;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ImageDbModel() { }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
