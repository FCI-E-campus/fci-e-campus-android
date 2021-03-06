package eg.edu.cu.fci.ecampus.fci_e_campus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Task;

/**
 * Created by ahmed on 6/21/2018.
 */

public class TasksAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> listHeader;
    private HashMap<String, Task> listChild;

    public TasksAdapter(Context context, ArrayList<String> listHeader, HashMap<String, Task> listChild) {
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listHeader.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        String headerTitle = (String) getGroup(groupPosition);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.tasks_list_group, null);
        }
        ImageView imageView = view.findViewById(R.id.tasks_header_image_view);
        int imageResourceId = isLastChild ? R.drawable.ic_up_button : R.drawable.ic_down_button;
        imageView.setImageResource(imageResourceId);

        TextView listGroup = view.findViewById(R.id.tasks_list_header);
        listGroup.setText(headerTitle);
        view.setPadding(36, 36, 36, 36);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        Task childTask = (Task) getChild(groupPosition, childPosition);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_task, null);
        }

        LinearLayout header = view.findViewById(R.id.ll_course_info);
        header.setVisibility(View.GONE);

        TextView taskNameTextView = view.findViewById(R.id.tv_task_name);
        taskNameTextView.setVisibility(View.GONE);

        TextView taskDescriptionTextView = view.findViewById(R.id.tv_task_description);
        taskDescriptionTextView.setText(childTask.getDescription());

        TextView creatorTextView = view.findViewById(R.id.tv_creator_username);
        creatorTextView.setText(childTask.getCreatorUsername());

        TextView weightTextView = view.findViewById(R.id.tv_weight);
        weightTextView.setText(childTask.getWeight());

        TextView createdDateTextView = view.findViewById(R.id.tv_date_created);
        createdDateTextView.setText(childTask.getConvertedCreatedDate());

        TextView dueDateTextView = view.findViewById(R.id.tv_due_date);
        dueDateTextView.setText(childTask.getConvertedDueDate());

        view.setPadding(24, 24, 24, 24);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
