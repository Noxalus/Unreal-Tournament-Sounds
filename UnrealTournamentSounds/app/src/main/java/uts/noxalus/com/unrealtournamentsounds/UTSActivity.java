package uts.noxalus.com.unrealtournamentsounds;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UTSActivity extends Activity {

    private int[] _soundsResources;
    private String[] _soundsTitles;
    private int _currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uts);

        _currentPosition = 0;
        // We store the resource id of each sound
        _soundsResources = new int[]
        {
            R.raw.headshot,
            R.raw.firstblood,
            R.raw.doublekill,
            R.raw.multikill,
            R.raw.ultrakill,
            R.raw.monsterkill,
            R.raw.killingspree,
            R.raw.rampage,
            R.raw.dominating,
            R.raw.unstoppable,
            R.raw.godlike,
            R.raw.lostmatch,
            R.raw.flagtaken,
            R.raw.armor_pick_up,
            R.raw.ammo_pick_up,
            R.raw.player_respawn,
            R.raw.item_respawn,
            R.raw.sniper_fire
        };

        // We create the sound list (text)
        ListView listView = (ListView) findViewById(R.id.sound_list);
        _soundsTitles = getResources().getStringArray(R.array.array_sound_titles);

        CustomAdapter adapter = new CustomAdapter(this, R.layout.sound_list_layout, _soundsTitles);
        listView.setAdapter(adapter);

        // Long click listener => show dialog to set the sound as ringtone/notification
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                CustomAdapter adapter = (CustomAdapter) adapterView.getAdapter();
                SetSoundAsDialogFragment dialog = new SetSoundAsDialogFragment(
                        adapter.context, _soundsResources[pos], _soundsTitles[pos]);

                dialog.setArguments(new Bundle());

                FragmentManager fm = getFragmentManager();

                dialog.show(fm, "SET_SOUND_AS");

                return true;
            }
        });

        // Click listener => play the selected sound
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                CustomAdapter adapter = (CustomAdapter) adapterView.getAdapter();

                _currentPosition = pos;

                // We load the selected sound and we play it
                MediaPlayer mp = MediaPlayer.create(adapter.context, _soundsResources[pos]);
                mp.start();

                // When the sound is finished
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        // We don't forget to release it !
                        mp.release();

                        /*
                        // Share with social networks
                        SharedPreferences pref = getSharedPreferences("shared_preferences", MODE_PRIVATE);
                        int shareTypeId = pref.getInt("SHARE_TYPE", -1);
                        if (shareTypeId > -1)
                        {
                            String name = _soundsTitles[_currentPosition];
                            String subject = "[UTS] Guess what I've just listened?";
                            String content = "The sound \"" + name + "\" thanks to the \"Unreal Tournament Sounds\" application! #UnrealTournamentSounds";
                            //shareIntent(ShareTypes[shareTypeId], subject, content);
                        }
                        */
                    };
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ut, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
