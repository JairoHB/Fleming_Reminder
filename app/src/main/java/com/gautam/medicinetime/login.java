package com.gautam.medicinetime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import com.gautam.medicinetime.medicine.MedicineActivity;


public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    public void MostrarDialgo(View v) {
        EditText usuario = (EditText) findViewById(R.id.txtuser);
        EditText pass = (EditText) findViewById(R.id.txtpass);


        if(usuario.getText().toString().equals("Jairo") && pass.getText().toString().equals("Torres")) {

            Button btn = (Button) findViewById(R.id.btnlogin);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent (login.this, MedicineActivity.class);
                    startActivity(intent2);
                }
            });
        }
        else{
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Algo salio mal :(");
            builder.setMessage("Revisa tus credenciales");

            // add a button
            builder.setPositiveButton("OK", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
