package com.example.administrator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class StudentPanel extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private TextView textViewWelcome;
    private TextView textViewName;
    private TextView textViewRegNo;
    private LinearLayout linearLayoutCourses;
    private Button buttonViewCourseList;
    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminsc);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewName = findViewById(R.id.textViewName);
        textViewRegNo = findViewById(R.id.textViewRegNo);
        buttonViewCourseList = findViewById(R.id.buttonViewCourseList);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);

        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(new ArrayList<>()); // Initialize with an empty list
        recyclerViewCourses.setAdapter(courseAdapter);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userDetails = "UID: " + user.getUid() + ", Email: " + user.getEmail();
            Toast.makeText(getApplicationContext(), userDetails, Toast.LENGTH_LONG).show();
            String userId = getIntent().getStringExtra("userId");
            fetchStudentDetails(user.getUid());
                buttonViewCourseList.setOnClickListener(v -> {
                    fetchCourseList(userId, 1);
                    // You can navigate to the course list activity or implement the desired behavior here
                    // Pass the necessary information to the next activity, if needed
                });
            } else {
                Toast.makeText(StudentPanel.this, "error UId", Toast.LENGTH_SHORT).show();
            }
        }
    private void fetchStudentDetails(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String name = document.getString("name");
                            String regNo = document.getString("regNo");
                            textViewWelcome.setText("Welcome, " + name + "!");
                            textViewName.setText("Name: " + name);
                            textViewRegNo.setText("Reg No: " + regNo);
                        }
                    } else {
                        // Handle errors
                    }
                });
    }
    private void fetchCourseList(String userId, int semester) {
        db.collection("users").document(userId)
                .collection("semesters").document("semester" + semester)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot semesterDocument = task.getResult();
                        if (semesterDocument != null && semesterDocument.exists()) {
                            List<String> registeredCourses = (List<String>) semesterDocument.get("registeredcourses");

                            if (registeredCourses != null) {
                                // Update the CourseAdapter with the retrieved courses
                                courseAdapter.updateCourses(registeredCourses);
                            }
                            else {
                                Toast.makeText(StudentPanel.this, "No registered courses found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(StudentPanel.this, "Semester document does not exist.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StudentPanel.this, "Sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


