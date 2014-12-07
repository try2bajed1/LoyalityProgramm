package su.ias.malina.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import su.ias.malina.app.AppSingleton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GeoDetectionService extends Service {

    private Location userLocation;
    private LocationManager locationManager;
    private Geocoder geoCoder;
    private LocationListener locationListener;

    public static final String ACTION_USER_REGION_FOUNDED = "ACTION_USER_REGION_FOUNDED";

    public static final String FOUNDED_REGION = "FOUNDED_REGION";
    public static final String USER_REGION_CHANGED = "USER_REGION_CHANGED";

    public static final String ACTION_USER_LOCATION_UPDATED = "USER_LOCATION_UPDATED";
    private static final long LOCATION_UPDATE_INTERVAL = 3 * 60 * 1000;


    // Москва
    public static final String CITY_MSC = "Москва";
    public static final String ADMIN_AREA_MSC = "Московская область";

    public static final double MSC_CENTER_LONG = 37.620865;
    public static final double MSC_CENTER_LAT = 55.753048;


    // Санкт-Петербург
    public static final String CITY_SPB = "Санкт-Петербург";
    public static final String ADMIN_AREA_SPB = "Ленинградская область";

    public static final double SPB_CENTER_LONG = 30.349733 ;
    public static final double SPB_CENTER_LAT = 59.926371;

    /**
     * административная зона, соответствующая региону по умолчанию
     */
    public static final String DEFAULT_ADMIN_AREA = "Московская область";


    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * 10;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (locationManager == null) {
            findUserLocation();
        }

        return Service.START_STICKY;
    }





    private Location getCurrentLocation() {
        Location location = null;
        try{
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.i("@", "(((((( no network provider is enabled ");
            } else {
//                this.canGetLocation = true;
                double latitude;
                double longitude;
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_INTERVAL, 20, locationListener);
                    Log.d("@", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_INTERVAL, 20, locationListener);
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("@", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return location;

    }



    /**
     * Определение местоположения пользователя всеми доступными способами
     */
    private void findUserLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geoCoder = new Geocoder(this, new Locale("ru", "RU"));


        String providerName = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(providerName);
//        Log.i("@","loc " + location);


        // пробуем найти сведения о последнем местоположении
        userLocation = getLastKnownLocation(true);
        if (userLocation == null) {
//            Log.e("@", "Недавние координаты не найдены");
        } else {
//            Log.e("@", "Недавние координаты найдены!!!");
            AppSingleton.get().userLocation = userLocation;
            findUserRegion();
        }


        // Запускаем периодический запрос на определение местоположения
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

//                Log.i("@", "получены даные от провайдера...");

                userLocation = location;

                // locationManager.removeUpdates(this);
                AppSingleton.get().userLocation = userLocation;
                findUserRegion();
                sendBroadcast(new Intent(ACTION_USER_LOCATION_UPDATED));
            }



            @Override
            public void onProviderDisabled(String arg0) {
//                Log.e("@", "Провайдер отрубился " + arg0);
                locationManager.removeUpdates(this);
                updateListenerProvider();
            }

            @Override
            public void onProviderEnabled(String arg0) {
//                Log.i("@", "Провайдер включился " + arg0);
            }

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

//                Log.i("@", "Статус провайдера " + arg0 + " изменился на " + arg1);
            }
        };


        updateListenerProvider();
    }




    private void updateListenerProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Log.i("@","best provider "+ bestProvider);
        if(bestProvider!=null) {
            locationManager.requestLocationUpdates(bestProvider, LOCATION_UPDATE_INTERVAL, 20, locationListener);
        }
    }


    /**
     * Пытаемся определить последнее сохраненное местоположения от всех
     * доступных провайдеров
     *
     * @param enabledProvidersOnly
     * @return
     */
    private Location getLastKnownLocation(boolean enabledProvidersOnly) {

        Location location = null;

        List<String> providers = locationManager.getProviders(enabledProvidersOnly);

        for (String provider : providers) {
//            Log.i("@","provider "+ provider);
            location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                // Если с момента получения координат прошло более 5 мин - считаем их не актуальными
                if (location.getTime() - System.currentTimeMillis() > 5 * 60 * 1000) {

//                    Log.e("@", "координаты от данного провайдера уже устарели: " + ((location.getTime() - System.currentTimeMillis()) / 1000));
                    location = null;
                } else {
                    break;
                }
            }

        }

        return location;
    }





    /**
     * Определение региона пользователя по данным геолокатора, отправка
     * бродкастом полученных (или дефолтовых) данных, окончание работы сервиса
     */
    private void findUserRegion() {

        String regionName = null;

//        Log.w("@", "получены координаты: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());

        List<Address> list = null;
        try {

            list = geoCoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);

            // Питер (для проверки)
            // list = geoCoder.getFromLocation(59.753802, 30.389911, 1);

        } catch (IOException e) {
            Log.e("@", "findUserRegion(): " + e.toString());
        }


        if (list != null && list.size() > 0) {

            Address address = list.get(0);
            String city = address.getLocality(); // населенный пункт
            String adminArea = address.getAdminArea(); // область
            String addressString = address.toString();// Адрес

//            Log.w("@", "City: " + city);
//            Log.w("@", "Admin area: " + adminArea);
//            Log.w("@", "Address full: " + addressString);

            // Проверяем Питер
            if ((city != null && city.equals(CITY_SPB)) || (adminArea != null && adminArea.equals(ADMIN_AREA_SPB)) || (addressString != null && (addressString.contains(CITY_SPB) || addressString.contains(ADMIN_AREA_SPB)))) {
                regionName = CITY_SPB;
            }
        }


        // Если не опаределили Питер - принимаем Москву
        if (regionName == null || !regionName.equals(CITY_SPB)) {
            regionName = CITY_MSC;
        }

        boolean regionChanged = regionName.equals(AppSingleton.get().getFromSharedPrefs(AppSingleton.SAVED_REGION));

        AppSingleton.get().updateUserRegion(regionName);
//        Log.d("@", "Принимаем регион: " + regionName);

        Intent intent = new Intent(ACTION_USER_REGION_FOUNDED);
        intent.putExtra(USER_REGION_CHANGED, regionChanged);
        sendBroadcast(intent);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
