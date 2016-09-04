package wtsang01.simplefitness;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import wtsang01.simplefitness.data.WTUserDAO;
import wtsang01.simplefitness.process.WTFitnessLocationService;
import wtsang01.simplefitness.viewwrapper.WTLeaderBoard;
import wtsang01.util.WTLogger;

import static wtsang01.util.WTStringUtils.urlEncode;


public class WTFitnessFragment extends Fragment {
    public static final String TAG = WTFitnessFragment.class.getSimpleName();
    public static final String USER_ID_EXTRA = "userID";
    private int mUserID;
    private TextView mServiceStatus;
    private Button mServiceAction;
    private TextView mFitnessSummary;
    private Button mLogoutBt;
    protected WTUserDAO mUserDAO;
    private Button mTweet;

    private WTLeaderBoard mLeaderBoard;

    private OnLogoutListener mListener;

    public WTFitnessFragment() {
        // Required empty public constructor
    }
    public static WTFitnessFragment newInstance(int userID) {
        WTFitnessFragment fragment = new WTFitnessFragment();
        Bundle args = new Bundle();
        args.putInt(USER_ID_EXTRA, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserID = getArguments().getInt(USER_ID_EXTRA);
        }
        mUserDAO = new WTUserDAO(getContext());
        WTLogger.l(mUserDAO.getFeetWalkToday(mUserID));
    }

    public boolean isFitnessLocationServiceRunning(){
        return WTSimpleFitnessActivity.isFitnessLocationServiceRunning(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);
        mServiceStatus = (TextView) view.findViewById(R.id.tv_service_status);
        mServiceAction = (Button) view.findViewById(R.id.bt_service_action);

        mServiceAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Intent intent= new Intent(getContext(), WTFitnessLocationService.class);
                intent.putExtra(USER_ID_EXTRA,mUserID);
                if(isFitnessLocationServiceRunning()){
                    WTLogger.l("STOPPING SERVICE");
                    getActivity().stopService(intent);
                }else{
                    WTLogger.l("STARTING SERVICE");
                    getActivity().startService(intent);
                }
                refreshServiceStatus();
            }
        });
        if(mLeaderBoard == null){
            mLeaderBoard = new WTLeaderBoard(getContext(),(WebView) view.findViewById(R.id.wv_fitness_board));
        }
        mFitnessSummary = (TextView) view.findViewById(R.id.tv_fitness_summary);
        mTweet = (Button) view.findViewById(R.id.bt_twitter);
        mTweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String tweet = "I've walked "+Float.toString(mUserDAO.getTotalDistanceInFeet(mUserID))+" feet, just for #fitness";
                String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s",
                        urlEncode(tweet));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                        intent.setPackage(info.activityInfo.packageName);
                    }
                }

                startActivity(intent);
            }
        });


        mLogoutBt = (Button) view.findViewById(R.id.bt_logout);
        mLogoutBt.setText("Logout");
        mLogoutBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isFitnessLocationServiceRunning()) {
                    WTLogger.l("STOPPING SERVICE");
                    final Intent intent = new Intent(getContext(), WTFitnessLocationService.class);
                    getActivity().stopService(intent);

                }
                mListener.onLogout();
            }
        });


        return view;
    }
    private void refreshServiceStatus(){
        if(mServiceStatus != null){
            mServiceStatus.setText("Welcome "+mUserDAO.getUserEmail(mUserID)+"\n Service is: "+(isFitnessLocationServiceRunning()? "Running":"Stopped"));
        }
        if(mServiceAction != null) {
            mServiceAction.setText("Click to "+(isFitnessLocationServiceRunning()? "STOP":"START"));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mLeaderBoard!=null){
            mLeaderBoard.refreshView();
        }
        refreshServiceStatus();
        refreshSummary();
    }

    private void refreshSummary() {

        mFitnessSummary.setText("For the past 24 hrs, you've walked "
                +Float.toString(mUserDAO.getFeetWalkToday(mUserID))
                +" FEET! \r\n"
        );
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLogoutListener) {
            mListener = (OnLogoutListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLogoutListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnLogoutListener {
        void onLogout();
    }
}
