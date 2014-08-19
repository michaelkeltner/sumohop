package com.masden.sumohop;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.MediaPlayer;
import android.view.Display;

public class Board extends MainActivity {

	private Canvas canvas;
	private Paint monet = new Paint();

	protected Bitmap bmp;
	protected Bitmap kinfe, note1;
	protected Bitmap sumoSquat;
	protected Bitmap sumoNormal;
	protected Bitmap sumoJump;
	protected Bitmap coin;
	protected Bitmap off, on;
	protected Bitmap exit;
	protected Bitmap danger;
	protected Bitmap background;
	private int boardLevel;
	private int maxBoard;

	protected Map<Integer, Integer> backgroundMap;

	// private SurfaceHolder holder;
	// private gameloop gameLoopThread;
	MediaPlayer mp1, jump, takecoin;
	protected int x = 0, y = 0, z = 0, delay = 0, getx, gety, sound = 1;
	protected long startPress, endPress;
	protected int show = 0, sx, sy;
	protected int cspeed = 0, kspeed = 0, gameover = 0;
	protected int score = 0, health = 100, reset = 0;
	protected String sumoState = "chillin";
	protected Integer dangerHeight = 0, dangerY = 0, level = 0, power = 0,
			iceDamage = 0, iceRemaining = 100, percentDamage = 0;
	protected Context gameContext;
	protected Display display;
	protected Resources resources;

	// Used for Singleton
	private static Board instance = null;

	public static Board getInstance(Resources resources) {
		if (instance == null) {
			instance = new Board(resources);
		}
		return instance;
	}

	public Board(Resources resources) {
		this.backgroundMap = new HashMap<Integer, Integer>();
		this.resources = resources;
		setLevels();
		boardLevel = 1;
		maxBoard = 10;
		cspeed = x / 2;
		kspeed = x / 2;
		sumoSquat = BitmapFactory.decodeResource(resources, R.drawable.sumo1);
		sumoNormal = BitmapFactory.decodeResource(resources, R.drawable.sumo2);
		sumoJump = BitmapFactory.decodeResource(resources, R.drawable.sumo3);
		coin = BitmapFactory.decodeResource(resources, R.drawable.coin);
		off = BitmapFactory.decodeResource(resources, R.drawable.off);
		on = BitmapFactory.decodeResource(resources, R.drawable.on);
		exit = BitmapFactory.decodeResource(resources, R.drawable.exit);
		kinfe = BitmapFactory.decodeResource(resources, R.drawable.kinfe);
		note1 = BitmapFactory.decodeResource(resources, R.drawable.note1);
		danger = BitmapFactory.decodeResource(resources, R.drawable.pbear);
		// background = BitmapFactory.decodeResource(resources,
		// R.drawable.back);
		background = BitmapFactory.decodeResource(resources, R.drawable.board2);
		off = Bitmap.createScaledBitmap(off, 25, 25, true);
		exit = Bitmap.createScaledBitmap(exit, 25, 25, true);
		on = Bitmap.createScaledBitmap(on, 25, 25, true);

	}

	public void setDims(int x, int y) {
		sx = x;
		sy = y;
	}

