package su.ias.malina.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Класс - загрузчик изображений, загружает изображение с определённого адреса и
 * устанавливает его в ImageView
 *
  */



public class BitmapLoader extends AsyncTask<Void, Void, Void> {

    ImageView img;
    String urlString;
    Bitmap bitmap;

    /**
     * @param img       ImageView в которое надо установить загруженное изображение
     * @param urlString строка, содержащая URL адрес источника изображения
     */
    public BitmapLoader(ImageView img, String urlString) {
        super();
        this.img = img;
        this.urlString = urlString;

    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            // непосредственно процесс загрузки изображения
            bitmap = BitmapFactory.decodeStream(new URL(urlString).openConnection().getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }



    @Override
    protected void onPostExecute(Void result) {
        if (bitmap != null) {

            img.setImageBitmap(bitmap);


        }

        super.onPostExecute(result);
    }



}
