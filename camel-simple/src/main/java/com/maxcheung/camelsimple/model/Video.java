package com.maxcheung.camelsimple.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

  //  @Column(nullable = false)
    @Column(nullable = false, unique = true)
    private String videoId;

    @Column(nullable = false)
	private String title;

    @Column(nullable = false)
	private String data;
    
    public Video() {
        super();
    }
	public Video(String videoId, String title) {
		this.videoId = videoId;
		this.title = title;
		this.data = "Random video.";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
