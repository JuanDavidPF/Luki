package com.example.luki.Gallery;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luki.R;
import com.example.luki.model.Thumbnail;

public class ThumbnailView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ConstraintLayout root;
    private ImageView thumbnailPicture;
    private ThumbnailListener listener;

    public ThumbnailView(ConstraintLayout root) {
        super(root);
        this.root = root;
        thumbnailPicture = root.findViewById(R.id.thumbnailView_thumbnail);
        thumbnailPicture.setOnClickListener(this);
    }//closes ThumnailView class

    public ConstraintLayout getRoot() {
        return root;
    }


    public ImageView getThumbnailPicture() {
        return thumbnailPicture;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) listener.onSelectImage(thumbnailPicture.getDrawable());
    }

    public void setListener(ThumbnailListener listener) {

        this.listener = listener;
    }

    public interface ThumbnailListener {

        void onSelectImage(Drawable thumbnail);

    }
}//closes Thumbnail Class
