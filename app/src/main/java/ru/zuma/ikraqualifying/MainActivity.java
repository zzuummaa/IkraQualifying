package ru.zuma.ikraqualifying;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.zuma.ikraqualifying.database.DbManager;
import ru.zuma.ikraqualifying.database.model.User;

import static android.widget.AdapterView.*;

/**
 * Главная активность приложения.
 * Содержит список участников команды.
 */
public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "MainActivity";

    // Convert list key to dataBase key
    HashMap<Integer, Long> toDataBaseKey;

    List<String> namesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<User> users = DbManager.getInstance().getUsers();

        // Put data like this: toDataBaseKey.put(listIndex, dataBaseID);
        toDataBaseKey = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            namesList.add(user.getName());
            toDataBaseKey.put(i, user.getId());
        }

        // находим список
        ListView lvParticipants = (ListView) findViewById(R.id.lvParticipants);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namesList);

        // присваиваем адаптер списку
        lvParticipants.setAdapter(adapter);

        lvParticipants.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "list onItemClick in pos=" + position + ", id=" + id);

                Intent intent = new Intent(MainActivity.this, ParticipantInfoActivity.class);
                intent.putExtra("participant_id", toDataBaseKey.get(position));
                startActivity(intent);
            }
        });
    }
}
