package it.polito.elite.mremote2.fragments;

/**
 * Created by bonino on 5/15/15.
 */
public interface OnTrackSelectedListener {
    /**
     * Called when a track in the given list position is selected
     * @param position The position of the selected track entry
     */
    public void onTrackSelected(int position, int trackId);
}
