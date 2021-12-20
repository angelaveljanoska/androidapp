package com.example.zivototekrug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zivototekrug.utility.ActivityDto;
import com.example.zivototekrug.utility.FirebaseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

public class VozrasenNewActivity extends AppCompatActivity {

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vozrasen_new);
        mAuth = FirebaseAuth.getInstance();
    }

    public void submitRequest(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        EditText name = findViewById(R.id.requestName);
        EditText description = findViewById(R.id.description);
        DatePicker datePicker = findViewById(R.id.time);
        String date = datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth();
        Boolean priority = ((CheckBox)findViewById(R.id.priority)).isChecked();
        Boolean repeatable = ((CheckBox)findViewById(R.id.repeatable)).isChecked();
        String status = "ACTIVE";
        firebaseUtils.firestoreDatabase.collection("activities").add(new ActivityDto(
                name.getText().toString(), description.getText().toString(), priority, repeatable, date, status, null, "", user.getUid()))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                        Toast.makeText(
                                getBaseContext(), "Successfully added activity!",
                                Toast.LENGTH_SHORT
                        ).show();
                        startActivity(new Intent(VozrasenNewActivity.this, VozrasenActivity.class));
                    }
                });
    }
}