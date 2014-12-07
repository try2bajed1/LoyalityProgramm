package su.ias.malina.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import su.ias.malina.R;
import su.ias.malina.db.DBAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 28.05.13
 * Time: 10:35
 * класс
 */
public class FilteredPointsCursorAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;



    public FilteredPointsCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }




    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

    	String distanceUnit = "м";
    	 String distanceValue;
        
    	String partnerName = cursor.getString(cursor.getColumnIndex(DBAdapter.FLD_NAME));
        String adress      = cursor.getString(cursor.getColumnIndex(DBAdapter.FLD_ADDRESS));
        double distance = cursor.getInt(cursor.getColumnIndex(DBAdapter.FLD_DISTANCE));
        
        if (distance > 1000){
        	
        	distanceUnit = "км";
        	distance /= 1000;
        	distanceValue = String.valueOf((Math.round(distance * 10))/10d);
        	
        } else {
        	
        	distanceValue = String.valueOf(Math.round(distance));
        }
        
        

        TextView partnerNameTV = (TextView) view.findViewById(R.id.partner_name);
        partnerNameTV.setText(partnerName);

        TextView addressTV = (TextView) view.findViewById(R.id.address);
        addressTV.setText(adress);

        TextView distanceTV = (TextView) view.findViewById(R.id.distance);
        
       
        
        
        distanceTV.setText(distanceValue + " " + distanceUnit);

    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.layout_places_as_list_listitem, parent, false);
        return view;
    }


}

























