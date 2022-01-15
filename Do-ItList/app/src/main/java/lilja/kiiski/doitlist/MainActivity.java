package lilja.kiiski.doitlist;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* MAINACTIVITY CLASS
- All operations done here
- All other java files are helper tools
- One activity, no fragments
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.MyOnClickListener, View.OnClickListener{
    ArrayList<Task> tasks;
    RecyclerViewAdapter myAdapter;
    RecyclerView recyclerView;

    MyFileHandler fileHandler;

    View editTaskView;
    MyDialog builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        String filePath = getFilesDir() + "/" + "tasks.json"; //Get tasks from file
        fileHandler = new MyFileHandler(new File(filePath));
        Gson gson = new Gson();

        setContentView(R.layout.home_page_layout);

        editTaskView = getLayoutInflater().inflate(R.layout.new_task_pop_up, null); //For Pop Ups
        tasks = new ArrayList<>();
        builder = new MyDialog(this);

        if (fileHandler.FILE.exists()){ //Retrieve previous tasks
            String text = fileHandler.readFile();
            Data data = gson.fromJson(text, Data.class);
            Iterator it = data.getMap().entrySet().iterator();

            while (it.hasNext()) { //Create tasks from map
                Map.Entry pair = (Map.Entry)it.next();
                Task task = new Task();
                task.name = data.getMap().get(String.valueOf(pair.getKey())).get("name");
                task.details = data.getMap().get(String.valueOf(pair.getKey())).get("details");
                task.done = data.getMap().get(String.valueOf(pair.getKey())).get("done");
                task.num = Integer.parseInt(data.getMap().get(String.valueOf(pair.getKey())).get("num"));

                tasks.add(task);
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.all_tasks); //Recyclerview for tasks
        myAdapter = new RecyclerViewAdapter(this, tasks, this);
        recyclerView.setAdapter(myAdapter);

        findViewById(R.id.add_button).setOnClickListener(this);
    }

    @Override
    public void onMyClick(int position, String type) { //For recyclerview box click
        switch (type){
            case "checkbox": //Checkbox has been clicked
                Task task = tasks.get(position);

                if (tasks.get(position).done.equals("false")){ //Change to done
                    task.done = "true";
                } else { //Change to not done
                    task.done = "false";
                }
                myAdapter.notifyItemChanged(position);

                //Save "done" settings of task
                String filePath = getFilesDir() + "/" + "tasks.json";
                MyFileHandler fileHandler = new MyFileHandler(new File(filePath));
                Gson gson = new Gson();

                if (fileHandler.FILE.exists()){ //Retrieve previous tasks
                    String text = fileHandler.readFile();
                    Data data = gson.fromJson(text, Data.class);

                    Map<String, String> thisTask = data.getMap().get(String.valueOf(task.num));
                    thisTask.put("done", task.done);

                    data.getMap().put(String.valueOf(task.num), thisTask);
                    String JsonUserData = gson.toJson(data);
                    fileHandler.writeFile(JsonUserData);
                }
                break;
            case "taskbox": //Taskbox has been clicked
                newEditTaskPopUp(tasks.get(position));
                ((TextView) editTaskView.findViewById(R.id.edit_task_name)).setText(tasks.get(position).name);
                ((TextView) editTaskView.findViewById(R.id.edit_details)).setText(tasks.get(position).details);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_button: //Add tasks
                newEditTaskPopUp(new Task());
                break;

            case R.id.save_task_button: //Save task
                builder.task.num = getTaskNum(builder.task);
                builder.task.name = ((EditText)editTaskView.findViewById(R.id.edit_task_name)).getText().toString();
                builder.task.details = ((EditText)editTaskView.findViewById(R.id.edit_details)).getText().toString();

                if (!tasks.contains(builder.task)){ //If task is in tasks list
                    tasks.add(builder.task);
                }

                saveTask(builder.task);
                dialog.dismiss();
                myAdapter.tasks = tasks;
                myAdapter.notifyDataSetChanged();
                break;

            case R.id.delete_task: //Delete task
                deleteTask(builder.task);
                tasks.remove(builder.task);
                dialog.dismiss();
                myAdapter.tasks = tasks;
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void newEditTaskPopUp(Task task){ //New pop up task window
        editTaskView = getLayoutInflater().inflate(R.layout.new_task_pop_up, null);

        builder.task = task;
        builder.setView(editTaskView);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        findViewById(R.id.add_button).setOnClickListener(this);
        editTaskView.findViewById(R.id.delete_task).setOnClickListener(this);
        editTaskView.findViewById(R.id.save_task_button).setOnClickListener(this);
    }

    public void saveTask(Task task){ //Save task
        Data data = getData();
        Gson gson = new Gson();

        task.num = getTaskNum(task);

        Map<String, String> newTask = new HashMap<>();
        newTask.put("name", task.name);
        newTask.put("details", task.details);

        task.num = getTaskNum(task);
        newTask.put("num", String.valueOf(task.num));

        //If task is done or not
        if (data.getMap().get("done") == null || data.getMap().get("done").equals("false")){
            newTask.put("done", "false");
        } else {
            newTask.put("done", String.valueOf(data.getMap().get("done")));
        }

        data.getMap().put(String.valueOf(task.num), newTask);
        String JsonUserData = gson.toJson(data);
        fileHandler.writeFile(JsonUserData);
    }

    public void deleteTask(Task task){ //Delete task
        Data data = getData();
        Gson gson = new Gson();
        task.num = getTaskNum(task);

        data.getMap().remove(String.valueOf(task.num));
        String JsonUserData = gson.toJson(data);
        fileHandler.writeFile(JsonUserData);
    }

    public int getTaskNum(Task task){ //Get task ID num
        Data data = getData();
        int taskNum = 1;

        if (task.num == 0){ //No ID set
            if (fileHandler.FILE.exists()) {
                while (data.getMap().containsKey(String.valueOf(taskNum))) {
                    taskNum++; //Set new num
                }
            }
        } else { //ID has been set before
            taskNum = task.num;
        }

        return taskNum;
    }

    public Data getData(){ //Returns Data
        Data data = new Data();
        Gson gson = new Gson();

        if (fileHandler.FILE.exists()) { //If more tasks exist
            String text = fileHandler.readFile();
            data = gson.fromJson(text, Data.class);
        }
        return data;
    }
}