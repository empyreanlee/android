package com.example.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private EditText editTextName;
    private EditText editTextRegNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminpage);

        tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        editTextName = findViewById(R.id.editTextName);
        editTextRegNo = findViewById(R.id.editTextRegNo);


        MyPagerAdapter adapter = new MyPagerAdapter(this, editTextName, editTextRegNo);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    Fragment fragment = adapter.createFragment(position);
                    if (fragment instanceof MyPagerAdapter.TitledFragment) {
                        tab.setText(((MyPagerAdapter.TitledFragment) fragment).getTitle());
                    }
                }
        ).attach();

        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.lavender),
                ContextCompat.getColor(this, R.color.purple)
        );
    }
    public EditText getEditTextName() {
        return editTextName;
    }

    public EditText getEditTextRegNo() {
        return editTextRegNo;
    }
}