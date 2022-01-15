package lilja.kiiski.doitlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/* RECYCLERVIEWADAPTER CLASS
- For tasks recyclerview
- Note for "On Click"
    - Some are handler in OnBindViewHolder here
    - Most in MyClickListener in MainActivity
- Helper Tool
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    public Context context;
    public ArrayList<Task> tasks;

    private MyOnClickListener listener;

    public RecyclerViewAdapter(Context context, ArrayList<Task> tasks, MyOnClickListener listener){
        this.tasks = tasks;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        ViewHolder holder = new ViewHolder(view, listener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Task task = tasks.get(position);
        holder.name.setText(task.name);
        holder.details.setText(task.details);

        if (tasks.get(position).done.equals("false")){ //Change to done
            holder.checkbox.setImageResource(R.mipmap.ic_checkbox);
        } else { //Change to not done
            holder.checkbox.setImageResource(R.mipmap.ic_checkbox_2);
        }

        //For drop down menu
        holder.bind(task);

        holder.dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();

                boolean expanded = task.expanded;
                task.expanded = !expanded;
                notifyItemChanged(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ConstraintLayout taskBox;
        ImageView checkbox;
        TextView name;
        TextView details;
        MyOnClickListener listener;

        ImageView dropDown;

        public ViewHolder(View itemView, MyOnClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            checkbox = itemView.findViewById(R.id.checkbox);
            details = itemView.findViewById(R.id.details);
            taskBox = itemView.findViewById(R.id.task_box);

            dropDown = itemView.findViewById(R.id.drop_down);

            this.listener = listener;
            checkbox.setOnClickListener(this);
            taskBox.setOnClickListener(this);
        }

        private void bind(Task task) {
            details.setVisibility(task.expanded ? View.VISIBLE : View.GONE);
            name.setText(task.name);
            details.setText(task.details);

            if (task.done.equals("false")){ //Change to not done
                checkbox.setImageResource(R.mipmap.ic_checkbox);
            } else { //Change to done
                checkbox.setImageResource(R.mipmap.ic_checkbox_2);
            }
            if (task.expanded){ //Change image
                dropDown.setImageResource(R.mipmap.ic_drop_up);
            } else { //Change to not done
                dropDown.setImageResource(R.mipmap.ic_drop_down);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.checkbox:
                    listener.onMyClick(getAdapterPosition(), "checkbox");
                    break;
                case R.id.task_box:
                    listener.onMyClick(getAdapterPosition(), "taskbox");
                    break;
            }
        }
    }

    public interface MyOnClickListener {
        void onMyClick(int position, String type);
    }
}