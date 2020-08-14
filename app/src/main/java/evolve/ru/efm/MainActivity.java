package evolve.ru.efm;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2;
    SeekBar sbar;
    MediaPlayer player = new MediaPlayer();
    final int MAX_VOLUME = 100;
    public int player_volume = 100;

    private void initializeMediaPlayer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            player.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build());
        } else {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        try {
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            String url = "http://45.141.102.187:8000/evolve";
            player.setDataSource(url);
            player.setVolume(player_volume, player_volume);
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseMediaPlayer() {
        player.stop();
    }

    private void setVolumePlayer(float volume) {
        player.setVolume(volume, volume);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.button2);

        btn2 = findViewById(R.id.button3);

        btn2.setVisibility(View.INVISIBLE);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeMediaPlayer();
                btn1.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.VISIBLE);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMediaPlayer();
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.INVISIBLE);
            }
        });

        sbar = findViewById(R.id.seekBar);
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        player_volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

        int set_progress = (int) (1 + (Math.log(MAX_VOLUME - player_volume) * Math.log(MAX_VOLUME)));

        sbar.setProgress(set_progress);

        sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float set_volume = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                setVolumePlayer(set_volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
