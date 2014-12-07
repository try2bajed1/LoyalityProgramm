package su.ias.malina.data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 03.03.14
 * Time: 11:56
 */
//todo: implement builder pattern
public class PartnerDataSimplified implements Serializable {

    private int id;
    private String logoUrl;
    private String partnerName;
    private String pointsRule;
    private String type;

    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";


    public PartnerDataSimplified() {

    }



    public PartnerDataSimplified(int id, String partnerName, String logoUrl, String pointsRule, String type) {
        this.id = id;
        this.partnerName = partnerName;
        this.logoUrl = logoUrl;
        this.pointsRule = pointsRule;
        this.type = type;
    }


    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPointsRule() {
        return pointsRule;
    }

    public void setPointsRule(String pointsRule) {
        this.pointsRule = pointsRule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
