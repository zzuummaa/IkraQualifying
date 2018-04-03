package ru.zuma.ikraqualifying;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ru.zuma.ikraqualifying.database.DbManager;
import ru.zuma.ikraqualifying.database.model.User;

public class ParticipantInfoActivity extends AppCompatActivity {

    private static final String TAG = ParticipantInfoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_info);
        TextView fullName = (TextView) findViewById(R.id.tvFullName);
        TextView group = (TextView) findViewById(R.id.tvGroup);
        TextView aboutMe = (TextView) findViewById(R.id.tvAboutMe);

        long id = getIntent().getLongExtra("participant_id", -1);
        User user = DbManager.getInstance().getUser(id);
        if (user == null) {
            /** TODO: Добавить обработку ошибок */
            Log.d(TAG, "User with ID " + String.valueOf(id) + " not found!");
            return;
        }

        String name = user.getName() + " " + user.getSecondName() + " " + user.getThirdName();
        fullName.setText(name);
        group.setText(user.getGroup());
        aboutMe.setText(user.getAbout());
    }
}
