package com.example.huynh.fablix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private CoordinatorLayout loginLayout;
    private TextView loginTextView;
    private TextView httpResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginTextView = findViewById(R.id.loginTextView);
        loginLayout = findViewById(R.id.loginLayout);
        httpResponse = findViewById(R.id.http_response);

        loginTextView.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.fade_in));
        loginLayout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.slide_up));
    }

    /**
     *Establish connection to server Tomcat
     *Perform some input validations and proceed to authentication
     *@param v button to click to establish connection
     */
    public void connectToTomcat(final View v) {
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        boolean valid1 = true;
        boolean valid2 = false;

        if (username.isEmpty()) {
            usernameEditText.setError(getText(R.string.empty_username));
            valid1 = false;
        }
        if (password.isEmpty()) {
            passwordEditText.setError(getText(R.string.empty_password));
            valid1 = false;
        }
        if (valid1) {
            // no user is logged in, so we must connect to the server

            // Use the same network queue across our application
            final RequestQueue queue = NetworkManager.sharedManager(this).queue;

            // 10.0.2.2 is the host machine when running the android emulator
            final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, "https://"+Utils.IP+":8443/project4/api/username",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("username.reponse", response);
                           // ((TextView) findViewById(R.id.http_response)).setText(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("username.error", error.toString());
                        }
                    }
            );


            final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://"+Utils.IP+":8443/project4/api/login",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("login.success", response);
                            if(response.contains("\"status\":\"fail\""))
                                httpResponse.setText(response.substring(47,response.length()-2));
                            else {
                                usernameEditText.setEnabled(false);
                                passwordEditText.setEnabled(false);
                                v.setEnabled(false);
                                findViewById(R.id.loginProgressBar).setVisibility(View.VISIBLE);
                                httpResponse.setText("Success");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(LoginActivity.this,MovieListActivity.class));
                                        LoginActivity.this.finish();
                                    }
                                },100);
                            }
                            // Add the request to the RequestQueue.
                            queue.add(afterLoginRequest);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("login.error", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    // Post request form data
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("email", username);
                    params.put("password", password);

                    return params;
                }
            };

            // !important: queue.add is where the login request is actually sent
            queue.add(loginRequest);

        }

        if (!valid1) {
            loginLayout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.error_shake));
        }
    }
}
