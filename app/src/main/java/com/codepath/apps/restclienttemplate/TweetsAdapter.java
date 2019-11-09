package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;
    TwitterClient client;

    public TweetsAdapter(Context context, List<Tweet> tweets, TwitterClient client){
        this.context = context;
        this.tweets = tweets;
        this.client = client;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweetsList){
        tweets.addAll(tweetsList);
        notifyDataSetChanged();
    }

    public TwitterClient getClient(){
        return client;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfilePicture;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTimeStamp;
        TextView tvSeparator;
        TextView tvUsername;
        TextView tvRetweetCount;
        TextView tvFavoriteCount;
        Button btnFavorite;
        Button btnRetweet;
        TimelineActivity timelineActivity;
//        TwitterClient client;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvUsername);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvSeparator = itemView.findViewById(R.id.tvSeparator);
            tvUsername = itemView.findViewById(R.id.tvScreenName);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            btnFavorite = itemView.findViewById(R.id.btnHeart);
        }

        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.userName);
            tvUsername.setText(context.getResources().getString(R.string.at_sign,tweet.user.screenName));
            Glide.with(context).load(tweet.user.publicImageUrl).into(ivProfilePicture);
            tvTimeStamp.setText(tweet.timeStamp);
            tvSeparator.setText("·");
            tvRetweetCount.setText(tweet.retweetCount);
            tvFavoriteCount.setText(tweet.favoriteCount);
            if (!tweet.isFavorited){
                btnFavorite.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vector_heart_stroke));
            }
            else{
                btnFavorite.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vector_heart));
            }
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TwitterClient client = getClient();
                    if (!tweet.isFavorited) {
                        Toast.makeText(context, String.valueOf(tweet.getFavoriteStatus()), Toast.LENGTH_SHORT).show();
                        client.publishFavorite(Long.valueOf(tweet.getID()), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Toast.makeText(context, tweet.getID(), Toast.LENGTH_LONG).show();
                                Log.i("TweetsAdapter", "onSuccess");
                                btnFavorite.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vector_heart));
//                                timelineActivity.refresh();
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                                Log.e("TweetsAdapter", "onFailure", throwable);
                            }
                        });
                    } else{
                        client.publishUnFavorite(Long.valueOf(tweet.getID()), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Toast.makeText(context, tweet.getID(), Toast.LENGTH_LONG).show();
                                Log.i("TweetsAdapter", "onSuccess");
                                btnFavorite.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vector_heart_stroke));
//                                timelineActivity.refresh();
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                                Log.e("TweetsAdapter", "onFailure", throwable);
                            }
                        });
                    }
                }
            });

            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, tweet.getID(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
