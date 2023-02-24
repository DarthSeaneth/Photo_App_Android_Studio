package android.group4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.group4.model.Photo;
import android.group4.model.PhotoTag;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

public class PhotoSearchPhotoDisplay extends AppCompatActivity {

    public static final String PHOTO = "photo";

    private Photo photo;
    private ImageView photoImageView;
    private ListView photoTagListView;
    private List<PhotoTag> photoTagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_search_photo_display);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Photo Display");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        photo = (Photo) bundle.getSerializable(PHOTO);
        photoTagList = photo.getTags();
        photoImageView = findViewById(R.id.photo_image_view);
        photoTagListView = findViewById(R.id.photo_tag_list);
        Uri uri = Uri.parse(photo.getFileLocation());
        photoImageView.setImageURI(uri);
        ArrayAdapter<PhotoTag> adapter = new ArrayAdapter<>(this, R.layout.photo_tag, photoTagList);
        photoTagListView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}