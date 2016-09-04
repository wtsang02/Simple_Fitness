package wtsang01.simplefitness.view;

import android.content.Context;

import android.webkit.WebView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wtsang01.simplefitness.data.WTLocation;

import wtsang01.simplefitness.data.WTUserDAO;

import static android.R.attr.data;
import static com.google.android.gms.analytics.internal.zzy.b;
import static com.google.android.gms.analytics.internal.zzy.g;
import static com.google.android.gms.analytics.internal.zzy.z;
import static java.util.Collections.sort;


/**
 * Created by wtsang01 on 9/3/2016.
 * Building this with HTML and rending in Webview isn't the best solution, but its quick and fast.. :).
 */
public class WTLeaderBoard {
    private WebView mLeaderBoard;
    private WTUserDAO mUserDAO;
    private Context mContext;
    public WTLeaderBoard(Context context,WebView webView){
        mLeaderBoard = webView;
        mContext = context;
    }

    public void refreshView(){

        updateLeaderBoard(generateLeaderBoardEntries());
    }

    /**
     * Basically just rebuild the html table.
     * @param list
     */
    public void updateLeaderBoard(List<LeaderBoardEntry> list){
        StringBuilder leaderBoardHtml =new StringBuilder("<HTML><head><style>.datagrid table { border-collapse: collapse; text-align: left; width: 100%; } .datagrid {font: normal 12px/150% Arial, Helvetica, sans-serif; background: #fff; overflow: hidden; border: 1px solid #006699; -webkit-border-radius: 3px; -moz-border-radius: 3px; border-radius: 3px; }.datagrid table td, .datagrid table th { padding: 3px 10px; }.datagrid table thead th {background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #006699), color-stop(1, #00557F) );background:-moz-linear-gradient( center top, #006699 5%, #00557F 100% );filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#006699', endColorstr='#00557F');background-color:#006699; color:#FFFFFF; font-size: 15px; font-weight: bold; border-left: 1px solid #0070A8; } .datagrid table thead th:first-child { border: none; }.datagrid table tbody td { color: #00557F;  border-left: 1px solid #E1EEF4;font-size: 12px;font-weight: normal; }.datagrid table tbody .alt td { background: #E1EEf4; color: #00557F; }.datagrid table tbody td:first-child { border-left: none; }.datagrid table tbody tr:last-child td { border-bottom: none; }</style></head>" +
                "<div class=\"datagrid\"><table><thead><tr><th>Rank</th><th>User</th><th>Total Distance (FT)</th></tr></thead><tbody>"
                );
        Collections.sort(list, Collections.reverseOrder());

        for(int i=0 ;i<list.size();i++){
            String leaderBoardEntryHtml ="<tr "+ (i%2 ==0  ? "":"class=alt")+"><td>"+Integer.toString(i+1)
                    +"</td><td>"+list.get(i).getUserName()+"</td><td>"+Float.toString(list.get(i).getTotalDistance())+"</td></tr>";
            leaderBoardHtml.append(leaderBoardEntryHtml);
        }
        leaderBoardHtml.append("</tbody></table></div></HTML>");
        mLeaderBoard.loadDataWithBaseURL(null, leaderBoardHtml.toString(), "text/html", "utf-8", null);
    }
    public List<LeaderBoardEntry> generateLeaderBoardEntries(){
        if(mUserDAO==null){
            mUserDAO = new WTUserDAO(mContext);
        }
        List<Integer> userIDList= mUserDAO.getAllUserId();
        List<LeaderBoardEntry> entries = new ArrayList<>();
        for(int userID:userIDList){
            entries.add(new LeaderBoardEntry(mUserDAO.getUserEmail(userID)
                    , WTLocation.getTotalDistance( mUserDAO.getLocations(userID))));
        }
        return entries;
    }

    class LeaderBoardEntry implements Comparable<LeaderBoardEntry>{
        private String mUserName;
        private float mTotalDistance;

        public String getUserName() {
            return mUserName;
        }

        public void setUserName(String userName) {
            mUserName = userName;
        }

        public float getTotalDistance() {
            return mTotalDistance;
        }

        public void setTotalDistance(float totalDistance) {
            mTotalDistance = totalDistance;
        }

        @Override
        public int compareTo(LeaderBoardEntry anotherEntry) {
            return Float.compare(mTotalDistance,anotherEntry.getTotalDistance());
        }

        LeaderBoardEntry(String userName,float totalDistance){
            mUserName = userName;
            mTotalDistance=totalDistance;
        }
    }
}
