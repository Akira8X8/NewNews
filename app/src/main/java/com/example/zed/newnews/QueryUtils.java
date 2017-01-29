package com.example.zed.newnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 1/28/2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){

    }

    public static List<Headline> fetchNews(String requestURL) {
        Log.d(QueryUtils.class.getSimpleName(), requestURL);

        URL url = createUrl(requestURL);

        String JSONResponse = null;
        try {
            JSONResponse = makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making HTTP request.", e);
        }

        List<Headline> headlines = extractNews(JSONResponse);
        return headlines;
    }

    private static URL createUrl(String urlString){
        URL url = null;
        try {
            url = new URL(urlString);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving News API JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();

            }
        }
        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<Headline> extractNews(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList for adding headlines
        List<Headline> headlines = new ArrayList<>();

        // Catch the exception to avoid crash; error message to logs.
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONObject from "response",
            JSONObject response = baseJsonResponse.getJSONObject("response");
            //extract books from "results,
            JSONArray results = response.getJSONArray("results");


            // For each article in the response, create an {@link Book} object
            for (int i = 0; i < results.length(); i++) {

                // Get a single headline at position i within the list of headlines
                JSONObject currentNews = results.getJSONObject(i);


                // Extract the value for the key called "title"
                String title = currentNews.getString("webTitle");

                String section = currentNews.getString("sectionName");

                String murl = currentNews.getString("webUrl");



                Headline headline = new Headline(title, section, murl);

                headlines.add(headline);
            }

        }

        catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing JSON results", e);
        }

        // Return the list of headlines
        return headlines;
    }


}
