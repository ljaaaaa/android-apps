package lilja.kiiski.doitlist;

import android.app.AlertDialog;
import android.content.Context;

/* MYDIALOG
- To keep task variable with pop up window
- Helper Tool
 */
public class MyDialog extends AlertDialog.Builder {
    Task task;

    protected MyDialog(Context context) {
        super(context);
    }
}