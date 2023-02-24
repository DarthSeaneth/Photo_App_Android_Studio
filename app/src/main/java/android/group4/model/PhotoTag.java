/**
 @author Sean Patrick
 @author Shaheer Syed
 */
package android.group4.model;

import java.io.Serializable;

/**
 * Class to store Photo Tag data.
 */
public class PhotoTag implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * String to store tag-name
     */
    private String tagName;
    /**
     * String to store tag-value
     */
    private String tagValue;

    /**
     * 2-arg constructor
     * Instantiates a new Photo tag.
     * @param tagName the tag-name String
     * @param tagValue the tag-value String
     */
    public PhotoTag(String tagName, String tagValue){
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    /**
     * Gets tag-name String.
     * @return the tag-name String
     */
    public String getTagName(){
        return tagName;
    }

    /**
     * Gets tag-value String.
     * @return the tag-value String
     */
    public String getTagValue(){
        return tagValue;
    }

    public void setTagName(String tagName){
        this.tagName = tagName;
    }

    public void setTagValue(String tagValue){
        this.tagValue = tagValue;
    }

    /**
     * Overridden toString method for Photo Tag Class
     * @return String representation of a Photo Tag
     */
    public String toString(){
        return tagName + " : " + tagValue;
    }

    /**
     * Overridden equals method for Photo Tag Class
     * @param o Object to check equality against
     * @return boolean value representing the equality of the two objects
     */
    public boolean equals(Object o){
        if(!(o instanceof PhotoTag)){
            return false;
        }
        PhotoTag photoTag = (PhotoTag) o;
        return this.tagName.equalsIgnoreCase(photoTag.tagName) && this.tagValue.equalsIgnoreCase(photoTag.tagValue);
    }
}
