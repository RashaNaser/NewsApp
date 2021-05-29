package com.rns.newsapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rns.newsapplication.R;
import com.rns.newsapplication.models.Result;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link Result} objects).
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<Result> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param results is the list of results, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<Result> results) {
        super(context, 0, results);
    }

    public static class ViewHolder {
        private ImageView customImage;
        private TextView customDate;
        private TextView customTitle;
        private TextView customSectionName;
        private TextView customAuthorName;
    }

    /**
     * Returns a list item view that displays information about the news at the given position
     * in the list of results.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_layout, parent, false);
        }
        holder = new ViewHolder();

        // Find the news at the given position in the list of results
        Result result = getItem(position);

        holder.customDate = listItemView.findViewById(R.id.customDate);
        holder.customDate.setText(formatDate(result.getWebPublicationDate()));

        holder.customTitle = listItemView.findViewById(R.id.customTitle);
        holder.customTitle.setText(result.getWebTitle());

        holder.customSectionName = listItemView.findViewById(R.id.customSectionName);
        holder.customSectionName.setText(result.getSectionName());

        holder.customAuthorName = listItemView.findViewById(R.id.customAuthor);
        holder.customAuthorName.setText(result.getTags().get(0).getWebTitle());

        holder.customImage = listItemView.findViewById(R.id.customImage);
        Picasso.get().load(result.getFields().getThumbnail())
                .placeholder(R.mipmap.ic_launcher_foreground)
                .into(holder.customImage);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Convert date and time in UTC (webPublicationDate) into a more readable representation
     * in Local time
     *
     * @param dateStringUTC is the web publication date of the article (i.e. 2021-03-29T11:00:08Z)
     * @return the formatted date string in Local time(i.e "Mar 29, 2021  2:00 PM")
     * from a date and time in UTC
     */
    private String formatDate(String dateStringUTC) {
        // Parse the dateString into a Date object
        @SuppressLint("SimpleDateFormat") //NON-NLS
                SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'"); //NON-NLS

        Date dateObject = null;
        try {
            dateObject = simpleDateFormat.parse(dateStringUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*
        Initialize a SimpleDateFormat instance and configure it to provide a more readable
        representation according to the given format, but still in UTC
        */
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.ENGLISH); //NON-NLS
        String formattedDateUTC = df.format(dateObject);
        // Convert UTC into Local time
        df.setTimeZone(TimeZone.getTimeZone("UTC")); //NON-NLS
        Date date = null;
        try {
            date = df.parse(formattedDateUTC);
            df.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }
}