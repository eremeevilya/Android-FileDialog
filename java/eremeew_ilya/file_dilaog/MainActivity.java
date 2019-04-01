package eremeew_ilya.file_dilaog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import eremeew_ilya.file_dilaog.file_dialog.FileDialogFragment;
import eremeew_ilya.file_dilaog.file_dialog.FileDialogListener;

public class MainActivity extends AppCompatActivity implements FileDialogListener
{
    private FileDialogFragment fileDialog;

    private CheckBox check_files;
    private EditText et_filter;
    private TextView tv_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Запршиваем разрешение на работу с памятью
        int permissiont_status = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissiont_status != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        check_files = (CheckBox)findViewById(R.id.check_files);
        check_files.setChecked(true);

        et_filter = (EditText)findViewById(R.id.et_filter);
        tv_selected = (TextView)findViewById(R.id.tv_selected);

        fileDialog = new FileDialogFragment();
        fileDialog.setFileDialogListener(this);
    }

    public void openDialog(View view)
    {
        fileDialog.show(getSupportFragmentManager(), "fileDialog");

        fileDialog.setFilter(check_files.isChecked());

        String []array_filter = et_filter.getText().toString().split(" ");
        fileDialog.setFilter(null);

        Log.i("QWERTY", "SIZE: " + array_filter.length);
        for(String s : array_filter)
        {
            Log.i("QWERTY", s);
        }
    }

    @Override
    public void onOk()
    {
        Log.i("QWERTY", "Ok");
        if(fileDialog.getFileSelected() == null)
            tv_selected.setText("");
        else
            tv_selected.setText(fileDialog.getFileSelected().getAbsolutePath());
    }

    @Override
    public void onCancel()
    {
        Log.i("QWERTY", "Cancel");
        tv_selected.setText("");
    }
}
