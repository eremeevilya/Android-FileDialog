package eremeew_ilya.file_dilaog.file_dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

import eremeew_ilya.file_dilaog.R;


public class FileDialogFragment extends DialogFragment
{
    private TextView tv_path;
    private ListView listFiles;

    private FilesSD_Adapter adapter;

    public FileDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_file_dialog, container, false);

        if(!FilesSD_Adapter.isSD_Enable())
        {
            dismiss();
            return view;
        }

        adapter = new FilesSD_Adapter(getContext());

        tv_path = (TextView)view.findViewById(R.id.tv_path);
        listFiles = (ListView)view.findViewById(R.id.listFiles);

        listFiles.setOnItemClickListener(itemClickListener);

        tv_path.setText(adapter.getRootPath().getAbsolutePath());
        listFiles.setAdapter(adapter);

        return view;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            File file = adapter.getFile(i);

            if(adapter.setRootPath(file))
                tv_path.setText(file.getAbsolutePath());
        }
    };
}
