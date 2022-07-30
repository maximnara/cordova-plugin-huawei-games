package io.luzh.cordova.plugin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Intent;

import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.api.HuaweiMobileServicesUtil;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.jos.JosAppsClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.player.Player;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class HuaweiGamesPlugin extends CordovaPlugin {

    private static final String TAG = "HUAWEI_GAMES";

    private static final int RC_AUTH_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final int RC_LEADERBOARDS_UI = 9005;
    private static final int RC_SAVED_GAMES = 9009;
    private static final int RC_SHOW_PROFILE = 9010;
    private static final int RC_SHOW_PLAYER_SEARCH = 9011;
    private static final int SHOW_SHARING_FRIENDS_CONSENT = 1111;

    private static final String EVENT_LOAD_SAVED_GAME_REQUEST = "loadSavedGameRequest";
    private static final String EVENT_SAVE_GAME_REQUEST = "saveGameRequest";
    private static final String EVENT_SAVE_GAME_CONFLICT = "saveGameConflict";
    private static final String EVENT_FRIENDS_LIST_REQUEST_SUCCESSFUL = "friendsListRequestSuccessful";

    private static final int ERROR_CODE_HAS_RESOLUTION = 1;
    private static final int ERROR_CODE_NO_RESOLUTION = 2;

    private RelativeLayout bannerContainerLayout;
    private CordovaWebView cordovaWebView;
    private ViewGroup parentLayout;
    private CallbackContext loginCallbackContext;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (action.equals("login")) {
            this.loginAction(args, callbackContext);
            return true;
        }

        else if (action.equals("logout")) {
            this.logoutAction(callbackContext);
            return true;
        }

//        else if (action.equals("unlockAchievement")) {
//            this.unlockAchievementAction(args.getString(0), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("incrementAchievement")) {
//            this.incrementAchievementAction(args.getString(0), args.getInt(1), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("showAchievements")) {
//            this.showAchievementsAction(callbackContext);
//            return true;
//        }
//
//        else if (action.equals("revealAchievement")) {
//            this.revealAchievementAction(args.getString(0), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("setStepsInAchievement")) {
//            this.setStepsInAchievementAction(args.getString(0), args.getInt(1), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("updatePlayerScore")) {
//            this.updatePlayerScoreAction(args.getString(0), args.getInt(1), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("loadPlayerScore")) {
//            this.loadPlayerScoreAction(args.getString(0), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("showLeaderboard")) {
//            this.showLeaderboardAction(args.getString(0), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("showAllLeaderboards")) {
//            this.showAllLeaderboardsAction(callbackContext);
//            return true;
//        }
//
//        else if (action.equals("showSavedGames")) {
//            this.showSavedGamesAction(args.getString(0), args.getBoolean(1), args.getBoolean(2), args.getInt(3), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("saveGame")) {
//            this.saveGameAction(args.getString(0), args.getString(1), args.getJSONObject(2), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("loadGameSave")) {
//            this.loadGameSaveAction(args.getString(0), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("getFriendsList")) {
//            this.getFriendsListAction(callbackContext);
//            return true;
//        }
//
//        else if (action.equals("showAnotherPlayersProfile")) {
//            this.showAnotherPlayersProfileAction(args.getString(0), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("showPlayerSearch")) {
//            this.showPlayerSearchAction(callbackContext);
//            return true;
//        }
//
//        else if (action.equals("getPlayer")) {
//            this.getPlayerAction(args.getString(0), args.getBoolean(1), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("getCurrentPlayerStats")) {
//            this.getCurrentPlayerStatsAction(callbackContext);
//            return true;
//        }
//
//        else if (action.equals("incrementEvent")) {
//            this.incrementEventAction(args.getString(0), args.getInt(1), callbackContext);
//            return true;
//        }
//
//        else if (action.equals("getAllEvents")) {
//            this.getAllEventsAction(callbackContext);
//            return true;
//        }
//
//        else if (action.equals("getEvent")) {
//            this.getEventAction(args.getString(0), callbackContext);
//            return true;
//        }

        return false;
    }

    /** --------------------------------------------------------------- */
    @Override
    public void initialize(final CordovaInterface cordova, final CordovaWebView webView) {
        cordovaWebView = webView;
        super.initialize(cordova, webView);
        HuaweiMobileServicesUtil.setApplication(this.cordova.getActivity().getApplication());
        JosAppsClient appsClient = JosApps.getJosAppsClient(this.cordova.getActivity());
        appsClient.init();
        Log.d(TAG, "Init success");
    }

    /** ----------------------- UTILS --------------------------- */

    private void emitWindowEvent(final String event) {
        final CordovaWebView view = this.webView;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl(String.format("javascript:cordova.fireWindowEvent('%s');", event));
            }
        });
    }

    private void emitWindowEvent(final String event, final JSONObject data) {
        final CordovaWebView view = this.webView;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl(String.format("javascript:cordova.fireWindowEvent('%s', %s);", event, data.toString()));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        HuaweiGamesPlugin self = this;
        if (intent != null) {
            if (requestCode == RC_AUTH_UI) {
                Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(intent);
                if (authAccountTask.isSuccessful()) {
                    // The sign-in is successful, and the user's ID information and authorization code are obtained.
                    AuthAccount authAccount = authAccountTask.getResult();
                    Log.d(TAG, "ServerAuthCode:" + authAccount.getAuthorizationCode());
                    getGamePlayer();
                } else {
                    // The sign-in failed.
                    Log.d(TAG, "Sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
                    loginCallbackContext.error(TAG + ": Sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
                }
            }
        }
    }

    /**
     * Login
     */
    private void loginAction(JSONArray args, final CallbackContext callbackContext) {
        loginCallbackContext = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode().createParams();
                Task<AuthAccount> authAccountTask = AccountAuthManager.getService(cordova.getActivity(), authParams).silentSignIn();
                authAccountTask.addOnSuccessListener(
                    new OnSuccessListener<AuthAccount>() {
                        @Override
                        public void onSuccess(AuthAccount authAccount) {
                            Log.d(TAG, "signIn success");
                            Log.d(TAG, "display:" + authAccount.getDisplayName());
                            //SignInCenter.get().updateAuthAccount(authAccount);
                            getGamePlayer();   // Obtain player information.
                        }
                    })
                    .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    Log.d(TAG, "signIn failed:" + apiException.getStatusCode());
                                    Log.d(TAG, "start getSignInIntent");
                                    runExplicitLogin();
                                } else {
                                    Log.d(TAG, "Some error happened while login.");
                                    loginCallbackContext.error(TAG + ": Some error happened while login.");
                                }
                            }
                        });
            }
        });
    }

    public void runExplicitLogin() {
        AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode().createParams();
        AccountAuthService service = AccountAuthManager.getService(cordova.getActivity(), authParams);
        cordova.setActivityResultCallback(this);
        cordova.getActivity().startActivityForResult(service.getSignInIntent(), RC_AUTH_UI);
    }

    public void getGamePlayer() {
        // Call the getPlayersClient method for initialization.
        PlayersClient client = Games.getPlayersClient(cordova.getActivity());
        // Obtain player information.
        Task<Player> task = client.getGamePlayer();
        task.addOnSuccessListener(new OnSuccessListener<Player>() {
            @Override
            public void onSuccess(Player player) {
                String accessToken = player.getAccessToken();
                String displayName = player.getDisplayName();
                String unionId = player.getUnionId();
                String openId = player.getOpenId();
                // The player information is successfully obtained. Your game is started after accessToken is verified.
                getClientTokenInfo(accessToken, displayName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    // Failed to obtain player information. Rectify the fault based on the result code.
                    if (7400 == ((ApiException) e).getStatusCode()||7018 == ((ApiException) e).getStatusCode()) {
                        // 7400 indicates that the user has not signed the joint operations agreement. You need to continue to call the init API.
                        // 7018 indicates that initialization fails. You need to continue to call the init API.
                        //init();
                        Log.d(TAG, "User declined explicit auth. Call login method once again.");
                        loginCallbackContext.error(TAG + ": User declined explicit auth. Call login method once again.");
                    } else {
                        Log.d(TAG, "Error while getting game player.");
                        loginCallbackContext.error(TAG + ": Error while getting game player.");
                    }
                }
            }
        });
    }

    private void getClientTokenInfo(String accessToken, String displayName) {
        cordova.getThreadPool().execute(() -> {
            String urlScope = "https://oauth-api.cloud.huawei.com/rest.php";
            HttpURLConnection client = null;
            try {
                URL url = new URL(urlScope);
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("nsp_svc=");
                stringBuffer.append("huawei.oauth2.user.getTokenInfo");
                stringBuffer.append("&open_id=");
                stringBuffer.append("OPENID");
                stringBuffer.append("&access_token=");
                stringBuffer.append(URLEncoder.encode(accessToken, "UTF-8"));

                client.setDoOutput(true);

                OutputStreamWriter streamWriter = new OutputStreamWriter(client.getOutputStream());
                streamWriter.write(stringBuffer.toString());
                streamWriter.flush();

                if (client.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader streamReader = new InputStreamReader(client.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String response = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((response = bufferedReader.readLine()) != null) {
                        stringBuilder.append(response + "\n");
                    }
                    bufferedReader.close();

                    JSONObject json = new JSONObject(stringBuilder.toString());
                    if (!json.isNull("project_id")) {
                        try {
                            json.put("display_name", displayName);
                            loginCallbackContext.success(json);
                        } catch (Exception e) {
                            Log.d(TAG, "Error while sending response");
                            loginCallbackContext.error(TAG + ": Error while sending response");
                        }
                    } else {
                        Log.d(TAG, "Access token verification failed.");
                        loginCallbackContext.error(TAG + ": Access token verification failed.");
                    }
                } else {
                    Log.d(TAG, "Access token verification failed. Error: " + client.getResponseMessage());
                    loginCallbackContext.error(TAG + ": Access token verification failed. Error: " + client.getResponseMessage());
                }
            }
            catch(MalformedURLException error) {
                Log.d(TAG, "Error while checking token:" + error.getMessage());
                loginCallbackContext.error(TAG + ": Error while checking token:" + error.getMessage());
            }
            catch (Exception error) {
                Log.d(TAG, "Error while checking token: " + error.getMessage());
                loginCallbackContext.error(TAG + ": Error while checking token: " + error.getMessage());
            }
            finally {
                if(client != null) // Make sure the connection is not null.
                    client.disconnect();
            }
        });
    }

    /**
     * Logout
     */
    private void logoutAction(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode().createParams();
                Task<Void> signOutTask = AccountAuthManager.getService(cordova.getActivity(), authParams).signOut();
                signOutTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Log.d(TAG, "Sign out successful");
                        callbackContext.success();
                    }
                });
            }
        });
    }

    /**
     * Unlock achievement
     */
