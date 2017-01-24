package com.bb.ragnar;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Series {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private Date downloadDate;
    private Integer currentSeason;
    private Integer nextEpisode;
    public Integer getNextEpisode() {
		return nextEpisode;
	}

	@Override
	public String toString() {
		return "Series [id=" + id + ", name=" + name + ", downloadDate=" + downloadDate + ", currentSeason="
				+ currentSeason + ", nextEpisode=" + nextEpisode + ", uploader=" + uploader + "]";
	}

	public void setNextEpisode(Integer nextEpisode) {
		this.nextEpisode = nextEpisode;
	}

	private String uploader;
    
    public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public Integer getCurrentSeason() {
		return currentSeason;
	}

	public void setCurrentSeason(Integer currentSeason) {
		this.currentSeason = currentSeason;
	}

	public Long getId() {
		return id;
	}

	public Date getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	protected Series() {}

    public Series(String name) {
        this.name = name;
    }
    
    public void incrementNextEpisode(){
    	 nextEpisode++;
    }

    public void incrementDownloadDate(){
    	this.downloadDate = Date.valueOf(downloadDate.toLocalDate().plusWeeks(1));
    }
}
