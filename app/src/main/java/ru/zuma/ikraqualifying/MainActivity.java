package ru.zuma.ikraqualifying;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ru.zuma.ikraqualifying.database.DbManager;
import ru.zuma.ikraqualifying.database.model.User;
import ru.zuma.ikraqualifying.database.model.UserFactory;

import static android.widget.AdapterView.*;

/**
 * Активность отображения списка участников команды.
 */
public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "MainActivity";
    final int ADD_RESULT = 1;

    Menu menu;

    /** Карта конвертации индекса списка участников в id БД */
    List<Long> toDataBaseKey;

    /** Список участников */
    List<String> participantList;

    /** Адаптер для отображения списка участников на экране */
    ArrayAdapter<String> simpleItemAdapter;
    ArrayAdapter<String> multiplyChoiceAdapter;

    ListView lvParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<User> users = DbManager.getInstance().getUsers();

        toDataBaseKey = new ArrayList<>();
        participantList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            addUserToActivity(user);
        }

        // создаем адаптер
        simpleItemAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, participantList);

        multiplyChoiceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, participantList);

        // находим список
        lvParticipants = (ListView) findViewById(R.id.lvParticipants);
        lvParticipants.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // присваиваем адаптер списку
        lvParticipants.setAdapter(simpleItemAdapter);

        lvParticipants.setOnItemClickListener(new OnItemClickListener() {
            long prevTime;
            long cd = 600;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lvParticipants.getAdapter() != simpleItemAdapter) {
                    return;
                }

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
        this.menu = menu;
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
                /** Показываем меню удаления участников */

                showChangeActionBar();
                lvParticipants.setAdapter(multiplyChoiceAdapter);

                return true;

            case R.id.accept:
                /** Удаляем выделенных пользователей и возвращаем главное меню */

                SparseBooleanArray sbArray = lvParticipants.getCheckedItemPositions();

                for (int i = sbArray.size() - 1; i >= 0; i--) {
                    int listViewIndex = sbArray.keyAt(i);

                    if (sbArray.get(listViewIndex)) {
                        /** Удаляем данные об участнике */

                        User user = DbManager.getInstance()
                                .deleteUserAndGet(toDataBaseKey.get(listViewIndex));

                        if (user == null) {
                            Log.e(LOG_TAG, "deleteUserAndGet() can't delete user");
                            continue;
                        }

                        if (!UserFactory.deleteUserData(user)) {
                            Log.e(LOG_TAG, "deleteUserData() can't delete user data");
                            continue;
                        }

                        removeUserFromActivity(listViewIndex);
                    }
                }

                showMainActionBar();
                lvParticipants.setAdapter(simpleItemAdapter);
                simpleItemAdapter.notifyDataSetChanged();
                multiplyChoiceAdapter.notifyDataSetChanged();

                return true;

            case R.id.cancel:
                /** Отменяем процесс удаления, возврщая главное меню */

                showMainActionBar();
                lvParticipants.setAdapter(simpleItemAdapter);

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
                simpleItemAdapter.notifyDataSetChanged();
                multiplyChoiceAdapter.notifyDataSetChanged();
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
     * <code>
     *     simpleItemAdapter.notifyDataSetChanged();
     *     multiplyChoiceAdapter.notifyDataSetChanged();
     * </code>
     *
     * {@link #simpleItemAdapter}
     * {@link #multiplyChoiceAdapter}
     *
     * @param user пользователь для отображения в activity
     */
    public void addUserToActivity(User user) {
        participantList.add(user.getName() + " " + user.getSecondName());
        toDataBaseKey.add(user.getId());
    }

    /**
     * Удаляет пользователя из струтур, свзяанных с activity.
     *
     * @param listViewIndex индекс пользователя из списка участников
     */
    public void removeUserFromActivity(int listViewIndex) {
        participantList.remove(listViewIndex);
        toDataBaseKey.remove(listViewIndex);
    }

    /**
     * Показывает значки меню для изменения списка участников
     */
    public void showChangeActionBar() {
        menu.findItem(R.id.add).setVisible(false);
        menu.findItem(R.id.remove).setVisible(false);

        menu.findItem(R.id.accept).setVisible(true);
        menu.findItem(R.id.cancel).setVisible(true);
    }

    /**
     * Показывает главные значки меню
     */
    public void showMainActionBar() {
        menu.findItem(R.id.accept).setVisible(false);
        menu.findItem(R.id.cancel).setVisible(false);

        menu.findItem(R.id.add).setVisible(true);
        menu.findItem(R.id.remove).setVisible(true);
    }
}
