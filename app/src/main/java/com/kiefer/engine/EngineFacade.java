package com.kiefer.engine;

import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

import nl.igorski.mwengine.MWEngine;
import nl.igorski.mwengine.core.Drivers;
import nl.igorski.mwengine.core.LPFHPFilter;
import nl.igorski.mwengine.core.Limiter;
import nl.igorski.mwengine.core.Notifications;
import nl.igorski.mwengine.core.ProcessingChain;
import nl.igorski.mwengine.core.SampleManager;
import nl.igorski.mwengine.core.SequencerController;
import nl.igorski.mwengine.core.JavaUtilities;

public class EngineFacade {
    /**
     * IMPORTANT : when creating native layer objects through JNI it
     * is important to remember that when the Java references go out of scope
     * (and thus are finalized by the garbage collector), the SWIG interface
     * will invoke the native layer destructors. As such we hold strong
     * references to JNI Objects during the application lifetime
     */
    private Limiter _limiter;
    private LPFHPFilter _lpfhpf;
    private MWEngine engine;
    private SequencerController _sequencerController;

    //private ArrayList<EngineTrack> tracks;

    private boolean _inited           = false;
    private boolean isPlaying = false;

    // AAudio is only supported from Android 8/Oreo onwards.
    private boolean _supportsAAudio = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O;
    private Drivers.types audioDriver;
    private String[] drivers;

    private int SAMPLE_RATE;
    private int BUFFER_SIZE;

    private float minFilterCutoff = 50.0f;
    private float maxFilterCutoff;

    //private int stepsPerMeasure;  // amount of subdivisions within a single measure
    private static String LOG_TAG = "MWENGINE"; // logcat identifier

    private LLPPDRUMS llppdrums;
    //private Thread notificationThread;


    private final int OUTPUT_CHANNELS = 2, BEAT_AMOUNT = 4, BEAT_UNIT = 4;
    //private int subs;

    public EngineFacade(LLPPDRUMS llppdrums, int tempo, int nOfSteps, String driver){
        //this.subs = subs;
        this.llppdrums = llppdrums;
        init(tempo, nOfSteps, driver);
    }

    int sequencerPosition;

    /** SETUP **/
    private void init(int tempo, int nOfSteps, String driver) {
        if ( _inited )
            return;

        /*
        //not sure if this is good. The idea is to keep updates in a separate thread. Remove if weird bugs occur!!!
        notificationThread = new Thread(new Runnable() {
            public void run() {
                llppdrums.handleSequencerPositionChange(sequencerPosition); //sequencerPosition gets updated in handleNotification()
            }
        });
        notificationThread.start(); //maybe start it when the sequencer starts? Will have to stop it somehow then.

         */

        // STEP 1 : preparing the native audio engine
        engine = new MWEngine(new StateObserver());
        MWEngine.optimizePerformance(llppdrums);

        // get the recommended buffer size for this device (NOTE : lower buffer sizes may
        // provide lower latency, but make sure all buffer sizes are powers of two of
        // the recommended buffer size (overcomes glitching in buffer callbacks )
        // getting the correct sample rate upfront will omit having audio going past the system
        // resampler reducing overall latency
        BUFFER_SIZE = MWEngine.getRecommendedBufferSize(llppdrums.getApplicationContext());
        SAMPLE_RATE = MWEngine.getRecommendedSampleRate(llppdrums.getApplicationContext());

        //set the audio driver
        drivers = new String[2];
        drivers[0] = "AAudio";
        drivers[1] = "OpenSL";

        if(driver != null){
            setDriver(driver);
            //setDriver("OpenSL");
        }
        else{
            //AAudio if supported
            audioDriver = _supportsAAudio ? Drivers.types.AAUDIO : Drivers.types.OPENSL;
            //setDriver("OpenSL");
        }

        //Log.e("EngineFacade.init()", "provided driver: "+driver);
        //Log.e("EngineFacade.init()", "driver: "+audioDriver);
        engine.createOutput(SAMPLE_RATE, BUFFER_SIZE, OUTPUT_CHANNELS, audioDriver);

        //tracks = new ArrayList<>();
        setup(tempo, nOfSteps);

        // STEP 3 : start your engine!
        // Starts engines render thread (NOTE: sequencer is still paused)
        // this ensures that audio will be output as appropriate (e.g. when
        // playing live events / starting sequencer and playing the song)
        //new Thread(new Runnable() { //thread for creating the Drawable
        //public void run() {
        //might be pointless or even destructive. Remove immediately if weird bugs start occurring!!!
        engine.start();
        //}
        //}).start();

        setupSamples();

        _inited = true;
    }

