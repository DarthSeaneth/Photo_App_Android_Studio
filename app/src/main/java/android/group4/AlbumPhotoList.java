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
import android.group4.model.User;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

public class AlbumPhotoList extends AppCompatActivity {

    private int albumIndex;
    private ListView photoListView;
    private List<Photo> photoList;
    private Album album;
    private User user;
    private int programImages[]; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_photo_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Album Photos");
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
        if(bundle != null) {
            albumIndex = bundle.getInt(AddEditDeleteOpenAlbum.ALBUM_INDEX);
        }
        album = user.getAlbumList().get(albumIndex);
        photoList = album.getAlbumPhotos();
        photoListView = findViewById(R.id.photo_list);
        photoListView.setAdapter(new PhotoAdapter(this, photoList));
        photoListView.setOnItemClickListener((list,view,pos,id) -> showPhoto(pos));
        registerActivities();
    }

    private void addPhoto(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startForResultAdd.launch(intent);
    }

    private ActivityResultLauncher<Intent> startForResultAdd;
    private ActivityResultLauncher<Intent> startForResultEdit;

    public void registerActivities(){
        startForResultAdd =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if(result.getResultCode() == Activity.RESULT_OK && result != null){
                               applyAdd(result.getData().getData());
                            }
                        });
        startForResultEdit =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if(result.getResultCode() == Activity.RESULT_OK){
                                applyEdit(result, "delete");
                            }
                            else if(result.getResultCode() == Activity.RESULT_FIRST_USER){
                                applyEdit(result, "move");
                            }
                        });
    }

    private void applyAdd(Uri uri){
        String filePath = uri.toString();
        Photo newPhoto = new Photo(filePath);
        album.addPhoto(newPhoto);
        photoList = album.getAlbumPhotos();
        photoListView.setAdapter(new PhotoAdapter(this, photoList));
        try{
            User.writeData(user, this);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void applyEdit(ActivityResult result, String deleteMove){
        Intent intent = result.getData();
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            return;
        }
        int index = bundle.getInt(DeleteOpenMovePhoto.PHOTO_INDEX);
        if(deleteMove.equals("move")){
            Photo photo = photoList.get(index);
            String albumTitle = bundle.getString(DeleteOpenMovePhoto.ALBUM_TITLE);
            for(Album a: user.getAlbumList()){
                if(a.getAlbumTitle().equals(albumTitle)){
                    a.addPhoto(photo);
                    break;
                }
            }
        }
        photoList.remove(index);
        photoListView.setAdapter(new PhotoAdapter(this, photoList));
        try{
            User.writeData(user, this);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void showPhoto(int pos){
        Bundle bundle = new Bundle();
        bundle.putInt(DeleteOpenMovePhoto.ALBUM_INDEX, albumIndex);
        bundle.putInt(DeleteOpenMovePhoto.PHOTO_INDEX, pos);
        bundle.putSerializable(DeleteOpenMovePhoto.USER, user);
        Intent intent = new Intent(this, DeleteOpenMovePhoto.class);
        intent.putExtras(bundle);
        startForResultEdit.launch(intent);
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
            addPhoto();
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