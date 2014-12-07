package su.ias.malina.data;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 04.03.14
 * Time: 16:33
 */
public class UserCardItem {


    private String code;
    private String status;
    private boolean  isActive;
    private String type;
    private boolean  irsEnabled;
    private String beelinePhoneNum;
    private String barCodeUrl;



    public UserCardItem(String code, boolean isActive, String type, boolean irsEnabled, String beelinePhoneNum, String barCodeUrl) {
        this.code = code;
    //    this.status = status;
        this.isActive = isActive;
        this.type = type;
        this.irsEnabled = irsEnabled;
        this.beelinePhoneNum = beelinePhoneNum;
        this.barCodeUrl = barCodeUrl;
    }


    public UserCardItem() {

    }

    public UserCardItem(String type, String code) {
        this.type = type;
        this.code = code;
    }


    public UserCardItem(String type, String code, boolean irsEnabled, String beelinePhoneNum, String status) {
        this.type = type;
        this.code = code;
        this.irsEnabled = irsEnabled;
        this.beelinePhoneNum = beelinePhoneNum;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean getIrsEnabled() {
        return irsEnabled;
    }

    public void setIrsEnabled(boolean irsEnabled) {
        this.irsEnabled = irsEnabled;
    }

    public String getBeelinePhoneNum() {
        return beelinePhoneNum;
    }

    public void setBeelinePhoneNum(String beelinePhoneNum) {
        this.beelinePhoneNum = beelinePhoneNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getBarCodeUrl() {
        return barCodeUrl;
    }

    public void setBarCodeUrl(String barCodeUrl) {
        this.barCodeUrl = barCodeUrl;
    }
}
