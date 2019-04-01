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
import java.io.FileFilter;
import java.text.SimpleDateFormat;

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
    protected File root_dir;

    protected boolean show_files; // true -показать файлы, иначе только директории
    protected String [] array_filter; // filter -массив строк, расширений файлов.

    protected Filter file_filter;

    // Конструктор с корнем в корне SD
    public FilesSD_Adapter(Context context)
    {
        this.context = context;

        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        root_dir = getSD_Path();

        file_filter = new Filter();

        list_root_files = root_dir.listFiles(file_filter);
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
            list_root_files = root_dir.listFiles(file_filter);

            // Обновляем ListView
            notifyDataSetChanged();
            return true;
        }

        notifyDataSetChanged();
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
            list_root_files = root_dir.listFiles(file_filter);

            // Обновляем ListView
            notifyDataSetChanged();

            return CREATE_DIR_OK;
        }
        return CREATE_DIR_ERROR;
    }

    // Переход на уровень выше
    public boolean cdUp()
    {
        if(root_dir.getAbsolutePath().equals(getSD_Path().getAbsolutePath()))
            return false;

        root_dir = new File(root_dir.getParent());
        list_root_files = root_dir.listFiles(file_filter);

        // Обновляем ListView
        notifyDataSetChanged();

        return true;
    }

    // Параметр true -показать файлы, иначе показать только директории
    public void setFilter(boolean showFiles)
    {
        show_files = showFiles;
    }

    // Параметр filter -массив строк, расширений файлов. Расширение без точки.
    public void setFilter(String []filter)
    {
        array_filter = filter;
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
        TextView tv_info = (TextView)view.findViewById(R.id.tv_info);

        String str_info;
        if(file.isDirectory())
        {
            str_info = "DIR";
        }
        else if(file.isFile())
        {
            if(file.length() > 9000)
            {
                float f = file.length() / 1024;
                str_info = Float.toString(f) + "KB";
            }
            else
            {
                str_info = file.length() + "B";
            }
        }
        else
        {
            str_info = "";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        tv_info.setText(str_info + "   " + dateFormat.format(file.lastModified()));

        return view;
    }

    //
    private class Filter implements FileFilter
    {
        @Override
        public boolean accept(File file)
        {
            if(!show_files && file.isFile())
                return false;

            if(array_filter != null && array_filter.length > 0 && file.isFile())
            {
                String file_name = file.getName().toLowerCase();
                for(String filter : array_filter)
                {
                    if(file_name.endsWith("." + filter.toLowerCase()))
                        return true;
                }
                return false;
            }

            return true;
        }
    }
}
