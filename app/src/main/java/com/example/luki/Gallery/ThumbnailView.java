package com.example.luki.Gallery;

import android.graphics.drawable.Drawable;
import android.net.Uri;
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
    private int ivId;
    private ThumbnailListener listener;
    private Uri uri;

    public ThumbnailView(ConstraintLayout root, int ivId) {
        super(root);
        this.root = root;
        this.ivId = ivId;
        thumbnailPicture = root.findViewById(ivId);
        thumbnailPicture.setOnClickListener(this);
    }//closes ThumnailView class

    public ConstraintLayout getRoot() {
        return root;
    }


    public ImageView getThumbnailPicture() {
        return thumbnailPicture;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) listener.onSelectImage(uri);
    }

    public void setListener(ThumbnailListener listener) {

        this.listener = listener;
    }

    public interface ThumbnailListener {

        void onSelectImage(Uri uri);

    }
}//closes Thumbnail Class
