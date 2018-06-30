package eg.edu.cu.fci.ecampus.fci_e_campus.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Comment;

/**
 * Created by ahmed on 6/23/2018.
 */

public class CommentsAdapter extends ArrayAdapter<Comment> {
    public CommentsAdapter(Activity context, ArrayList<Comment> comments) {
        super(context, 0, comments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_item, parent, false);
        }
        Comment currentComment = getItem(position);

        TextView commentUsername = listItemView.findViewById(R.id.comment_username);
        commentUsername.setText(currentComment.getAuthorUsername());

        TextView commentText = listItemView.findViewById(R.id.comment_text_view);
        commentText.setText(currentComment.getText());

        TextView commentDate = listItemView.findViewById(R.id.comment_date);
        commentDate.setText(currentComment.getConvertedDate());

        return listItemView;
    }
}
