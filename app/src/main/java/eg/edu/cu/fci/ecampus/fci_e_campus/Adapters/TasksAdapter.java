package eg.edu.cu.fci.ecampus.fci_e_campus.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

/**
 * Created by ahmed on 6/21/2018.
 */

public class TasksAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> listHeader;
    private HashMap<String, ArrayList<String>> listChild;

    public TasksAdapter(Context context, ArrayList<String> listHeader, HashMap<String, ArrayList<String>> listChild) {
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
        return listChild.get(listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listHeader.get(groupPosition)).get(childPosition);
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

        TextView lblListHeader = view.findViewById(R.id.tasks_list_header);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        String childText = (String) getChild(groupPosition, childPosition);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.tasks_list_child, null);
        }

        TextView txtListChild = view.findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
