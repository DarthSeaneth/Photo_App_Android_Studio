package android.group4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.group4.model.Album;
import android.group4.model.Photo;
import android.group4.model.PhotoTag;
import android.group4.model.User;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PhotoSearch extends AppCompatActivity {

    private User user;
    private List<Album> albumList;
    private List<Photo> photoList;
    private List<Photo> resultPhotoList;
    private ListView photoListView;
    private EditText singleTagName;
    private AutoCompleteTextView singleTagValueAC;
    private EditText conjunctiveTagName1;
    private AutoCompleteTextView conjunctiveTagValueAC1;
    private EditText conjunctiveTagName2;
    private AutoCompleteTextView conjunctiveTagValueAC2;
    private EditText disjunctiveTagName1;
    private AutoCompleteTextView disjunctiveTagValueAC1;
    private EditText disjunctiveTagName2;
    private AutoCompleteTextView disjunctiveTagValueAC2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_search);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Search For Photos");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try{
            User.readData(this);
            user = User.getInstance();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        albumList = user.getAlbumList();
        photoList = new ArrayList<>();
        resultPhotoList = new ArrayList<>();
        for(Album album: albumList){
            photoList.addAll(album.getAlbumPhotos());
        }
        resultPhotoList = photoList;
        photoListView = findViewById(R.id.photo_list_view);
        singleTagName = findViewById(R.id.singleTagType);
