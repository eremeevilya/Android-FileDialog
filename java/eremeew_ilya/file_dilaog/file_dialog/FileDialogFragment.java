package eremeew_ilya.file_dilaog.file_dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eremeew_ilya.file_dilaog.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileDialogFragment extends DialogFragment
{


    public FileDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_dialog, container, false);
    }

}
