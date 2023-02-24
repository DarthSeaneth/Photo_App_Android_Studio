package android.group4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.group4.model.Album;
import android.group4.model.User;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

public class Photos extends AppCompatActivity {

    private ListView albumListView;
    private List<Album> albumList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Photo Albums");
        setSupportActionBar(myToolbar);
        //load user data from user.dat if possible
        user = null;
        try{
            User.readData(this);
            user = User.getInstance();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if(user == null){
            user = User.getInstance();
        }
        albumList = user.getAlbumList();
        //populate album list view with user's album list
        albumListView = findViewById(R.id.album_list);
        ArrayAdapter<Album> adapter = new ArrayAdapter<>(this, R.layout.album, albumList);
        albumListView.setAdapter(adapter);

        albumListView.setOnItemClickListener((list,view,pos,id) -> showAlbum(pos));
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

    private void applyEdit(ActivityResult result, String addEditDelete){
        Intent intent = result.getData();
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            return;
        }
        String title = bundle.getString(AddEditDeleteOpenAlbum.ALBUM_TITLE);
        int index = bundle.getInt(AddEditDeleteOpenAlbum.ALBUM_INDEX);
        switch (addEditDelete) {
            case "edit":
                Album album = albumList.get(index);
                album.setAlbumTitle(title);
                break;
            case "add":
                albumList.add(new Album(title));
                break;
            case "delete":
                albumList.remove(index);
                break;
        }
        albumListView.setAdapter(new ArrayAdapter<>(this, R.layout.album, albumList));
        try {
            User.writeData(user, this);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void showAlbum(int pos){
        Bundle bundle = new Bundle();
        Album selectedAlbum = albumList.get(pos);
        bundle.putInt(AddEditDeleteOpenAlbum.ALBUM_INDEX, pos);
        bundle.putString(AddEditDeleteOpenAlbum.ALBUM_TITLE, selectedAlbum.getAlbumTitle());
        bundle.putSerializable(AddEditDeleteOpenAlbum.USER, user);
        Intent intent = new Intent(this, AddEditDeleteOpenAlbum.class);
        intent.putExtras(bundle);
        startForResultEdit.launch(intent);
    }

    private void addAlbum(){
        Intent intent = new Intent(this, AddEditDeleteOpenAlbum.class);
        startForResultAdd.launch(intent);
    }

    private void searchForPhotos(){
        Intent intent = new Intent(this, PhotoSearch.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            addAlbum();
            return true;
        }
        else if(item.getItemId() == R.id.action_search){
            searchForPhotos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}