package lilja.kiiski.candyclicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/*
HOMEPAGE CLASS
- Fragment for home page
- Initializes username
 */
public class HomePage extends Fragment {
    EditText username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home_layout = inflater.inflate(R.layout.home_page_layout, container, false);
        username = home_layout.findViewById(R.id.username_edit_text);

        return home_layout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Click Listener for Send Message Button
        view.findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username.getText().toString());
                bundle.putString("type", "waitPage");
                bundle.putString("clicks", "0");

                NetworkPage page = new NetworkPage();
                page.setArguments(bundle);

                //Navigate to Wait Page
                getParentFragmentManager().beginTransaction().replace(R.id.activity_main, page).commit();
            }
        });
    }
}