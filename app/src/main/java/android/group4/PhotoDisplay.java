package android.group4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.group4.model.Album;
import android.group4.model.Photo;
import android.group4.model.PhotoTag;
import android.group4.model.User;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

public class PhotoDisplay extends AppCompatActivity {

    private int albumIndex;
    private int photoIndex;
    private Album album;
    private Photo photo;
    private User user;
    private ImageView photoImageView;
    private ListView photoTagListView;
    private List<PhotoTag> photoTagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_display);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Photo Display");
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
        if(bundle != null){
            albumIndex = bundle.getInt(DeleteOpenMovePhoto.ALBUM_INDEX);
            photoIndex = bundle.getInt(DeleteOpenMovePhoto.PHOTO_INDEX);
        }
        album = user.getAlbumList().get(albumIndex);
        photo = album.getAlbumPhotos().get(photoIndex);
        photoTagList = photo.getTags();
        photoImageView = findViewById(R.id.photo_image_view);
        Uri uri = Uri.parse(photo.getFileLocation());
        photoImageView.setImageURI(uri);
        photoTagListView = findViewById(R.id.photo_tag_list);
        ArrayAdapter<PhotoTag> adapter = new ArrayAdapter<>(this, R.layout.photo_tag, photoTagList);
        photoTagListView.setAdapter(adapter);
        photoTagListView.setOnItemClickListener((list, view, pos, id) -> showPhotoTag(pos));
        registerActivities();
    }

    private ActivityResultLauncher<Intent> startForResultEdit;
    private ActivityResultLauncher<Intent> startForResultAdd;

    public void registerActivities(){
        startForResultEdit =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                applyEdit(result, "edit");
                            }
                            else if(result.getResultCode() == Activity.RESULT_FIRST_USER) {
                                applyEdit(result, "delete");
                            }
                        });
        startForResultAdd =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                applyEdit(result, "add");
                            }
                        });
    }

    private void applyEdit(ActivityResult result, String addEdit){
        Intent intent = result.getData();
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            return;
        }
        String photoTagName = bundle.getString(AddDeletePhotoTag.PHOTO_TAG_NAME);
        String photoTagValue = bundle.getString(AddDeletePhotoTag.PHOTO_TAG_VALUE);
        int index = bundle.getInt(AddDeletePhotoTag.PHOTO_TAG_INDEX);
        switch (addEdit) {
            case "edit":
                PhotoTag photoTag = photo.getTags().get(index);
                photoTag.setTagName(photoTagName);
                photoTag.setTagValue(photoTagValue);
                break;
            case "add":
                photoTagList.add(new PhotoTag(photoTagName, photoTagValue));
                break;
            case "delete":
                photoTagList.remove(index);
                break;
        }
        photoTagList = photo.getTags();
        photoTagListView.setAdapter(new ArrayAdapter<>(this, R.layout.photo_tag, photoTagList));
        try {
            User.writeData(user, this);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void showPhotoTag(int pos){
        Bundle bundle = new Bundle();
        PhotoTag photoTag = photoTagList.get(pos);
        bundle.putSerializable(AddDeletePhotoTag.USER, user);
        bundle.putInt(AddDeletePhotoTag.ALBUM_INDEX, albumIndex);
        bundle.putInt(AddDeletePhotoTag.PHOTO_INDEX, photoIndex);
        bundle.putInt(AddDeletePhotoTag.PHOTO_TAG_INDEX, pos);
        bundle.putString(AddDeletePhotoTag.PHOTO_TAG_NAME, photoTag.getTagName());
        bundle.putString(AddDeletePhotoTag.PHOTO_TAG_VALUE, photoTag.getTagValue());
        Intent intent = new Intent(this, AddDeletePhotoTag.class);
        intent.putExtras(bundle);
        startForResultEdit.launch(intent);
    }

    private void addPhotoTag(){
        Intent intent = new Intent(this, AddDeletePhotoTag.class);
        startForResultAdd.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            addPhotoTag();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}