//    private void unlockAchievementAction(String achievementId, final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getAchievementsClient(cordova.getActivity()).unlock(achievementId);
//                callbackContext.success();
//            }
//        });
//    }
//
//    /**
//     * Increment achievement
//     */
//    private void incrementAchievementAction(String achievementId, Integer count, final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getAchievementsClient(cordova.getActivity()).increment(achievementId, count);
//                callbackContext.success();
//            }
//        });
//    }
//
//    /**
//     * Reveal achievement for current user
//     */
//    private void revealAchievementAction(String achievementId, final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getAchievementsClient(cordova.getActivity()).reveal(achievementId);
//                callbackContext.success();
//            }
//        });
//    }
//
//    /**
//     * Show achievements
//     */
//    private void showAchievementsAction(final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                AchievementsClient client = PlayGames.getAchievementsClient(cordova.getActivity());
//                client
//                    .load(true)
//                    .addOnSuccessListener((data) -> {
//                       client
//                           .getAchievementsIntent()
//                           .addOnSuccessListener(new OnSuccessListener<Intent>() {
//                               @Override
//                               public void onSuccess(Intent intent) {
//                                   cordova.setActivityResultCallback(self);
//                                   cordova.getActivity().startActivityForResult(intent, RC_ACHIEVEMENT_UI);
//                                   callbackContext.success();
//                               }
//                           });
//                    });
//            }
//        });
//    }
//
//    /**
//     * Set steps in achievement
//     */
//    private void setStepsInAchievementAction(String achievementId, int count, final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getAchievementsClient(cordova.getActivity()).setSteps(achievementId, count);
//                callbackContext.success();
//            }
//        });
//    }
//
//    /**
//     * Update player score
//     */
//    private void updatePlayerScoreAction(String leaderboardId, Integer score, final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getLeaderboardsClient(cordova.getActivity())
//                        .submitScore(leaderboardId, score);
//                callbackContext.success();
//            }
//        });
//    }
//
//    /**
//     * Load player score
//     */
//    private void loadPlayerScoreAction(String leaderboardId, final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getLeaderboardsClient(cordova.getActivity())
//                    .loadCurrentPlayerLeaderboardScore(leaderboardId, LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC)
//                    .addOnSuccessListener(new OnSuccessListener<AnnotatedData<LeaderboardScore>>() {
//                        @Override
//                        public void onSuccess(AnnotatedData<LeaderboardScore> score) {
//                            try {
//                                @Nullable LeaderboardScore scoreObject = score.get();
//                                JSONObject result = new JSONObject();
//                                result.put("score", scoreObject != null ? scoreObject.getRawScore() : 0);
//                                callbackContext.success(result);
//                            } catch (JSONException err) {
//                                callbackContext.error(err.getMessage());
//                            }
//                        }
//                    });
//            }
//        });
//    }
//
//    /**
//     * Show leaderboard
//     */
//    private void showLeaderboardAction(String leaderboardId, final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getLeaderboardsClient(cordova.getActivity())
//                        .getLeaderboardIntent(leaderboardId)
//                        .addOnSuccessListener(new OnSuccessListener<Intent>() {
//                            @Override
//                            public void onSuccess(Intent intent) {
//                                cordova.setActivityResultCallback(self);
//                                cordova.getActivity().startActivityForResult(intent, RC_LEADERBOARD_UI);
//                                callbackContext.success();
//                            }
//                        });
//            }
//        });
//    }
//
//    /**
//     * Show all leaderboards
//     */
//    private void showAllLeaderboardsAction(final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                LeaderboardsClient client = PlayGames.getLeaderboardsClient(cordova.getActivity());
//                client
//                        .loadLeaderboardMetadata(true)
//                        .addOnSuccessListener(new OnSuccessListener<AnnotatedData<LeaderboardBuffer>>() {
//                            @Override
//                            public void onSuccess(AnnotatedData<LeaderboardBuffer> data) {
//                                client.getAllLeaderboardsIntent()
//                                        .addOnSuccessListener(new OnSuccessListener<Intent>() {
//                                            @Override
//                                            public void onSuccess(Intent intent) {
//                                                cordova.setActivityResultCallback(self);
//                                                cordova.getActivity().startActivityForResult(intent, RC_LEADERBOARDS_UI);
//                                                if (data.get() != null) {
//                                                    data.get().release();
//                                                }
//                                                callbackContext.success();
//                                            }
//                                        });
//                            }
//                        });
//            }
//        });
//    }
//
//    /**
//     * Show saved games
//     */
//    private void showSavedGamesAction(String title, Boolean allowAddButton, Boolean allowDelete, Integer numberOfSavedGames, final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                SnapshotsClient snapshotsClient =
//                        PlayGames.getSnapshotsClient(cordova.getActivity());
//                int maxNumberOfSavedGamesToShow = numberOfSavedGames;
//
//                snapshotsClient
//                    .getSelectSnapshotIntent(title, allowAddButton, allowDelete, maxNumberOfSavedGamesToShow)
//                    .addOnSuccessListener(
//                        new OnSuccessListener<Intent>(){
//                            @Override
//                            public void onSuccess(Intent intent) {
//                                cordova.setActivityResultCallback(self);
//                                cordova.getActivity().startActivityForResult(intent, RC_SAVED_GAMES);
//                                callbackContext.success();
//                            }
//                        }
//                    );
//            }
//        });
//    }
//
//    /**
//     * Save game
//     */
//    private void saveGameAction(String snapshotName, String snapshotDescription, JSONObject snapshotContents, final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                int conflictResolutionPolicy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;
//                SnapshotsClient snapshotsClient =
//                        PlayGames.getSnapshotsClient(cordova.getActivity());
//                snapshotsClient
//                    .open(snapshotName, true, conflictResolutionPolicy)
//                    .addOnSuccessListener(new OnSuccessListener<SnapshotsClient.DataOrConflict<Snapshot>>(){
//                        @Override
//                        public void onSuccess(SnapshotsClient.DataOrConflict<Snapshot> dataOrConflict) {
//                            if (dataOrConflict.isConflict()) {
//                                // Send conflict id
//                                try {
//                                    JSONObject result = new JSONObject();
//                                    result.put("conflictId", dataOrConflict.getConflict().getConflictId());
//                                    self.emitWindowEvent(EVENT_SAVE_GAME_CONFLICT, result);
//                                } catch (JSONException err) {
//                                    callbackContext.error(err.getMessage());
//                                }
//                                return;
//                            }
//                            // Set the data payload for the snapshot
//                            dataOrConflict.getData().getSnapshotContents().writeBytes(snapshotContents.toString().getBytes(StandardCharsets.UTF_8));
//
//                            // Create the change operation
//                            SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
//                                    .setDescription(snapshotDescription)
//                                    .build();
//
//                            // Commit the operation
//                            snapshotsClient.commitAndClose(dataOrConflict.getData(), metadataChange);
//                            callbackContext.success();
//                        }
//                    });
//            }
//        });
//    }
//
//    /**
//     * Save game
//     */
//    private void loadGameSaveAction(String snapshotName, final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                Log.e(TAG, "READ START");
//                SnapshotsClient snapshotsClient =
//                        PlayGames.getSnapshotsClient(cordova.getActivity());
//                // In the case of a conflict, the most recently modified version of this snapshot will be used.
//                int conflictResolutionPolicy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;
//
//                // Open the saved game using its name.
//                snapshotsClient
//                    .open(snapshotName, true, conflictResolutionPolicy)
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.e(TAG, "Error while opening Snapshot.", e);
//                            callbackContext.error(e.getMessage());
//                        }
//                    })
//                    .continueWith(new Continuation<SnapshotsClient.DataOrConflict<Snapshot>, byte[]>() {
//                        @Override
//                        public byte[] then(@NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task) throws Exception {
//                            Snapshot snapshot = task.getResult().getData();
//                            // Opening the snapshot was a success and any conflicts have been resolved.
//                            try {
//                                // Extract the raw data from the snapshot.
//                                return snapshot.getSnapshotContents().readFully();
//                            } catch (IOException e) {
//                                Log.e(TAG, "Error while reading Snapshot.", e);
//                                callbackContext.error(e.getMessage());
//                            }
//                            return null;
//                        }
//                    })
//                    .addOnCompleteListener(new OnCompleteListener<byte[]>() {
//                        @Override
//                        public void onComplete(@NonNull Task<byte[]> task) {
//                            task.addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                @Override
//                                public void onSuccess(byte[] bytes) {
//                                    try {
//                                        Log.e(TAG, "READ SUCCESS");
//                                        callbackContext.success(new JSONObject(new String(bytes, StandardCharsets.UTF_8)));
//                                    } catch (JSONException e) {
//                                        callbackContext.error(e.getMessage());
//                                    }
//                                }
//                            });
//                        }
//                    });
//            }
//        });
//    }
//
//    /**
//     * Get friends list
//     */
//    private void getFriendsListAction(final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                int pageSize = 200;
//                PlayGames.getPlayersClient(cordova.getActivity())
//                        .loadFriends(pageSize,false)
//                        .addOnSuccessListener(
//                            new OnSuccessListener<AnnotatedData<PlayerBuffer>>() {
//                                @Override
//                                public void onSuccess(AnnotatedData<PlayerBuffer> data) {
//                                    PlayerBuffer playerBuffer = data.get();
//
//                                    JSONArray players = new JSONArray();
//                                    try {
//                                        AtomicInteger currentCount = new AtomicInteger();
//                                        int totalCount = playerBuffer.getCount();
//                                        for (int i = 0; i < playerBuffer.getCount(); i++) {
//                                            JSONObject player = new JSONObject();
//                                            player.put("id", playerBuffer.get(i).getPlayerId());
//                                            player.put("name", playerBuffer.get(i).getDisplayName());
//                                            player.put("title", playerBuffer.get(i).getTitle());
//                                            player.put("retrievedTimestamp", playerBuffer.get(i).getRetrievedTimestamp());
//                                            if (playerBuffer.get(i).getBannerImageLandscapeUri() != null) {
//                                                player.put("bannerImageLandscapeUri", playerBuffer.get(i).getBannerImageLandscapeUri().toString());
//                                            }
//                                            if (playerBuffer.get(i).getBannerImagePortraitUri() != null) {
//                                                player.put("bannerImagePortraitUri", playerBuffer.get(i).getBannerImagePortraitUri().toString());
//                                            }
//                                            if (playerBuffer.get(i).hasIconImage()) {
//                                                player.put("iconImageUri", playerBuffer.get(i).getIconImageUri().toString());
//                                            }
//                                            if (playerBuffer.get(i).hasHiResImage()) {
//                                                player.put("hiResImageUri", playerBuffer.get(i).getHiResImageUri().toString());
//                                            }
//                                            if (playerBuffer.get(i).getLevelInfo() != null) {
//                                                JSONObject levelInfo = new JSONObject();
//                                                levelInfo.put("currentLevel", playerBuffer.get(i).getLevelInfo().getCurrentLevel().getLevelNumber());
//                                                levelInfo.put("maxXp", playerBuffer.get(i).getLevelInfo().getCurrentLevel().getMaxXp());
//                                                levelInfo.put("minXp", playerBuffer.get(i).getLevelInfo().getCurrentLevel().getMinXp());
//                                                levelInfo.put("hashCode", playerBuffer.get(i).getLevelInfo().getCurrentLevel().hashCode());
//                                                player.put("levelInfo", levelInfo);
//                                            }
//                                            if (playerBuffer.get(i).getCurrentPlayerInfo() != null) {
//                                                JSONObject currentPlayerInfo = new JSONObject();
//                                                currentPlayerInfo.put("friendsListVisibilityStatus", playerBuffer.get(i).getCurrentPlayerInfo().getFriendsListVisibilityStatus());
//                                                player.put("currentPlayerInfo", currentPlayerInfo);
//                                            }
//                                            if (playerBuffer.get(i).getRelationshipInfo() != null) {
//                                                player.put("friendStatus", playerBuffer.get(i).getRelationshipInfo().getFriendStatus());
//                                            }
//
//                                            if (playerBuffer.get(i).hasIconImage()) {
//                                                ImageManager mgr = ImageManager.create(cordova.getContext());
//                                                mgr.loadImage((uri, drawable, isRequestedDrawable) -> {
//                                                    if (isRequestedDrawable) {
//                                                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                                                        byte[] byteArray = byteArrayOutputStream.toByteArray();
//                                                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                                                        try {
//                                                            player.put("iconImageBase64", "data:image/png;base64, " + encoded);
//                                                            players.put(player);
//                                                            currentCount.addAndGet(1);
//                                                            if (currentCount.intValue() >= totalCount) {
//                                                                callbackContext.success(players);
//                                                            }
//                                                        } catch (Exception e) {
//                                                            Log.e(TAG, "Error while getting base64 for user from friend list.");
//                                                        }
//                                                    }
//                                                }, Objects.requireNonNull(playerBuffer.get(i).getIconImageUri()));
//                                            } else {
//                                                players.put(player);
//                                                currentCount.addAndGet(1);
//                                                if (currentCount.intValue() >= totalCount) {
//                                                    callbackContext.success(players);
//                                                }
//                                            }
//                                        }
//                                    } catch (JSONException err) {
//                                        try {
//                                            JSONObject error = new JSONObject();
//                                            error.put("code", ERROR_CODE_NO_RESOLUTION);
//                                            error.put("message", "Error while retrieving friends list: " + err.getMessage());
//                                            callbackContext.error(error);
//                                        } catch (JSONException e) {
//                                            callbackContext.error("Error while retrieving friends list: " + e.getMessage());
//                                        }
//                                    }
//                                }
//                            }
//                        )
//                        .addOnFailureListener(exception -> {
//                            if (exception instanceof FriendsResolutionRequiredException) {
//                                PendingIntent pendingIntent =
//                                        ((FriendsResolutionRequiredException) exception).getResolution();
//                                try {
//                                    cordova.setActivityResultCallback(self);
//                                    cordova.getActivity().startIntentSenderForResult(
//                                            pendingIntent.getIntentSender(),
//                                            /* requestCode */ SHOW_SHARING_FRIENDS_CONSENT,
//                                            /* fillInIntent */ null,
//                                            /* flagsMask */ 0,
//                                            /* flagsValues */ 0,
//                                            /* extraFlags */ 0,
//                                            /* options */ null);
//                                    try {
//                                        JSONObject error = new JSONObject();
//                                        error.put("code", ERROR_CODE_HAS_RESOLUTION);
//                                        error.put("message", "Waiting user give permission for fetch friends list, check events.");
//                                        callbackContext.error(error);
//                                    } catch (JSONException e) {
//                                        callbackContext.error("Error while retrieving friends list: " + e.getMessage());
//                                    }
//                                } catch (IntentSender.SendIntentException err) {
//                                    try {
//                                        JSONObject error = new JSONObject();
//                                        error.put("code", ERROR_CODE_NO_RESOLUTION);
//                                        error.put("message", "Error while asking permission for retrieving friends list: " + err.getMessage());
//                                        callbackContext.error(error);
//                                    } catch (JSONException e) {
//                                        callbackContext.error("Error while retrieving friends list: " + e.getMessage());
//                                    }
//                                }
//                            }
//                        });
//            }
//        });
//    }
//
//    /**
//     * Show another player profile, used also for player search
//     */
//    private void showAnotherPlayersProfileAction(String playerId, @Nullable final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getPlayersClient(cordova.getActivity())
//                    .getCompareProfileIntent(playerId)
//                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
//                        @Override
//                        public void onSuccess(Intent  intent) {
//                            cordova.setActivityResultCallback(self);
//                            cordova.getActivity().startActivityForResult(intent, RC_SHOW_PROFILE);
//                            if (callbackContext != null) {
//                                callbackContext.success();
//                            }
//                        }});
//            }
//        });
//    }
//
//    /**
//     * Show player search default window
//     */
//    private void showPlayerSearchAction(final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getPlayersClient(cordova.getActivity())
//                        .getPlayerSearchIntent()
//                        .addOnSuccessListener(new OnSuccessListener<Intent>() {
//                            @Override
//                            public void onSuccess(Intent  intent) {
//                                cordova.setActivityResultCallback(self);
//                                cordova.getActivity().startActivityForResult(intent, RC_SHOW_PLAYER_SEARCH);
//                                callbackContext.success();
//                            }});
//            }
//        });
//    }
//
//    private void getPlayerAction(String id, Boolean forceReload, final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getPlayersClient(cordova.getActivity())
//                    .loadPlayer(id, forceReload)
//                    .addOnSuccessListener((data) -> {
//                        Player player = data.get();
//                        JSONObject result = new JSONObject();
//                        if (player == null) {
//                            callbackContext.success(result);
//                            return;
//                        }
//                        try {
//                            result.put("id", player.getPlayerId());
//                            result.put("name", player.getDisplayName());
//                            result.put("title", player.getTitle());
//                            result.put("retrievedTimestamp", player.getRetrievedTimestamp());
//                            if (player.getBannerImageLandscapeUri() != null) {
//                                result.put("bannerImageLandscapeUri", player.getBannerImageLandscapeUri().toString());
//                            }
//                            if (player.getBannerImagePortraitUri() != null) {
//                                result.put("bannerImagePortraitUri", player.getBannerImagePortraitUri().toString());
//                            }
//                            if (player.hasIconImage()) {
//                                result.put("iconImageUri", player.getIconImageUri().toString());
//                            }
//                            if (player.hasHiResImage()) {
//                                result.put("hiResImageUri", player.getHiResImageUri().toString());
//                            }
//                            if (player.getLevelInfo() != null) {
//                                JSONObject levelInfo = new JSONObject();
//                                levelInfo.put("currentLevel", player.getLevelInfo().getCurrentLevel().getLevelNumber());
//                                levelInfo.put("maxXp", player.getLevelInfo().getCurrentLevel().getMaxXp());
//                                levelInfo.put("minXp", player.getLevelInfo().getCurrentLevel().getMinXp());
//                                levelInfo.put("hashCode", player.getLevelInfo().getCurrentLevel().hashCode());
//                                result.put("levelInfo", levelInfo);
//                            }
//
//                            if (player.hasIconImage()) {
//                                ImageManager mgr = ImageManager.create(cordova.getContext());
//                                mgr.loadImage((uri, drawable, isRequestedDrawable) -> {
//                                    if (isRequestedDrawable) {
//                                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                                        byte[] byteArray = byteArrayOutputStream.toByteArray();
//                                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                                        try {
//                                            result.put("iconImageBase64", "data:image/png;base64, " + encoded);
//                                            callbackContext.success(result);
//                                        } catch (Exception e) {
//                                            Log.e(TAG, "Error while getting base64 for user from friend list.");
//                                        }
//                                    }
//                                }, Objects.requireNonNull(player.getIconImageUri()));
//                            } else {
//                                callbackContext.success(result);
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, "Error while getting player.");
//                        }
//                    });
//            }
//        });
//    }
//
//    /**
//     * Returns current player stats
//     */
//    private void getCurrentPlayerStatsAction(final CallbackContext callbackContext) {
//        PlayGames.getPlayerStatsClient(cordova.getActivity())
//            .loadPlayerStats(true)
//            .addOnCompleteListener(new OnCompleteListener<AnnotatedData<PlayerStats>>() {
//                @Override
//                public void onComplete(@NonNull Task<AnnotatedData<PlayerStats>> task) {
//                    if (task.isSuccessful()) {
//                        // Check for cached data.
//                        if (task.getResult().isStale()) {
//                            Log.d(TAG, "Using cached data");
//                        }
//                        PlayerStats stats = task.getResult().get();
//                        JSONObject resultStats = new JSONObject();
//                        if (stats != null) {
//                            Log.d(TAG, "Player stats loaded");
//                            try {
//                                resultStats.put("averageSessionLength", stats.getAverageSessionLength());
//                                resultStats.put("daysSinceLastPlayed", stats.getDaysSinceLastPlayed());
//                                resultStats.put("numberOfPurchases", stats.getNumberOfPurchases());
//                                resultStats.put("numberOfSessions", stats.getNumberOfSessions());
//                                resultStats.put("sessionPercentile", stats.getSessionPercentile());
//                                resultStats.put("spendPercentile", stats.getSpendPercentile());
//                                callbackContext.success(resultStats);
//                            } catch (JSONException e) {
//                                callbackContext.error("Error while creating player stats result object. Error: " + e.getMessage());
//                            }
//                        }
//                    } else {
//                        int status = CommonStatusCodes.DEVELOPER_ERROR;
//                        if (task.getException() instanceof ApiException) {
//                            status = ((ApiException) task.getException()).getStatusCode();
//                        }
//                        if (task.getException() != null) {
//                            callbackContext.error("Failed to fetch Stats Data status: " + status + ": " + task.getException().getMessage());
//                        }
//                        Log.d(TAG, "Failed to fetch Stats Data status: " + status + ": " + task.getException());
//                    }
//                }
//            });
//    }
//
//    /**
//     * Get all events (user custom stats)
//     */
//    private void getAllEventsAction(final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getEventsClient(cordova.getActivity())
//                    .load(true)
//                    .addOnCompleteListener(new OnCompleteListener<AnnotatedData<EventBuffer>>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AnnotatedData<EventBuffer>> task) {
//                            if (task.isSuccessful()) {
//                                // Process all the events.
//                                JSONArray events = new JSONArray();
//                                AtomicInteger currentCount = new AtomicInteger();
//                                int totalCount = task.getResult().get().getCount();
//                                if (totalCount == 0) {
//                                    callbackContext.success(events);
//                                }
//                                for (Event event : task.getResult().get()) {
//                                    Log.d(TAG, "loaded event " + event.getName() + ", " + event.getEventId() + ", " + event.getDescription() + ", " + event.getIconImageUri() + ", " + event.getFormattedValue() + ", " + event.getValue() + ", " + event.getPlayer().getPlayerId() + ", " + event.isVisible());
//                                    try {
//                                        JSONObject resultEvent = new JSONObject();
//                                        resultEvent.put("id", event.getEventId());
//                                        resultEvent.put("name", event.getName());
//                                        resultEvent.put("description", event.getDescription());
//                                        resultEvent.put("iconImageUri", event.getIconImageUri());
//                                        resultEvent.put("formattedValue", event.getFormattedValue());
//                                        resultEvent.put("value", event.getValue());
//
//                                        JSONObject player = new JSONObject();
//                                        player.put("id", event.getPlayer().getPlayerId());
//                                        player.put("name", event.getPlayer().getDisplayName());
//                                        player.put("title", event.getPlayer().getTitle());
//                                        player.put("retrievedTimestamp", event.getPlayer().getRetrievedTimestamp());
//                                        if (event.getPlayer().getBannerImageLandscapeUri() != null) {
//                                            player.put("bannerImageLandscapeUri", event.getPlayer().getBannerImageLandscapeUri().toString());
//                                        }
//                                        if (event.getPlayer().getBannerImagePortraitUri() != null) {
//                                            player.put("bannerImagePortraitUri", event.getPlayer().getBannerImagePortraitUri().toString());
//                                        }
//                                        if (event.getPlayer().hasIconImage()) {
//                                            player.put("iconImageUri", event.getPlayer().getIconImageUri().toString());
//                                        }
//                                        if (event.getPlayer().hasHiResImage()) {
//                                            player.put("hiResImageUri", event.getPlayer().getHiResImageUri().toString());
//                                        }
//                                        if (event.getPlayer().getLevelInfo() != null) {
//                                            JSONObject levelInfo = new JSONObject();
//                                            levelInfo.put("currentLevel", event.getPlayer().getLevelInfo().getCurrentLevel().getLevelNumber());
//                                            levelInfo.put("maxXp", event.getPlayer().getLevelInfo().getCurrentLevel().getMaxXp());
//                                            levelInfo.put("minXp", event.getPlayer().getLevelInfo().getCurrentLevel().getMinXp());
//                                            levelInfo.put("hashCode", event.getPlayer().getLevelInfo().getCurrentLevel().hashCode());
//                                            player.put("levelInfo", levelInfo);
//                                        }
//
//                                        if (event.getPlayer().hasIconImage()) {
//                                            ImageManager mgr = ImageManager.create(cordova.getContext());
//                                            mgr.loadImage((uri, drawable, isRequestedDrawable) -> {
//                                                if (isRequestedDrawable) {
//                                                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                                                    byte[] byteArray = byteArrayOutputStream.toByteArray();
//                                                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                                                    try {
//                                                        player.put("iconImageBase64", "data:image/png;base64, " + encoded);
//                                                        resultEvent.put("player", player);
//                                                        events.put(resultEvent);
//                                                        currentCount.addAndGet(1);
//                                                        if (currentCount.intValue() >= totalCount) {
//                                                            callbackContext.success(events);
//                                                        }
//                                                    } catch (Exception e) {
//                                                        Log.e(TAG, "Error while getting base64 for user from friend list.");
//                                                    }
//                                                }
//                                            }, Objects.requireNonNull(event.getPlayer().getIconImageUri()));
//                                        } else {
//                                            resultEvent.put("player", player);
//                                            events.put(resultEvent);
//                                            currentCount.addAndGet(1);
//                                            if (currentCount.intValue() >= totalCount) {
//                                                callbackContext.success(events);
//                                            }
//                                        }
//                                    } catch (JSONException e) {
//                                        Log.d(TAG, "Error while receiving event: " + e.getMessage());
//                                    }
//                                }
//                            } else {
//                                // Handle Error
//                                Exception exception = task.getException();
//                                int statusCode = CommonStatusCodes.DEVELOPER_ERROR;
//                                if (exception instanceof ApiException) {
//                                    ApiException apiException = (ApiException) exception;
//                                    statusCode = apiException.getStatusCode();
//                                }
//                                Log.d(TAG, "event error " + statusCode);
//                            }
//                        }
//                    });
//            }
//        });
//    }
//
//    /**
//     * Get current user event
//     */
//    private void getEventAction(String id, final CallbackContext callbackContext) {
//        HuaweiGamesPlugin self = this;
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getEventsClient(cordova.getActivity())
//                    .loadByIds(true, id)
//                    .addOnCompleteListener(new OnCompleteListener<AnnotatedData<EventBuffer>>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AnnotatedData<EventBuffer>> task) {
//                            if (task.isSuccessful()) {
//                                // Process all the events.
//                                for (Event event : task.getResult().get()) {
//                                    Log.d(TAG, "loaded event " + event.getName() + ", " + event.getEventId() + ", " + event.getDescription() + ", " + event.getIconImageUri() + ", " + event.getFormattedValue() + ", " + event.getValue() + ", " + event.getPlayer().getPlayerId() + ", " + event.isVisible());
//                                    try {
//                                        JSONObject resultEvent = new JSONObject();
//                                        resultEvent.put("id", event.getEventId());
//                                        resultEvent.put("name", event.getName());
//                                        resultEvent.put("description", event.getDescription());
//                                        resultEvent.put("iconImageUri", event.getIconImageUri());
//                                        resultEvent.put("formattedValue", event.getFormattedValue());
//                                        resultEvent.put("value", event.getValue());
//
//                                        JSONObject player = new JSONObject();
//                                        player.put("id", event.getPlayer().getPlayerId());
//                                        player.put("name", event.getPlayer().getDisplayName());
//                                        player.put("title", event.getPlayer().getTitle());
//                                        player.put("retrievedTimestamp", event.getPlayer().getRetrievedTimestamp());
//                                        if (event.getPlayer().getBannerImageLandscapeUri() != null) {
//                                            player.put("bannerImageLandscapeUri", event.getPlayer().getBannerImageLandscapeUri().toString());
//                                        }
//                                        if (event.getPlayer().getBannerImagePortraitUri() != null) {
//                                            player.put("bannerImagePortraitUri", event.getPlayer().getBannerImagePortraitUri().toString());
//                                        }
//                                        if (event.getPlayer().hasIconImage()) {
//                                            player.put("iconImageUri", event.getPlayer().getIconImageUri().toString());
//                                        }
//                                        if (event.getPlayer().hasHiResImage()) {
//                                            player.put("hiResImageUri", event.getPlayer().getHiResImageUri().toString());
//                                        }
//                                        if (event.getPlayer().getLevelInfo() != null) {
//                                            JSONObject levelInfo = new JSONObject();
//                                            levelInfo.put("currentLevel", event.getPlayer().getLevelInfo().getCurrentLevel().getLevelNumber());
//                                            levelInfo.put("maxXp", event.getPlayer().getLevelInfo().getCurrentLevel().getMaxXp());
//                                            levelInfo.put("minXp", event.getPlayer().getLevelInfo().getCurrentLevel().getMinXp());
//                                            levelInfo.put("hashCode", event.getPlayer().getLevelInfo().getCurrentLevel().hashCode());
//                                            player.put("levelInfo", levelInfo);
//                                        }
//
//                                        if (event.getPlayer().hasIconImage()) {
//                                            ImageManager mgr = ImageManager.create(cordova.getContext());
//                                            mgr.loadImage((uri, drawable, isRequestedDrawable) -> {
//                                                if (isRequestedDrawable) {
//                                                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                                                    byte[] byteArray = byteArrayOutputStream.toByteArray();
//                                                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                                                    try {
//                                                        player.put("iconImageBase64", "data:image/png;base64, " + encoded);
//                                                        resultEvent.put("player", player);
//                                                        callbackContext.success(resultEvent);
//                                                    } catch (Exception e) {
//                                                        Log.e(TAG, "Error while getting base64 for user from friend list.");
//                                                    }
//                                                }
//                                            }, Objects.requireNonNull(event.getPlayer().getIconImageUri()));
//                                        } else {
//                                            resultEvent.put("player", player);
//                                            callbackContext.success(resultEvent);
//                                        }
//                                    } catch (JSONException e) {
//                                        Log.d(TAG, "Error while receiving event: " + e.getMessage());
//                                    }
//                                }
//                            } else {
//                                // Handle Error
//                                Exception exception = task.getException();
//                                int statusCode = CommonStatusCodes.DEVELOPER_ERROR;
//                                if (exception instanceof ApiException) {
//                                    ApiException apiException = (ApiException) exception;
//                                    statusCode = apiException.getStatusCode();
//                                }
//                                Log.d(TAG, "event error " + statusCode);
//                            }
//                        }
//                    });
//            }
//        });
//    }
//
//    /**
//     * Set value for user event
//     */
//    private void incrementEventAction(String id, int amount, final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                PlayGames.getEventsClient(cordova.getActivity()).increment(id, amount);
//                callbackContext.success();
//            }
//        });
//    }
}
