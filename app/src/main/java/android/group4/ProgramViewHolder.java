package android.group4;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramViewHolder {
    ImageView photoImage;
    TextView photoName;
    ProgramViewHolder(View v) {
        photoImage = v.findViewById(R.id.singleImageView);
        photoName = v.findViewById(R.id.nameTextView);
    }
}
