package com.example.infobyte_technologies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_Activity extends AppCompatActivity {
    TextView already_have_account;
    EditText Edit_mail,Edit_password,Edit_confirm_password;
    Button btn_register;
    String emailPattern="[a-zA-Z0-9._-]+@gmail+\\.+com";
    ProgressDialog progressDialog;


    FirebaseAuth mAuth;
    FirebaseUser mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Edit_mail=findViewById(R.id.Edit_mail);
        Edit_password=findViewById(R.id.Edit_password);
        Edit_confirm_password=findViewById(R.id.Edit_confirm_password);
        btn_register=findViewById(R.id.btn_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        already_have_account=findViewById(R.id.already_have_account);






        already_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register_Activity.this,Login_Page.class));
                finish();
            }
        }); 
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();

            }
        });
        
        
    }

    private void PerformAuth() {

            String email = Edit_mail.getText().toString();
            String password = Edit_password.getText().toString();
            String confirmPassword = Edit_confirm_password.getText().toString();

            if (!email.matches(emailPattern)) {
                Edit_mail.setError("Enter Correct Email");
                Edit_mail.requestFocus();

            } else if (password.isEmpty() || password.length() < 6) {
                Edit_password.setError("Enter Proper Password");

            } else if (!password.equals(confirmPassword)) {
                Edit_confirm_password.setError("Password Not Match Both Field");
            } else {
                progressDialog.setMessage("Please Wait While Registration...");
                progressDialog.setTitle("Registration");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            sendUserToNextActivity();
                            Toast.makeText(Register_Activity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register_Activity.this, "Registration Unsuccessful,\nAccount Already Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(Register_Activity.this, Dashboard_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}