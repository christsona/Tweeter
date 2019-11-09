package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public long id;
    public String timeStamp;
    public String retweetCount;
    public String favoriteCount;
    public boolean isFavorited;

    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");
        tweet.timeStamp = getFormattedTimestamp(jsonObject.getString("created_at"));
        tweet.retweetCount = jsonObject.getString("retweet_count");
        tweet.favoriteCount = jsonObject.getString("favorite_count");
        tweet.isFavorited = jsonObject.getBoolean("favorited");
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i<jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public static String getFormattedTimestamp(String timeCreated){
        timeCreated = TimeFormatter.getTimeDifference(timeCreated);
        return timeCreated;
    }

    public String getID(){
        return String.valueOf(id);
    }

    public boolean getFavoriteStatus(){
        return isFavorited;
    }

}
