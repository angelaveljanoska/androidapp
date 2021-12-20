package com.example.zivototekrug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zivototekrug.utility.FirebaseUtils;
import com.example.zivototekrug.utility.RegisterDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String TAG = "Login";
    private boolean register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        checkView();
        Spinner dropdown = findViewById(R.id.role);
        ArrayList roles = new ArrayList(Arrays.asList(new String[]{"vozrasen", "mlad", "organizator"}));
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,roles);
        dropdown.setAdapter(adapter);
    }

    private void updateUI(FirebaseUser user) {

    }
    private void checkView(){
        EditText name = findViewById(R.id.name);
        EditText surname = findViewById(R.id.surname);
        EditText phone = findViewById(R.id.phone);
        Spinner role = findViewById(R.id.role);
        Button button = findViewById(R.id.submit);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);
        if (register) {
            name.setVisibility(View.VISIBLE);
            surname.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            role.setVisibility(View.VISIBLE);
            button.setText("Register");
            registerButton.setBackgroundColor(Color.parseColor("#6a8894"));
            loginButton.setBackgroundColor(Color.parseColor("#163440"));
        }
        else {
            name.setVisibility(View.INVISIBLE);
            surname.setVisibility(View.INVISIBLE);
            phone.setVisibility(View.INVISIBLE);
            role.setVisibility(View.INVISIBLE);
            button.setText("Login");
            registerButton.setBackgroundColor(Color.parseColor("#163440"));
            loginButton.setBackgroundColor(Color.parseColor("#6a8894"));
        }
    }

    public void onSubmit(View view) {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if(register) {
            String name = ((EditText) findViewById(R.id.name)).getText().toString();
            String surname = ((EditText) findViewById(R.id.surname)).getText().toString();
            String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
            String role = ((Spinner) findViewById(R.id.role)).getSelectedItem().toString();
            createAccount(email, password, name, surname, phone, role);
        }
        else {
            login(email, password);
        }
    }

    public void onChangeView(View view) {
            Button button = findViewById(view.getId());
            Log.d(TAG, (String) button.getText());
            register = button.getText().equals("Register");
            checkView();
    }
    private void createAccount (String email, String password, String name, String surname, String phone, String role) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Authentication is successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                new FirebaseUtils().firestoreDatabase.collection("users").document(user.getUid()).set(new RegisterDto(name, surname, phone, role));
                            }
                            register = false;
                            checkView();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Logged in successfully");
                            Toast.makeText(LoginActivity.this, "Login is successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            redirect();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Login:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void redirect() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            new FirebaseUtils().firestoreDatabase.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        String role = (String) task.getResult().getData().get("role");
                        Intent intent = new Intent();

                        if(role.equals("vozrasen")) {
                            intent.setClass(LoginActivity.this, VozrasenActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Feature still not implemented.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}