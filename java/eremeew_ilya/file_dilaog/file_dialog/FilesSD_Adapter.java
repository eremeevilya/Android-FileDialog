package eremeew_ilya.file_dilaog.file_dialog;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;

import eremeew_ilya.file_dilaog.R;

public class FilesSD_Adapter extends BaseAdapter
{
    public static final int CREATE_DIR_ERROR = 1;
    public static final int CREATE_DIR_OK = 2;
    public static final int DIR_EXITS = 3;

    // Проверяет доступност SD
    public static boolean isSD_Enable()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Путь к SD карте
    public static File getSD_Path()
    {
        return Environment.getExternalStorageDirectory();
    }

    protected LayoutInflater inflater;

    protected Context context;

    // Список файлов и деректорий в корне
    protected File []list_root_files;

    // Текущая корневая папка
    File root_dir;

    // Конструктор с корнем в корне SD
    public FilesSD_Adapter(Context context)
    {
        this.context = context;

        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        root_dir = getSD_Path();

        list_root_files = root_dir.listFiles();
    }

    public File getFile(int index)
    {
        return list_root_files[index];
    }

    // Устанавливает папку в качестве корневой
    public boolean setRootPath(File dir)
    {
        if(dir.isDirectory())
        {
            root_dir = dir;
            list_root_files = root_dir.listFiles();

            // Обновляем ListView
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public File getRootPath()
    {
        return root_dir;
    }

    // Создает новую папку в текущей директории
    public int createDir(String dirName)
    {
        Log.i("QWERTY",root_dir.getAbsolutePath() + "/" + dirName);
        File newDir = new File(root_dir.getAbsolutePath() + "/" + dirName);
        if(newDir.exists())
            return DIR_EXITS;

        if(newDir.mkdirs())
        {
            list_root_files = root_dir.listFiles();

            // Обновляем ListView
            notifyDataSetChanged();

            return CREATE_DIR_OK;
        }
        return CREATE_DIR_ERROR;
    }

    // Переход на уровень выше
    public boolean cdUp()
    {
        //Log.i("QWERTY", root_dir.getParent());
        if(root_dir.getAbsolutePath().equals(getSD_Path().getAbsolutePath()))
            return false;

        root_dir = new File(root_dir.getParent());
        list_root_files = root_dir.listFiles();

        // Обновляем ListView
        notifyDataSetChanged();

        return true;
    }

    @Override
    public int getCount() {
        if(list_root_files == null)
            return 0;
        return list_root_files.length;
    }

    @Override
    public Object getItem(int i) {
        return list_root_files[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.item_list_files_sd, viewGroup, false);
        }

        File file = list_root_files[i];

        ((TextView)view.findViewById(R.id.name)).setText(file.getName());
        TextView type = (TextView)view.findViewById(R.id.type);
        if(file.isDirectory())
        {
            type.setText("Directory");
        }
        else if(file.isFile())
        {
            type.setText("File");
        }

        return view;
    }
}
