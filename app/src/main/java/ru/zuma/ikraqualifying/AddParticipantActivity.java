package ru.zuma.ikraqualifying;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ru.zuma.ikraqualifying.database.DbManager;
import ru.zuma.ikraqualifying.database.model.User;
import ru.zuma.ikraqualifying.utils.ImageDecoder;
import ru.zuma.ikraqualifying.utils.RealPathUtil;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddParticipantActivity extends AppCompatActivity {
    private final int PICK_PHOTO_RESULT = 1;
    private final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    EditText nameEditText;
    EditText secondNameEditText;
    EditText thirdNameEditText;
    EditText groupEditText;
    EditText aboutMeEditText;
    Button addPhotoButton;
    Button removePhotoButton;
    ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant);

        nameEditText = (EditText) findViewById(R.id.etName);
        secondNameEditText = (EditText) findViewById(R.id.etSecondName);
        thirdNameEditText = (EditText) findViewById(R.id.etThirdName);
        groupEditText = (EditText) findViewById(R.id.etGroup);
        aboutMeEditText = (EditText) findViewById(R.id.etAboutMe);

        imageView = (ImageView) findViewById(R.id.ivPhoto);

        addPhotoButton = (Button) findViewById(R.id.btAddPhoto);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isGranted = requestPermission(
                        AddParticipantActivity.this,
                        READ_EXTERNAL_STORAGE,
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                );

                if (isGranted) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, PICK_PHOTO_RESULT);
                }

            }
        });

        removePhotoButton = (Button) findViewById(R.id.btRemovePhoto);
        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.empty_photo);

                bitmap.recycle();
                bitmap = null;

                removePhotoButton.setVisibility(View.INVISIBLE);
                addPhotoButton.setText(R.string.add_photo);
            }
        });

        requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_participant, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_RESULT && resultCode == Activity.RESULT_OK) {
            // We need to recyle unused bitmaps
            if (bitmap != null) {
                bitmap.recycle();
            }

            String imagePath;
            if (Build.VERSION.SDK_INT > 19) {
                imagePath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
            } else if (Build.VERSION.SDK_INT >= 11) {
                imagePath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
            } else {
                imagePath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
            }

            bitmap = ImageDecoder.decodeSampledBitmapFromResource(
                    imagePath,
                    getWindow().getDecorView().getWidth(),
                    imageView.getHeight()
            );

            imageView.setImageBitmap(bitmap);

            addPhotoButton.setText(R.string.change_photo);
            removePhotoButton.setVisibility(View.VISIBLE);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                User user = parseUserData(
                        nameEditText.getText().toString(),
                        secondNameEditText.getText().toString(),
                        thirdNameEditText.getText().toString(),
                        groupEditText.getText().toString(),
                        aboutMeEditText.getText().toString()
                );

                if (user == null) {
                    Toast.makeText(
                            this, R.string.invalid_user_data, Toast.LENGTH_LONG
                    ).show();
                    return true;
                }

                long userID = -1;
                if (bitmap != null) {
                    userID = DbManager.getInstance().addUser(user, bitmap);
                } else {
                    userID = DbManager.getInstance().addUser(user);
                }

                Intent intent = new Intent();
                intent.putExtra("user_id", userID);
                setResult(RESULT_OK, intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     * @param name
     * @param secondName
     * @param thirdName
     * @param group
     * @param aboutMe
     * @return
     */
    private User parseUserData(String name, String secondName, String thirdName,
                               String group, String aboutMe) {

        if (name.equals("") || secondName.equals("")) {
            return null;
        }

        User user = new User(name, secondName, thirdName, group, aboutMe);

        return user;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public boolean requestPermission(Activity thisActivity, String permission, int requestCode) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    permission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            }
        } else {
            // Permission has already been granted
            return true;
        }

        return false;
    }
}
