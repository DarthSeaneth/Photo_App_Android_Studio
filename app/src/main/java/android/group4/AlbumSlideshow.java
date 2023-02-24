package android.group4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.group4.model.Album;
import android.group4.model.Photo;
import android.group4.model.User;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class AlbumSlideshow extends AppCompatActivity {

    private int albumIndex;
    private User user;
    private Album album;
    private List<Photo> photoList;
    private int index;
    private ImageView photoImageView;
    private Button previousButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_slideshow);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Album Slideshow");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        try{
            User.readData(this);
            user = User.getInstance();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        albumIndex = bundle.getInt(AddEditDeleteOpenAlbum.ALBUM_INDEX);
        index = 0;
        album = user.getAlbumList().get(albumIndex);
        photoList = album.getAlbumPhotos();
        photoImageView = findViewById(R.id.photo_image_view);
        previousButton = (Button) findViewById(R.id.previous_photo);
        nextButton = (Button) findViewById(R.id.next_photo);
        Uri uri = Uri.parse(photoList.get(index).getFileLocation());
        photoImageView.setImageURI(uri);
        previousButton.setEnabled(index != 0);
        grayOutButton(previousButton, index == 0);
        nextButton.setEnabled(index != photoList.size() - 1);
        grayOutButton(nextButton, index == photoList.size() - 1);
    }

    public void previous(View view){
        index --;
        Uri uri = Uri.parse(photoList.get(index).getFileLocation());
        photoImageView.setImageURI(uri);
        previousButton.setEnabled(index != 0);
        grayOutButton(previousButton, index == 0);
        nextButton.setEnabled(index != photoList.size() - 1);
        grayOutButton(nextButton, index == photoList.size() - 1);
    }

    public void next(View view){
        index ++;
        Uri uri = Uri.parse(photoList.get(index).getFileLocation());
        photoImageView.setImageURI(uri);
        previousButton.setEnabled(index != 0);
        grayOutButton(previousButton, index == 0);
        nextButton.setEnabled(index != photoList.size() - 1);
        grayOutButton(nextButton, index == photoList.size() - 1);
    }

    private void grayOutButton(Button button, boolean condition){
        if(condition){
            button.getBackground().setColorFilter(new BlendModeColorFilter(Color.GRAY, BlendMode.MULTIPLY));
        }
        else{
            button.getBackground().clearColorFilter();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}