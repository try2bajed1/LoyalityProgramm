package su.ias.malina.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import su.ias.malina.R;


public class BaseActivity extends ActionBarActivity {


    protected Dialog operationInProgressWindow;
    protected Dialog messageDialog;
    protected Dialog resultDialog;

//    protected long lastBackTouch;

    protected ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

//        lastBackTouch = 0;

        actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setIcon(R.drawable.action_bar_icon);
        }
    }




    @Override
    protected void onDestroy() {
        closeDialog(operationInProgressWindow);
        closeDialog(messageDialog);
        closeDialog(resultDialog);

        super.onDestroy();
    }


    protected void closeDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }



/*

    @Override
    public void onBackPressed() {
        askForExit();
    }
  protected void askForExit() {

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastBackTouch > 2000) {
            Toast.makeText(getApplicationContext(), R.string.exit_app_message, Toast.LENGTH_SHORT).show();
            lastBackTouch = currentTime;
        } else {
            exitApp();
        }
    }*/



    /**
     * Вызов прелоадера
     */
    protected void showOperationInProgressWindow() {
        operationInProgressWindow = new Dialog(this, R.style.Theme_Dialog_Translucent);
        operationInProgressWindow.setCancelable(false);
        operationInProgressWindow.setContentView(R.layout.window_operation_in_progress);
        operationInProgressWindow.show();
    }


    /**
     * Метод выводит на экран отменяемый диалог-оповещение с 2мя кнопками с
     * переданными титулом и текстом сообщения и действием для позитивной кнопки
     *
     * @param title
     * @param message
     */

    protected void showMessageDialog(String title, String message, final Runnable yesAction, boolean hasCancelBtn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                yesAction.run();
                dialog.dismiss();
            }
        });

        if (hasCancelBtn) {
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }

        messageDialog = builder.create();
        messageDialog.show();
    }



    /**
     * Метод выводит на экран отменяемый диалог-оповещение с 1й кнопкой с
     * переданными титулом и текстом сообщения
     *
     * @param title
     * @param message
     */
    protected void showResultDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        resultDialog = builder.create();
        resultDialog.show();
    }


    protected void showNoInternetMessage() {
        showResultDialog(getString(R.string.Error), getString(R.string.error_no_internet));
    }

    protected void showServerErrorMessage() {
        showResultDialog(getString(R.string.Error), getString(R.string.error_server));
    }




}