	public void setLevels() {
		if (this.resources == null) {
			System.out.println("Resources is Null");
		}
		if (this.backgroundMap == null) {
			System.out.println("Background is null");
		}
		try {
			backgroundMap.put(1, R.drawable.board1);
			backgroundMap.put(2, R.drawable.board2);
			backgroundMap.put(3, R.drawable.board3);
			backgroundMap.put(4, R.drawable.board4);
			backgroundMap.put(5, R.drawable.board5);
			backgroundMap.put(6, R.drawable.board6);
			backgroundMap.put(7, R.drawable.board7);
			backgroundMap.put(8, R.drawable.board8);
			backgroundMap.put(9, R.drawable.board9);
			backgroundMap.put(10, R.drawable.board10);
			/*
			 * backgroundMap.put(2, BitmapFactory.decodeResource(resources,
			 * R.drawable.board2)); this.backgroundMap.put(3,
			 * BitmapFactory.decodeResource( this.resources,
			 * R.drawable.board3)); this.backgroundMap.put(4,
			 * BitmapFactory.decodeResource( this.resources,
			 * R.drawable.board4)); this.backgroundMap.put(5,
			 * BitmapFactory.decodeResource( this.resources,
			 * R.drawable.board5)); this.backgroundMap.put(6,
			 * BitmapFactory.decodeResource( this.resources,
			 * R.drawable.board6)); this.backgroundMap.put(7,
			 * BitmapFactory.decodeResource( this.resources,
			 * R.drawable.board7)); this.backgroundMap.put(8,
			 * BitmapFactory.decodeResource( this.resources,
			 * R.drawable.board8)); this.backgroundMap.put(9,
			 * BitmapFactory.decodeResource( this.resources,
			 * R.drawable.board9)); this.backgroundMap.put(10,
			 * BitmapFactory.decodeResource( this.resources,
			 * R.drawable.board10)); //
			 */
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	public void scaleBackground() {
		// background = Bitmap.createScaledBitmap(background, 2 * sx, sy, true);
		// currentBackground =
		// Bitmap.createScaledBitmap(background.get(boardLevel), 2 * sx, sy,
		// true);
		// health dec
		Bitmap scaleMe = BitmapFactory.decodeResource(resources,
				backgroundMap.get(boardLevel));
		background = Bitmap.createScaledBitmap(scaleMe, sx, sy, true);
		note1 = Bitmap.createScaledBitmap(note1, sx, sy, true);
	}

	public void setCanvas(Canvas thisCanvas) {
		canvas = thisCanvas;
	}

	public void setColor(int color) {
		canvas.drawColor(color);
	}

	// Game is over because they suck
	public void GameOver() {
		monet.setColor(Color.RED);
		monet.setStrokeWidth(10);
		canvas.drawText("GAME OVER", sx / 2, sy / 2, monet);
		canvas.drawText("Restart", 91, 25, monet);
	}

	public void Victory() {
		monet.setColor(Color.BLUE);
		monet.setAntiAlias(true);
		monet.setFakeBoldText(true);
		monet.setTextSize(15);
		monet.setTextAlign(Align.LEFT);
		DrawText("All your victory are belong to us", sx / 2, sy / 2, monet);
		DrawText("Get ready for the next level", 91, 25, monet);
	}

	public void changeBoard(Integer level, int z) {
		boardLevel = level;
		scaleBackground();
		DrawSomething("Background", z, 0, null);
	}

	public void DrawMenu(Integer level) {
		monet.setColor(Color.BLUE);
		monet.setAntiAlias(true);
		monet.setFakeBoldText(true);
		monet.setTextSize(15);
		monet.setTextAlign(Align.LEFT);
		DrawText("Level :" + level.toString(), 0, 20, monet);
		// setup Menu options
		DrawPicture(canvas, exit, 0, 0, null);
		DrawPicture(canvas, off, 30, 0, null);
		DrawPicture(canvas, on, 60, 0, null);
	}

	public void DrawTrys(Integer trys) {
		monet.setColor(Color.BLUE);
		monet.setAntiAlias(true);
		monet.setFakeBoldText(true);
		monet.setTextSize(15);
		monet.setTextAlign(Align.LEFT);
		DrawText("Jumps :" + trys.toString(), 0, 20, monet);
	}

	public void DrawHealthMeter(int health) {
		// Setup the health meter
		monet.setColor(Color.BLACK);
		monet.setStrokeWidth(20);
		DrawText("Health", 0, 40, monet);
		if (health == 100) {
			monet.setColor(Color.GREEN);
		} else if (health > 50) {
			monet.setColor(Color.YELLOW);
		} else {
			monet.setColor(Color.RED);
		}
		DrawRectangle(20, 40, health * 2, 60, monet);
	}

	public void DrawPowerBar(int powerLevel) {

		monet.setColor(Color.RED);
		monet.setStrokeWidth(40);
		DrawRectangle(15, powerLevel, 35, sy - 15, monet);

	}

	public void DrawSomething(String drawThis, int x, int y, Paint myPaint) {
		Bitmap drawMe = sumoNormal;

		if (drawThis == "Background") {
			System.out.println("setting drawMe to Background");
			drawMe = background;
			// drawMe = BitmapFactory.decodeResource(resources,
			// R.drawable.back);

		} else if (drawThis == "BadGuy") {
			drawMe = danger;
		} else if (drawThis == "Squat") {
			drawMe = sumoSquat;
		} else if (drawThis == "Normal") {
			drawMe = sumoNormal;
		} else if (drawThis == "Jump") {
			drawMe = sumoJump;
		} else if (drawThis == "Boarder") {
			drawMe = note1;
		}
		Board.DrawPicture(canvas, drawMe, x, y, myPaint);
	}

	static void DrawPicture(Canvas canvas, Bitmap drawMe, int x, int y,
			Paint myPaint) {
		try {
			canvas.drawBitmap(drawMe, x, y, myPaint);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void DrawText(String drawMe, int x, int y, Paint myPaint) {
		canvas.drawText(drawMe, x, y, myPaint);
	}

	private void DrawRectangle(int left, int top, int right, int bottom,
			Paint myPaint) {
		canvas.drawRect(left, top, right, bottom, myPaint);
	}

}
// */