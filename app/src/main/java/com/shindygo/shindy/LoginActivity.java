package com.shindygo.shindy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.shindygo.shindy.model.User;
import com.shindygo.shindy.utils.FontUtils;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.appTitle)
    TextView appTitle;
    @BindView(R.id.app_text)
    TextView appText;

    @BindView(R.id.fb_verfication_txt)
    TextView fbVerficationTxt;
    LoginButton loginButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    //   private ProfileTracker profileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        FontUtils.setFont(appTitle, FontUtils.Roboto_Light);
        FontUtils.setFont(appText, FontUtils.Roboto_Light);
        //FontUtils.setFont(fbLoginBtn, FontUtils.Roboto_Medium);
        FontUtils.setFont(fbVerficationTxt, FontUtils.Roboto_Light);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                if (newToken != null)
                    newToken.getDeclinedPermissions();

            }
        };

      /*  profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            //    nextActivity(newProfile);
            }
        };*/
        accessTokenTracker.startTracking();
        /*     profileTracker.startTracking();*/

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList( "public_profile", "email"));
        loginButton.registerCallback(callbackManager, callback);


    }


    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("user_birthday","email", "user_location"));

        //    Set<String> per = loginResult.getAccessToken().getDeclinedPermissions();
        //    Set<String> paer = loginResult.getAccessToken().getPermissions();
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());
                            String email = "";

                            try {
                                email = object.getString("email");
                            } catch (Exception e) {
                                email = "noemail@gmail.com";
                            }
                            //  String birthday = object.getString("birthday"); // 01/31/1980 format

                            nextActivity(Profile.getCurrentProfile(), email, "");
                             /*   LoginManager.getInstance().logInWithReadPermissions(
                                        LoginActivity.this,
                                        Arrays.asList("user_birthday","email","user_location"));*/
                        }

                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,birthday");
            request.setParameters(parameters);
            request.executeAsync();


        }

        @Override
        public void onCancel() {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(FacebookException e) {
            progressBar.setVisibility(View.GONE);
        }
    };

    private void nextActivity(Profile profile, String email, String birth) {
        if (profile != null) {

            SharedPreferences sharedPref = getSharedPreferences("set", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name", profile.getName());
            editor.putString("fbid", profile.getId());
            String photo = profile.getProfilePictureUri(200, 200).toString();
            editor.putString("url",photo );
            editor.putString("photo",photo);

            editor.apply();
            Api api = new Api(this);
            User user = new User(profile.getId(), profile.getName(), email);

            user.setPhoto(photo);
            api.checkUser(user, new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    Log.v("login", response.message());
                    //no data

                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
/*
            main.putExtra("name", profile.getFirstName());
            main.putExtra("surname", profile.getLastName());
            main.putExtra("imageUrl", profile.getProfilePictureUri(200,200).toString());
*/
                    main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    finish();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    t.printStackTrace();;
                    Toast.makeText(LoginActivity.this, "Login Failed, please try again", Toast.LENGTH_LONG).show();;
                }
            });
        }
        else
        {
            LoginManager.getInstance().logOut();
//            loginButton.callOnClick();
        }

    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        //   Profile profile = Profile.getCurrentProfile();
        // nextActivity(profile);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        //profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }

    public void OnClickFaceBookLogin(View view) {
        loginButton.callOnClick();
        progressBar.setVisibility(View.VISIBLE);
    }
}
