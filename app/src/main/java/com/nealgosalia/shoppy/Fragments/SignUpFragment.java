package com.nealgosalia.shoppy.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nealgosalia.shoppy.R;

public class SignUpFragment extends Fragment {


    private Button signUpButton;
    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText password2Field;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView signInText;
    private View view;
    public static final String TAG = "SignUpFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference();

        nameField = (EditText) view.findViewById(R.id.nameField);
        emailField = (EditText) view.findViewById(R.id.emailField);
        passwordField = (EditText) view.findViewById(R.id.passwordField);

        password2Field = (EditText) view.findViewById(R.id.password2Field);
        signInText = (TextView) view.findViewById(R.id.signInText);
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.slide_up, R.animator.slide_down)
                        .replace(android.R.id.content, new SignInFragment())
                        .commit();
            }
        });

        signUpButton = (Button) view.findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nameField.getText().toString();
                String email=emailField.getText().toString();
                String password=passwordField.getText().toString();
                String password2=password2Field.getText().toString();
                if(TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(email)||
                        TextUtils.isEmpty(password)||
                        TextUtils.isEmpty(password2)
                        ) {
                    Log.d(TAG,"1");
                    Toast.makeText(getActivity(), R.string.empty_fields,
                            Toast.LENGTH_SHORT).show();
                }else if(!password.equals(password2)){
                    Log.d(TAG,"2");
                    Toast.makeText(getActivity(), R.string.password_mismatch,
                            Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG,"3");
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getActivity(), R.string.auth_failed,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), R.string.auth_successful,
                                                Toast.LENGTH_SHORT).show();
                                        onAuthenticationSucess(task.getResult().getUser());
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }

    private void onAuthenticationSucess(FirebaseUser mUser) {
        saveNewUser(mUser.getUid(), nameField.getText().toString(), emailField.getText().toString());
        signOut();
        getActivity()
                .getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_up, R.animator.slide_down)
                .replace(android.R.id.content, new SignInFragment())
                .commit();
    }

    private void saveNewUser(String userId, String name, String email) {
        mDatabase.child("users").child(userId).child("Name").setValue(name);
        mDatabase.child("users").child(userId).child("Email").setValue(email);
    }

    private void signOut() {
        mAuth.signOut();
    }

}
