package lilja.kiiski.candyclicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/*
GAMEPAGE CLASS
- Fragment for which game page is played on
- No networking, just button clicks
 */
public class GamePage extends Fragment {
    String username;
    Client client;

    int numOfClicks = 0;
    int timeLeft = 30;
    TextView numOfClicksText;
    TextView timeText;
    ImageButton candyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.game_page_layout, container, false);
        numOfClicksText = layout.findViewById(R.id.click_number_text);
        timeText = layout.findViewById(R.id.time_left);
        candyButton = layout.findViewById(R.id.candy_button);

        Bundle bundle = getArguments();
        username = bundle.getString("username");
        client = bundle.getParcelable("client");

        switch (bundle.getString("candy")){ //Set candy image
            case "1":
                candyButton.setImageResource(R.drawable.candy1);
                break;
            case "2":
                candyButton.setImageResource(R.drawable.candy2);
                break;
            case "3":
                candyButton.setImageResource(R.drawable.candy3);
                break;
            case "4":
                candyButton.setImageResource(R.drawable.candy4);
                break;
            case "5":
                candyButton.setImageResource(R.drawable.candy5);
                break;
        }

        return layout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Click Listener for Send Message Button
        view.findViewById(R.id.candy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfClicks += 1;
                numOfClicksText.setText(String.valueOf(numOfClicks));
            }
        });
        runClock();
    }

    public void runClock(){ //Runs timer in background
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (timeLeft > 0) {  //Run task once a second
                    try {
                        Thread.sleep(1000);
                        timeLeft--;

                        //UI Stuff on UI Thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (timeLeft > 9) { //0:10
                                    timeText.setText("0:" + timeLeft);
                                } else { //0:00
                                    timeText.setText("0:0" + timeLeft);
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString("type", "resultsPage");
                bundle.putString("clicks", String.valueOf(numOfClicks));
                bundle.putString("username", username);
                bundle.putParcelable("client", client);

                NetworkPage page = new NetworkPage();
                page.setArguments(bundle);

                //Navigate to Results Page
                getParentFragmentManager().beginTransaction().replace(R.id.activity_main, page).commit();

            }
        }).start();
    }
}