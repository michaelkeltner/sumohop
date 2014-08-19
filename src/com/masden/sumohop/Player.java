package com.masden.sumohop;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String state;
	private Integer health;
	private Integer power;
	private Boolean canJump;
	private long startPress;
	private long endPress;
	private List<String> rewards = new ArrayList<String>();
	private Integer xCord = 1;
	private Integer yCord = 1;

	// Used for Singleton
	private static Player instance = null;

	/**
	 * Singleton constructor
	 * 
	 * @return instance of player
	 */
	public static Player getInstance() {
		if (instance == null) {
			instance = new Player();
		}
		return instance;
	}

	/*
	 * Create a new player
	 */
	public Player() {
		this.reset();
	}

	/**
	 * rests the players health, state and goodies
	 */
	public void reset() {
		// player starts out normal
		this.setState("Normal");
		// player starts with all his health
		this.setHealth(100);
		// clear out the goodies
		// this.clearGoodies();
		// set Jump State
		this.setCanJump(false);

	}

	/**
	 * Clears all the goodies
	 */
	private void clearRewards() {
		this.rewards.clear();
	}

	/**
	 * 
	 * @return The current state of the player
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * 
	 * @return the current health of the player
	 */
	public Integer getHealth() {
		return this.health;
	}

	/**
	 * 
	 * @return the current power stored
	 */
	public Integer getPower() {
		return this.power;
	}

	/**
	 * 
	 * @return 1 if player can jump else 0
	 */

	public Boolean getCanJump() {
		return this.canJump;
	}

	/**
	 * 
	 * @return all the items the player can use in the game
	 */

	public List<String> getRewards() {
		return this.rewards;
	}

	/**
	 * 
	 * @return player x coordinate
	 */
	public Integer getxCord() {
		return this.xCord;
	}

	/**
	 * 
	 * @return player y coordinate
	 */
	public Integer getyCord() {
		return this.yCord;
	}

	/**
	 * 
	 * @param state
	 *            set the state of the player set the state of the player
	 */
	private void setState(String state) {
		this.state = state;
	}

	/**
	 * 
	 * @param health
	 *            sets the health of the player
	 */
	private void setHealth(Integer health) {
		this.health = health;
	}

	/**
	 * 
	 * @param xCord
	 *            x coordinate of the player
	 */
	public void setxCord(Integer xCord) {
		this.xCord = xCord;
	}

	/**
	 * 
	 * @param yCord
	 *            y coordinate of the player
	 */
	public void setyCord(Integer yCord) {
		this.yCord = yCord;
	}

	/**
	 * 
	 * @param canJump
	 *            1 if player can jump else 0
	 * 
	 */
	public void setCanJump(Boolean canJump) {
		this.canJump = canJump;
	}

	/**
	 * 
	 * @param sy
	 *            indicates where the player's y coordinate is
	 */
	public void setPower(Integer sy) {
		this.power = ((int) (15 - endPress) * sy / 30) + (sy / 5);
	}

	/**
	 * 
	 * @param goodies
	 *            sets all the goodies
	 */
	public void setRewardss(List<String> rewards) {
		this.rewards = rewards;
	}

	/**
	 * Add an item the player can use during the game
	 * 
	 * @param goody
	 *            - the name of the goody
	 */
	public void addReward(String reward) {
		this.rewards.add(reward);
	}

	/**
	 * Removes a goody from the goody list
	 * 
	 * @param goody
	 *            The goody to remove
	 */
	public void removeReward(String reward) {
		this.rewards.remove(reward);
	}

	public void removeAllReward() {
		this.rewards.clear();
	}

	/**
	 * Decrease the health of the player
	 * 
	 * @param deltaHealth
	 *            - the amount of health to remove
	 */
	public void decreaseHealth(Integer deltaHealth) {
		this.setHealth(this.getHealth() - deltaHealth);
	}

	/**
	 * Increase the health of the player
	 * 
	 * @param deltaHealth
	 *            - the amount of health to remove
	 */
	public void increaseHealth(Integer deltaHealth) {
		this.setHealth(this.getHealth() + deltaHealth);
	}

	/**
	 * make the player jump
	 */
	public void jump() {
		// set the duration they held down the sumo guy
		this.setEndPress();
		this.setState("Jump");
		this.setCanJump(true);

	}

	/**
	 * make the player squat
	 */
	public void squat() {
		this.setState("Squat");
		this.setStartPress();

	}

	private void setEndPress() {
		endPress = (System.currentTimeMillis() - this.startPress) / 125;
	}

	/**
	 * 
	 * @return when the button was no longer pressed
	 * 
	 */
	public long getEndPress() {
		return this.endPress;
	}

	private void setStartPress() {
		this.startPress = System.currentTimeMillis();
	}

	public long getStartPress() {
		return this.startPress;
	}

	/**
	 * make the player stand
	 */
	public void normal() {
		this.setState("Normal");
	}

	/**
	 * 
	 * @return true if player can jump otherwise false
	 */
	public Boolean canJump() {
		return this.getCanJump();
	}

	/**
	 * 
	 * @param posX
	 *            x coordinate of the player
	 * @param posY
	 *            y coordinate of the player
	 */
	public void setPlayerCoords(Integer posX, Integer posY) {

		this.setxCord(posX);
		this.setyCord(posY);
	}
}
