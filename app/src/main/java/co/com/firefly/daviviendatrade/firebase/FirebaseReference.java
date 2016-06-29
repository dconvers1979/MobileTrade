package co.com.firefly.daviviendatrade.firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.com.firefly.daviviendatrade.firebase.model.User;

/**
 * Created by toshiba on 28/06/2016.
 */
public class FirebaseReference {

    private static FirebaseReference instance = new FirebaseReference();

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    String email;
    String password;

    private AppCompatActivity context;

    private FirebaseReference(){

    }

    public static FirebaseReference getInstance(){
        if(instance==null){
            instance = new FirebaseReference();
        }

        return instance;
    }

    public boolean loginFirebase(String email, String password){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        this.email =  email;
        this.password = password;

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }

        return true;
    }

    public void signIn(AppCompatActivity context) {
        if (email==null || email.equals("")) {
            return;
        }

        if (password==null || password.equals("")) {
            return;
        }

        this.context = context;


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(FirebaseReference.this.context.getApplicationContext(), "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUp(AppCompatActivity context) {

        if (email==null || email.equals("")) {
            return;
        }

        if (password==null || password.equals("")) {
            return;
        }

        this.context = context;


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(FirebaseReference.this.context.getApplicationContext(), "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }



    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]



}
