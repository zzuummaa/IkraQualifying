package ru.zuma.ikraqualifying.database;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.TransactionTooLargeException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.io.ByteArrayOutputStream;

import ru.zuma.ikraqualifying.App;
import ru.zuma.ikraqualifying.R;
import ru.zuma.ikraqualifying.database.tables.ImageDbModel;
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

    @Migration(version = 0, database = AppDatabase.class)
    public static class InitMigration extends BaseMigration {

        @Override
        public void migrate(@NonNull DatabaseWrapper database) {
            UserDbModel initUser = new UserDbModel();
            initUser.setName("Test");
            initUser.setSecondName("User");
            initUser.setThirdName("OfUser");
            initUser.setGroup("H11");
            initUser.setAbout("Ok!");
            long userId = initUser.insert(database);

            Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(),
                    R.drawable.photo1);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            final byte[] bytes = stream.toByteArray();
            final Blob blob = new Blob(bytes);

            ImageDbModel initImage = new ImageDbModel();
            initImage.setUserId(userId);
            initImage.setImage(blob);
            /** Не получаестя нормально сохранить блоб */
            initImage.save(database);
        }
    }
}
