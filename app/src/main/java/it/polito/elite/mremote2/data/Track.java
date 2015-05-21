package it.polito.elite.mremote2.data;

/**
 * Created by bonino on 5/15/15.
 */
public class Track
{
    private int id;
    private String title;
    private String album;
    private String genre;
    private String artist;

    public Track(int id, String title, String album, String genre, String artist)
    {
        this.id = id;
        this.title = title;
        this.album = album;
        this.genre = genre;
        this.artist = artist;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    @Override
    public String toString()
    {
        return this.artist+" - "+this.title;
    }
}
