package com.example.parth.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parth.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Handler handler = new Handler();
    private int currentImageIndex = 0;
    private final int swipeDelayMillis = 2000; // Time delay between each image swipe (in milliseconds)
    private Runnable swipeRunnable;
    private int totalImages; // Total number of images

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the HorizontalScrollView
        HorizontalScrollView horizontalScrollView = binding.imgScroll;

        // Get the LinearLayout inside the HorizontalScrollView
        LinearLayout linearLayout = (LinearLayout) horizontalScrollView.getChildAt(0);

        // Get the total number of images (children inside the LinearLayout)
        totalImages = linearLayout.getChildCount();

        // Define the auto-swiping logic
        swipeRunnable = new Runnable() {
            @Override
            public void run() {
                // Ensure that the layout and its children have been laid out
                linearLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int imageWidth = linearLayout.getChildAt(0).getWidth(); // Get the width of one image (assumed all images have the same width)

                        // Scroll to the next image
                        currentImageIndex++;
                        if (currentImageIndex >= totalImages) {
                            // If we've reached the last image, reset to the first image
                            currentImageIndex = 0;
                            horizontalScrollView.scrollTo(0, 0); // Reset to the first image
                        } else {
                            // Scroll to the next image by calculating the horizontal scroll offset
                            horizontalScrollView.smoothScrollTo(currentImageIndex * imageWidth, 0);
                        }

                        // Continue swiping
                        handler.postDelayed(swipeRunnable, swipeDelayMillis);
                    }
                });
            }
        };

        // Start auto-swiping
        handler.postDelayed(swipeRunnable, swipeDelayMillis);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop the auto-swiping when the fragment is destroyed
        handler.removeCallbacks(swipeRunnable);
        binding = null;
    }
}
