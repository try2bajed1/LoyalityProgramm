package su.ias.malina.utils;

import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verifier {

	public static final String RESULT_OK = "OK";

	public static String VerifyPassword(String login, String password) {

		// проверка на совпадения пароля с логином
		if (login.equals(password)) {
			return AppSingleton.get().getString(
					R.string.error_login_and_password_are_the_same);
		}

		// проверка на допустимую длину
		if (password.length() < 4 || password.length() > 20) { // потом поменять мин длину на 6
			return AppSingleton.get().getString(
					R.string.error_wrong_password_length);
		}

		// проверка на допустимые символы
		Pattern allowableSymbols = Pattern.compile("[\\w]{" + password.length() + "}");

		Matcher matcher = allowableSymbols.matcher(password);
		if (!matcher.find()) {
			return AppSingleton.get().getString(
					R.string.error_wrong_password_symbols);
		}

		return RESULT_OK;

	}


}
