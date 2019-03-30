package eremeew_ilya.file_dilaog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import eremeew_ilya.file_dilaog.file_dialog.FileDialogFragment;

public class MainActivity extends AppCompatActivity
{
    private FileDialogFragment fileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileDialog = new FileDialogFragment();
    }

    public void openDialog(View view)
    {
        fileDialog.show(getSupportFragmentManager(), "fileDialog");
    }
}
