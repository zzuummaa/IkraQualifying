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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.zuma.ikraqualifying.database.DbManager;
import ru.zuma.ikraqualifying.database.model.User;

import static android.widget.AdapterView.*;

/**
 * Активность отображения списка участников команды.
 */
public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "MainActivity";
    final int ADD_RESULT = 1;

    /** Карта конвертации индекса списка участников в id БД */
    HashMap<Integer, Long> toDataBaseKey;

    /** Список участников */
    List<String> participantList;

    /** Адаптер для отображения списка участников на экране */
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<User> users = DbManager.getInstance().getUsers();

        toDataBaseKey = new HashMap<>();
        participantList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            addUserToActivity(user);
        }

        // находим список
        ListView lvParticipants = (ListView) findViewById(R.id.lvParticipants);

        // создаем адаптер
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, participantList);

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

                /** Вызываем activity отображения информации об участнике */
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
                /** Вызываем activity добавление участника */
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
                /** Отображаем созданного участника */

                long userID = data.getLongExtra("user_id", -1);
                if (userID == -1) {
                    Log.w(LOG_TAG, "ADD_RESULT doesn't contains user_id value");
                    return;
                }

                User user = DbManager.getInstance().getUser(userID);
                addUserToActivity(user);
                adapter.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Добавляет пользователя в струтуры, свзяанные с activity.
     *
     * !Не обновляет отображемые данные!
     *
     * Для отображения изменений необходимо вызвать
     * <code>adapter.notifyDataSetChanged()</code>
     *
     * {@link #adapter}
     *
     * @param user
     */
    public void addUserToActivity(User user) {
        participantList.add(user.getName() + " " + user.getSecondName());
        toDataBaseKey.put(participantList.size() - 1, user.getId());
    }
}
