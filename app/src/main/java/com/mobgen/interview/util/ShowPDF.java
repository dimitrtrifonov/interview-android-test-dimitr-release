package com.mobgen.interview.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.mobgen.interview.R;
import com.mobgen.interview.activity.MainActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by LenovoY700 on 12/18/2016.
 */

public class ShowPDF {

    AssetManager assetManager;
    Context context;
    String storageDirectoryName;
    String pdfAssetName;
    int FM_NOTIFICATION_ID=42;

    public ShowPDF(Context context, AssetManager assetManager, String pdfAssetName)
    {
        String storageDirectoryName=context.getResources().getString(R.string.app_name);//subdirectory where pdf will be stored

        this.storageDirectoryName=storageDirectoryName;
        this.assetManager=assetManager;
        this.context=context;
        this.pdfAssetName=pdfAssetName;

        storePdfToStorage();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + this.storageDirectoryName + "/" + pdfAssetName), "application/pdf");
        context.startActivity(intent);

        addNotification();
        Toast.makeText(this.context,this.pdfAssetName+" opened",Toast.LENGTH_SHORT).show();
    }

    private void storePdfToStorage(){ //store the pdf to storage so that external app could open it

        InputStream in = null;
        OutputStream out = null;

        String strDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator + this.storageDirectoryName;
        File fileDir = new File(strDir);
        fileDir.mkdirs();
        File file = new File(fileDir, this.pdfAssetName);

        try
        {
            in = this.assetManager.open(this.pdfAssetName);
            out = new BufferedOutputStream(new FileOutputStream(file));

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("ShowPDF", e.getMessage());
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

    private void addNotification() { //Display notification on pdf opening

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(this.pdfAssetName+" opened");

        Intent notificationIntent = new Intent(this.context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this.context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(FM_NOTIFICATION_ID, builder.build());
    }

}
