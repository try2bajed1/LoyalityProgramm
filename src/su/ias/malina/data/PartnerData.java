package su.ias.malina.data;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 24.02.14
 * Time: 17:41
 */
public class PartnerData {


    private int id;
    private String name;
    private String code;
    private String description;
    private String region;
    private String logoUrl;
    private boolean isCardEmitter;
    private boolean hasTransactions;
    private boolean hasPoints;
    private boolean showOnMapFlag;
    private Integer order;

    private String points_rule;
    private String type;


    public PartnerData() {

    }



    public PartnerData(int id, String name, String code, String description, String region, String logoUrl, boolean isCardEmitter, boolean hasTransactions, boolean hasPoints, boolean showOnMapFlag, int order, String type) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.region = region;
        this.logoUrl = logoUrl;
        this.isCardEmitter = isCardEmitter;
        this.hasTransactions = hasTransactions;
        this.hasPoints = hasPoints;
        this.order = order;
        this.showOnMapFlag = showOnMapFlag;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Boolean isCardEmitter() {
        return isCardEmitter;
    }

    public void setIsCardEmitter(Boolean isCardEmitter) {
        this.isCardEmitter = isCardEmitter;
    }

    public Boolean hasTransactions() {
        return hasTransactions;
    }

    public void setHasTransactions(Boolean hasTransactions) {
        this.hasTransactions = hasTransactions;
    }

    public Boolean hasPoints() {
        return hasPoints;
    }

    public void setHasPoints(Boolean hasPoints) {
        this.hasPoints = hasPoints;
    }
    

    public String getPointsRule() {
		return points_rule;
	}

    public void setPointsRule(String points_rule) {
		this.points_rule = points_rule;
	}



	public boolean getShowOnMapFlag() {
        return showOnMapFlag;
    }

    public void setShowOnMapFlag(boolean showOnMapFlag) {
        this.showOnMapFlag = showOnMapFlag;
    }


}
