package eg.edu.cu.fci.ecampus.fci_e_campus.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Task;

public class AllTasksAdapter extends RecyclerView.Adapter<AllTasksAdapter.ViewHolder>{

    private static final String TAG = AllTasksAdapter.class.getSimpleName();

    private List<Task> allTasks;

    public AllTasksAdapter(List<Task> allTasks) {
        this.allTasks = allTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = allTasks.get(position);

        holder.courseCodeTextView.setText(task.getCourseCode());
        holder.taskNameTextView.setText(task.getName());
        holder.taskDescriptionTextView.setText(task.getDescription());
        holder.dateCreatedTextView.setText(task.getCreatedDate().toString());
        holder.dueDateTextView.setText(task.getDueDate().toString());
        holder.creatorUserNameTextView.setText(task.getCreatorUsername());
        holder.taskWeightTextView.setText(task.getWeight());
    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_course_code) TextView courseCodeTextView;
        @BindView(R.id.tv_task_name) TextView taskNameTextView;
        @BindView(R.id.tv_task_description) TextView taskDescriptionTextView;
        @BindView(R.id.tv_date_created) TextView dateCreatedTextView;
        @BindView(R.id.tv_due_date) TextView dueDateTextView;
        @BindView(R.id.tv_creator_username) TextView creatorUserNameTextView;
        @BindView(R.id.tv_weight) TextView taskWeightTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }



}
