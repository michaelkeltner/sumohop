package com.masden.sumohop;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private String state;
	private Boolean playSound;
	private Boolean show;
	private Integer level;

	private Integer gameJumps;
	private Integer levelJumps;
	private Integer maxLevels;

	private List<String> achievement = new ArrayList<String>();

	private static Game instance = null;

	/**
	 * Singleton constructor
	 * 
	 * @return instance of game
	 */
	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}

	/*
	 * Create a new game
	 */
	public Game() {
		this.setGameJumps(0);
		// set the number of jumps taken to 0
		this.setLevelJumps(0);
		this.setMaxLevel(10);
		this.setPlaySound(true);
		this.setShow(false);
		this.setState("play");
	}

	private void setMaxLevel(int max) {
		this.maxLevels = max;
	}

	public int getMaxLevel() {
		return this.maxLevels;
	}

	public void setPlaySound(Boolean sound) {
		this.playSound = sound;
	}

	/**
	 * 
	 * @param state
	 *            set the state of the player set the state of the player
	 */
	private void setState(String state) {
		this.state = state;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * 
	 * @param levelJumps
	 *            the number of jumps made this level
	 */
	private void setLevelJumps(Integer levelJumps) {
		this.levelJumps = levelJumps;
	}

	/**
	 * 
	 * @param achievements
	 *            sets all the achievements
	 */
	public void setAchievements(List<String> achievements) {
		this.achievement = achievements;
	}

	/**
	 * 
	 * @param gameJumps
	 *            the number of jumps made this game
	 */
	private void setGameJumps(Integer gameJumps) {
		this.gameJumps = gameJumps;
	}

	/**
	 * 
	 * @return The current state of the player
	 */
	public String getState() {
		return this.state;
	}

	public Boolean getShow() {
		return this.show;
	}

	public Boolean getPlaySound() {
		return this.playSound;
	}

	public Integer getLevel() {
		return this.level;
	}

	/**
	 * 
	 * @return the number of jumps made during this level
	 */
	public Integer getLevelJumps() {
		return this.levelJumps;
	}

	/**
	 * 
	 * @return the number of jumps made during the game
	 */
	public Integer getGameJumps() {
		return this.gameJumps;
	}

	/**
	 * 
	 * @return all acheivements of the player
	 */
	public List<String> getAchievement() {
		return this.achievement;
	}

	public void reset() {
		this.setState("reset");
	}

	public void nextLevel(int nextLevel) {
		this.setLevel(nextLevel);
		this.setState("play");
	}

	public void addJump() {
		this.setGameJumps(this.getGameJumps() + 1);
		this.setLevelJumps(this.getLevelJumps() + 1);
	}

	/**
	 * Add an achievement
	 * 
	 * @param achievement
	 *            the name of the achievement they unlocked
	 */
	public void addAchievement(String achievement) {
		this.achievement.add(achievement);
	}

	public void over() {
		this.setState("over");
	}
}
