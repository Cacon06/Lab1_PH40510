package com.example.lab1_ph40510;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextConfirmPass;
    private Button buttonSinUp;
    private TextView txtLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPass = findViewById(R.id.editTextConfirmPass);
        buttonSinUp = findViewById(R.id.buttonSinUp);
        txtLogin = findViewById(R.id.txtLogin);
        mAuth = FirebaseAuth.getInstance();

        buttonSinUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String rppassword = editTextConfirmPass.getText().toString();
                if(email.equals("")||password.equals("")||rppassword.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(rppassword)){
                    Toast.makeText(SignUpActivity.this,    "mật khẩu không khớp nhau!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(SignUpActivity.this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6 || !Character.isUpperCase(password.charAt(0))) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu phải có ít nhất 6 kí tự và viết hoa chữ cái đầu tiên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent in = new Intent(SignUpActivity.this,LoginEmailActivity.class);
                                    in.putExtra("email",email);
                                    in.putExtra("password",password);
                                    startActivity(in);
                                    Toast.makeText(SignUpActivity.this, "Đăng Kí Thành Công!",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(SignUpActivity.this, "Đăng kí thất bại.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SignUpActivity.this, LoginEmailActivity.class);
                startActivity(in);
            }
        });
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}