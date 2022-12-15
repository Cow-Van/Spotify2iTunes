package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.spotify.SpotifyApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Spotify2iTunes {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
//        BetteriTunes iTunes = new BetteriTunes();
        SpotifyApi spotifyApi = new SpotifyApi(System.getenv("SPOTIFY_CLIENT_ID"), System.getenv("SPOTIFY_CLIENT_SECRET"));

        JSONArray tracks = spotifyApi.getPlaylist("1OeL09zxHjIxafVJjFrB55").getJSONArray("items");

        for (Object object : tracks) {
            JSONObject track = (JSONObject) object;
            JSONObject trackInfo = track.getJSONObject("track");

            String trackName = trackInfo.getString("name");
            ArrayList<String> trackArtists = new ArrayList<>();

            JSONArray trackArtistsData = trackInfo.getJSONArray("artists");

            for (Object trackArtistData : trackArtistsData) {
                JSONObject trackArtist = (JSONObject) trackArtistData;
                trackArtists.add(trackArtist.getString("name"));
            }

            String trackImage = trackInfo.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");

            System.out.println(trackName);
            System.out.println(Arrays.toString(trackArtists.toArray()));
            System.out.println(trackImage);
        }
    }
}
