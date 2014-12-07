package su.ias.malina.data;

import android.content.res.Resources;
import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;

import java.util.LinkedHashMap;

public class AccountData {

    private String fio;
    private String region;
    private String registrationDate;
    private String account_number;
    private String status;
    private String pointsAvaliable;
    private String rubsAvailable;
    private String burningPoints;

    private String userMobileNum; //
    private String bonusPoints; //


    public AccountData(String fio, String region, String registrationDate, String account_number, String status, String pointsAvaliable, String rubsAvailable, String burningPoints) {

        this.fio = fio;
        this.region = region;
        this.registrationDate = registrationDate;
        this.account_number = account_number;
        this.status = status;
        this.pointsAvaliable = pointsAvaliable;
        this.rubsAvailable = rubsAvailable;
        this.burningPoints = burningPoints;

    }



    public AccountData() {

    }


    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getFio() {
        return fio;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPointsAvaliable(String pointsAvaliable) {
        this.pointsAvaliable = pointsAvaliable;
    }

    public String getPointsAvaliable() {
        return pointsAvaliable== null ? "0":pointsAvaliable;
    }

    public void setRubsAvailable(String rubsAvailable) {
        this.rubsAvailable = rubsAvailable;
    }

    public String getRubsAvailable() {
        return rubsAvailable;
    }

    public void setBurningPoints(String burningPoints) {
        this.burningPoints = burningPoints;
    }

    public String getBurningPoints() {
        return burningPoints;
    }


    public LinkedHashMap<String, String> getAccountParams() {

        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

        Resources resources = AppSingleton.get().getResources();
        params.put(resources.getString(R.string.account_number), account_number);
        params.put(resources.getString(R.string.account_fio), fio);
        params.put(resources.getString(R.string.account_registration_date), registrationDate);
        params.put(resources.getString(R.string.account_status), status);
        params.put(resources.getString(R.string.account_region), region);
        params.put(resources.getString(R.string.account_points), pointsAvaliable);
        params.put(resources.getString(R.string.account_burning_points), burningPoints);
        params.put(resources.getString(R.string.account_exchange_points), rubsAvailable);

        return params;
    }

    public String getUserMobileNum() {
        return userMobileNum;
    }

    public void setUserMobileNum(String userMobileNum) {
        this.userMobileNum = userMobileNum;
    }

    public String getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(String bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
}
