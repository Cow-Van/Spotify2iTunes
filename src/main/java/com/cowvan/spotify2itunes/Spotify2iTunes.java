package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.betterentity.BetterPlaylist;
import com.cowvan.spotify2itunes.betterentity.BetteriTunes;
import info.schnatterer.itunes4j.ITunesException;

public class Spotify2iTunes {
    public static void main(String[] args) throws ITunesException {
        BetteriTunes iTunes = new BetteriTunes();
        BetterPlaylist p = iTunes.createPlaylist("Foo");
    }
}
