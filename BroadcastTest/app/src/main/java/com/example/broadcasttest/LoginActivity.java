package com.example.broadcasttest;
//强制下线例子

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private Button button;
    private EditText editTextAccount;
    private EditText editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        button = (Button)findViewById(R.id.bn_login);
        editTextAccount = (EditText)findViewById(R.id.et_account);
        editTextPassword = (EditText)findViewById(R.id.et_password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = editTextAccount.getText().toString();
                String password = editTextPassword.getText().toString();

                if (account.equals("admin") && password.equals("123456")){
                    startActivity(new Intent(LoginActivity.this, TestActivity.class));
                }else {
                    Toast.makeText(LoginActivity.this,"wrong info!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
