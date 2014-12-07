package su.ias.malina.app;

import android.app.Application;
import android.content.*;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import org.apache.http.client.CookieStore;
import su.ias.malina.data.AccountData;
import su.ias.malina.data.UserData;
import su.ias.malina.db.DBAdapter;
import su.ias.malina.services.GeoDetectionService;

import java.io.File;

public class AppSingleton extends Application  {

    public boolean internetAvailable;
    private ConnectivityManager connectivityManager;
    private NetworkStateReceiver networkStateReceiver;

    public static final String SAVED_USER_LOGIN = "SAVED_USER_LOGIN";
    public static final String SAVED_USER_PASSWORD = "SAVED_USER_PASSWORD";

    public static final String SAVED_REGION = "SAVED_REGION";

    public boolean userLoggedIn;

    public String userRegion;

    private static AppSingleton application;
    private DBAdapter dbAdapter;

    private AccountData accountData;
    private UserData userData;

    public static final int UPDATE_ACCOUNT_CODE = 100500;
    private static final long UPDATE_ACCOUNT_PERIOD_WORK = 5 * 60 * 1000;

    public Location userLocation;

    public SharedPreferences prefs;

    public static CookieStore cookieStore;

    public static AppSingleton get() {
        return application;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        userLoggedIn = false;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        accountData = new AccountData();
        userData = new UserData();

        // На старте приложения регистрируем слушатель состояния подключения к сети и проверяем текущий статус
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkStateReceiver = new NetworkStateReceiver();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);
        checkInternetState();

        // Загружаем сохраненный регион или выставляем по умолчанию
        if (getFromSharedPrefs(SAVED_REGION) == null) {
            prefs.edit().putString(SAVED_REGION, GeoDetectionService.CITY_MSC).apply();
        }

        userRegion = getFromSharedPrefs(SAVED_REGION);

        // Запуск сервиса для определения геолокации
        userLocation = null;
        startService(new Intent(this, GeoDetectionService.class));

        // инициализируем адаптер б/д
        initDB();
        initImageLoader();

    }








    private void initImageLoader() {

        File cacheDir = StorageUtils.getCacheDirectory(this);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.stub_image)
//                .showImageForEmptyUrl(R.drawable.image_for_empty_url)
                .resetViewBeforeLoading()
                .cacheInMemory()
                .cacheOnDisc()
//                .decodingType(ImageScaleType.EXACT)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//                .memoryCacheExtraOptions(480, 800) // width, height
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG,70) // width, height, compress format, quality
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 2 Mb
                .discCache(new UnlimitedDiscCache(cacheDir))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                .defaultDisplayImageOptions(options )
                .build();

        ImageLoader.getInstance().init(config);
    }



    public DBAdapter getDBAdapter() {
        return dbAdapter;
    }



    private void initDB() {
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
    }



    public void updateUserRegion(String newRegion) {
        userRegion = newRegion;
        prefs.edit().putString(SAVED_REGION, newRegion).apply();
    }



    /**
     * Определение текущего состояния подключения к сети
     */
    private void checkInternetState() {
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
            internetAvailable =  ( connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        } else {
            internetAvailable = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        }
    }


    public String getFromSharedPrefs(String key) {
        return prefs.getString(key, null);
    }


    public void removeFromSharedPrefs(String key) {
        prefs.edit().remove(key).apply();
    }


    public void resetUserData() {
        prefs.edit().remove(SAVED_USER_LOGIN)
                    .remove(SAVED_USER_PASSWORD)
                    .putString(SAVED_REGION, GeoDetectionService.CITY_MSC).apply();
    }


    public AccountData getAccountData() {
        return accountData;
    }


    public UserData getUserData() {
        return userData;
    }



    /**
     * Слушатель состояния интернета
     *
     */
    class NetworkStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkInternetState();
        }
    }



}
