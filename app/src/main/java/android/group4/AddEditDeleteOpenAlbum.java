package android.group4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.group4.model.User;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEditDeleteOpenAlbum extends AppCompatActivity {

    public static final String ALBUM_INDEX = "albumIndex";
    public static final String ALBUM_TITLE = "albumTitle";
    public static final String USER = "user";
    private int albumIndex;
    private User user;
    private EditText albumTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_delete_album);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Add/Edit/Delete/Open Album");
        setSupportActionBar(myToolbar);
        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albumTitle = findViewById(R.id.album_title);
        Bundle bundle = getIntent().getExtras();
        try{
            User.readData(this);
            user = User.getInstance();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if(bundle != null){
            albumIndex = bundle.getInt(ALBUM_INDEX);
            albumTitle.setText(bundle.getString(ALBUM_TITLE));
            Button slideshowButton = (Button) findViewById(R.id.album_slideshow);
            if(user.getAlbumList().get(albumIndex).getNumPhotos() < 1){
                slideshowButton.setEnabled(false);
                slideshowButton.getBackground().setColorFilter(new BlendModeColorFilter(Color.GRAY, BlendMode.MULTIPLY));
            }
        }
        else{
            Button deleteButton = (Button) findViewById(R.id.album_delete);
            Button openButton = (Button) findViewById(R.id.album_open);
            Button slideshowButton = (Button) findViewById(R.id.album_slideshow);
            deleteButton.setEnabled(false);
            deleteButton.getBackground().setColorFilter(new BlendModeColorFilter(Color.GRAY, BlendMode.MULTIPLY));
            openButton.setEnabled(false);
            openButton.getBackground().setColorFilter(new BlendModeColorFilter(Color.GRAY, BlendMode.MULTIPLY));
            slideshowButton.setEnabled(false);
            slideshowButton.getBackground().setColorFilter(new BlendModeColorFilter(Color.GRAY, BlendMode.MULTIPLY));
        }
    }

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view){
        String title = albumTitle.getText().toString();
        if(title == null || title.length() == 0){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter an Album title!");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putString(ALBUM_TITLE, title);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void delete(View view){
        String title = albumTitle.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putString(ALBUM_TITLE, title);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_FIRST_USER, intent);
        finish();
    }

    public void open(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        bundle.putInt(ALBUM_INDEX, albumIndex);
        Intent intent = new Intent(this, AlbumPhotoList.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void albumSlideshow(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        bundle.putInt(ALBUM_INDEX, albumIndex);
        Intent intent = new Intent(this, AlbumSlideshow.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}