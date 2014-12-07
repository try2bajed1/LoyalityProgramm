package su.ias.malina.interfaces;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 02.06.2014
 * Time: 12:54
 */
public interface IListener {

    public void responseCompleteHandler(String actionName, String jsonFromApi);
    public void responseErrorHandler(String actionName, String errorStr);


   /* //if cookies are expired
    public void authorizeUser();
*/

}
