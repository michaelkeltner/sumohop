package com.masden.sumohop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	MediaPlayer mp1, jump, takecoin;
	gameloop gameLoopThread;
	public Board board;
	public Player player;
	public BadGuy badGuy;
	public Game game;
	public Display display;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// for no title
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		game = Game.getInstance();
		board = Board.getInstance(this.getResources());
		player = Player.getInstance();
		badGuy = new BadGuy();
		setContentView(new GameView(this));
	}

	public class GameView extends SurfaceView {
		private Canvas canvas;
		private SurfaceHolder holder;
		// private gameloop gameLoopThread;
		protected int x = 0, z = 0, delay = 0, getx, gety;
		protected int sx, sy;
		protected Integer damage = 0, percentDamage = 0;
		protected Context gameContext;

		@SuppressWarnings("deprecation")
		public GameView(Context context) {
			super(context);

			display = getWindowManager().getDefaultDisplay();
			sx = display.getWidth();
			sy = display.getHeight();
			// set the display width and height
			board.setDims(sx, sy);

			gameContext = context;
			gameLoopThread = new gameloop(this);
			holder = getHolder();

			holder.addCallback(new SurfaceHolder.Callback() {
				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					// for stoping the game
					gameLoopThread.setRunning(false);
					gameLoopThread.getThreadGroup().interrupt();
				}

				@SuppressLint("WrongCall")
				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					gameLoopThread.setRunning(true);
					gameLoopThread.start();

				}

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format,
						int width, int height) {
				}
			});
			setupMedia();
			player.setPlayerCoords(sx / 2, 20 * sy / 30);
			setupLevel(1);
		}

		private void setupMedia() {
			mp1 = MediaPlayer.create(MainActivity.this, R.raw.game);
			jump = MediaPlayer.create(MainActivity.this, R.raw.jump);
			takecoin = MediaPlayer.create(MainActivity.this, R.raw.cointake);
		}

		private void LevelUp() {
			setupLevel(game.getLevel() + 1);
		}

		private void setupLevel(int thisLevel) {
			thisLevel = (thisLevel > game.getMaxLevel()) ? 1 : thisLevel;
			game.nextLevel(thisLevel);
			Integer intOut = thisLevel;
			System.out.println("change board call with level"
					+ intOut.toString());
			board.changeBoard(thisLevel, z);
			player.reset();
			badGuy.reset(sx, sy, board.danger.getHeight());

			cout("danger height " + board.danger.getHeight());
			cout("level " + game.getLevel().toString() + " parameters:");
			cout("Hit Points set to " + badGuy.getHitPoints().toString());
			cout("Danger height set to " + badGuy.getHeight().toString());
			cout("Jumps Left "
					+ (badGuy.getMaxLevelHits() - game.getLevelJumps()));
		}

		// on touch method
		int powerBar = 0;
		int downX = 0;
		int downY = 0;

		private Boolean touchingPlayer(int getx, int gety) {
			return (getx < (player.getxCord() + board.sumoNormal.getWidth() + 2)
					&& getx > (player.getxCord() - 2)
					&& gety < (player.getyCord() + board.sumoNormal.getHeight() + 2) && gety > (player
					.getyCord() - 2));
		}

		private class PrepairPowerBar extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... powerLevel) {
				board.DrawPowerBar(Integer.parseInt(powerLevel[0]));
				return powerLevel[0];
			}

			@Override
			protected void onPostExecute(String result) {
				board.DrawPowerBar(Integer.parseInt(result));
			}
		}

		// int progressBarStatus = 0;
		// boolean progressStarted = false;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			getx = (int) event.getX();
			gety = (int) event.getY();

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (touchingPlayer(getx, gety)) {
					downX = getx;
					downY = gety;
					// startPress = System.currentTimeMillis();
					player.squat();
					// sumoState = "Squat";
					PrepairPowerBar task = new PrepairPowerBar();
					int progressLevel = (int) ((System.currentTimeMillis() - player
							.getStartPress()) / 12);
					Integer pLevel = (Integer) progressLevel;
					task.execute(new String[] { pLevel.toString() });
					// reset progress bar status
					// if (!progressStarted) {
					/*
					 * new Thread(new Runnable() { public void run() { while
					 * (sumoState == "Squat") {
					 * 
					 * // get the power bar level progressBarStatus = (int)
					 * (System .currentTimeMillis() - startPress) / 12;
					 * cout("progressBarStatus = " + progressBarStatus); // your
					 * computer is too fast, sleep 1 second
					 * 
					 * // Update the progress bar
					 * board.DrawPowerBar(progressBarStatus); }
					 * 
					 * // Sumo guy can jump if (sumoState == "Jump") { // sleep
					 * 1 second, so that you can see the // bar try {
					 * Thread.sleep(1000); } catch (InterruptedException e) {
					 * e.printStackTrace(); }
					 * 
					 * // close the progress bar dialog
					 * 
					 * } } }).start(); //
					 */
					// progressStarted = true;
					// }

				} else {
					downX = 0;
					downY = 0;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				System.out.println("Released " + powerBar);
				player.setCanJump(false);
				if (touchingPlayer(downX, downY)) {
					// progressStarted = false;
					player.jump();
					game.addJump();
					downX = 0;
					downY = 0;
				}
				powerBar = 0;

				// exit
				if (getx < 25 && gety < 25) {
					player.setCanJump(false);
					System.exit(0);

				}
				// sound off
				if (getx > 25 && getx < 60) {
					if (gety < 25) {
						player.setCanJump(false);
						game.setPlaySound(false);
						mp1.stop();
					}
				}
				// sound on
				if (getx > 61 && getx < 90) {
					if (gety < 25) {
						player.setCanJump(false);
						game.setPlaySound(true);
					}
				}
				// restart game
				if (getx > 91 && gety < 25) {
					if (player.getHealth() > 0) {
						gameLoopThread.setPause(0);
						player.reset();
						game.reset();

					}
				}
				if (player.canJump()) {
					game.setShow(true);
					player.setPower(sy);
					damage = sy - player.getPower();
					if (badGuy.getHitPoints() > 0) {
						percentDamage = Math
								.round(((float) damage / (float) badGuy
										.getHitPoints()) * 100);
					} else {
						percentDamage = 0;
					}
					badGuy.hit(damage);
				}
			}

			return true;
		}

		// Game is over because they suck
		public void GameOver() {
			mp1.stop();
			board.DrawHealthMeter(player.getHealth());
			board.GameOver();
			setupLevel(1);
			player.removeAllReward();
			game.over();
			gameLoopThread.setPause(1);
		}

		public void Victory() {
			board.Victory();
			LevelUp();
		}

		@SuppressLint({ "WrongCall", "DrawAllocation" })
		@Override
		protected void onDraw(Canvas inCanvas) {

			canvas = inCanvas;
			board.setCanvas(canvas);
			board.setColor(Color.BLACK);
			// background moving
			/*
			 * z -= 10; if (z == -sx) { z = 0; } //
			 */
			board.DrawSomething("Background", z, 0, null);
			// jump
			x += 5;
			if (x == 20) {
				x = 5;
			}
			board.DrawSomething("BadGuy", badGuy.getxCord(), badGuy.getyCord(),
					null);

			// for jump
			if (game.getShow()) {
				if (game.getPlaySound()) {

					jump.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						public void onPrepared(MediaPlayer mp) {
							mp.start();
						}
					});
				}
				cout("Jump Power " + player.getPower().toString());
				cout("Hit Damage " + damage.toString());
				cout("Percent Damage " + percentDamage.toString());
				cout("Hit Points Remaining " + badGuy.getHitPoints().toString());
				cout("Danger at " + badGuy.getHeight().toString());
				cout("Jumps Left "
						+ (badGuy.getMaxLevelHits() - game.getLevelJumps()));

				board.DrawSomething(player.getState(), sx / 2,
						player.getPower(), null);

				if ((player.getPower()) <= badGuy.getHeight()) {
					player.decreaseHealth(40);
				}
				// jump-hold
				delay += 1;
				if (delay == 3) {
					game.setShow(false);
					delay = 0;
				}
				board.DrawTrys(badGuy.getMaxLevelHits() - game.getLevelJumps());
				// updateTrys(-1);
				game.setShow(false);
			} else {
				if (x % 2 == 0 && downX == 0 && downY == 0) {
					player.normal();
				}
				board.DrawSomething(player.getState(), player.getxCord(),
						player.getyCord(), null);
				if (badGuy.getHitPoints() <= 0) {
					Victory();
				}
			}

			// Level
			board.DrawMenu(game.getLevel());
			if (game.getPlaySound()) {
				mp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					public void onPrepared(MediaPlayer mp) {
						mp.start();
					}
				});
				mp1.setLooping(true);
			}
			// health
			board.DrawHealthMeter(player.getHealth());
			// game over
			if (player.getHealth() <= 0
					|| game.getLevelJumps() > badGuy.getMaxLevelHits()) {
				GameOver();
			}
			// restart
			if (game.getState() == "reset") {
				setupLevel(1);
				gameLoopThread.setPause(0);
			}
		}

	}

	private void cout(String statement) {
		System.out.println(statement);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			setContentView(R.layout.about);
			return true;
		case R.id.exit:
			System.exit(0);
			return true;
		case R.id.mute:
			mp1.stop();
			takecoin.stop();
			jump.stop();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
