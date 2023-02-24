package android.group4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.group4.model.Album;
import android.group4.model.Photo;
import android.group4.model.PhotoTag;
import android.group4.model.User;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class AddDeletePhotoTag extends AppCompatActivity {

    public static final String USER = "user";
    public static final String ALBUM_INDEX = "albumIndex";
    public static final String PHOTO_INDEX = "photoIndex";
    public static final String PHOTO_TAG_INDEX = "photoTagIndex";
    public static final String PHOTO_TAG_NAME = "photoTagName";
    public static final String PHOTO_TAG_VALUE = "photoTagValue";
    private int albumIndex;
    private int photoIndex;
    private int photoTagIndex;
    private HashMap<String, List<PhotoTag>> globalTagList;
    private User user;
    private Album album;
    private Photo photo;
    private PhotoTag photoTag;
    private EditText photoTagName;
    private EditText photoTagValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_delete_photo_tag);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Add/Delete Photo Tag");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        photoTagName = findViewById(R.id.photo_tag_name);
        photoTagValue = findViewById(R.id.photo_tag_value);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            user = (User) bundle.getSerializable(USER);
            albumIndex = bundle.getInt(ALBUM_INDEX);
            photoIndex = bundle.getInt(PHOTO_INDEX);
            photoTagIndex = bundle.getInt(PHOTO_TAG_INDEX);
            photoTagName.setText(bundle.getString(PHOTO_TAG_NAME));
            photoTagValue.setText(bundle.getString(PHOTO_TAG_VALUE));
            album = user.getAlbumList().get(albumIndex);
            photo = album.getAlbumPhotos().get(photoIndex);
            photoTag = photo.getTags().get(photoTagIndex);
            globalTagList = user.getGlobalTagList();
        }
        else{
            Button deleteButton = (Button) findViewById(R.id.photo_tag_delete);
            deleteButton.setEnabled(false);
            deleteButton.getBackground().setColorFilter(new BlendModeColorFilter(Color.GRAY, BlendMode.MULTIPLY));
        }
    }

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view){
        String photoTagType = photoTagName.getText().toString();
        String photoTagVal = photoTagValue.getText().toString();
        if(photoTagType == null || photoTagType.length() == 0 || photoTagVal == null || photoTagVal.length() == 0){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter a Photo Tag Type and Value!");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        if(!photoTagType.equalsIgnoreCase("location") && !photoTagType.equalsIgnoreCase("person")){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter a valid Photo Tag Type! (Location or Person)");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        try{
//            User.readData(this);
            user = User.getInstance();
            user.addGlobalTag(new PhotoTag(photoTagType, photoTagVal));

            User.writeData(user, this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(PHOTO_TAG_INDEX, photoTagIndex);
        bundle.putString(PHOTO_TAG_NAME, photoTagType);
        bundle.putString(PHOTO_TAG_VALUE, photoTagVal);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void delete(View view){
        Bundle bundle = new Bundle();
        bundle.putInt(PHOTO_TAG_INDEX, photoTagIndex);
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