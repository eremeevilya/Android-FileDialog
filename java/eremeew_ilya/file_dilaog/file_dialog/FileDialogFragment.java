package eremeew_ilya.file_dilaog.file_dialog;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView tv_new_dir;

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
        (tv_new_dir = (TextView)view.findViewById(R.id.tv_new)).setOnClickListener(clickListener);

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

    public boolean setRootPath(File dir)
    {
        if(adapter == null)
            return false;
        return adapter.setRootPath(dir);
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
                case R.id.tv_new:
                    dialogNewDir();
                    break;
            }
        }
    };

    private void dialogNewDir()
    {
        final EditText et = new EditText(getContext());
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_dir)
                .setView(et)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        File dir;
                        switch(adapter.createDir(et.getText().toString()))
                        {
                            case FilesSD_Adapter.DIR_EXITS:
                                Toast.makeText(getContext(), getResources().getText(R.string.dir_exits),
                                        Toast.LENGTH_LONG).show();
                                break;
                            case FilesSD_Adapter.CREATE_DIR_ERROR:
                                Toast.makeText(getContext(), getResources().getText(R.string.can_not_create_a_dir),
                                        Toast.LENGTH_LONG).show();
                                break;
                            case FilesSD_Adapter.CREATE_DIR_OK:
                                dir = new File(adapter.getRootPath().getAbsolutePath() + "/" +
                                        et.getText().toString());
                                if(adapter.setRootPath(dir))
                                    tv_path.setText(dir.getAbsolutePath());
                                break;
                        }
                    }
                });
        dialog.create().show();
    }
}
