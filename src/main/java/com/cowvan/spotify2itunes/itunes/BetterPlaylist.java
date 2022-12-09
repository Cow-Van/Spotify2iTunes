package com.cowvan.spotify2itunes.itunes;

import com4j.itunes.IITUserPlaylist;
import info.schnatterer.itunes4j.entity.Playlist;

public class BetterPlaylist extends Playlist {
    private final IITUserPlaylist wrappedPlaylist;

    public BetterPlaylist(IITUserPlaylist wrappedPlaylist) throws NoSuchFieldException, IllegalAccessException {
        super(wrappedPlaylist);

        this.wrappedPlaylist = wrappedPlaylist;
    }

    public void delete() {
        wrappedPlaylist.delete();
    }
}
