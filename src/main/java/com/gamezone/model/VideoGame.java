package com.gamezone.model;

/** Represents a video game product with platform and genre information. */
public class VideoGame extends Product {
    private static final long serialVersionUID = 1L;
    private String platform;
    private String genre;

    /** Creates a video game product. */
    public VideoGame(String id, String name, double price, int stock, String platform, String genre) {
        super(id, name, price, stock); setPlatform(platform); setGenre(genre);
    }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { if (platform == null || platform.isBlank()) throw new IllegalArgumentException("Platform is required."); this.platform = platform.trim(); }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { if (genre == null || genre.isBlank()) throw new IllegalArgumentException("Genre is required."); this.genre = genre.trim(); }
    @Override public String getDescription() { return "Video game | platform=" + platform + " | genre=" + genre; }
}