    protected void setup(int tempo, int nOfSteps) {
        _sequencerController = engine.getSequencerController();
        _sequencerController.setTempoNow(tempo, BEAT_AMOUNT, BEAT_UNIT); //tempo will be set by the activated sequence and can be anything here
        _sequencerController.updateMeasures( 1, nOfSteps); // we'll loop just a single measure with given subdivisions

        // cache some of the engines properties
        //final ProcessingChain masterBus = engine.getMasterBusProcessors();

        // create a lowpass filter to catch all low rumbling and a limiter to prevent clipping of output :)
        //_lpfhpf  = new LPFHPFilter(( float )  MWEngine.SAMPLE_RATE, 55, OUTPUT_CHANNELS );
        //_limiter = new Limiter( 10f, 500f, 0.6f );
        //masterBus.addProcessor( _lpfhpf );
        //masterBus.addProcessor( _limiter );
    }

    public void updateNOfSteps(){
        setSteps(llppdrums.getDrumMachine().getPlayingSequence().getNOfSteps());
        setTempo(llppdrums.getDrumMachine().getPlayingSequence().getTempo());
    }

    /** CONTROLS **/

    public void playSequencer(){
        engine.getSequencerController().setPlaying(true);
        isPlaying = true;
    }

    //sometimes paused when not playing (ex. changing steps while paused/stopped), the bool is used to prevent playing when the action is done
    public boolean pauseSequencer(){
        boolean wasPlaying = isPlaying;
        engine.getSequencerController().setPlaying(false);
        isPlaying = false;
        return wasPlaying;
    }

    public void stopSequencer(){
        engine.getSequencerController().setPlaying(false);
        engine.getSequencerController().rewind();
        isPlaying = false;
    }

