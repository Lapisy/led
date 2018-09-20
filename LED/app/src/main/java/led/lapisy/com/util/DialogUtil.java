package led.lapisy.com.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.ProgressBar;

import led.lapisy.com.led.R;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/3/24
 * Desc  :
 */

public class DialogUtil {
    public static AlertDialog showDialog(Context pContext, @StringRes int pTitle, @StringRes int pMsg, DialogInterface.OnClickListener pOk, DialogInterface.OnClickListener pCancel) {
        String title = pTitle == 0 ? null : pContext.getResources().getString(pTitle);
        String message = pMsg == 0 ? null : pContext.getResources().getString(pMsg);
        return showDialog(pContext, title, message, pOk, pCancel);
    }

    /**
     * 显示系统默认的dialog
     *
     * @param pContext
     * @param pTitle
     * @param pMsg
     * @param pOk
     * @param pCancel
     */
    public static AlertDialog showDialog(Context pContext, String pTitle, String pMsg, DialogInterface.OnClickListener pOk, final DialogInterface.OnClickListener pCancel) {
        final AlertDialog dialog = new AlertDialog.Builder(pContext).create();
        if (!TextUtils.isEmpty(pTitle))
            dialog.setTitle(pTitle);

        if (!TextUtils.isEmpty(pMsg))
            dialog.setMessage(pMsg);

        if (null != pOk) {
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, pContext.getResources().getString(R.string.dialog_ok), pOk);
        }

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, pContext.getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (pCancel != null)
                    pCancel.onClick(dialogInterface, i);
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    /**
     * 显示不带取消按钮的dialog
     *
     * @param pContext
     * @param pTitle
     * @param pMsg
     * @param pOk
     */
    public static AlertDialog showNoCancelDialog(Context pContext, @StringRes int pTitle, @StringRes int pMsg, DialogInterface.OnClickListener pOk) {
        String title = pTitle == 0 ? null : pContext.getResources().getString(pTitle);
        String message = pMsg == 0 ? null : pContext.getResources().getString(pMsg);
        return showNoCancelDialog(pContext, title, message, pOk);
    }

    /**
     * 显示不带取消按钮的dialog
     *
     * @param pContext
     * @param pTitle
     * @param pMsg
     * @param pOk
     */
    public static AlertDialog showNoCancelDialog(Context pContext, String pTitle, String pMsg, DialogInterface.OnClickListener pOk) {
        final AlertDialog dialog = new AlertDialog.Builder(pContext).create();
        if (!TextUtils.isEmpty(pTitle))
            dialog.setTitle(pTitle);

        if (!TextUtils.isEmpty(pMsg))
            dialog.setMessage(pMsg);

        if (null != pOk)
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, pContext.getResources().getString(R.string.dialog_ok), pOk);
        dialog.show();
        return dialog;
    }

    public static Dialog showProgressDialog(Context context, @StringRes int msg) {
        return showProgressDialog(context, context.getString(msg));
    }

    public static Dialog showProgressDialog(Context context, String msg) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.show();
        return dialog;
    }

}
