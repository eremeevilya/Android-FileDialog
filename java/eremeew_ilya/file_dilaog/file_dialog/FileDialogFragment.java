package eremeew_ilya.file_dilaog.file_dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

import eremeew_ilya.file_dilaog.R;


public class FileDialogFragment extends DialogFragment// implements View.OnClickListener
{
    private DialogFragment this_dialog;

    private TextView tv_path;
    private ListView listFiles;

    private TextView tvUp;
    private TextView tvOk;
    private TextView tvCancel;

    private FilesSD_Adapter adapter;

    private boolean show_files = true;
    private String [] array_filter;

    private File file_select = null;

    private FileDialogListener fileDialogListener = null;

    public FileDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        this_dialog = this;

        View view = inflater.inflate(R.layout.fragment_file_dialog, container, false);

        if(!FilesSD_Adapter.isSD_Enable())
        {
            dismiss();
            return view;
        }

        adapter = new FilesSD_Adapter(getContext());

        adapter.setFilter(show_files);
        adapter.setFilter(array_filter);

        tv_path = (TextView)view.findViewById(R.id.tv_path);
        listFiles = (ListView)view.findViewById(R.id.listFiles);
        (tvUp = (TextView)view.findViewById(R.id.tv_up)).setOnClickListener(clickListener);
        (tvOk = (TextView)view.findViewById(R.id.tv_ok)).setOnClickListener(clickListener);
        (tvCancel = (TextView)view.findViewById(R.id.tv_cancel)).setOnClickListener(clickListener);

        listFiles.setOnItemClickListener(itemClickListener);

        tv_path.setText(adapter.getRootPath().getAbsolutePath());
        listFiles.setAdapter(adapter);

        return view;
    }

    // Параметр true -показать файлы, иначе показать только директории
    public void setFilter(boolean showFiles)
    {
        show_files = showFiles;

        if(adapter != null)
            adapter.setFilter(show_files);
    }

    // Параметр filter -массив строк, расширений файлов. Расширение без точки.
    public void setFilter(String []filter)
    {
        array_filter = filter;

        if(adapter != null)
            adapter.setFilter(array_filter);
    }

    public FilesSD_Adapter getAdapter() {return adapter;}

    public File getFileSelected()
    {
        return file_select;
    }

    public void setFileDialogListener(FileDialogListener listener)
    {
        fileDialogListener = listener;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            File file = adapter.getFile(i);
            adapter.setRootPath(file);
            tv_path.setText(file.getAbsolutePath());

            file_select = file;
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            switch(view.getId())
            {
                case R.id.tv_up:
                    if(adapter.cdUp())
                        tv_path.setText(adapter.getRootPath().getAbsolutePath());
                    break;
                case R.id.tv_ok:
                    if(fileDialogListener != null)
                        fileDialogListener.onOk();

                    this_dialog.dismiss();
                    break;
                case R.id.tv_cancel:
                    if(fileDialogListener != null)
                        fileDialogListener.onCancel();

                    this_dialog.dismiss();
                    break;
            }
        }
    };
}
