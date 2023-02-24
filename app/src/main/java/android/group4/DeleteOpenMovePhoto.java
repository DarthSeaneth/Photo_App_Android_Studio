package android.group4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.group4.model.Album;
import android.group4.model.Photo;
import android.group4.model.User;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DeleteOpenMovePhoto extends AppCompatActivity {

    public static final String USER = "user";
    public static final String ALBUM_TITLE = "albumTitle";
    public static final String ALBUM_INDEX = "albumIndex";
    public static final String PHOTO_INDEX = "photoIndex";
    private int albumIndex;
    private int photoIndex;
    private User user;
    private Album album;
    private Photo photo;
    private EditText albumTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_open_move_photo);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Delete/Open Photo");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        albumTitle = findViewById(R.id.album_title);
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable(USER);
        albumIndex = bundle.getInt(ALBUM_INDEX);
        photoIndex = bundle.getInt(PHOTO_INDEX);
        album = user.getAlbumList().get(albumIndex);
        photo = album.getAlbumPhotos().get(photoIndex);
    }

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void delete(View view){
        Bundle bundle = new Bundle();
        bundle.putInt(PHOTO_INDEX, photoIndex);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void open(View view){
        Bundle bundle = new Bundle();
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putInt(PHOTO_INDEX, photoIndex);
        bundle.putSerializable(USER, user);
        Intent intent = new Intent(this, PhotoDisplay.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void move(View view){
        String title = albumTitle.getText().toString();
        if(title == null || title.length() == 0){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter an Album title!");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        boolean albumFound = false;
        for(Album a: user.getAlbumList()){
            if(a.getAlbumTitle().equals(title)){
                albumFound = true;
                break;
            }
        }
        if(!albumFound){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Album does not exist!");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(PHOTO_INDEX, photoIndex);
        bundle.putString(ALBUM_TITLE, title);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_FIRST_USER, intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}