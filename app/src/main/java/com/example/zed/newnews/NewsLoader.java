package com.example.zed.newnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Zed on 1/27/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<Headline>> {

    private String mUrl;

    public NewsLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Headline> loadInBackground() {
        if (mUrl == null) {
            return null;
        } else  {
            List<Headline> headlines = QueryUtils.fetchNews(mUrl);
            return  headlines;
        }
    }
}
