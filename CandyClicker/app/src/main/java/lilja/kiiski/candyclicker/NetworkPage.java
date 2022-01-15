package lilja.kiiski.candyclicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
NETWORKPAGE CLASS
- This class is used for anything that uses networking
- When moving to this page must define "type" (waitPage or resultsPage)
- Same client used for all networking pages (pass through bundle)
 */
public class NetworkPage extends Fragment {
    Client client; //Used for both
    String type;

    String username; //For WaitPage Only

    String clicks; //For ResultsPage Only
    TextView player1Name;
    TextView player1Score;
    TextView player2Name;
    TextView player2Score;

    Lock lock = new ReentrantLock(); //Locks & Conditions
    Condition clientInitialized = lock.newCondition();
    Condition sentMessage = lock.newCondition();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = null;

        Bundle bundle = getArguments();  //Get data from bundle
        username = bundle.getString("username");
        client = bundle.getParcelable("client");
        type = bundle.getString("type");
        clicks = bundle.getString("clicks");

        if (type.equals("waitPage")){ //Set to WaitPage
            layout = inflater.inflate(R.layout.wait_page_layout, container, false);

        } else if (type.equals("resultsPage")){ //Set to ResultsPage
            layout = inflater.inflate(R.layout.results_page_layout, container, false);

            player1Name = layout.findViewById(R.id.player1_name_text);
            player1Score = layout.findViewById(R.id.player1_score_text);
            player2Name = layout.findViewById(R.id.player2_name_text);
            player2Score = layout.findViewById(R.id.player2_score_text);
        }

        if (client == null) { //If client hasn't been initialized
            try {
                client = new Client(this);
                lock.lock();
                while (!client.initialized) {
                    clientInitialized.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } else { //Client page still needs to be updated
            client.thisPage = this;
        }

        if (!client.connected) {
            Toast.makeText(getActivity(), "COULD NOT CONNECT TO SERVER", Toast.LENGTH_LONG).show();
        }

        return layout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (type.equals("waitPage")){
            client.sendMessage("NAME:" + username); //Send name
        } else if (type.equals("resultsPage")){
            client.sendMessage("SCORE:" + clicks); //Send score

        } try { //Wait for message to send
            lock.lock();
            while (!client.sentMessage) {
                sentMessage.await();
            }
            client.sentMessage = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();

            if (type.equals("waitPage")){ //Receive waitPage messages
                waitPageMessages();
            } else if (type.equals("resultsPage")){ //Receive resultsPage messages
               resultsPageMessages();
            }
        }
    }

    public void resultsPageMessages(){ //Receives messages for ResultsPage
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                         if (client.br.readLine().equals("RESULTS")) { //Received game results
                             String player1NameText = client.br.readLine();
                             String player1ScoreText = client.br.readLine();
                             String player2NameText = client.br.readLine();
                             String player2ScoreText = client.br.readLine();

                             //UI Stuff on UI Thread
                             getActivity().runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     //Set Results
                                     try {
                                         player1Name.setText(player1NameText);
                                         player1Score.setText(player1ScoreText);
                                         player2Name.setText(player2NameText);
                                         player2Score.setText(player2ScoreText);
                                     } catch (Exception e){
                                         e.printStackTrace();
                                     }
                                 }
                             });
                             break; //Break out of thread
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void waitPageMessages(){ //Receives messages for WaitPage
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String candy = "";
                    while (true) {
                        if (client.br.readLine().equals("START GAME")) { //Starts game
                            candy = client.br.readLine();
                            break; //Break out of thread
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putParcelable("client", client);
                    bundle.putString("candy", candy);

                    GamePage page = new GamePage();
                    page.setArguments(bundle);

                    //Navigate to Game Page
                    getParentFragmentManager().beginTransaction().replace(R.id.activity_main, page).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}