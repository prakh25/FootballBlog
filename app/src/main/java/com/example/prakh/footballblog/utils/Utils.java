package com.example.prakh.footballblog.utils;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;

import com.example.prakh.footballblog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by prakh on 16-11-2017.
 */

public class Utils {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static int selectCategoryBackground(String category) {

        if (category != null && !category.isEmpty()) {
            switch (category) {
                case "La Liga":
                    return R.drawable.category_la_liga_background;
                case "Premier League":
                    return R.drawable.category_premier_league_background;
                case "Bundesliga":
                    return R.drawable.category_bundesliga_background;
                case "International":
                    return R.drawable.category_background_international;
                default:
                    return R.drawable.category_background;
            }
        }
        return R.drawable.category_background;
    }

    public static int selectCategoryLogo(String category) {
        if (category != null && !category.isEmpty()) {
            switch (category) {
                case "La Liga":
                    return R.drawable.logo_la_liga;
                case "Premier League":
                    return R.drawable.logo_premier_league_4;
                case "Bundesliga":
                    return R.drawable.logo_bundesliga_2;
                case "International":
                    return R.drawable.logo_international;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public static String getFormattedDateSimple(String date_str) {
        if (date_str != null && !date_str.trim().equals("")) {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            try {
                return newFormat.format(oldFormat.parse(date_str));
            } catch (ParseException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static Intent sharingIntent(Spanned title, String appName, String url) {

        String sb = "Read Article \'" + title + "\'\n" +
                "Using app \'" + appName + "\'\n" +
                "Source : " + url + "";

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sb);

        return sharingIntent;
    }
}
