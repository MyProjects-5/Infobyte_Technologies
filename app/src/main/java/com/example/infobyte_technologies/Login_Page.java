package com.example.infobyte_technologies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Page extends AppCompatActivity {
    TextView create_new_account;
    Button btn_login;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    EditText Edit_mail,Edit_password;
    String emailPattern="[a-zA-Z0-9._-]+@gmail+\\.+com";
    ProgressDialog progressDialog;

TextView forgot_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        create_new_account = findViewById(R.id.create_new_account);
        btn_login=findViewById(R.id.btn_login);
        Edit_mail=findViewById(R.id.Edit_mail);
        Edit_password=findViewById(R.id.Edit_password);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        forgot_password=findViewById(R.id.forgot_password);





        create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Page.this, Register_Activity.class));
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Login_Page.this);
                View dialogView=getLayoutInflater().inflate(R.layout.activity_forgot_password,null);
                EditText emailBox=dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog=builder.create();


                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail=emailBox.getText().toString();

                        if(TextUtils.isEmpty(userEmail)&& !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(Login_Page.this,"Enter your registered email id",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login_Page.this,"Check your email",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(Login_Page.this,"Unable to send,failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() !=null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

    }

    private void performLogin() {
        String email=Edit_mail.getText().toString();
        String password=Edit_password.getText().toString();

        if(!email.matches(emailPattern))
        {
            Edit_mail.setError("Enter Correct Email");
            Edit_mail.requestFocus();

        }
        else if(password.isEmpty() || password.length()<6)
        {
            Edit_password.setError("Enter Proper Password");

        }

        else {
            progressDialog.setMessage("Please Wait While Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Login_Page.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Login_Page.this, "Login Unsuccessful,\nAccount Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }
    private void sendUserToNextActivity() {
        Intent intent=new Intent(Login_Page.this,Dashboard_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}


















