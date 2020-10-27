package com.example.luki.Gallery;

import android.graphics.drawable.Drawable;
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

    public ThumbnailsAdapter() {

        thumbnails = new ArrayList<Thumbnail>();

    }//closes thumbnailsAdapter Constructor

    public void AddThumbnail(Thumbnail thumbnail) {

        thumbnails.add(thumbnail);
        this.notifyDataSetChanged();
    }//closes AddThumbnail method


    @NonNull
    @Override
    public ThumbnailView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View thumbnailXML = inflater.inflate(R.layout.thumnail, null);
        ConstraintLayout thumbnailRoot = (ConstraintLayout) thumbnailXML;
        ThumbnailView thumbnailView = new ThumbnailView(thumbnailRoot);
        return thumbnailView;
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailView holder, int position) {

        Glide.with(holder.getThumbnailPicture().getContext()).load(thumbnails.get(position).getThumbnailUri()).fitCenter().into(holder.getThumbnailPicture());
        holder.getThumbnailPicture().setClipToOutline(true);
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
    public void onSelectImage(Drawable thumbnail) {
        observer.SetMainImage(thumbnail);
    }

    public interface ThumbnailHandler {
        void SetMainImage(Drawable drawable);
    }
}//closes ThumnailsAdapter class
