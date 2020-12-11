package com.example.luki.model;

import android.net.Uri;

public class Thumbnail {

    private Uri thumbnailUri;

    public Thumbnail(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public Thumbnail() {
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }
}//closes Thumnail class