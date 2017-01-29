package com.example.zed.newnews;

import android.net.Uri;

/**
 * Created by Zed on 1/26/2017.
 */

public class Headline {

    public final String title;
    public final String section;
    public final String url;

    public Headline(String mtitle, String msection, String murl){
        title = mtitle;
        section = msection;
        url = murl;
    }

    public String getTitle() {return title;}
    public String getSection() {return section;}
    public String getUrl() {return url;}

}
