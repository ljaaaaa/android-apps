package lilja.kiiski.candyclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
MAINACTIVITY CLASS
- Only activity used in project
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_main, new HomePage()).commit(); //Starts page at home
    }
}