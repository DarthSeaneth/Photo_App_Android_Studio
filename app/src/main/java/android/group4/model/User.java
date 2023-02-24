/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package android.group4.model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to store User data.
 */
public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * Array list of Albums owned by User
     */
    private List<Album> albumList;

    /**
     * Reference for all created photoTags
     */
    private HashMap<String, List<PhotoTag>> globalTagList;

    private static User userInstance = null;

    /**
     * No-arg constructor
     * Instantiates a new User.
     */
    private User(){
        albumList = new ArrayList<Album>();
        globalTagList = new HashMap<String, List<PhotoTag>>();
        globalTagList.put("location", new LinkedList<PhotoTag>());
        globalTagList.put("person", new LinkedList<PhotoTag>());
    }

    public static User getInstance(){
        if(userInstance == null){
            userInstance = new User();
        }
        return userInstance;
    }

    /**
     * Gets Album array list.
     * @return the Album array list
     */
    public List<Album> getAlbumList() {
        return albumList;
    }

    /**
     * Adds Album to User Album array list.
     * @param album the album to add
     */
    public void addAlbum(Album album){
        albumList.add(album);
    }

    /**
     * Removes Album from User Album array list.
     * @param album the Album to remove
     */
    public void removeAlbum(Album album){
        albumList.remove(album);
    }

    public static void writeData(User user, Context context) throws IOException{
        FileOutputStream fos = context.openFileOutput("user.dat", Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(user);
        oos.close();
    }

    public static void readData(Context context) throws IOException, ClassNotFoundException{
        FileInputStream fis = context.openFileInput("user.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        User user = (User) ois.readObject();
        ois.close();
        userInstance = user;
    }

    public HashMap<String, List<PhotoTag>> getGlobalTagList() {
//        if (globalTagList == null) {
//            globalTagList = new HashMap<String, List<PhotoTag>>();
//        }
        return globalTagList;
    }

    public void addGlobalTag(PhotoTag pt) {
        globalTagList.get(pt.getTagName()).add(pt);
//        if (globalTagList.containsKey(pt.getTagName())) {
//        }
//        else {
//            globalTagList.put(pt.getTagName(), new LinkedList<PhotoTag>());
//        }
    }
    public void removeGlobalTag(PhotoTag pt) {
        if (globalTagList.containsKey(pt.getTagName())) {
            globalTagList.get(pt.getTagName()).remove(pt);
        }
    }
}
