package com.example.luki.Gallery;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luki.R;
import com.example.luki.model.Thumbnail;

import java.util.ArrayList;

public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailView> implements ThumbnailView.ThumbnailListener {

    private ArrayList<Thumbnail> thumbnails;
    private ThumbnailHandler observer;
    private int ViewHolderId;
    private int ImageViewId;

    public ThumbnailsAdapter(int ViewHolerId, int ImageViewId) {

        thumbnails = new ArrayList<Thumbnail>();
        this.ViewHolderId = ViewHolerId;
        this.ImageViewId = ImageViewId;

    }//closes thumbnailsAdapter Constructor



    public void AddThumbnail(Thumbnail thumbnail) {

        thumbnails.add(thumbnail);
        this.notifyDataSetChanged();
    }//closes AddThumbnail method

    public ArrayList<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    @NonNull
    @Override
    public ThumbnailView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View thumbnailXML = inflater.inflate(ViewHolderId, null);
        ConstraintLayout thumbnailRoot = (ConstraintLayout) thumbnailXML;
        ThumbnailView thumbnailView = new ThumbnailView(thumbnailRoot, ImageViewId);
        return thumbnailView;
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailView holder, int position) {

        Glide.with(holder.getThumbnailPicture().getContext()).load(thumbnails.get(position).getThumbnailUri()).into(holder.getThumbnailPicture());
        holder.setUri(thumbnails.get(position).getThumbnailUri());
        holder.setListener(this);
    }

    @Override
    public int getItemCount() {
        return thumbnails.size();
    }

    public void setObserver(ThumbnailHandler observer) {
        this.observer = observer;
    }

    @Override
    public void onSelectImage(Uri thumbnail) {
        observer.SetMainImage(thumbnail);
    }

    public interface ThumbnailHandler {
        void SetMainImage(Uri uri);
    }
}//closes ThumnailsAdapter class
