package com.example.administrator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Fragment1 extends Fragment implements MyPagerAdapter.TitledFragment {

    private Button buttonRegister, button;
    private EditText editTextName;
    private EditText editTextRegNo;
    private CheckBox Aftereffects, ComputerVision, NeuralNetworks ,LLM, QuantumComputing, Robotics, TuringThesis ;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for Fragment2
        View view = inflater.inflate(R.layout.fragment1, container, false);

        FirebaseApp.initializeApp(requireContext()); // Use requireContext() instead of this

        mAuth = FirebaseAuth.getInstance();


        buttonRegister = view.findViewById(R.id.buttonRegister);
        editTextName = ((AdminPage) requireActivity()).getEditTextName();
        editTextRegNo = ((AdminPage) requireActivity()).getEditTextRegNo();

        Aftereffects = view.findViewById(R.id.AfterEffects);
        ComputerVision = view.findViewById(R.id.ComputerVision);
        NeuralNetworks = view.findViewById(R.id.NeuralNetworks);
        LLM = view.findViewById(R.id.LLM);
        QuantumComputing = view.findViewById(R.id.QuantumComputing);
        Robotics = view.findViewById(R.id.Robotics);
        TuringThesis = view.findViewById(R.id.TuringThesis);

        buttonRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String regNo = editTextRegNo.getText().toString();

            ArrayList<String> selectedCourses = new ArrayList<>();

            if (Aftereffects.isChecked()) selectedCourses.add("After Effects");
            if (ComputerVision.isChecked()) selectedCourses.add("Computer Vision");
            if (NeuralNetworks.isChecked()) selectedCourses.add("Neural Networks");
            if (LLM.isChecked()) selectedCourses.add("LLM");
            if (QuantumComputing.isChecked()) selectedCourses.add("Quantum Computing");
            if (Robotics.isChecked()) selectedCourses.add("Robotics");
            if (TuringThesis.isChecked()) selectedCourses.add("Turing Thesis");


            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(regNo) || selectedCourses.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = mAuth.getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersCollection = db.collection("users");
            DocumentReference studentDocRef = usersCollection.document(userId);
            CollectionReference semestersCollection = studentDocRef.collection("semesters");

            Student student = new Student(name, regNo);

            studentDocRef.set(student)
                    .addOnSuccessListener(aVoid -> {
                        // Document successfully written
                        Toast.makeText(getActivity(), "Student information added for " + name, Toast.LENGTH_SHORT).show();

                        SemesterData semesterData = new SemesterData(1, selectedCourses);

                        semestersCollection.document("semester")
                                .set(semesterData)
                                .addOnSuccessListener(aVoid1 -> {
                                    // Document successfully written
                                    Toast.makeText(getActivity(), "Registration successful for Semester 1. Proceed to Sem 2", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                    Toast.makeText(getActivity(), "Error registering for Semester 1", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(getActivity(), "Error adding student information", Toast.LENGTH_SHORT).show();
                    });
        });

        return view;
    }
    @Override
    public CharSequence getTitle() {
        return "Semester 1";
    }
}
