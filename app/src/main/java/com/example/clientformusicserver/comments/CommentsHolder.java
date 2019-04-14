package com.example.clientformusicserver.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.clientformusicserver.R;
import com.example.clientformusicserver.model.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class CommentsHolder extends RecyclerView.ViewHolder {

    private TextView mName, mComment, mDate;


    CommentsHolder(View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.tv_name);
        mComment = itemView.findViewById(R.id.tv_comment);
        mDate = itemView.findViewById(R.id.tv_date);
    }

    void bind(Comment item) {
        mName.setText(item.getAuthor());
        mComment.setText(item.getText());
        mDate.setText(formatTimestamp(item.getTimestamp()));

    }

    private String formatTimestamp(String timestamp) {

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formatCurrentDate = dateFormat.format(currentDate);

        if (timestamp.startsWith(formatCurrentDate)) {
            return timestamp.substring(11);
        } else {
            return timestamp.substring(0, 9);
        }
    }
}
