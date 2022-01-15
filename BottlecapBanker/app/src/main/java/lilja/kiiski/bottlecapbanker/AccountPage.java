package lilja.kiiski.bottlecapbanker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class AccountPage extends Fragment {
    public MyFileHandler fileHandler, bankingFileHandler;
    public TextView user_name, user_age, user_balance;
    public ImageView user_profile_pic;

    public ArrayList<String> banking_history;
    public ListView banking_listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View account_layout = inflater.inflate(R.layout.account_page_layout, container, false);

        //User Info
        String filePath = getContext().getFilesDir() + "/" + "user_info.json";
        fileHandler = new MyFileHandler(new File(filePath));

        user_name = account_layout.findViewById(R.id.name);
        user_age = account_layout.findViewById(R.id.age);
        user_profile_pic = account_layout.findViewById(R.id.profile_pic);
        user_balance = account_layout.findViewById(R.id.balance);

        //Banking History
        String bankingFilePath = getContext().getFilesDir() + "/" + "banking_history.json";
        bankingFileHandler = new MyFileHandler(new File(bankingFilePath));
        banking_listview = account_layout.findViewById(R.id.banking_list);
        banking_history = new ArrayList<>();

        //Load Info Is User File Exists
        if (fileHandler.FILE.exists()){
            File file = new File(getActivity().getFilesDir(), "user_info.json");
            if (file.exists()) {
                loadInfo();
            }
        }

        //Load Banking History If History File Exists
        if (bankingFileHandler.FILE.exists()) {
            Gson gson = new Gson();
            String text = bankingFileHandler.readFile();
            Data data = gson.fromJson(text, Data.class);

            int num = 1;
            while (data.getMap().containsKey(String.valueOf(num))) { //Adds all transactions to list
                banking_history.add(data.getMap().get(String.valueOf(num)).get("type") + " on " + data.getMap().get(String.valueOf(num)).get("date"));
                num++;
            }

            ArrayAdapter<String> historyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, banking_history);
            banking_listview.setAdapter(historyAdapter);
            banking_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
                public void onItemClick(AdapterView <?> parent, View view, int position, long id ){
                 new AlertDialog.Builder(view.getContext())
                         .setMessage("Type: " + data.getMap().get(String.valueOf(position+1)).get("type") + "\n"
                                 + "Date: " + data.getMap().get(String.valueOf(position+1)).get("date")  + "\n"
                                 + "From/To: " + data.getMap().get(String.valueOf(position+1)).get("from") + "\n"
                                 + "Reason: " + data.getMap().get(String.valueOf(position+1)).get("reason") + "\n"
                                 + "Amount: " + data.getMap().get(String.valueOf(position+1)).get("amount")
                         )
                         .setNegativeButton("Close", null).show();
                }
            });
        }
        return account_layout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Edit Button
        view.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccountPage.this)
                        .navigate(R.id.account_to_edit_account);
            }
        });
    }

    //Loads User Info
    public void loadInfo() {
        Gson gson = new Gson();
        String text = fileHandler.readFile();

        //Set User Data On Account Page
        Data data = gson.fromJson(text, Data.class);
        user_name.setText(data.getMap().get("user account").get("name"));
        user_age.setText("Age: " + data.getMap().get("user account").get("age"));
        user_balance.setText("Balance: " + data.getMap().get("user account").get("balance"));

        if (data.getMap().get("user account").get("name").length() > 20){
            user_name.setText(data.getMap().get("user account").get("name").substring(0, 19));
        }

        if (data.getMap().get("user account").get("age").length() > 4){
            user_age.setText("Age: " + data.getMap().get("user account").get("age").substring(0, 2));
        }

        //For changing profile pic id to int
        String old_id = data.getMap().get("user account").get("picture").replaceAll("\\s", "");
        int new_id = Integer.parseInt(old_id);

        user_profile_pic.setImageResource(new_id);
    }
}