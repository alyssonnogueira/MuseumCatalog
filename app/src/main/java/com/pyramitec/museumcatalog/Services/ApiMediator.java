package com.pyramitec.museumcatalog.Services;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by alyss on 02/12/2017.
 */

public class ApiMediator {
/*
    private final String mEmail;
    private final String mPassword;
    private final String LOG_TAG = ApiMediator.class.getSimpleName();
    BufferedReader reader = null;
    String responseJson = null;
    //private ActModel mAtividade;
    private Context mContext;
    //private long idActivity;
    private String IP ="http://45.55.92.219/api/";  //TODO: SERVIDOR -> "http://45.55.92.219/api/";  MAQUINA LOCAL-> "http://192.168.0.15/api/";

    public ApiMediator(String email, String password, Context context, String command) {
        mEmail = email;
        mPassword = password;
        //mAtividade = atividade;
        //idActivity = atividade.getIdActivity();
        mContext = context;
        if (command.equals("POST")){
            new Sender().execute();
        }else{
            if(command.equals("GET")){
                new Receiver().execute();
            }
        }
    }

    private class Sender extends AsyncTask<Void, Integer, Boolean> {
        BufferedReader reader = null;
        String responseJson = null;

        private List<AccModel> dadosAcelerometro;
        private List<GyrModel> dadosgiroscopio;
        private List<GpsModel> dadosGps;

        DatabaseMediator mediator;

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        final NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId;
        int mTotalData;
        int mSendData;
        PowerManager.WakeLock wl;

        int qtSend = 500;
        int totalRemoveAcc = 0;
        int totalRemoveGyr = 0;
        int totalRemoveGps = 0;
        @Override
        protected void onPreExecute() {
            mBuilder.setContentTitle("Sincronizando atividades com o servidor")
                    .setContentText("Contabilizando atividades não sincronizadas")
                    .setSmallIcon(R.drawable.quantum_ic_refresh_white_24)
                    .setOngoing(true)
                    .setVisibility(1)
                    .setUsesChronometer(true)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));

            notificationManager.notify(notificationId, mBuilder.build());

            try {
                Uri som = RingtoneManager.getDefaultUri(2);
                Ringtone toque = RingtoneManager.getRingtone(mContext, som);
                toque.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PowerManager pm = (PowerManager) mContext.getSystemService(mContext.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Active Life");
            wl.acquire();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mediator = new DatabaseMediator(mContext);
            List<ActModel> actModelList = mediator.getDataActs();
            if(actModelList.size() == 0) {
                Log.i(LOG_TAG, "Nenhuma atividade a enviar");
                return false;
            }

            for (int i = 0; i < actModelList.size(); i++) {
                mTotalData = (actModelList.get(i).getAccModels().size() + actModelList.get(i).getGyrModels().size() +
                        actModelList.get(i).getGpsModels().size());
                mSendData = 0;
                if (actModelList.get(i).getIdActivityMongoDB().equals("0")) {
                    publishProgress(0, (int) actModelList.get(i).getIdActivity());
                    mBuilder.setProgress(mTotalData, mSendData, false);
                    mBuilder.setContentTitle("Sincronizando Atividade: " + (int) actModelList.get(i).getIdActivity());
                    mBuilder.setContentText("Dados: " + mSendData + "/" + mTotalData);
                    notificationManager.notify(notificationId, mBuilder.build());
                    if (sendActivity(actModelList.get(i), "POST")) {
                        //publishProgress(1, actModelList.get(i).getAccModels().size());
                        try {
                            if (sendAccData(actModelList.get(i))) {
                                //publishProgress(2, actModelList.get(i).getGyrModels().size());
                                try {
                                    if (sendGyrData(actModelList.get(i))) {
                                        //publishProgress(3, actModelList.get(i).getGpsModels().size());
                                        try {
                                            if (!sendGpsData(actModelList.get(i))) {
                                                Log.i(LOG_TAG, "Dados do Gps não enviados");
                                                return false;
                                            }
                                            publishProgress(4);
                                        }catch (Exception e){ e.printStackTrace(); }
                                    } else{
                                        Log.i(LOG_TAG, "Dados do Giroscopio não enviados");
                                        return false;
                                    }
                                }catch (Exception e) { e.printStackTrace(); }
                            } else {
                                Log.i(LOG_TAG, "Dados do Acelerometro não enviados");
                                return false;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else{
                        Log.i(LOG_TAG, "Atividade não criada");
                        return false;
                    }
                } else {
                    publishProgress(0, (int) actModelList.get(i).getIdActivity());
                    mBuilder.setProgress(mTotalData, mSendData, false);
                    mBuilder.setContentTitle("Atualizando Atividade: " + (int) actModelList.get(i).getIdActivity());
                    mBuilder.setContentText("Dados: " + mSendData + "/" + mTotalData);
                    notificationManager.notify(notificationId, mBuilder.build());
                    if (sendActivity(actModelList.get(i), "PUT")) {
                        //publishProgress(1, actModelList.get(i).getAccModels().size());
                        if (sendAccData(actModelList.get(i))) {
                            //publishProgress(2, actModelList.get(i).getGyrModels().size());
                            if (sendGyrData(actModelList.get(i))) {
                                //publishProgress(3, actModelList.get(i).getGpsModels().size());
                                if (!sendGpsData(actModelList.get(i))) {
                                    Log.i(LOG_TAG, "Dados do Gps não enviados");
                                    return false;
                                }
                            } else {
                                Log.i(LOG_TAG, "Dados do Giroscopio não enviados");
                                return false;
                            }
                        } else {
                            Log.i(LOG_TAG, "Dados do Acelerometro não enviados");
                            return false;
                        }
                    } else {
                        Log.i(LOG_TAG, "Atividade não criada");
                        return false;
                    }
                }
            }
            mediator.close();
            return true;
        }

        private Boolean sendActivity(ActModel actModel, String Method){
            // TODO: attempt authentication against a network service.
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(IP+"activity/"); // http://192.168.0.15/api/activity/ && http://www.myactive.life/app/dados.php
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json"); //charset=UTF-8 authenticated.put("request", "post-json");
                urlConnection.setConnectTimeout(30000);
                urlConnection.setReadTimeout(30000);

                urlConnection.setRequestMethod(Method);

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                JSONObject authenticated = new JSONObject();
                authenticated.put("email", mEmail);
                authenticated.put("password", mPassword);
                JSONObject atividade = new JSONObject();

                //Atividade
                if(actModel.getModality() == null)
                    atividade.put("modality", "Outra"); //TODO: Alterar Hardcode
                else
                    atividade.put("modality", actModel.getModality());

                atividade.put("distance", actModel.getDistance());
                atividade.put("vigorousActivity", actModel.getVigorousActivity());
                atividade.put("moderateActivity", actModel.getModerateActivity());
                atividade.put("lightActivity", actModel.getLightActivity());
                atividade.put("timeMvpa", actModel.getTimeMvpa());
                atividade.put("totalTime",actModel.getTimestamp());
                atividade.put("time", actModel.getTimestamp());
                Log.e(LOG_TAG, "Objeto Atividade: " + actModel.toString());

                //JSON
                authenticated.put("atividade", atividade);

                String result = authenticated.toString();
                out.write(result);

                out.close();

                // Lê a o input stream (entrada) como uma string
                InputStream inputStream;
                try{
                    inputStream = urlConnection.getInputStream();
                } catch (Exception e) {
                    try {
                        int responseCode = urlConnection.getResponseCode();
                        Log.e(LOG_TAG, "Erro de Resposta: " + responseCode);
                        if (responseCode >= 400 && responseCode < 500)
                            inputStream = urlConnection.getErrorStream();
                        else throw e;
                        e.printStackTrace();
                    } catch (Exception es) {
                        es.printStackTrace();
                        throw es;
                    }
                }

                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return false;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                Log.e(LOG_TAG, "resposta: " + reader);
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    // Como o buffer está vazio, retornamos null
                    return false;
                }

                // Atribui o buffer como string à variável que armazena a resposta
                responseJson = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Erro: ", e);
                // Como houve uma excessão, retornamos null
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    // Se houve conexão, fechamos
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    // Se o reader não estiver nulo ainda, tentamos fechar
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        // Se houver excessão ao fechar, escrevemos um Log
                        Log.e(LOG_TAG, "Erro fechando o (BufferedReader) reader", e);
                    }
                }
            }
            try {
                JSONObject respostaJson = new JSONObject(responseJson);
                if ("Ok".equals(respostaJson.getString("status"))){
                    DatabaseMediator mediator = new DatabaseMediator(mContext);
                    mediator.setIdActivityMongoDB(actModel, respostaJson.getString("IdActivityMongoDB"));
                    Log.e(LOG_TAG, "Mongo ID: " + actModel.getIdActivityMongoDB());
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // TODO: register the new account here.
            return true;
        }
        private Boolean sendAccData(ActModel actModel) {
            //for(int i = 0; i < actModel.getAccModels().size(); i+=10) { //Envia de 100 em 100 dados (Pode chegar a centenas de milhares, talvez ter que aumentar o i
            int totalSize = actModel.getAccModels().size();
            int cont = 0;
            while (actModel.getAccModels().size() > 0){//for(int i = 0; i < actModel.getAccModels().size(); i+= qtSend) {//while (actModel.getAccModels().size() > 0){
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(IP + "dados/"); // http://192.168.0.15/api/activity/ && http://www.myactive.life/app/dados.php
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    urlConnection.setRequestProperty("Content-Type", "application/json"); //charset=UTF-8 authenticated.put("request", "post-json");
                    //urlConnection.setConnectTimeout(30000);
                    //urlConnection.setReadTimeout(30000);
                    urlConnection.setRequestMethod("POST");

                    OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                    JSONObject authenticated = new JSONObject();
                    authenticated.put("email", mEmail);
                    authenticated.put("password", mPassword);
                    authenticated.put("IdActivityMongoDB", actModel.getIdActivityMongoDB());
                    JSONObject acelerometro = new JSONObject();
                    JSONArray acelerometroArray = new JSONArray();
                    //int i = 0; //(j < i+100) &&
                    Log.e(LOG_TAG, "Comprimindo JSON");
                    for (int j = 0; (j < qtSend) && (j < actModel.getAccModels().size()); j++) {
                        acelerometro.put("xAccelerometer", actModel.getAccModels().get(j).getxAccelerometer());
                        acelerometro.put("yAccelerometer", actModel.getAccModels().get(j).getyAccelerometer());
                        acelerometro.put("zAccelerometer", actModel.getAccModels().get(j).getzAccelerometer());
                        acelerometroArray.put(j, acelerometro);
                    }
                    //JSON
                    authenticated.put("acelerometroArray", acelerometroArray);
                    //Log.e(LOG_TAG, "Conferindo Objeto: " + authenticated);

                    String result = authenticated.toString();
                    out.write(result);
                    out.close();

                    // Lê a o input stream (entrada) como uma string
                    InputStream inputStream;
                    try {
                        Log.e(LOG_TAG, "Esperando resposta do servidor");
                        inputStream = urlConnection.getInputStream();
                    } catch (Exception e) {
                    /*inputStream = urlConnection.getInputStream();
                    e.printStackTrace();*/
                    /*    try {
                            int responseCode = urlConnection.getResponseCode();
                            Log.e(LOG_TAG, "Erro de Resposta: " + responseCode);
                            if (responseCode >= 400 && responseCode < 500)
                                inputStream = urlConnection.getErrorStream();
                            else throw e;
                            e.printStackTrace();
                        } catch (Exception es) {
                            es.printStackTrace();
                            throw es;
                        }
                    }

                    StringBuffer buffer = new StringBuffer();

                    if (inputStream == null) {
                        // Se a resposta for null, retornamos null
                        return false;
                    }
                    Log.e(LOG_TAG, "Decodificando Mensagem");
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    Log.e(LOG_TAG, "resposta: " + reader);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    if (buffer.length() == 0) {
                        return false;
                    }
                    responseJson = buffer.toString();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Erro: ", e);
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Erro fechando o (BufferedReader) reader", e);
                        }
                    }
                }
                try {
                    JSONObject respostaJson = new JSONObject(responseJson);
                    if ("Ok".equals(respostaJson.getString("status"))) {
                        Log.e(LOG_TAG, "Pacote de ACC Data enviado ao Server");
                    } else {
                        return false;
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                //Remove dados já enviados
                //cont = 0;
                Log.e(LOG_TAG, "Removendo dados locais");
                mediator.getRealmInstance().beginTransaction();
                for(int j = 0; (j < qtSend) && (j < actModel.getAccModels().size()); j++) {
                    //actModel.removeAccModel(actModel.getAccModels().get(j));
                    actModel.getAccModels().get(j).deleteFromRealm();
                    cont++;
                }
                mediator.getRealmInstance().commitTransaction();
                mSendData = cont; //totalRemoveAcc*qtSend;
                mBuilder.setProgress(mTotalData, mSendData, false);
                mBuilder.setContentText("Dados: " + mSendData + "/" + mTotalData);
                notificationManager.notify(notificationId, mBuilder.build());
                Log.e(LOG_TAG, "Parte do processo concluido");
                //publishProgress(5, cont, (totalSize - cont));
            }
            //Remove ACC
            Log.i(LOG_TAG, "Atividade totalmente enviada");
            /*mediator.getRealmInstance().beginTransaction();
            for(int i = 0; (i < totalRemoveAcc* qtSend) && (i < actModel.getAccModels().size()); i++) {
                //actModelList.get(i).removeAccModel(actModelList.get(i).getAccModels().get(j));
                actModel.getAccModels().get(i).deleteFromRealm();
            }*/
            /*mediator.getRealmInstance().commitTransaction();
            return true;
        }
        private Boolean sendGyrData(ActModel actModel) {
            //for(int i = 0; i < actModel.getGyrModels().size(); i+=10){
            if(actModel.getGyrModels() == null)
                return true;
            int totalSize = actModel.getGyrModels().size();
            int cont = 0;
            while (actModel.getGyrModels().size() > 0){
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(IP+"dados"); // http://192.168.0.15/api/activity/ && http://www.myactive.life/app/dados.php
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    urlConnection.setRequestProperty("Content-Type", "application/json"); //charset=UTF-8 authenticated.put("request", "post-json");
                    //urlConnection.setConnectTimeout(30000);
                    //urlConnection.setReadTimeout(30000);
                    urlConnection.setRequestMethod("POST");

                    OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                    JSONObject authenticated = new JSONObject();
                    authenticated.put("email", mEmail);
                    authenticated.put("password", mPassword);
                    authenticated.put("IdActivityMongoDB", actModel.getIdActivityMongoDB());
                    JSONObject giroscopio = new JSONObject();
                    JSONArray giroscopioArray = new JSONArray();

                    //Giroscopio
                    //int i = 0; //(j < i+100) &&
                    for(int j = 0; (j < qtSend) && (j < actModel.getGyrModels().size()); j++) {
                        giroscopio.put("xGyroscope", actModel.getGyrModels().get(j).getxGyroscope());
                        giroscopio.put("yGyroscope", actModel.getGyrModels().get(j).getyGyroscope());
                        giroscopio.put("zGyroscope", actModel.getGyrModels().get(j).getzGyroscope());
                        giroscopioArray.put(j, giroscopio);
                    }

                    authenticated.put("giroscopioArray", giroscopioArray);

                    String result = authenticated.toString();
                    out.write(result);
                    out.close();

                    InputStream inputStream;
                    try{
                        inputStream = urlConnection.getInputStream();
                    } catch (Exception e) {
                        try {
                            int responseCode = urlConnection.getResponseCode();
                            Log.e(LOG_TAG, "Erro de Resposta: " + responseCode);
                            if (responseCode >= 400 && responseCode < 500)
                                inputStream = urlConnection.getErrorStream();
                            else throw e;
                            e.printStackTrace();
                        } catch (Exception es) {
                            es.printStackTrace();
                            throw es;
                        }
                    }

                    StringBuffer buffer = new StringBuffer();

                    if (inputStream == null) {
                        return false;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    //Log.e(LOG_TAG, "resposta: " + reader);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    if (buffer.length() == 0) {
                        return false;
                    }
                    responseJson = buffer.toString();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Erro: ", e);
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Erro fechando o (BufferedReader) reader", e);
                        }
                    }
                }
                try {
                    JSONObject respostaJson = new JSONObject(responseJson);
                    if ("Ok".equals(respostaJson.getString("status"))){
                        Log.e(LOG_TAG, "Pacote de Gyroscópio Data enviado ao Servidor");
                    } else {
                        return false;
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                //Remove dados já enviados
                cont = 0;
                mediator.getRealmInstance().beginTransaction();
                for(int j = 0; (j < qtSend) && (j < actModel.getGyrModels().size()); j++) {
                    //actModel.removeGyrModel(actModel.getGyrModels().get(j));
                    actModel.getGyrModels().get(j).deleteFromRealm();
                    cont++;
                }
                mediator.getRealmInstance().commitTransaction();
                mSendData += cont;
                mBuilder.setProgress(mTotalData, mSendData, false);
                mBuilder.setContentText("Dados: " + mSendData + "/" + mTotalData);
                notificationManager.notify(notificationId, mBuilder.build());
                //publishProgress(5, cont, (totalSize - cont));
            }
            return true;
        }
        private Boolean sendGpsData(ActModel actModel) {
            //for(int i = 0; i < actModel.getGpsModels().size(); i+=10){
            if(actModel.getGpsModels() == null)
                return true;
            int totalSize = actModel.getGpsModels().size();
            int cont = 0;
            while(actModel.getGpsModels().size() > 0){
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(IP+"gps"); // http://192.168.0.15/api/activity/ && http://www.myactive.life/app/dados.php
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    urlConnection.setRequestProperty("Content-Type", "application/json"); //charset=UTF-8 authenticated.put("request", "post-json");
                    //urlConnection.setConnectTimeout(30000);
                    //urlConnection.setReadTimeout(30000);
                    urlConnection.setRequestMethod("POST");

                    OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                    JSONObject authenticated = new JSONObject();
                    authenticated.put("email", mEmail);
                    authenticated.put("password", mPassword);
                    authenticated.put("IdActivityMongoDB", actModel.getIdActivityMongoDB());
                    JSONObject gps = new JSONObject();
                    JSONArray gpsArray = new JSONArray();

                    //Gps
                    //int i = 0; //(j < i+100) &&
                    for(int j = 0; (j < qtSend) && (j < actModel.getGpsModels().size()); j++) {
                        gps.put("xGps", actModel.getGpsModels().get(j).getxGps());
                        gps.put("yGps", actModel.getGpsModels().get(j).getyGps());
                        gps.put("Speed", actModel.getGpsModels().get(j).getSpeed());
                        gps.put("timestamp", actModel.getGpsModels().get(j).getTimestamp());
                        gpsArray.put(j, gps);
                        //cont++;
                    }
                    //JSON
                    authenticated.put("gpsArray", gpsArray);

                    String result = authenticated.toString();
                    out.write(result);
                    out.close();

                    InputStream inputStream;
                    try{
                        inputStream = urlConnection.getInputStream();
                    } catch (Exception e) {
                        try {
                            int responseCode = urlConnection.getResponseCode();
                            Log.e(LOG_TAG, "Erro de Resposta: " + responseCode);
                            if (responseCode >= 400 && responseCode < 500)
                                inputStream = urlConnection.getErrorStream();
                            else throw e;
                            e.printStackTrace();
                        } catch (Exception es) {
                            es.printStackTrace();
                            throw es;
                        }
                    }

                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return false;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    //Log.e(LOG_TAG, "resposta: " + reader);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    if (buffer.length() == 0) {
                        return false;
                    }

                    responseJson = buffer.toString();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Erro: ", e);
                    // Como houve uma excessão, retornamos null
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Erro fechando o (BufferedReader) reader", e);
                        }
                    }
                }
                try {
                    JSONObject respostaJson = new JSONObject(responseJson);
                    if ("Ok".equals(respostaJson.getString("status"))){
                        Log.e(LOG_TAG, "Pacote de dados do GPS enviado Server");
                        //return true;
                    } else {
                        return false;
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                //Remove dados já enviados
                cont = 0;
                mediator.getRealmInstance().beginTransaction();
                for(int j = 0; (j < qtSend) && (j < actModel.getGpsModels().size()); j++) {
                    //actModel.removeGpsModel(actModel.getGpsModels().get(j));
                    actModel.getGpsModels().get(j).deleteFromRealm();
                    cont++;
                }
                mediator.getRealmInstance().commitTransaction();

                mSendData += cont;
                mBuilder.setProgress(mTotalData, mSendData, false);
                mBuilder.setContentText("Dados: " + mSendData + "/" + mTotalData);
                notificationManager.notify(notificationId, mBuilder.build());
                //publishProgress(5, cont, (totalSize - cont));
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //switch (values[0]) {
            if(values[0] == 0)
                Toast.makeText(mContext, "Criando Atividade " + String.valueOf(values[1]) + " no Servidor: ", Toast.LENGTH_SHORT).show();
            if(values[0] == 1)
                Toast.makeText(mContext, "Atividade Criada.. Enviando " + String.valueOf(values[1]) + " dados de acelerometria..", Toast.LENGTH_SHORT).show();
            if(values[0] == 2)
                Toast.makeText(mContext, "Dados de Acelerometria enviados.. Enviando " + String.valueOf(values[1]) + " dados do giroscópio..", Toast.LENGTH_SHORT).show();
            if(values[0] == 3)
                Toast.makeText(mContext, "Dados do giroscópio enviados.. Enviando "+ String.valueOf(values[1]) + " dados do Gps..", Toast.LENGTH_SHORT).show();
            if(values[0] == 4)
                Toast.makeText(mContext, "Dados do Gps Enviados", Toast.LENGTH_SHORT).show();
            if(values[0] == 5)
                Toast.makeText(mContext, String.valueOf(values[1]) + "dados enviados, restam:" + String.valueOf(values[2]), Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(Boolean sucesso) {

            wl.release();


            if (sucesso) {
                mBuilder.setContentTitle("Atividades Sincronizadas")
                        .setProgress(0,0,false);
                mBuilder.setOngoing(false);
                notificationManager.notify(notificationId, mBuilder.build());
                DatabaseMediator mediator = new DatabaseMediator(mContext);
                mediator.resetSensors();
                //mediator.resetDatabase();
                Log.i(LOG_TAG, "Dados no servidor");
                Toast.makeText(mContext, "Dados no servidor", Toast.LENGTH_SHORT).show();
                mediator.close();
            } else {
                mBuilder.setContentTitle("Atividades não sincronizadas, verifique a sua internet e tente novamente")
                        .setProgress(0,0,false);
                mBuilder.setOngoing(false);
                notificationManager.notify(notificationId, mBuilder.build());
                Toast.makeText(mContext, "Sem conexão, dados não enviados", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent();
            intent.addFlags(1);
            intent.setAction("REFRESH_SCREEN");
            mContext.sendBroadcast(intent);

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(mContext, "Sem conexão, atividade não criada", Toast.LENGTH_SHORT).show();
        }
    }

    private class Receiver extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://www.myactive.life/app/index.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestMethod("POST");

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

                JSONObject authenticated = new JSONObject();
                authenticated.put("email", mEmail);
                authenticated.put("password", mPassword);
                authenticated.put("request", "post-json");

                String result = authenticated.toString();
                out.write(result);

                out.close();

                // Lê a o input stream (entrada) como uma string
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    Long.valueOf(0);
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                Log.e(LOG_TAG, "resposta:" + reader);
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Json: " + line);
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    return false;
                }
                // Atribui o buffer como string à variável que armazena a resposta
                responseJson = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Erro: ", e);
                // Como houve uma excessão, retornamos null
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    // Se houve conexão, fechamos
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    // Se o reader não estiver nulo ainda, tentamos fechar
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        // Se houver excessão ao fechar, escrevemos um Log
                        Log.e(LOG_TAG, "Erro fechando o (BufferedReader) reader", e);
                    }
                }
            }
            try {
                return saveData(responseJson);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // TODO: register the new account here.
            return false;
        }

        Boolean saveData(String atividade) throws JSONException {
            try {
                return true;
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                System.out.println("Finish!");
            } else {
                Toast.makeText(mContext, "Sem conexão, atividade não criada", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(mContext, "Sem conexão, atividade não criada", Toast.LENGTH_SHORT).show();
        }
    } */
}
