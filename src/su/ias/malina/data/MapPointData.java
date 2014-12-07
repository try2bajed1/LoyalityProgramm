package su.ias.malina.data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 27.02.14
 * Time: 12:30
 */
public class MapPointData implements Serializable {

    private double latitude;
    private double longtude;

    private double distance;
    private final String partnerName;
    private String name;
    private String address;
    private int partner_id;
    private String cardUsage;
    private String metro;
    private String logoUrl;


    public void setPartnerName(String name) {
        this.name = name;
    }

    public String getPartnerName() {
        return partnerName;
    }


    public MapPointData(double latitude, double longtude,String partnerName,  String name, int distance, String address, int partner_id, String cardUsage, String metro, String logoUrl) {

        this.latitude = latitude;
        this.longtude = longtude;
        this.partnerName = partnerName;
        this.name = name;
        this.address = address;
        this.partner_id = partner_id;
        this.cardUsage = cardUsage;
        this.metro = metro;
        this.logoUrl = logoUrl;
    }





/*
    private double getDistanceFromMeInMeters(float lat_my, float lng_my, float lat_point, float lng_point) {

        float pk = (float) (180/3.14159);

        float a1 = lat_my / pk;
        float a2 = lng_my / pk;
        float b1 = lat_point / pk;
        float b2 = lng_point / pk;

        float t1 = FloatMath.cos(a1)*FloatMath.cos(a2)*FloatMath.cos(b1)*FloatMath.cos(b2);
        float t2 = FloatMath.cos(a1)*FloatMath.sin(a2)*FloatMath.cos(b1)*FloatMath.sin(b2);
        float t3 = FloatMath.sin(a1)*FloatMath.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }
*/


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtude() {
        return longtude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getCardUsage() {
        return cardUsage;
    }

    public void setCardUsage(String cardUsage) {
        this.cardUsage = cardUsage;
    }

    public String getMetro() {
        return metro;
    }

    public void setMetro(String metro) {
        this.metro = metro;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    public String getMapPointName() {
        return name;
    }

    public void setMapPointName(String mapPointName) {
        this.name = mapPointName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
