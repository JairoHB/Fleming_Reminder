package com.gautam.medicinetime;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.gautam.medicinetime.Session;
import com.gautam.medicinetime.data.source.local.MedicineDBHelper;

public class registerpac extends AppCompatActivity {

    MedicineDBHelper db;
    EditText nom, tel, username, password;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_pac);
        session = new Session(this);

        db= new MedicineDBHelper(this);
        nom=(EditText)findViewById(R.id.txtregpacnom);
        tel=(EditText)findViewById(R.id.txtregpactel);
        username=(EditText)findViewById(R.id.txtregpacuser);
        password=(EditText)findViewById(R.id.txtregpacpass);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addpac_task_done);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom1 = nom.getText().toString();
                String tel1 = tel.getText().toString();
                String usn = username.getText().toString();
                String pas = password.getText().toString();
                String medico = session.getid();
                if(usn.equals("") || nom1.equals("") || pas.equals("") || tel1.equals("")){
                    Toast.makeText(getApplicationContext(),"No dejes ningun campo vacio", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), medico, Toast.LENGTH_SHORT).show();
                    //user jaz27 pass 123 DOCTOR
                    Boolean chkeusername = db.chkusernamepac(usn);
                    if(chkeusername==true){
                        Boolean insert1 = db.insertpacientes(nom1, usn, tel1, pas, medico);
                        if (insert1 == true) {
                            Toast.makeText(getApplicationContext(), "Registro Con Exito", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Usuario Registrado, intenta otro", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
