/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package android.group4.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to store Album data.
 */
public class Album implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * String title of Album
     */
    private String albumTitle;
    /**
     * Array list of Photos owned by Album
     */
    private List<Photo> albumPhotos;
    /**
     * Number of photos in Album
     */
    private int numPhotos;
    /**
     * List to store pre-set and user-defined Photo Tag Names
     */
    private List<String> photoTagNameList;

    /**
     * No-arg constructor
     * Instantiates a new Album.
     */
    public Album(){
        albumTitle = "";
        albumPhotos = new ArrayList<Photo>();
        numPhotos = 0;
        photoTagNameList = new ArrayList<String>();
        photoTagNameList.add("Name");
        photoTagNameList.add("Location");
    }

    /**
     * 1-arg constructor
     * Instantiates a new Album
     * @param albumTitle String to assign to albumTitle field
     */
    public Album(String albumTitle){
        this.albumTitle = albumTitle;
        albumPhotos = new ArrayList<Photo>();
        numPhotos = 0;
        photoTagNameList = new ArrayList<String>();
        photoTagNameList.add("Name");
        photoTagNameList.add("Location");
    }

    /**
     * Gets Album title string.
     * @return the string title of Album
     */
    public String getAlbumTitle(){
        return albumTitle;
    }

    /**
     * Gets Album photo array list.
     * @return the array list of photos in Album
     */
    public List<Photo> getAlbumPhotos(){
        return albumPhotos;
    }

    /**
     * Gets number of photos in Album.
     * @return the number of photos
     */
    public int getNumPhotos(){
        return numPhotos;
    }

    /**
     * Gets the List of pre-set and user-defined Photo Tag Names
     * @return list of Photo Tag Names
     */
    public List<String> getPhotoTagNameList(){
        return photoTagNameList;
    }

    /**
     * Adds a user-defined Photo Tag Name to the list
     * @param photoTagName String Photo Tag Name to add
     */
    public void addPhotoTagNameToList(String photoTagName){
        photoTagNameList.add(photoTagName);
    }

    /**
     * Sets Album title.
     * @param albumTitle the Album title
     */
    public void setAlbumTitle(String albumTitle){
        this.albumTitle = albumTitle;
    }

    /**
     * Adds photo to Album photos array list.
     * @param photo the photo to add
     */
    public void addPhoto(Photo photo){
        albumPhotos.add(photo);
        numPhotos ++;
    }

    /**
     * Removes photo from Album photos array list.
     * @param photo the photo to remove
     */
    public void removePhoto(Photo photo){
//        albumPhotos.remove(photo);
        int targetIndex = -1;
        for(int i = 0; i < albumPhotos.size(); i++) {
            if (albumPhotos.get(i) == photo) {
                targetIndex = i;
            }
        }
        albumPhotos.remove(targetIndex);
        numPhotos --;
    }

    /**
     * Overridden toString method for Album Class
     * @return String representation of Album
     */
    public String toString(){
        return "Album Title: " + getAlbumTitle();
    }
}
