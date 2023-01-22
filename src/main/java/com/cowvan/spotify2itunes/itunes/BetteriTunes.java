package com.cowvan.spotify2itunes.itunes;

import com4j.itunes.*;
import info.schnatterer.itunes4j.ITunes;
import info.schnatterer.itunes4j.ITunesException;

public class BetteriTunes extends ITunes {
    private final IiTunes iTunes;

    public BetteriTunes() {
        iTunes = ClassFactory.createiTunesApp();
    }

    @Override
    public BetterPlaylist createPlaylist(String playlistName) throws ITunesException {
        IITPlaylist playlist = iTunes.createPlaylist(playlistName);

        if (ITPlaylistKind.ITPlaylistKindUser.equals(playlist.kind())) {
            return new BetterPlaylist(playlist.queryInterface(IITUserPlaylist.class));
        } else {
            throw new ITunesException(
                    "Created playlist was of unexpected type \""
                            + playlist.kind() + "\". Expected \""
                            + ITPlaylistKind.ITPlaylistKindUser.name() + "\"");
        }
    }
}
