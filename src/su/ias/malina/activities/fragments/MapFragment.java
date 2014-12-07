package su.ias.malina.activities.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.map.MapEvent;
import ru.yandex.yandexmapkit.map.OnMapListener;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import su.ias.malina.R;
import su.ias.malina.activities.SelectedPartnerActivity;
import su.ias.malina.activities.StatesActivity;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.async.PostTask;
import su.ias.malina.data.MapPointData;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.services.GeoDetectionService;
import su.ias.malina.utils.AppApiUtils;

import java.util.ArrayList;
import java.util.Set;


public class MapFragment extends Fragment implements IListener {

    public static final String MODE_EXTRA = "mode";

    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private ArrayList<Overlay> overlays;
    private MapView map;
    private int contentType;
    private AsyncTask<String, Void, Boolean> _getPointsTask;


    public static MapFragment newInstance(int mode) {

        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(MODE_EXTRA, mode);
        mapFragment.setArguments(args);

        return mapFragment;
    }


    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);

        contentType = getArguments().getInt(MODE_EXTRA, -1);
    }



    @Override
    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        map = (MapView) view.findViewById(R.id.map);

        mMapController = map.getMapController();
        mMapController.initializeScreenButtons();

        mOverlayManager = mMapController.getOverlayManager();
        mOverlayManager.getMyLocation().setEnabled(true);

        mMapController.addMapListener(new OnMapListener() {
            @Override
            public void onMapActionEvent(MapEvent mapEvent) {
                int msg = mapEvent.getMsg();

                switch (msg) {
                    case MapEvent.MSG_ZOOM_END:
                    case MapEvent.MSG_SCALE_END:
                    case MapEvent.MSG_SCROLL_END:
                        //функционал для подзагрузки точек только для режима пешком
                        if (contentType == StatesActivity.FOOT_MAP_STATE ) {
                            loadPoints(contentType);
                        }
                    break;
                }
            }
        });


        overlays = new ArrayList<Overlay>();
        return view;
    }




    @Override
    public void onResume() {

        super.onResume();

        loadPoints(contentType);
    }




    public void loadPoints(int state) {


        if (state == StatesActivity.CAR_MAP_STATE) {
            loadPointsForCarState();
        } else if(state == StatesActivity.FOOT_MAP_STATE) {
            loadPointsForFootState();
        }
    }



    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    private void clearMap() {
        if (overlays != null) {
            for (Overlay overlay : overlays) {
                mOverlayManager.removeOverlay(overlay);
            }
            overlays.clear();
            mMapController.notifyRepaint();
        }
    }



    public int getContentType() {
        return contentType;
    }



    public void setContentType(int contentType) {
        this.contentType = contentType;
    }



    private GeoPoint getUserCrds(){

        double longitude = -1;
        double latitude = -1;

        Location userLocation = AppSingleton.get().userLocation;
        if (userLocation != null) {
            longitude = userLocation.getLongitude();
            latitude = userLocation.getLatitude();
        }



        if(longitude == -1 || latitude == -1) {

            String userRegion = AppSingleton.get().getFromSharedPrefs(AppSingleton.SAVED_REGION);
            if (userRegion.equals(GeoDetectionService.CITY_MSC)) {
                longitude = GeoDetectionService.MSC_CENTER_LONG;
                latitude = GeoDetectionService.MSC_CENTER_LAT;
            } else if (userRegion.equals(GeoDetectionService.CITY_SPB)) {
                longitude = GeoDetectionService.SPB_CENTER_LONG;
                latitude = GeoDetectionService.SPB_CENTER_LAT;
            }
        }

        return new GeoPoint(longitude, latitude);
    }



    private GeoPoint getCurrentMapCenterCrds(){
        return mMapController.getMapCenter();
    }



    public void loadPointsForFootState() {
        GeoPoint mapCenterPoint = getCurrentMapCenterCrds();
        int radius = getRadiusByZoom();
        String jsonStr = AppApiUtils._getMapPointsPostJson(AppApiUtils.MODE_BY_FOOT, radius, mapCenterPoint.getLon(), mapCenterPoint.getLat()).toString();
        restartTask(jsonStr);
    }




    public void loadPointsForCarState() {

        GeoPoint userCrds = getUserCrds();
        int radius = 50000;
        String jsonToPost = AppApiUtils._getMapPointsPostJson(AppApiUtils.MODE_BY_CAR, radius, userCrds.getLat(), userCrds.getLon()).toString();
        restartTask(jsonToPost);
    }



    private void restartTask(String jsonAsStr) {
        // kill previous thread if exists
        if (_getPointsTask != null && _getPointsTask.getStatus() == AsyncTask.Status.RUNNING) {
            _getPointsTask.cancel(true);
        }

        _getPointsTask = new PostTask(this);
        _getPointsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppApiUtils.ACTION_GET_PARTNERS_POINTS, jsonAsStr);
    }



    private int getRadiusByZoom() {
        float zoomLevel =  mMapController.getZoomCurrent();

        if(zoomLevel < 11)
            return 7000;
        else if (zoomLevel >= 11 && zoomLevel < 12)
            return 5000;
        else if (zoomLevel >= 12 && zoomLevel < 13)
            return 4000;
        else return 3000;
    }




    @Override
    public void responseCompleteHandler(String actionName, String strFromApi) {

        if(getActivity() != null)
            ((StatesActivity) getActivity()).setActionBarLoading(false);

        try {

            StatesActivity.pointsDataArr.clear();

            JSONObject jsonFromApi = new JSONObject(strFromApi);
//            Log.i("@", "#### points " + strFromApi);
            JSONArray pointsJsonArr = jsonFromApi.getJSONArray("partner_points");
            if (pointsJsonArr.length() != 0) {
                String region = AppSingleton.get().userRegion;
                Set<Integer> partnersIdsSet = AppSingleton.get().getDBAdapter().getEnabledPartnersIdsByRegion(region);
                for (int i = 0; i < pointsJsonArr.length(); i++) {

                    JSONObject pointJson = pointsJsonArr.getJSONObject(i);
                    Integer partnerId = pointJson.has("partner_id") ? pointJson.getInt("partner_id") : -1;

                    //сверяем со списком партнеров (только для пешком) , для которых выставлены галки,
                    // если такого нет, то он нас больше не интересует => идем на следующую итерацию
                    if (contentType == StatesActivity.FOOT_MAP_STATE && !partnersIdsSet.contains(partnerId)) {
                        continue;
                    }

                    int distance = pointJson.has("distance") ? pointJson.getInt("distance") : -1;
                    double longitude = pointJson.has("longitude") ? pointJson.getDouble("longitude") : -1;
                    double latitude = pointJson.has("latitude") ? pointJson.getDouble("latitude") : -1;
                    String name = pointJson.has("name") ? pointJson.getString("name") : "";
                    String partnerName = pointJson.has("partner_name") ? pointJson.getString("partner_name") : "";
                    String metro = pointJson.has("metro") ? pointJson.getString("metro") : "";
                    String icon = pointJson.has("icon") ? pointJson.getString("icon") : "";
                    String address = pointJson.has("address") ? pointJson.getString("address") : "";
                    String cardUsage = pointJson.has("card_usage") ? pointJson.getString("card_usage") : "";

                    //добавляем сформированную точку в поле класса,
                    // чтобы потом это поле передавать в активити, для передачи в другие фрагменты ( показ в виде списка например )
                    StatesActivity.pointsDataArr.add(new MapPointData(latitude, longitude,partnerName, name, distance, address, partnerId, cardUsage, metro, icon));
                }

                drawFilteredPoints(StatesActivity.pointsDataArr);

            } else {
                Toast.makeText(getActivity(), "Отсутствуют точки поблизости", Toast.LENGTH_SHORT).show();
                clearMap();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private void drawFilteredPoints(ArrayList<MapPointData> filteredArr) {

        if(getActivity() == null ) return;

        clearMap();

        Resources resources = getActivity().getResources();
        float density = resources.getDisplayMetrics().density;
        int offsetX = (int) (6 * density);
        int offsetY = (int) (-18 * density);

        for (final MapPointData pointData : filteredArr) {

            int partnerId = pointData.getPartner_id();
            if (partnerId != -1) {

                double latitude = pointData.getLatitude();
                double longitude = pointData.getLongtude();
                String partnerName = pointData.getPartnerName();
                String pointName = pointData.getMapPointName();
                String address = pointData.getAddress();

                GeoPoint geoPoint = new GeoPoint(latitude, longitude);
                Overlay overlay = new Overlay(mMapController);
                OverlayItem overlayItem = new OverlayItem(geoPoint, resources.getDrawable(R.drawable.pin));
                overlayItem.setOffsetX(offsetX);
                overlayItem.setOffsetY(offsetY);

                // balloon model for the object
                BalloonItem balloonItem = new BalloonItem(getActivity(), geoPoint);
                if(partnerName.equals(pointName)) {
                    balloonItem.setText(partnerName + "\n" + address);
                } else {
                    balloonItem.setText(partnerName + "\n" + pointName + "\n" + address);
                }
//              balloonItem.setText(Html.fromHtml("<p><b>First, </b><br/>" +
//                                        "Please press the" + "<img src = 'http://yandex.st/lego/_/X31pO5JJJKEifJ7sfvuf3mGeD_8.png' />" + " to insert a new event.</p>"));
                //todo: do something with a icon in a balloon =\\\\
                balloonItem.setOffsetX(-offsetX / 2);
                overlayItem.setBalloonItem(balloonItem);

//              balloonItem.setOnBalloonViewClickListener(new OnBalloonListener();
                balloonItem.setOnBalloonListener(new OnBalloonListener() {
                    @Override
                    public void onBalloonViewClick(BalloonItem balloonItem, View view) {

                        startActivity(new Intent(getActivity(), SelectedPartnerActivity.class)
                                .putExtra(SelectedPartnerActivity.SELECTED_PARTNER_ID, pointData.getPartner_id())
                                .putExtra(SelectedPartnerActivity.MODE_EXTRA, SelectedPartnerActivity.ALL_MODE)
                                .putExtra(SelectedPartnerActivity.IS_ONLINE, false));

                    }

                    @Override
                    public void onBalloonShow(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonHide(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonAnimationStart(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonAnimationEnd(BalloonItem balloonItem) {

                    }
                });

                overlay.addOverlayItem(overlayItem);
                mOverlayManager.addOverlay(overlay);
                overlays.add(overlay);
            }
        }

        mMapController.notifyRepaint();
    }





    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        Toast.makeText(getActivity(), "Ошибка при передаче данных", Toast.LENGTH_SHORT).show();
        if(getActivity()!=null) ((StatesActivity) getActivity()).setActionBarLoading(false);
    }



}
