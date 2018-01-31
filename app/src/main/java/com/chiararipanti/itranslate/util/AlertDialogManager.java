package com.chiararipanti.itranslate.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author chiararipanti
 *         date 04/05/2013
 */
public class AlertDialogManager {
    Boolean ok;

    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     *                - pass null if you don't want icon
     */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        ok = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ok = true;
                    }
                });
        alertDialog.setNegativeButton("Annulla",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ok = false;
                    }
                });
        //Titolo della finestra di dialogo
        alertDialog.setTitle(title);
        //Messaggio mostrato
        alertDialog.setMessage(message);

        //Visualizzazione della finestra di dialogo
        alertDialog.show();

    }
}
