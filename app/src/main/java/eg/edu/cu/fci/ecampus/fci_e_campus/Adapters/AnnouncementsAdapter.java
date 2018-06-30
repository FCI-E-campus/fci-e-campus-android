package eg.edu.cu.fci.ecampus.fci_e_campus.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import eg.edu.cu.fci.ecampus.fci_e_campus.R;
import eg.edu.cu.fci.ecampus.fci_e_campus.models.Announcement;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder> {

    private static final String TAG = AnnouncementsAdapter.class.getSimpleName();

    private Announcement [] announcements;

    public AnnouncementsAdapter(Announcement[] announcements) {
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements[position];

        holder.titleTextView.setText(announcement.getTitle());
        holder.bodyTextView.setText(announcement.getBody());
        holder.dateTextView.setText(announcement.getPublishDate().toString());

    }

    @Override
    public int getItemCount() {
        return announcements.length;
    }

    public void swapAnnouncements(Announcement[] announcements) {
        this.announcements = announcements;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title) TextView titleTextView;
        @BindView(R.id.tv_body) TextView bodyTextView;
        @BindView(R.id.tv_date) TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
