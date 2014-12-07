package su.ias.malina.data;

import java.util.LinkedHashMap;

import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;
import android.content.res.Resources;

public class UserData {

	private String birthDate;
	private String BuildingNo;
	private String city;
	private String cityType;
	private String codeword;
	private String district;
	private String email;
	private String flatNo;
	private String homePhone;
	private String houseFrac;
	private String houseNo;
	private String mobilPhone;
	private String postcode;
	private String street;

	public UserData() {

	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDay) {
		this.birthDate = birthDay;
	}

	public String getBuildingNo() {
		return BuildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		BuildingNo = buildingNo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityType() {
		return cityType;
	}

	public void setCityType(String cityType) {
		this.cityType = cityType;
	}

	public String getCodeword() {
		return codeword;
	}

	public void setCodeword(String codeword) {
		this.codeword = codeword;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getHouseFrac() {
		return houseFrac;
	}

	public void setHouseFrac(String houseFrac) {
		this.houseFrac = houseFrac;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String hoseNo) {
		this.houseNo = hoseNo;
	}

	public String getMobilPhone() {
		return mobilPhone;
	}

	public void setMobilPhone(String mobilPhone) {
		this.mobilPhone = mobilPhone;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Метод возвращает строку с адресом пользователя, состоящим из имеющихся
	 * данных
	 * 
	 * @return
	 */
	public String getUserAddress() {

		String address = "";

		String[] addressUnits = { postcode, district, new String(cityType + " " + city).trim(), street,
				houseNo, houseFrac, flatNo };

		for (String string : addressUnits) {

			if (string != null && string.length() > 0 && !string.equals("null")) {

				if (address.length() > 0) {
					address += ", ";
				}

				address += string;
			}
		}
		
		return address;

	}

	public LinkedHashMap<String, String> getUserDataParams() {

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		Resources resources = AppSingleton.get().getResources();
		params.put(resources.getString(R.string.user_birth_date), birthDate);
		params.put(resources.getString(R.string.user_codeword), codeword);
		params.put(resources.getString(R.string.user_email), email);
		params.put(resources.getString(R.string.user_mobil_phone), mobilPhone);
		params.put(resources.getString(R.string.user_home_phone), homePhone);
		params.put(resources.getString(R.string.user_address), getUserAddress());

		return params;
	}

}
