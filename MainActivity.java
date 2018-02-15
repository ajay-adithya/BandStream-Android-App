package com.example.ajayadithya.bandstream;

import java.lang.ref.WeakReference;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandGsrEvent;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.SampleRate;
import com.microsoft.band.sensors.HeartRateConsentListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import java.util.Timer;
import java.util.TimerTask;




public class MainActivity extends Activity {

    private BandClient client = null;
    private Button btnStart, btnConsent;
    private TextView txtStatus;
    Firebase base;
    BandInfo[] devices;
    final Handler handler = new Handler();

    Timer timer=new Timer();
    TimerTask timerTask;




    private BandGsrEventListener mGsrEventListener = new BandGsrEventListener() {
        @Override
        public void onBandGsrChanged(final BandGsrEvent event) {
            if (event != null) {
                //appendToUI(String.format("Resistance = %d kOhms\n", event.getResistance()));


                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
                        String strDate = sdf.format(c.getTime());
                        String base_url = "https://sensordata-88f95.firebaseio.com/";
                        base = new Firebase(base_url).child(devices[0].getName()+devices[0].getMacAddress());
                        base.child("GSR").child(strDate).setValue(event.getResistance(), new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    Log.e("Write_error", firebaseError.toString());
                                } else {
                                    Log.e("Firebase", "wrote");
                                }
                            }
                        });


            }
        }
    };

    private BandAccelerometerEventListener mAccelerometerEventListener = new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(final BandAccelerometerEvent event) {
            if (event != null) {
                appendToUI(String.format(" X = %.3f \n Y = %.3f\n Z = %.3f", event.getAccelerationX(),
                        event.getAccelerationY(), event.getAccelerationZ()));


                String acceleration = String.valueOf(event.getAccelerationX()) + " ";
                        acceleration += String.valueOf(event.getAccelerationY()) + " ";
                        acceleration += String.valueOf(event.getAccelerationZ());

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
                        String strDate = sdf.format(c.getTime());
                        String base_url = "https://sensordata-88f95.firebaseio.com/";
                        base = new Firebase(base_url).child(devices[0].getName()+devices[0].getMacAddress());
                        base.child("Accelerometer").child(strDate).setValue(acceleration, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    Log.e("Write_error", firebaseError.toString());
                                } else {
                                    Log.e("Firebase", "wrote");
                                }
                            }
                        });





            }
        }
    };

    private BandHeartRateEventListener mHeartRateEventListener = new BandHeartRateEventListener() {
        @Override
        public void onBandHeartRateChanged(final BandHeartRateEvent event) {
            if (event != null) {
               // appendToUI(String.format("Heart Rate = %d beats per minute\n"
                      //  + "Quality = %s\n", event.getHeartRate(), event.getQuality()));
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("EST"));
                String strDate = sdf.format(c.getTime());
                String base_url = "https://sensordata-88f95.firebaseio.com/";
                base = new Firebase(base_url).child(devices[0].getName()+devices[0].getMacAddress());
                base.child("HeartRate").child(strDate).setValue(event.getHeartRate(), new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Log.e("Write_error", firebaseError.toString());
                        } else {
                            Log.e("Firebase", "wrote");
                        }
                    }
                });
            }
        }
    };
    private BandSkinTemperatureEventListener mSkinTemperatureEventListener = new BandSkinTemperatureEventListener() {
        @Override
        public void onBandSkinTemperatureChanged(final BandSkinTemperatureEvent event) {
            if (event != null) {
                // appendToUI(String.format("Heart Rate = %d beats per minute\n"
                //  + "Quality = %s\n", event.getHeartRate(), event.getQuality()));
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("EST"));
                String strDate = sdf.format(c.getTime());
                String base_url = "https://sensordata-88f95.firebaseio.com/";
                base = new Firebase(base_url).child(devices[0].getName()+devices[0].getMacAddress());
                base.child("skinTemp").child(strDate).setValue(event.getTemperature(), new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Log.e("Write_error", firebaseError.toString());
                        } else {
                            Log.e("Firebase", "wrote");
                        }
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");
                new GsrSubscriptionTask().execute();
            }
        });

        final WeakReference<Activity> reference = new WeakReference<Activity>(this);

        btnConsent = (Button) findViewById(R.id.btnConsent);
        btnConsent.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                new HeartRateConsentTask().execute(reference);
            }
        });

        Firebase.setAndroidContext(getApplicationContext());




    }

    @Override
    protected void onResume() {
        super.onResume();
        //txtStatus.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (client != null) {
            try {
                client.getSensorManager().unregisterGsrEventListener(mGsrEventListener);
            } catch (BandIOException e) {
                appendToUI(e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (client != null) {
            try {
                client.disconnect().await();
            } catch (InterruptedException e) {
                // Do nothing as this is happening during destroy
            } catch (BandException e) {
                // Do nothing as this is happening during destroy
            }
        }
        super.onDestroy();
    }

    private class GsrSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
                    if (hardwareVersion >= 20) {
                        appendToUI("Band is connected.\n");
                        client.getSensorManager().registerGsrEventListener(mGsrEventListener);
                        client.getSensorManager().registerAccelerometerEventListener(mAccelerometerEventListener,SampleRate.MS128);
                        if(client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                             client.getSensorManager().registerHeartRateEventListener(mHeartRateEventListener);
                        }
                        else {
                            appendToUI("You have not given this application consent to access heart rate data yet."
                                    + " Please press the Heart Rate Consent button.\n");
                        }
                        client.getSensorManager().registerSkinTemperatureEventListener(mSkinTemperatureEventListener);




                    } else {
                        appendToUI("The Gsr sensor is not supported with your Band version. Microsoft Band 2 is required.\n");
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }


    private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
        @Override
        protected Void doInBackground(WeakReference<Activity>... params) {
            try {
                if (getConnectedBandClient()) {

                    if (params[0].get() != null) {
                        client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                            @Override
                            public void userAccepted(boolean consentGiven) {
                            }
                        });
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }

    private void appendToUI(final String string) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText(string);
            }
        });
    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.\n");
                return false;
            }
            client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);



        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        appendToUI("Band is connecting...\n");
        return ConnectionState.CONNECTED == client.connect().await();
    }
}