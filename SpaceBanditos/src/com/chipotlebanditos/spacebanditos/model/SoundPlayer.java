package com.chipotlebanditos.spacebanditos.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.chipotlebanditos.spacebanditos.R;

public class SoundPlayer {
    private SoundPool soundPool;
    private SparseIntArray soundsMap;
    public static final int SHOOTING_LASER_SOUND = 1;
    public static final int TAKING_DAMAGE_SOUND = 2;
    private Context context;
    
    public SoundPlayer(Context context) {
        this.context = context;
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundsMap = new SparseIntArray();
        soundsMap.put(SHOOTING_LASER_SOUND,
                soundPool.load(context, R.raw.laser_sound, 1));
        soundsMap.put(TAKING_DAMAGE_SOUND,
                soundPool.load(context, R.raw.explosion_sound, 1));
    }
    
    public void playSound(int sound) {
        AudioManager mgr = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        
        soundPool.play(soundsMap.get(sound), volume, volume, 1, 0, 1);
    }
}
