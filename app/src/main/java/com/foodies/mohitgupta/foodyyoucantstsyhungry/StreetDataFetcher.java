package com.foodies.mohitgupta.foodyyoucantstsyhungry;

/**
 * Created by Mohit Gupta on 12-04-2018.
 */

public class StreetDataFetcher {

     private String UserName;
     private String Long_Desc;
     private String Short_Desc;
     private String DateTime;
     private String VideoName;
     private String VideoPath;
     private String Video_Thumb;
     private String ProfileImage;


    public StreetDataFetcher( String userName, String long_Desc, String short_Desc, String dateTime, String videoName, String videoPath, String profileImage, int likes, long views,String Video_thumb ) {
        UserName = userName;
        Long_Desc = long_Desc;
        Short_Desc = short_Desc;
        DateTime = dateTime;
        VideoName = videoName;
        VideoPath = videoPath;
        ProfileImage = profileImage;
        Video_Thumb=Video_thumb;


    }


    public StreetDataFetcher() {

    }

    public String getVideo_Thumb() {
        return Video_Thumb;
    }

    public void setVideo_Thumb( String video_Thumb ) {
        Video_Thumb = video_Thumb;
    }

    public void setProfileImage( String profileImage ) {
        ProfileImage = profileImage;
    }

    public StreetDataFetcher( String profileImage ) {
        ProfileImage = profileImage;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName( String userName ) {
        UserName = userName;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName( String videoName ) {
        VideoName = videoName;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath( String videoPath ) {
        VideoPath = videoPath;
    }

    public String getLong_Desc() {
        return Long_Desc;
    }

    public void setLong_Desc( String long_Desc ) {
        Long_Desc = long_Desc;
    }

    public String getShort_Desc() {
        return Short_Desc;
    }

    public void setShort_Desc( String short_Desc ) {
        Short_Desc = short_Desc;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime( String DateTime ) {
        DateTime = DateTime;
    }


}
