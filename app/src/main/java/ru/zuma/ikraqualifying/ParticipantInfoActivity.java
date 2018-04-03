package ru.zuma.ikraqualifying;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;

import ru.zuma.ikraqualifying.database.DbManager;
import ru.zuma.ikraqualifying.database.model.User;
import ru.zuma.ikraqualifying.utils.ImageDecoder;

/**
 * Активность, отвечающая за отображение
 * информации об участнике.
 */
public class ParticipantInfoActivity extends AppCompatActivity {

    private static final String TAG = ParticipantInfoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_info);
        TextView fullName = (TextView) findViewById(R.id.tvFullName);
        TextView group = (TextView) findViewById(R.id.tvGroup);
        TextView aboutMe = (TextView) findViewById(R.id.tvAboutMe);
        final ImageView imageView = (ImageView) findViewById(R.id.tvImage);

        long id = getIntent().getLongExtra("participant_id", -1);
        User user = DbManager.getInstance().getUser(id);
        if (user == null) {
            /** TODO: Добавить обработку ошибок */
            Log.d(TAG, "User with ID " + String.valueOf(id) + " not found!");
            return;
        }

        final String image = user.getImage();

        // Устанавливаем изображение, как только будут известны размеры imageView
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            String localImage;

            @Override
            public void onGlobalLayout() {
                // Вызывается только один раз, пока localImage null
                if (localImage == null && image != null) {
                    localImage = image;

                    int reqWidth = imageView.getWidth();
                    int reqHeight = imageView.getHeight();
                    Bitmap decodedImage = ImageDecoder.decodeSampledBitmapFromResource(
                            image,
                            reqWidth,
                            reqHeight
                    );
                    Log.d(TAG, "set image with width=" + decodedImage.getWidth()
                                               + ", height=" + decodedImage.getHeight());
                    imageView.setImageBitmap(decodedImage);
                }
            }
        });


        String name = user.getName() + " " + user.getSecondName() + " " + user.getThirdName();
        fullName.setText(name);
        group.setText(user.getGroup());
        aboutMe.setText(user.getAbout());
    }
}
