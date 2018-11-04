package com.gautam.medicinetime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gautam.medicinetime.data.source.local.MedicineDBHelper;
import com.gautam.medicinetime.medicine.MedicineActivity;
import com.gautam.medicinetime.utils.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class login extends AppCompatActivity {
    MedicineDBHelper db;
    EditText username, password;
    Button btninicio;
    RadioButton rbdoc, rbpac;
    LoginButton loginButton;
    private CallbackManager callbackManager;
    private Session session;
    String emailFac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        callbackManager = CallbackManager.Factory.create();
        initViews();

        session = new Session(this);
        db= new MedicineDBHelper(this);
        username=(EditText)findViewById(R.id.txtuser);
        password=(EditText)findViewById(R.id.txtpass);
        btninicio=(Button)findViewById(R.id.btnlogin);
        rbdoc=(RadioButton)findViewById(R.id.rbDoctor);
        rbpac=(RadioButton)findViewById(R.id.rbPaciente);

        //Boton normal
        btninicio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usn = username.getText().toString();
                String pas = password.getText().toString();
                if(rbdoc.isChecked()){
                    String login1 = db.login_doc(usn, pas);
                    //username=jairo27 pass=123
                    if(!login1.equals("0")){
                        Toast.makeText(getApplicationContext(),"Incio Sesión Con Exito", Toast.LENGTH_SHORT).show();
                        session.setusername(usn);
                        session.settype("1");
                        session.setid(login1);
                        PreferenceUtils.saveuser(usn, getApplicationContext());
                        PreferenceUtils.savePassword(pas, getApplicationContext());
                        Intent intent = new Intent(login.this, MedicineActivity.class);
                        startActivity(intent);
                    }
                    else if (login1.equals("0")){
                        Toast.makeText(getApplicationContext(),"Contraseña o Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(rbpac.isChecked()){
                    String login1 = db.login_pac(usn, pas);
                    //username=jairo27 pass=123
                    if(!login1.equals("0")){
                        Toast.makeText(getApplicationContext(),"Incio Sesión Con Exito", Toast.LENGTH_SHORT).show();
                        session.setusername(usn);
                        session.settype("2");
                        session.settel(login1);
                        PreferenceUtils.saveuser(usn, getApplicationContext());
                        PreferenceUtils.savePassword(pas, getApplicationContext());
                        Intent intent = new Intent(login.this, MedicineActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else if (login1.equals("0")){
                        Toast.makeText(getApplicationContext(),"Contraseña o Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Inicio Facebook
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                requestEmail(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Se cancelo el Inicio", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),"Error al conectar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registeractivity(View v){
        TextView register = (TextView) findViewById(R.id.txtviewreg);
        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(login.this, registermed.class);
                startActivity(intent);
            }
        });
    }

    private void initViews(){
        PreferenceUtils utils = new PreferenceUtils();
        if (utils.getuser(this) != null || AccessToken.getCurrentAccessToken() != null){
            Intent intent = new Intent(login.this, MedicineActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{

        }
    }

    private void goMainScreen(){
        Intent intent = new Intent(this, MedicineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestEmail(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(getApplicationContext(), response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    String email = object.getString("email");
                    setEmail(email);
                    Boolean LogFac = db.chkemail(emailFac);
                    if(LogFac == false) {
                        goMainScreen();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setEmail(String email) {
        emailFac = email;
    }
}
