package com.masden.sumohop;

public class BadGuy {

	private Integer xCord;
	private Integer yCord;
	private Integer height;
	private Integer hitPoints;
	private Integer maxLevelHits;

	/*
	 * Create a new bad guy
	 */
	public BadGuy() {

	}

	public Integer getxCord() {
		return xCord;
	}

	public Integer getyCord() {
		return yCord;
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getHitPoints() {
		return this.hitPoints;
	}

	/**
	 * 
	 * @return the number of jumps allowed on this level
	 */
	public Integer getMaxLevelHits() {
		return this.maxLevelHits;
	}

	private void setxCord(Integer xCord) {
		this.xCord = xCord;
	}

	private void setyCord(Integer yCord) {
		this.yCord = yCord;
	}

	private void setHeight(Integer height) {
		this.height = height;
	}

	private void setHitPoints(Integer hitPoints) {
		this.hitPoints = hitPoints;
	}

	private void setMaxLevelHits(Integer maxHitPoints) {
		this.maxLevelHits = maxHitPoints;
	}

	public void reset(int sx, int sy, int boardHeight) {
		this.setyCord((int) (Math.random() * (sy / 3)));
		this.setxCord(sx / 2 + 50);
		this.setHeight(this.getyCord() + boardHeight);
		this.setHitPoints((int) (Math.random() * 4000 + 1000));
		this.calculateMinimumHits();
	}

	public void calculateMinimumHits() {
		this.setMaxLevelHits((this.getHitPoints() + 50)
				/ (this.getHeight() - 50));
	}

	public void hit(int power) {
		this.setHitPoints(this.getHitPoints() - power);
	}
}
