package com.example.administrator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Confirm extends AppCompatActivity {


    private Button buttonRegister, button;
    private EditText editTextName;
    private EditText editTextRegNo;
    private CheckBox checkboxAnimation, checkboxBlockchain, checkboxVR, checkboxCyberSecurity, checkboxMachine, checkboxSystem, checkboxCompilerDesign;
    private DatabaseReference studentsRef;
    private FirebaseAuth mAuth;
    private TextView textViewWelcome;
    private TextView textViewName;
    private TextView textViewRegNo;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonRegister = findViewById(R.id.buttonRegister);
        editTextName = findViewById(R.id.editTextName);
        editTextRegNo = findViewById(R.id.editTextRegNo);


        buttonRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String regNo = editTextRegNo.getText().toString();
            fetchStudentDetails(regNo, textViewWelcome, textViewName, textViewRegNo);

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(regNo)) {
                Toast.makeText(Confirm.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Confirm.this, StudentPanel.class);
            intent.putExtra("regNo", regNo);
            startActivity(intent);

            checkUserExists(name, regNo);
        });
    }
    private void fetchStudentDetails(String regNo, TextView textViewWelcome, TextView textViewName, TextView textViewRegNo) {
        db.collection("users")
                .whereEqualTo("regNo", regNo)  // Query using regNo
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming there's only one document matching the regNo
                                String name = document.getString("name");
                                String regNo = document.getString("regNo");
                                textViewWelcome.setText("Welcome, " + name + "!");
                                textViewName.setText("Name: " + name);
                                textViewRegNo.setText("Reg No: " + regNo);
                            }
                        } else {
                            // Handle errors
                        }
                    }
                });
    }

    private void checkUserExists(String name, String regNo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        // Query to check if a user with the given name and regNo exists
        Query query = usersCollection.whereEqualTo("name", name).whereEqualTo("regNo", regNo);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // User with the given name and regNo already exists
                    Toast.makeText(Confirm.this, "Welcome ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Confirm.this, StudentPanel.class);
                    startActivity(intent);
                } else {
                    // User does not exist, proceed with registration logic
                }
            } else {
                // Handle query failure
                Toast.makeText(Confirm.this, "Error checking user existence", Toast.LENGTH_SHORT).show();
            }
        });
    }
}





