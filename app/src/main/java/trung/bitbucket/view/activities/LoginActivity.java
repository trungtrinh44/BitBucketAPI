package trung.bitbucket.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import trung.bitbucket.R;
import trung.bitbucket.interfaces.PresenterViewOps;
import trung.bitbucket.interfaces.ViewOps;
import trung.bitbucket.model.Repositories;
import trung.bitbucket.model.Repository;
import trung.bitbucket.model.UserInfo;
import trung.bitbucket.presenter.MainPresenter;


public class LoginActivity extends AppCompatActivity implements ViewOps {


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private PresenterViewOps presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        presenter = new MainPresenter(this);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter != null)
                    presenter.login(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

    }

    @Override
    public void loginSuccessful(UserInfo userInfo, String token) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USER_INFO, userInfo);
        intent.putExtra(MainActivity.TOKEN, token);
        finish();
        startActivity(intent);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void returnRepositories(Repositories repos) {

    }

    @Override
    public void repoCreated(Repository repository) {

    }

    @Override
    public void storeToken(String token) {

    }

    @Override
    public void repoDeleted(Repository repo) {

    }

    @Override
    public void deleteRepo(Repository repo) {

    }
}

