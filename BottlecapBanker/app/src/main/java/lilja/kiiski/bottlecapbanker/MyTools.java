package lilja.kiiski.bottlecapbanker;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MyTools {
    public MyFileHandler fileHandler;

    public MyTools(MyFileHandler fileHandler){
        this.fileHandler = fileHandler;
    }

    //For BANKING HISTORY
    public void makeTransaction(String type, String date, String fromPerson, String amount, String reason){

        Data data = new Data(); //If first bank entry
        Gson gson = new Gson();
        int num = 1; //Transaction number

        if (fileHandler.FILE.exists()) { //If some bank entries have already been made
            String text = fileHandler.readFile();
            data = gson.fromJson(text, Data.class);

            //Goes through map and finds what next transaction should be called "1, 2, 3, etc."
            while (data.getMap().containsKey(String.valueOf(num))) {
                num++;
            }
        }

        //Json Stuff
        Map<String, String> transaction = new HashMap<>();
        transaction.put("type", type);
        transaction.put("date", date);
        transaction.put("from", fromPerson);
        transaction.put("reason", reason);
        transaction.put("amount", amount);
        data.getMap().put(String.valueOf(num), transaction);

        //Gson Stuff
        String JsonUserData = gson.toJson(data);
        fileHandler.writeFile(JsonUserData);
    }

    //For USER ACCOUNT
    public void editBalance(int newBalance) {
        Gson gson = new Gson();

        String text = fileHandler.readFile();
        Data data = gson.fromJson(text, Data.class);

        data.getMap().get("user account").put("balance", String.valueOf(newBalance));

        //Gson Stuff
        String JsonUserData = gson.toJson(data);
        fileHandler.writeFile(JsonUserData);
    }

    //For USER ACCOUNT
    public int getBalance(){
        Gson gson = new Gson();
        String text = fileHandler.readFile();
        Data data = gson.fromJson(text, Data.class);

        return Integer.parseInt(data.getMap().get("user account").get("balance"));
    }
}