//        singleTagValueAC = findViewById(R.id.singleTagValueAC);
        singleTagValueAC = (AutoCompleteTextView) findViewById(R.id.singleTagValueAC);
        conjunctiveTagName1 = findViewById(R.id.conjunctiveTagType1);
        conjunctiveTagValueAC1 = (AutoCompleteTextView) findViewById(R.id.conjunctiveTagValueAC1);

        conjunctiveTagName2 = findViewById(R.id.conjunctiveTagType2);
        conjunctiveTagValueAC2 = (AutoCompleteTextView) findViewById(R.id.conjunctiveTagValueAC2);

        disjunctiveTagName1 = findViewById(R.id.disjunctiveTagType1);
        disjunctiveTagValueAC1 = (AutoCompleteTextView) findViewById(R.id.disjunctiveTagValueAC1);

        disjunctiveTagName2 = findViewById(R.id.disjunctiveTagType2);
        disjunctiveTagValueAC2 = (AutoCompleteTextView) findViewById(R.id.disjunctiveTagValueAC2);

        photoListView.setAdapter(new PhotoAdapter(this, photoList));
        photoListView.setOnItemClickListener((list,view,pos,id) -> openPhoto(pos));

        autoCompleteTag(singleTagName, singleTagValueAC);
        autoCompleteTag(conjunctiveTagName1, conjunctiveTagValueAC1);
        autoCompleteTag(conjunctiveTagName2, conjunctiveTagValueAC2);
        autoCompleteTag(disjunctiveTagName1, disjunctiveTagValueAC1);
        autoCompleteTag(disjunctiveTagName2, disjunctiveTagValueAC2);
    }

    public void singleSearch(View view){
        String tagName = singleTagName.getText().toString();
        String tagValue = singleTagValueAC.getText().toString();
        if(tagName == null || tagName.length() == 0 || tagValue == null || tagValue.length() == 0){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter a Photo Tag Type and Value!");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        if(!tagName.equalsIgnoreCase("location") && !tagName.equalsIgnoreCase("person")){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter a valid Photo Tag Type! (Location or Person)");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        PhotoTag targetPhotoTag = new PhotoTag(tagName, tagValue);
        Predicate<Photo> tagPredicate = photo -> photo.getTag(tagName, tagValue).equals(targetPhotoTag);
        resultPhotoList = filter(photoList, tagPredicate);
        photoListView.setAdapter(new PhotoAdapter(this, resultPhotoList));
        singleTagName.setText("");
        singleTagValueAC.setText("");
    }

    public void conjunctiveSearch(View view){
        String tagName1 = conjunctiveTagName1.getText().toString();
        String tagValue1 = conjunctiveTagValueAC1.getText().toString();
        String tagName2 = conjunctiveTagName2.getText().toString();
        String tagValue2 = conjunctiveTagValueAC2.getText().toString();
        if(tagName1 == null || tagName1.length() == 0 || tagValue1 == null || tagValue1.length() == 0 || tagName2 == null || tagName2.length() == 0 || tagValue2 == null || tagValue2.length() == 0){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter Photo Tag Types and Values!");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        if((!tagName1.equalsIgnoreCase("location") && !tagName1.equalsIgnoreCase("person")) || (!tagName2.equalsIgnoreCase("location") && !tagName2.equalsIgnoreCase("person"))){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter valid Photo Tag Types! (Location or Person)");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        PhotoTag photoTag1 = new PhotoTag(tagName1, tagValue1);
        PhotoTag photoTag2 = new PhotoTag(tagName2, tagValue2);
        Predicate<Photo> tagPredicate1 = photo -> photo.getTag(tagName1, tagValue1).equals(photoTag1);
        Predicate<Photo> tagPredicate2 = photo -> photo.getTag(tagName2, tagValue2).equals(photoTag2);
        resultPhotoList = filter(photoList, tagPredicate1.and(tagPredicate2));
        photoListView.setAdapter(new PhotoAdapter(this, resultPhotoList));
        conjunctiveTagName1.setText("");
        conjunctiveTagValueAC1.setText("");
        conjunctiveTagName2.setText("");
        conjunctiveTagValueAC2.setText("");
    }

    public void disjunctiveSearch(View view){
        String tagName1 = disjunctiveTagName1.getText().toString();
        String tagValue1 = disjunctiveTagValueAC1.getText().toString();
        String tagName2 = disjunctiveTagName2.getText().toString();
        String tagValue2 = disjunctiveTagValueAC2.getText().toString();
        if(tagName1 == null || tagName1.length() == 0 || tagValue1 == null || tagValue1.length() == 0 || tagName2 == null || tagName2.length() == 0 || tagValue2 == null || tagValue2.length() == 0){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter Photo Tag Types and Values!");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        if((!tagName1.equalsIgnoreCase("location") && !tagName1.equalsIgnoreCase("person")) || (!tagName2.equalsIgnoreCase("location") && !tagName2.equalsIgnoreCase("person"))){
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY, "Please enter valid Photo Tag Types! (Location or Person)");
            DialogFragment dialogFragment = new AlbumDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "badfield");
            return;
        }
        PhotoTag photoTag1 = new PhotoTag(tagName1, tagValue1);
        PhotoTag photoTag2 = new PhotoTag(tagName2, tagValue2);
        Predicate<Photo> tagPredicate1 = photo -> photo.getTag(tagName1, tagValue1).equals(photoTag1);
        Predicate<Photo> tagPredicate2 = photo -> photo.getTag(tagName2, tagValue2).equals(photoTag2);
        resultPhotoList = filter(photoList, tagPredicate1.or(tagPredicate2));
        photoListView.setAdapter(new PhotoAdapter(this, resultPhotoList));
        disjunctiveTagName1.setText("");
        disjunctiveTagValueAC1.setText("");
        disjunctiveTagName2.setText("");
        disjunctiveTagValueAC2.setText("");
    }

    private void openPhoto(int pos){
        Photo photo = resultPhotoList.get(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PhotoSearchPhotoDisplay.PHOTO, photo);
        Intent intent = new Intent(this, PhotoSearchPhotoDisplay.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean hasTag(Photo photo, String photoTag){
        boolean result = photo.getTags().stream().filter(t-> t.getTagName().equalsIgnoreCase(photoTag)).count() > 0;
        return result;
    }

    private void autoCompleteTag(EditText et, AutoCompleteTextView actv) { //List<Photo> ; Photo ; PhotoTags ; tagName
        Context context = this;
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @SuppressLint("ResourceType")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String watch = et.getText().toString();
                    List<PhotoTag> ptMatches = user.getGlobalTagList().get(et.getText().toString());
                    List<String> matches = new LinkedList<>();
//                    Predicate<Photo> tagPredicate = photo -> photo.getTag(tagName, tagValue).equals(targetPhotoTag);
                    for (PhotoTag pt : ptMatches) {
                        if (matches.contains(pt.getTagValue().toLowerCase(Locale.ROOT))) {
                            continue;
                        }
                        matches.add(pt.getTagValue().toLowerCase(Locale.ROOT));
                    }
                    String[] pop = matches.toArray(new String[0]);
                    int currRes = actv.getId();

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, pop);
//                    actv.setOnDismissListener( () -> {assert adapter != null;});
                    adapter.setDropDownViewResource(actv.getId());
                    actv.setAdapter(adapter);
                    actv.setThreshold(3);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
        });
    }

    private static<Photo> List<Photo> filter(List<Photo> photoList, Predicate<Photo> p){
        List<Photo> result = new ArrayList<Photo>();
        for(Photo photo: photoList){
            if(p.test(photo)){
                result.add(photo);
            }
        }
        return result;
    }
}