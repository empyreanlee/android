package com.example.administrator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Email;
    private EditText Password;
    private Button button;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        button = findViewById(R.id.loginButton);

        button.setOnClickListener(v -> signInUser());
    }

    private void signInUser() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String userId = mAuth.getCurrentUser().getUid();

        // Query Firestore to check if the student has registered for courses
        DocumentReference studentDocRef = db.collection("users").document(userId);

        studentDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // The student has registered for courses
                    // Redirect to the page displaying student info and courses
                    Intent intent = new Intent(SignInActivity.this, StudentPanel.class);
                    startActivity(intent);
                } else {
                    // If not registered, proceed with email/password sign-in
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, signInTask -> {
                                if (signInTask.isSuccessful()) {
                                    // Sign-in success, update UI or navigate to the next screen
                                    Intent intent = new Intent(SignInActivity.this, AdminPage.class);
                                    startActivity(intent);

                                } else {
                                    // If sign-in fails, display a message to the user.
                                    Toast.makeText(SignInActivity.this, "Sign-in failed: " + signInTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
