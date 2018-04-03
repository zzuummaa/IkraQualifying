package ru.zuma.ikraqualifying;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ParticipantInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_info);

        int id = getIntent().getIntExtra("participant_id", -1);

        // Get participant from data base by id

        TextView fullName = (TextView) findViewById(R.id.tvFullName);
        TextView group = (TextView) findViewById(R.id.tvGroup);
        TextView aboutMe = (TextView) findViewById(R.id.tvAboutMe);

        //Replace strings by true information
        fullName.setText("Фоменко Степан Владимирович");
        group.setText("CM5-62");
        aboutMe.setText("Люблю походы в горы, играть на гитаре и дзюдо. Жизнь создана для приключений!");
    }
}
