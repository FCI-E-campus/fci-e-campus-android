package eg.edu.cu.fci.ecampus.fci_e_campus.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;

import eg.edu.cu.fci.ecampus.fci_e_campus.R;

public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_map, container, false);

        PhotoView mapPhotoView = fragmentView.findViewById(R.id.pv_map);

        // load image from assets
        try {
            // get input stream
            InputStream facultyMapInputStream = getContext().getAssets().open("faculty_map.png");
            // load image as Drawable
            Drawable facultyMapDrawable = Drawable.createFromStream(facultyMapInputStream, null);
            // set image to PhotoView
            mapPhotoView.setImageDrawable(facultyMapDrawable);
        }
        catch (IOException e) {
            Log.d(TAG, e.toString());

            Toast.makeText(getContext(), "Error loading map!", Toast.LENGTH_SHORT).show();
        }

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Toast.makeText(getContext(), "Hint: use pinch to zoom.", Toast.LENGTH_SHORT).show();
    }
}
