package lilja.kiiski.bottlecapbanker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EditAccountPage extends Fragment implements AdapterView.OnItemSelectedListener{
    public MyFileHandler fileHandler;
    public EditText edit_name, edit_age;
    public int profile_pic_id;
    public int[] images;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View account_layout = inflater.inflate(R.layout.edit_account_page_layout, container, false);

        String filePath = getContext().getFilesDir() + "/" + "user_info.json";
        fileHandler = new MyFileHandler(new File(filePath));

        edit_name = account_layout.findViewById(R.id.edit_name);
        edit_age = account_layout.findViewById(R.id.edit_age);
        profile_pic_id = R.drawable.pic1;

        images = new int[] {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,
                R.drawable.pic6, R.drawable.pic7, R.drawable.pic8, R.drawable.pic9, R.drawable.pic10};

        Spinner spin = (Spinner)account_layout.findViewById(R.id.profile_spinner);
        spin.setOnItemSelectedListener(this);

        CustomAdapter customAdapter = new CustomAdapter(getActivity(),images);
        spin.setAdapter(customAdapter);

        if (new File(filePath).exists()) { //If user info file exists, add info as set up for user info
            Gson gson = new Gson();
            String text = fileHandler.readFile();

            Data data = gson.fromJson(text, Data.class);

            edit_name.setText(data.getMap().get("user account").get("name"));
            edit_age.setText(data.getMap().get("user account").get("age"));

            //For changing profile pic id to int
            String old_id = data.getMap().get("user account").get("picture").replaceAll("\\s", "");
            int new_id = Integer.parseInt(old_id);

            for (int x = 0; x < images.length; x++) {
                if (images[x] == new_id) {
                    spin.setSelection(x);
                }
            }
        }

        return account_layout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Save Button
        view.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
                Toast.makeText(getActivity(), "Updated Account Info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //For Spinner Selection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        profile_pic_id = images[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    //Saves User Info
    public void saveInfo() {
        Gson gson = new Gson();

        //Json Stuff
        Map<String, String> account = new HashMap<>();
        Map<String, Map<String, String>> map = new HashMap<>();

            //If first time making account, balance will be 0
            account.put("balance", "0");

            if (fileHandler.FILE.exists()) {
                String text = fileHandler.readFile();
                Data data = gson.fromJson(text, Data.class);
                account.put("balance", data.getMap().get("user account").get("balance"));
            }

            account.put("name", edit_name.getText().toString());
            account.put("age", edit_age.getText().toString());
            account.put("picture", String.valueOf(profile_pic_id));

            map.put("user account", account);

        //Gson Stuff
        Data data2 = new Data(map);
        String JsonUserData = gson.toJson(data2);

        fileHandler.writeFile(JsonUserData);
    }
}