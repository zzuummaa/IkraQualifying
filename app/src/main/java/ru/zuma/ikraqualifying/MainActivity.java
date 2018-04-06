package ru.zuma.ikraqualifying;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    final int ADD_RESULT = 1;

    // Convert list key to dataBase key
    HashMap<Integer, Long> toDataBaseKey;

    List<String> namesList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<User> users = DbManager.getInstance().getUsers();

        // Put data like this: toDataBaseKey.put(listIndex, dataBaseID);
        toDataBaseKey = new HashMap<>();
        namesList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            addUserToActivity(user);
        }
        Collections.sort(namesList);

        // находим список
        ListView lvParticipants = (ListView) findViewById(R.id.lvParticipants);

        // создаем адаптер
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namesList);

        // присваиваем адаптер списку
        lvParticipants.setAdapter(adapter);

        lvParticipants.setOnItemClickListener(new OnItemClickListener() {
            long prevTime;
            long cd = 600;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Обрабатываем событие только раз в cd миллисекундах
                long currTime = System.currentTimeMillis();
                if (currTime - prevTime < cd) {
                    return;
                }
                prevTime = currTime;

                Log.d(LOG_TAG, "list onItemClick in pos=" + position + ", id=" + id);

                Intent intent = new Intent(MainActivity.this, ParticipantInfoActivity.class);
                intent.putExtra("participant_id", toDataBaseKey.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, AddParticipantActivity.class);
                startActivityForResult(intent, ADD_RESULT);
                return true;
            case R.id.remove:
                // TODO: реализовать функционал
                Toast.makeText(this, R.string.not_supported, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_RESULT) {
            if (resultCode == RESULT_OK) {

                long userID = data.getLongExtra("user_id", -1);
                if (userID == -1) {
                    Log.w(LOG_TAG, "ADD_RESULT doesn't contains user_id value");
                    return;
                }

                User user = DbManager.getInstance().getUser(userID);
                addUserToActivity(user);
                Collections.sort(namesList);
                adapter.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addUserToActivity(User user) {
        namesList.add(user.getName());
        toDataBaseKey.put(namesList.size() - 1, user.getId());
    }
}
