package com.example.virginia.mybakingapp;

public class RecipeStep {
    private String id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public RecipeStep(String stepId,String stepShortDesc,String stepDesc,String stepVidURL,String stepThumbnail){
        id =stepId;
        shortDescription =stepShortDesc;
        description =stepDesc;
        videoURL =stepVidURL;
        thumbnailURL =stepThumbnail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getVideoURL() {
        return videoURL;
    }
}
