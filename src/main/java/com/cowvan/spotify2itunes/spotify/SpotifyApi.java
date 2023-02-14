package com.cowvan.spotify2itunes.spotify;

import com.cowvan.spotify2itunes.utils.ParseUtils;
import com.cowvan.spotify2itunes.utils.RequestUtils;
import com.cowvan.spotify2itunes.utils.StringUtils;
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

    public PlaylistData getPlaylistData(String playlistId) throws URISyntaxException, IOException, InterruptedException {
        ArrayList<SongData> songsData = new ArrayList<>();

        JSONObject playlistData = getPlaylistJSON(playlistId);
        String name = playlistData.getString("name");
        JSONArray tracks = playlistData.getJSONObject("tracks").getJSONArray("items");

        for (Object object : tracks) {
            JSONObject track = (JSONObject) object;
            JSONObject trackInfo = track.getJSONObject("track");

            String trackName = trackInfo.getString("name");
            String[] trackArtists = getArtistsName(trackInfo.getJSONObject("album").getJSONArray("artists"));
            String albumName = trackInfo.getJSONObject("album").getString("name");
            String[] albumArtists = getArtistsName(trackInfo.getJSONObject("album").getJSONArray("artists"));
            String trackImageUrl = trackInfo.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");

            songsData.add(new SongData(trackName, StringUtils.removeIllegalFilenameCharacters(trackName), trackArtists, StringUtils.removeIllegalFilenameCharactersFromStrings(trackArtists),
                    albumName, StringUtils.removeIllegalFilenameCharacters(albumName), albumArtists, StringUtils.removeIllegalFilenameCharactersFromStrings(albumArtists), trackImageUrl));
        }

        return new PlaylistData(name, StringUtils.removeIllegalFilenameCharacters(name), songsData.toArray(new SongData[0]));
    }

    private String[] getArtistsName(JSONArray artists) {
        String[] artistsName = new String[artists.length()];

        for (int i = 0; i < artistsName.length; i++) {
            artistsName[i] = artists.getJSONObject(i).getString("name");
        }

        return artistsName;
    }

    private JSONObject getPlaylistJSON(String playlistId) throws URISyntaxException, IOException, InterruptedException {
        checkAccessToken();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + accessToken);

        HttpResponse<String> response = RequestUtils.getRequest("https://api.spotify.com/v1/playlists/" + playlistId, headers, (String) null);

        return ParseUtils.parseJSONStringToJSONObject(response.body());
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
