package wtsang01.simplefitness;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * A login screen that offers login via email/password.
 */
public class WTSimpleFitnessActivity extends AppCompatActivity implements WTLoginFragment.OnFragmentInteractionListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wtlogin);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

