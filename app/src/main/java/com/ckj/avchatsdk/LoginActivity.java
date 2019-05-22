package com.ckj.avchatsdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName=findViewById(R.id.username);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userName.getText().toString().equals("")){
                    AVSdkClient.myselfId=userName.getText().toString();
                }
                Intent intent=new Intent(LoginActivity.this,OnlineUser.class);
                startActivity(intent);
            }
        });
    }
}
