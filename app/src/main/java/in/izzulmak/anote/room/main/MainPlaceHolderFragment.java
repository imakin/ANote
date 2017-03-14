package in.izzulmak.anote.room.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.izzulmak.anote.MainActivity;
import in.izzulmak.anote.R;

/**
 * Created by Izzulmakin on 31/07/16.
 */

/**
 * contains view
 */
public class MainPlaceHolderFragment extends Fragment {
    public final static int SECTION_LIST = MainActivity.SECTION_LIST+1;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    MainActivity mref = null;
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainPlaceHolderFragment newInstance(int sectionNumber, MainActivity mref) {
        MainPlaceHolderFragment fragment = new MainPlaceHolderFragment();
        fragment.setMref(mref);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainPlaceHolderFragment() {
    }

    public void setMref(MainActivity mref) {
        this.mref = mref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int section = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //mref.loadCustomButtons();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
