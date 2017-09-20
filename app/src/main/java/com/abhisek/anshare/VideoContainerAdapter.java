package com.abhisek.anshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by bapu on 2/26/2017.
 */

public class VideoContainerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<VideoData> data = Collections.emptyList();
    List<VideoData> filterList = new ArrayList<VideoData>();
    int logoCount;
    String taskId, taskDetails;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    int positionFinal;


   // SessionManagement session;
    ProgressDialog progress;

    public final String TAG= "viewpager";
    //LinearLayout profileImgParent;

    // create constructor to innitilize context and data sent from MainActivity
    public VideoContainerAdapter(Context context, List<VideoData> data) {

        Log.i("Method","SampleContainerAdapter");

            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
          //  this.filterList = new ArrayList<SampleData>();
            filterList.addAll(this.data);

        Log.i("filterList", String.valueOf(filterList.size()));

    }

    public VideoContainerAdapter(List<VideoData> data) {
//        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.video_container, parent, false);
        TaskHolder taskHolder = new TaskHolder(view);
        //profileImgParent = (LinearLayout)view.findViewById(R.id.profileImagell);
        return taskHolder;
    }

    public void removeAt(int position) {
        Log.i("method", "removeAt");
        filterList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, filterList.size());
    }





    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
            Log.i("position" , String.valueOf(position));
        // Log.i("Method","onBindViewHolder");
        final TaskHolder myTaskHolder = (TaskHolder) holder;
        final VideoData current = filterList.get(position);




    }


    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);

        //return 2;
    }
    @Override
    public int getItemViewType(int position) {
        return (position == filterList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }





    class TaskHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        TextView textSampleName;
        TextView textcustName, textStyleCode;
        TextView textId, textCreated, textUpdated,buttonViewOption;
        LinearLayout profileImgParent;

// create constructor to get widget reference

        public TaskHolder(View itemViewTask) {
            super(itemViewTask);


            itemViewTask.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

         /*   Intent intent = new Intent(context, SampleDetailsActivity.class);
           String containerId = textId.getText().toString();
            intent.putExtra("sampleContainerId", containerId);
            context.startActivity(intent);*/

        }
    }

    public void showProgressBar() {

        //   ProgressDialog progress;

        progress = new ProgressDialog(context);
        // progress.setTitle("Please Wait!!");
        progress.setMessage("Loading..Please Wait..");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }


    public void add(VideoData mc) {
        filterList.add(mc);
        Log.i("filteradd", String.valueOf(filterList.size()));
        notifyItemInserted(filterList.size() - 1);
    }

    public void addAll(List<VideoData> mcList) {
        for (VideoData mc : mcList) {
            add(mc);
        }

    }

    public void remove(VideoData city) {
        int position = data.indexOf(city);
        if (position > -1) {
           filterList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        isLoadingAdded = false;
        data.clear();
        filterList.clear();
        /*while (getItemCount() > 0) {
            remove(getItem(0));
        }*/
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        //add(new SampleData());
    }
    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = filterList.size()-1;
        VideoData item = getItem(position);

        if (item != null) {
            filterList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public VideoData getItem(int position) {
        return filterList.get(position);
    }


    private class taskResponse extends AsyncTask<Void, Void, String> {

        String status;


        @Override
        protected String doInBackground(Void... urls) {
            DataOutputStream outputStream = null;
            HttpURLConnection httpURLConnection = null;
            try {


                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {

                        stringBuilder.append(line).append("\n");
                    }

                    Log.i("response1", stringBuilder.toString());
                    bufferedReader.close();
                    Log.i("response", stringBuilder.toString());
                    return stringBuilder.toString();

                } finally {

                    status= httpURLConnection.getResponseMessage();
                    Log.i("status", String.valueOf(status));

                    Log.i("urlConnect", String.valueOf( httpURLConnection.getErrorStream()));
                    httpURLConnection.disconnect();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
              //  Log.e(TAG, "Error sending ID token to backend.", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {

            Log.i("taskCreateResponse", response);

        }
    }



}
