package wtsang01.simplefitness;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


/**
 * A login screen that offers login via email/password.
 */
public class WTSimpleFitnessActivity extends AppCompatActivity implements WTLoginFragment.OnFragmentLoginSuccessListener ,WTFitnessFragment.OnLogoutListener{
    Fragment mFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentByTag(WTLoginFragment.TAG);
        if (mFragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            mFragment = WTLoginFragment.newInstance();
            ft.add(android.R.id.content, mFragment, WTLoginFragment.TAG);
            ft.commit();
        }
    }

    @Override
    public void onLoginSuccess(int userID) {
        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentByTag(WTFitnessFragment.TAG);
        if (mFragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            mFragment = WTFitnessFragment.newInstance(userID);
            ft.replace(android.R.id.content, mFragment, WTFitnessFragment.TAG);

            ft.commit();
        }
    }
    @Override
    public void onLogout() {
        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentByTag(WTLoginFragment.TAG);
        if (mFragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            mFragment = WTLoginFragment.newInstance();
            ft.replace(android.R.id.content, mFragment, WTLoginFragment.TAG);
            ft.commit();
        }
    }
}

