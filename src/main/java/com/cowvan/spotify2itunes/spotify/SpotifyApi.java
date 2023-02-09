package com.cowvan.spotify2itunes.spotify;

import com.cowvan.spotify2itunes.utils.ParseUtils;
import com.cowvan.spotify2itunes.utils.RequestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SpotifyApi {
    private final String clientId;
    private final String clientSecret;
    private String accessToken;
    private double accessTokenExpireTime;

    public SpotifyApi(String clientId, String clientSecret) throws URISyntaxException, IOException, InterruptedException {
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        updateAccessToken();
    }

    public JSONObject getPlaylistData(String playlistId) throws URISyntaxException, IOException, InterruptedException {
        checkAccessToken();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + accessToken);

        HttpResponse<String> response = RequestUtils.getRequest("https://api.spotify.com/v1/playlists/" + playlistId, headers, (String) null);

        return ParseUtils.parseJSONStringToJSONObject(response.body());
    }

    public Playlist getPlaylist(String playlistId) throws URISyntaxException, IOException, InterruptedException {
        ArrayList<Song> songs = new ArrayList<>();

        JSONObject playlistData = getPlaylistData(playlistId);
        String name = playlistData.getString("name");
        JSONArray tracks = playlistData.getJSONObject("tracks").getJSONArray("items");

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

            songs.add(new Song(trackName, trackArtists.toArray(new String[0]), trackImage));
        }

        return new Playlist(name, songs.toArray(new Song[0]));
    }

    private void updateAccessToken() throws URISyntaxException, IOException, InterruptedException {
        String credentials = clientId + ":" + clientSecret;
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8)));

        HttpResponse<String> response = RequestUtils.postRequest("https://accounts.spotify.com/api/token", headers, "grant_type=client_credentials");
        JSONObject body = ParseUtils.parseJSONStringToJSONObject(response.body());

        accessToken = body.getString("access_token");
        accessTokenExpireTime = new Date().getTime() / 1000d + body.getInt("expires_in");
    }

    private void checkAccessToken() throws URISyntaxException, IOException, InterruptedException {
        if (new Date().getTime() / 1000d > accessTokenExpireTime) {
            updateAccessToken();
        }
    }
}
