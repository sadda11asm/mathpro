package ca.javajeff.mathpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

/**
 * Created by Саддам on 05.08.2017.
 */

public class LoginActivity extends AppCompatActivity {
    public static int APP_REQUEST_CODE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            launchAccountActivity();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                String toastMeassage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMeassage, Toast.LENGTH_LONG).show();
            } else if (loginResult.getAccessToken() != null) {
                launchAccountActivity();
            }
        }
    }

    private void onLogin(final LoginType loginType) {
        final Intent intent = new Intent (this, AccountKitActivity.class);

        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );
        final AccountKitConfiguration configuration = configurationBuilder.build();

        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


    public void onPhoneLogin (View view) {
        onLogin(LoginType.PHONE);
    }

    public void onEmailLogin (View view) {
        onLogin(LoginType.EMAIL);
    }

    public void onSkip (View view) { launchAccountActivity();}

    private void launchAccountActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
