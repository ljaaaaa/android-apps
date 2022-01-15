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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DepositPage extends Fragment implements AdapterView.OnItemSelectedListener{
    public MyFileHandler fileHandler, userInfoFileHandler;
    public MyTools tools, userInfoTools;
    public String fromPerson, date;
    public EditText edit_amount, edit_reason;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View account_layout = inflater.inflate(R.layout.deposit_page_layout, container, false);

        //File Handler & Tools
        String filePath = getContext().getFilesDir() + "/" + "banking_history.json";
        fileHandler = new MyFileHandler(new File(filePath));
        tools = new MyTools(fileHandler);

        String userInfoFilePath = getContext().getFilesDir() + "/" + "user_info.json";
        userInfoFileHandler = new MyFileHandler(new File(userInfoFilePath));
        userInfoTools = new MyTools(userInfoFileHandler);

        //Spinner
        Spinner spin = (Spinner)account_layout.findViewById(R.id.from_person_spinner);
        spin.setOnItemSelectedListener(this);

        edit_amount = account_layout.findViewById(R.id.edit_deposit_amount);
        edit_reason = account_layout.findViewById(R.id.edit_deposit_reason);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        date = df.format(c);

        return account_layout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Account Button
        view.findViewById(R.id.deposit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = getContext().getFilesDir() + "/" + "user_info.json";
                int newBalance;

                if (errorCheck().equals("All Good")) {
                    newBalance = userInfoTools.getBalance() + Integer.parseInt(edit_amount.getText().toString().replaceAll("\\s", ""));
                    tools.makeTransaction("deposit", date, fromPerson, edit_amount.getText().toString(), edit_reason.getText().toString());
                    userInfoTools.editBalance(newBalance);
                    Toast.makeText(getActivity(), edit_amount.getText().toString() + " Deposited To Your Account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //For Spinner Selection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] possibles = {"Choose", "Joona", "Senja", "Lilja", "Noora", "Petteri", "Susanna", "Luukas"};
        fromPerson = possibles[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    public String errorCheck(){
        String filePath = getContext().getFilesDir() + "/" + "user_info.json";

        if (!new File(filePath).exists()) { //No account created
            Toast.makeText(getActivity(), "Create An Account First!", Toast.LENGTH_LONG).show();
            return "No Account Created";
        }

        else if (fromPerson.equals("Choose") || //Info fields are left blank
                edit_amount.getText().toString().equals("") ||
                edit_reason.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Please Fill In All Required Fields", Toast.LENGTH_LONG).show();
            return "Incomplete Info Fields";
        }

        else if (edit_amount.getText().toString().length() >= 5) { //Amount isn't super big
            Toast.makeText(getActivity(), "You Can't Deposit That Much Money!", Toast.LENGTH_LONG).show();
            return "Too Much Money";
        }

        return "All Good";
    }
}