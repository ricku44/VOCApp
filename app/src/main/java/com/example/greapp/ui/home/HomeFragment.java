package com.example.greapp.ui.home;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.greapp.Profile;
import com.example.greapp.R;
import com.example.greapp.RecyclerViewAdapter;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFrag";
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<String> btns = new ArrayList<>();
    private ArrayList<GradientDrawable> clrs = new ArrayList<>();



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView button = root.findViewById(R.id.profil);

        button.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Profile.class));
        });

        getImages(root);

        return root;
    }


    private void getImages(View root){

        Log.d(TAG, "Loop");

        ArrayList<GradientDrawable> gdx = new ArrayList<>();


        gdx.add(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x779c3636,0x77a12323}));

        gdx.add(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x772d59be,0x772d59be}));

        gdx.add(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x7739c450,0x7734b349}));

        gdx.add(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x77eaf03a,0x77dbe036}));


        gdx.get(0).setCornerRadius(50f);
        gdx.get(1).setCornerRadius(50f);
        gdx.get(2).setCornerRadius(50f);
        gdx.get(3).setCornerRadius(50f);

        ids.add("01.");
        texts.add("Quick Puzzle");
        btns.add("Play Now");
        clrs.add(gdx.get(0));

        ids.add("02.");
        texts.add("Easy Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(1));

        ids.add("03.");
        texts.add("Fun Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(2));

        ids.add("04.");
        texts.add("Hard Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(3));

        ids.add("01.");
        texts.add("Quick Puzzle");
        btns.add("Play Now");
        clrs.add(gdx.get(0));

        ids.add("02.");
        texts.add("Easy Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(1));

        ids.add("03.");
        texts.add("Fun Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(2));

        ids.add("04.");
        texts.add("Hard Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(3));

        ids.add("01.");
        texts.add("Quick Puzzle");
        btns.add("Play Now");
        clrs.add(gdx.get(0));

        ids.add("02.");
        texts.add("Easy Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(1));

        ids.add("03.");
        texts.add("Fun Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(2));

        ids.add("04.");
        texts.add("Hard Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(3));

        initRecyclerView(root);

    }


    private void initRecyclerView(View root){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), ids, texts, btns, clrs);
        recyclerView.setAdapter(adapter);
    }
}