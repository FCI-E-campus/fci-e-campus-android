package eg.edu.cu.fci.ecampus.fci_e_campus.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Forum;
import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;

/**
 * Created by ahmed on 6/23/2018.
 */

public class ForumAdapter extends ArrayAdapter<Forum> {

    public ForumAdapter(Activity context, ArrayList<Forum> forums) {
        super(context, 0, forums);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.course_forum_list_item, parent, false);
        }
        Forum currentForum = getItem(position);

        TextView forumHeader = listItemView.findViewById(R.id.forum_header);
        forumHeader.setText(currentForum.getHeader());

        TextView forumBody = listItemView.findViewById(R.id.forum_body);
        forumBody.setText(currentForum.getBody());


        TextView forumDate = listItemView.findViewById(R.id.forum_date);
        forumDate.setText(currentForum.getConvertedDate());

        ImageView forumAnswered = listItemView.findViewById(R.id.forum_answered_image_view);
        if (currentForum.isAnswered() == 0) {
            forumAnswered.setVisibility(View.GONE);
        }
        return listItemView;
    }
}
