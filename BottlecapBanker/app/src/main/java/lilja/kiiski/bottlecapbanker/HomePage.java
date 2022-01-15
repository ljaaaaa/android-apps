package lilja.kiiski.bottlecapbanker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class HomePage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home_layout = inflater.inflate(R.layout.home_page_layout, container, false);

        return home_layout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Account Button
        view.findViewById(R.id.account_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(HomePage.this)
                        .navigate(R.id.home_to_account);
            }
        });

        //Deposit Button
        view.findViewById(R.id.deposit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomePage.this)
                        .navigate(R.id.home_to_deposit);
            }
        });

        //Withdraw Button
        view.findViewById(R.id.withdraw_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomePage.this)
                        .navigate(R.id.home_to_withdraw);
            }
        });
    }
}