    /** GET **/
    public int getStep(){
        return _sequencerController.getStepPosition();
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public boolean supportsAAudio(){
        return _supportsAAudio;
    }

    public String[] getDrivers() {
        return drivers;
    }

    public String getDriver(){
        if(audioDriver == Drivers.types.AAUDIO){
            //Log.e("..ineFacade.getDriver()", Drivers.types.AAUDIO.toString());
            return drivers[0];
        }
        else{
            //Log.e("..ineFacade.getDriver()", Drivers.types.OPENSL.toString());
            return drivers[1];
        }
    }

    public int getBEAT_AMOUNT() {
        return BEAT_AMOUNT;
    }

    public ProcessingChain getMasterProcessingChain(){
        return engine.getMasterBusProcessors();
    }

    public int getBEAT_UNIT() {
        return BEAT_UNIT;
    }

    public int getBUFFER_SIZE() {
        return BUFFER_SIZE;
    }

    public int getSAMPLE_RATE() {
        return SAMPLE_RATE;
    }

    /** SET **/

    public void setDriver(String selectedValue){
        //String selectedValue = parent.getItemAtPosition(pos).toString();
        //Log.e("..ineFacade.setDriver()", selectedValue);
        //_audioDriver = selectedValue.toLowerCase().equals(drivers[0]) ? Drivers.types.AAUDIO : Drivers.types.OPENSL;

        if(selectedValue.equals(drivers[0])){
            audioDriver = Drivers.types.AAUDIO;
        }
        else{
            audioDriver = Drivers.types.OPENSL;
        }
        engine.setAudioDriver(audioDriver);
    }

    public void setTempo(int tempo){
        //Log.e("Engine", "setTempo(), tempo: "+tempo * subs+", BEAT_AMOUNT: "+BEAT_AMOUNT * subs);
        _sequencerController.setTempo(tempo, BEAT_AMOUNT, BEAT_UNIT);
    }

    /*
    public void setSubs(int subs){
        this.subs = subs;
    }

     */

    public void setSteps(int steps){
        //Log.e("Engine", "setSteps(), steps: "+steps * subs);
        _sequencerController.updateMeasures(1, steps); // we'll loop just a single measure with given subdivisions
    }

    /** DESTRUCTION **/
    public void destroy(){
        flushSong();        // free memory allocated by song
        dispose();  // dispose the engine
        Log.d(LOG_TAG, "MWEngineFacade destroyed" );
    }

    private void flushSong() {
        // this ensures that Song resources currently in use by the engine are released

        engine.stop();

        // clear Vectors so all references to the events are broken
        /*
        for(EngineTrack track : tracks){
            track.destroy();
        }
        tracks.clear();

         */

        // detach all processors from engine's master bus
        engine.getMasterBusProcessors().reset();

        // and these (garbage collection invokes native layer destructors, so we'll let
        // these processors be cleared lazily)
        _lpfhpf = null;

        // flush sample memory allocated in the SampleManager
        SampleManager.flushSamples();
    }

    private void dispose(){
        engine.dispose();  // dispose the engine

    }

    /*
    private void postStats(int step){
        String tag = "STATS";
        if(step == 0) {
            Log.e(tag, "---------------------------------------------");

            for (int trackNo = 0; trackNo < tracks.size(); trackNo++) { //tracks
                EngineTrack engineTrack = tracks.get(trackNo);
                Log.e(tag, "track: "+trackNo+", oscillators: "+tracks.get(trackNo).getNOfInstruments());
                for (int oscillatorNo = 0; oscillatorNo < engineTrack.getOscillators().length; oscillatorNo++) { //instruments

                    Oscillator oscillator = engineTrack.getOscillators()[oscillatorNo];

                    if (oscillator != null) {
                        Log.e(tag, "track: "+trackNo+", oscillator: " + oscillatorNo + ", events: " + oscillator.getNoOfEvents());
                    }
                }
            }
        }
    }

     */

    /** sSTATE CHANGE MESSAGE LISTENER **/


    private class StateObserver implements MWEngine.IObserver {
        private final Notifications.ids[] _notificationEnums = Notifications.ids.values(); // cache the enumerations (from native layer) as int Array
        public void handleNotification( final int aNotificationId ) {
            switch ( _notificationEnums[ aNotificationId ]) {
                case ERROR_HARDWARE_UNAVAILABLE:
                    Log.d( LOG_TAG, "ERROR : received driver error callback from native layer" );
                    engine.dispose();
                    break;
                case MARKER_POSITION_REACHED:
                    Log.d( LOG_TAG, "Marker position has been reached" );
                    break;
                case RECORDING_COMPLETED:
                    Log.d( LOG_TAG, "Recording has completed" );
                    break;
            }
        }

        public void handleNotification( final int aNotificationId, final int aNotificationValue ) {
            switch ( _notificationEnums[ aNotificationId ]) {
                case SEQUENCER_POSITION_UPDATED:

                    // for this notification id, the notification value describes the precise buffer offset of the
                    // engine when the notification fired (as a value in the range of 0 - BUFFER_SIZE). using this value
                    // we can calculate the amount of samples pending until the next step position is reached
                    // which in turn allows us to calculate the engine latency

                    //we get UIthread-errors here without this. Maybe since the engine runs in its own thread?
                    sequencerPosition = _sequencerController.getStepPosition();
                    llppdrums.runOnUiThread(() -> {
                        llppdrums.handleSequencerPositionChange(sequencerPosition);
                    });
                    break;
                case RECORDED_SNIPPET_READY:
                    llppdrums.runOnUiThread(() -> {
                        // we run the saving on a different thread to prevent buffer under runs while rendering audio
                        engine.saveRecordedSnippet( aNotificationValue ); // notification value == snippet buffer index
                    });
                    break;
                case RECORDED_SNIPPET_SAVED:
                    Log.d( LOG_TAG, "Recorded snippet " + aNotificationValue + " saved to storage" );
                    break;
            }
        }
    }

    /** SAMPLES **/
    private void setupSamples(){
        //BASS
        loadWAVAsset("E-Mu-Proteus-FX-909-Kick.wav", "E-Mu-Proteus-FX-909-Kick");
        loadWAVAsset("E-Mu-Proteus-FX-Wacky-Kick.wav", "E-Mu-Proteus-FX-Wacky-Kick");
        loadWAVAsset("Dry-Kick.wav", "Dry-Kick");

        //SNARE
        loadWAVAsset("Ensoniq-ESQ-1-Snare.wav", "Ensoniq-ESQ-1-Snare");

        //HH
        loadWAVAsset("Cowbell-1.wav", "Cowbell-1");
        loadWAVAsset("Cowbell-2.wav", "Cowbell-2");

        //TOM
        loadWAVAsset("Electro-Tom.wav", "Electro-Tom");
        loadWAVAsset("Electronic-Tom-1.wav", "Electronic-Tom-1");
        loadWAVAsset("Electronic-Tom-2.wav", "Electronic-Tom-2");
        loadWAVAsset("Electronic-Tom-3.wav", "Electronic-Tom-3");
        loadWAVAsset("Electronic-Tom-4.wav", "Electronic-Tom-4");

        //MISC
        loadWAVAsset("Hand-Drum.wav", "Hand-Drum");
        loadWAVAsset("Hi-Bongo.wav", "Hi-Bongo");
        loadWAVAsset("High-Conga-2.wav", "High-Conga-2");
    }

    private void loadWAVAsset(String assetName, String sampleName) {
        JavaUtilities.createSampleFromAsset(
                sampleName, llppdrums.getAssets(), llppdrums.getCacheDir().getAbsolutePath(), assetName
        );
    }
}
