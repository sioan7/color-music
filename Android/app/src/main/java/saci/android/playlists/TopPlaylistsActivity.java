package saci.android.playlists;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saci.android.ColorMusicApplication;
import saci.android.CustomPreferences;
import saci.android.R;
import saci.android.dtos.PlaylistDto;
import saci.android.dtos.SongDto;
import saci.android.liked.LikedListActivity;
import saci.android.network.PlaylistApi;
import saci.android.network.RestClient;
import saci.android.network.SongsApi;
import saci.android.playlists.adapter.TopPlaylistAdapter;
import saci.android.song.SongDetails;

/**
 * Created by corina on 28.06.2017.
 */

public class TopPlaylistsActivity extends AppCompatActivity {

    private String userId;
    private List<PlaylistDto> topPlaylistsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlists_list);

        SharedPreferences preferences = ColorMusicApplication.getSharedPreferences();

        userId = preferences.getString(CustomPreferences.USER_ID, new String());

        topPlaylistsList = new ArrayList<>();
        playlists();
    }

    private void playlists() {
        final PlaylistApi playlistApi = RestClient.getClient().create(PlaylistApi.class);
        playlistApi.getTopPlaylists().enqueue(new Callback<List<PlaylistDto>>() {
            @Override
            public void onResponse(Call<List<PlaylistDto>> call, Response<List<PlaylistDto>> response) {
                topPlaylistsList = response.body();

                createListAdapter();
            }

            @Override
            public void onFailure(Call<List<PlaylistDto>> call, Throwable t) {

            }
        });

    }

    private void createListAdapter() {
        ArrayAdapter adapter = new TopPlaylistAdapter(this, topPlaylistsList);

        ListView listView = (ListView) findViewById(R.id.playlists);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getSelectedItemId();

                SongsApi songsApi = RestClient.getClient().create(SongsApi.class);
                songsApi.getById(topPlaylistsList.get(i).getId()).enqueue(new Callback<SongDto>() {
                    @Override
                    public void onResponse(Call<SongDto> call, Response<SongDto> response) {
                        if (response.code() == 200) {
                            Intent detailsIntent = new Intent(TopPlaylistsActivity.this, SongDetails.class);
                            detailsIntent.putExtra("song", response.body());
                            startActivity(detailsIntent);
                        } else {
                            Toast.makeText(TopPlaylistsActivity.this, "Cannot find song!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SongDto> call, Throwable t) {
                        Toast.makeText(TopPlaylistsActivity.this, "Cannot find song!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }
}