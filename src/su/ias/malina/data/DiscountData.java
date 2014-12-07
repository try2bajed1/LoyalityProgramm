package su.ias.malina.data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 04.09.2014
 * Time: 13:03
 */
public class DiscountData implements Serializable {

    private String participateUrl;
    private String logoUrl;
    private String rules;
    private String description;

    private String content;
    private String offerUrl;
    private String offerType;
    private String sliderImgUrl;
    private String startDate;
    private String endDate;
    private String title;
    private String sliderDescr;
    private String sliderTitle;
    private int id ;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public String getParticipateUrl() {
        return participateUrl;
    }

    public void setParticipateUrl(String participateUrl) {
        this.participateUrl = participateUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfferUrl() {
        return offerUrl;
    }

    public void setOfferUrl(String offerUrl) {
        this.offerUrl = offerUrl;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getSliderImgUrl() {
        return sliderImgUrl;
    }

    public void setSliderImgUrl(String sliderImgUrl) {
        this.sliderImgUrl = sliderImgUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSliderDescr() {
        return sliderDescr;
    }

    public void setSliderDescr(String sliderDescr) {
        this.sliderDescr = sliderDescr;
    }

    public String getSliderTitle() {
        return sliderTitle;
    }

    public void setSliderTitle(String sliderTitle) {
        this.sliderTitle = sliderTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /*
    {
        "participate_url": "http://promo.malina.ru/actions/apply.php?action_id=3193&card=6393000000081010",
            "logo_url": "http://malina.ru/media/partners/admitad-logo.png",
            "rules": null,
            "description": "Скидки до -50% на определенные группы товаров.",
            "offer_url": "http://malina.ru/redirect/?brand=akuserstvo&cardnumber=6393000000081010&directory=http://ad.admitad.com/goto/9f26cba2079b082de664748f778371/",
            "offer_type": "online",
            "partner_ids": [393],
            "slider_image": "http://malina.ru/media/partners/small_banner_226_2.jpg",
            "end_date": "2014-12-31T19:59:59+00:00",
            "start_date": "2013-12-08T20:00:00+00:00",
            "title": "Скидки до -50% ",
            "slider_desc": null,
            "id": 3193,
            "slider_title": null
    }
*/


}
