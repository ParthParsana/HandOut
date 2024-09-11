package com.example.parth.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.parth.R;
import com.example.parth.databinding.FragmentDashboardBinding;
import com.example.parth.electronics;
import com.example.parth.footware;
import com.example.parth.grocery;
import com.example.parth.milk;
import com.example.parth.stationary;
import com.example.parth.toys;
import com.example.parth.vegfruits;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private CardView groceryCardView;
    private CardView vegfruitsCardView;
    private CardView milkproductCardView;
    private CardView footwareCardView;
    private CardView electronicsCardView;
    private CardView toysCardView;
    private CardView stationaryCardView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the CardView
        groceryCardView = root.findViewById(R.id.grocery);
        vegfruitsCardView = root.findViewById(R.id.vegfruits);
        milkproductCardView = root.findViewById(R.id.milkproguct);
        footwareCardView = root.findViewById(R.id.footware);
        electronicsCardView = root.findViewById(R.id.electronics);
        toysCardView = root.findViewById(R.id.toys);
        stationaryCardView = root.findViewById(R.id.stationary);

        // Set OnClickListener for the CardView
        groceryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the grocery activity
                Intent intent = new Intent(getContext(), grocery.class); // Use getActivity() instead of DashboardFragment.this
                startActivity(intent);
            }
        });

        vegfruitsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the grocery activity
                Intent intent = new Intent(getContext(),vegfruits.class);
                startActivity(intent);
            }
        });

        milkproductCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the grocery activity
                Intent intent = new Intent(getContext(), milk.class);
                startActivity(intent);
            }
        });

        footwareCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the grocery activity
                Intent intent = new Intent(getContext(), footware.class);
                startActivity(intent);
            }
        });

        electronicsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the grocery activity
                Intent intent = new Intent(getContext(), electronics.class);
                startActivity(intent);
            }
        });

        toysCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the grocery activity
                Intent intent = new Intent(getContext(), toys.class);
                startActivity(intent);
            }
        });

        stationaryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the grocery activity
                Intent intent = new Intent(getContext(), stationary.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
