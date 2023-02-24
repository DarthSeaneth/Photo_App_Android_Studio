package android.group4;

import android.content.Context;
import android.group4.model.Photo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import java.util.List;

public class PhotoAdapter extends ArrayAdapter<Photo> {
    Context context;
    List<Photo> photos;
    int[] images;
    String[] photoNames;

    public PhotoAdapter(Context context, List<Photo> photos) {
        super(context, R.layout.single_item, R.id.nameTextView, photos);
        this.context = context;
        this.photos = photos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View singleItem = convertView;
        ProgramViewHolder holder = null;
        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.single_item , parent, false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        }
        else {
            holder = (ProgramViewHolder) singleItem.getTag();
        }
        holder.photoImage.setImageURI(Uri.parse(photos.get(position).getFileLocation()));
        holder.photoName.setText(photos.get(position).getName());
        return singleItem;
    }
}
