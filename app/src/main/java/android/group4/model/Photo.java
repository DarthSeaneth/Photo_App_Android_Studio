/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package android.group4.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to store Photo data.
 */
public class Photo implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * Specific name of Photo
     */
    private String name;
    /**
     * Specific file location of Photo
     */
    private String fileLocation;

    /**
     * List of Photo Tags attached to Photo
     */
    private List<PhotoTag> photoTags;

    /**
     * No-arg constructor
     * Instantiates a new Photo.
     */
    public Photo(){
        name = "";
        fileLocation = "";
        photoTags = new ArrayList<PhotoTag>();
    }

    /**
     * 1-arg constructor
     * Instantiates a new Photo
     * @param fileLocation file location of Photo
     */
    public Photo(String fileLocation){
        this.name = fileLocation;
        this.fileLocation = fileLocation;
        photoTags = new ArrayList<PhotoTag>();
    }

    /**
     * Gets the name String of Photo
     * @return String name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the nam String of Photo
     * @param name String name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the file location of the Photo
     * @return String file location
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * Sets file location of Photo
     * @param fileLocation String file location
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Gets List of Photo Tags of Photo
     * @return Photo Tag list of Photo
     */
    public List<PhotoTag> getTags() {
        return photoTags;
    }

    /**
     * Gets the Photo Tag by the Tag Name and Value
     * @param tagName String Tag Name to check for
     * @param tagValue String Tag Value to check for
     * @return the corresponding Photo Tag (Empty Photo Tag if not found)
     */
    public PhotoTag getTag(String tagName, String tagValue){
        for(PhotoTag photoTag: photoTags){
            if(photoTag.getTagName().equalsIgnoreCase(tagName) && photoTag.getTagValue().equalsIgnoreCase(tagValue)){
                return photoTag;
            }
        }
        return new PhotoTag("", "");
    }

    /**
     * Adds Photo Tag to Photo
     * @param tagName String tag-name of Photo Tag
     * @param tagValue String tag-value of Photo Tag
     */
    public void addTag(String tagName, String tagValue){
        photoTags.add(new PhotoTag(tagName, tagValue));

    }

    /**
     * Determines if the Photo has the same photo file as another Photo
     * @param photo Photo to compare to
     * @return boolean value representing the equality of the file locations
     */
    public boolean duplicateFile(Photo photo){
        return this.getFileLocation().compareTo(photo.getFileLocation()) == 0;
    }

    /**
     * Overridden equals method for Photo Class
     * @param o Object to check equality against
     * @return boolean value representing the equality of the two objects
     */
    public boolean equals(Object o){
        if(!(o instanceof Photo)){
            return false;
        }
        Photo photo = (Photo) o;
        return this.getName().compareTo(photo.getFileLocation()) == 0 && this.duplicateFile(photo);
    }

    /**
     * Overridden toString method for Photo Class
     * @return String representation of a Photo
     */
    public String toString(){
        return name;
    }
}
