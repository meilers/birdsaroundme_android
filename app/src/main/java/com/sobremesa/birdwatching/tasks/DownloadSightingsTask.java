package com.sobremesa.birdwatching.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.sobremesa.birdwatching.BAMApplication;
import com.sobremesa.birdwatching.clients.DownloadSightingsClient;
import com.sobremesa.birdwatching.database.SightingTable;
import com.sobremesa.birdwatching.managers.EbirdApiClientManager;
import com.sobremesa.birdwatching.models.remote.RemoteSighting;
import com.sobremesa.birdwatching.providers.BAMContentProvider;
import com.sobremesa.birdwatching.synchronizers.SightingSynchronizer;
import com.sobremesa.birdwatching.util.SyncUtil;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by omegatai on 2014-06-17.
 */
public class DownloadSightingsTask extends AsyncTask<Double, Void, Void> {
    @Override
    protected Void doInBackground(Double... params) {

        Context context = BAMApplication.getContext();

        DownloadSightingsClient client = EbirdApiClientManager.getInstance().getClient(context, DownloadSightingsClient.class);

        try {
            List<RemoteSighting> sightings = client.downloadSightings(params[0], params[1], 50, 30, "json");

            Cursor localSightingCursor = context.getContentResolver().query(BAMContentProvider.Uris.SIGHTINGS_URI, SightingTable.ALL_COLUMNS, null, null, null);
            localSightingCursor.moveToFirst();
            SyncUtil.synchronizeRemoteSightings(sightings, localSightingCursor,
                    localSightingCursor.getColumnIndex(SightingTable.SCI_NAME), localSightingCursor.getColumnIndex(SightingTable.LOC_ID), localSightingCursor.getColumnIndex(SightingTable.OBS_DT),
                    new SightingSynchronizer(context), null);
            localSightingCursor.close();



        } catch (RetrofitError e) {
            Log.d("worked,", "wor");

        }

        return null;
    }
}
