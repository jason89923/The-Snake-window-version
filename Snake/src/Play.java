import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Play extends JPanel implements Runnable {
	private static int score;
	public static int highestScore = 0;
	public static Difficulty difficulty;
	public static Direction KeyIn;
	public static boolean gameOver = false;
	public static boolean gameStart = false;
	public static COORD foodCoord;
	public static Snake snake;
	public static Map map;
	private int initialDelay_easy;
	private int initialDelay_normal;
	private int initialDelay_hard;
	private int delay_control;

	public Play(Difficulty difficulty) {
		snake = new Snake();
		delay_control = 0;
		initialDelay_easy = 150;
		initialDelay_normal = 100;
		initialDelay_hard = 100;
		Play.difficulty = difficulty;
		gameOver = false;
		gameStart = false;
	} // Play

	private int CalculateDelay(boolean click, boolean eat) {
		if (difficulty == Difficulty.hard) {
			if (eat) {
				initialDelay_hard++;
			} // if

			if (initialDelay_hard > 10 && click) {
				if (delay_control > 0) {
					delay_control--;
				} // if
				else {
					initialDelay_hard = initialDelay_hard - 2;
				} // else
			} // if

			return initialDelay_hard;
		} // if
		else if (difficulty == Difficulty.normal) {
			if (initialDelay_normal > 20 && eat) {
				initialDelay_normal = initialDelay_normal - 3;
			} // if

			return initialDelay_normal;
		} // else if
		else if (difficulty == Difficulty.easy) {
			if (initialDelay_easy > 30 && eat) {
				initialDelay_easy = initialDelay_easy - 5;
			} // if

			return initialDelay_easy;
		} // else

		return -1;
	} // CalculateDelay

	private int CalculatrScore(int delay) {
		double temp = (double) 1 / (double) delay;
		int num = (int) (temp * 10000);
		num = (num - 66) * 50 / 467;
		num = num * 2 + 50;
		return num;
	} // CalculatrScore

	private Direction GetNextDirection() {
		if (KeyIn == Direction.UP && snake.GetDirection() == Direction.DOWN) {
			return Direction.DOWN;
		} // if
		else if (KeyIn == Direction.DOWN && snake.GetDirection() == Direction.UP) {
			return Direction.UP;
		} // else if
		else if (KeyIn == Direction.LEFT && snake.GetDirection() == Direction.RIGHT) {
			return Direction.RIGHT;
		} // else if
		else if (KeyIn == Direction.RIGHT && snake.GetDirection() == Direction.LEFT) {
			return Direction.LEFT;
		} // else if

		return KeyIn;
	} // Translate

	private boolean CheckFoodLocation(COORD tempCoord) {
		for (int i = 0; i < snake.GetLength(); i++) {
			if (snake.GetBodyLocation().elementAt(i).equals(tempCoord)) {
				return false;
			} // if
		} // for

		return true;
	} // CheckFoodLocation

	private void PutFood() {
		COORD tempCoord;
		do {
			tempCoord = new COORD((int) (Math.random() * 90), (int) (Math.random() * 50));
		} while (!CheckFoodLocation(tempCoord));

		foodCoord = tempCoord;

	} // PutFood

	public void run() {
		int count = 0;
		boolean click = false;
		boolean eat = false;
		int delay = CalculateDelay(click, eat);
		KeyIn = Direction.LEFT;
		gameOver = false;
		gameStart = true;
		score = 0;
		PutFood();
		
		GamePanel.scoreLabel.setText("SCORE : 0");
		GamePanel.countDownLabel.setVisible(true);
		for (int countdown = 3; countdown > 0; countdown--) {
			try {
				GamePanel.countDownLabel.setText(Integer.toString(countdown));
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		GamePanel.countDownLabel.setVisible(false);
		
		while (true) {
			click = false;
			eat = false;
			count++;
			if (count == 15) {
				count = 0;
				click = true;
			} // if

			snake.MoveSnake(GetNextDirection());
			if (snake.HitWall() || snake.HitItself()) {
				gameOver = true;
				break;
			} // if

			if (snake.EatFood(foodCoord)) {
				eat = true;
				delay_control = 2;
				PutFood();
				score = score + CalculatrScore(delay);
				if(score > highestScore) {
					highestScore = score;
					GamePanel.highestScoreLabel.setText("HIGHEST SCORE : " + GetHighestScore());
				}
				GamePanel.scoreLabel.setText("SCORE : " + GetScore());
			} // if

			try {
				delay = CalculateDelay(click, eat);
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			map.repaint();
		} // while
		GameOver();
	} // Start

	private void GameOver() {
		map.setVisible(false);
		GamePanel.gameOverPanel = new JPanel(new GridLayout(3, 1));
		JLabel gameOverLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
		JLabel nullLabel = new JLabel();
		JLabel gameOverHint = new JLabel("PRESS ENTER TO RESTART", SwingConstants.CENTER);
		GamePanel.gameOverPanel.setOpaque(false);
		GamePanel.gameOverPanel.setBounds(Main.Zoom(200), Main.Zoom(100), Main.Zoom(1100), Main.Zoom(600));
		GamePanel.gameOverPanel.add(gameOverLabel);
		GamePanel.gameOverPanel.add(nullLabel);
		GamePanel.gameOverPanel.add(gameOverHint);
		gameOverLabel.setFont(new Font("Rockwell", Font.BOLD, Main.Zoom(150)));
		gameOverLabel.setForeground(Color.WHITE);
		gameOverHint.setFont(new Font("Rockwell", Font.PLAIN, Main.Zoom(50)));
		gameOverHint.setForeground(Color.YELLOW);
		GamePanel.gameOverPanel.setVisible(true);
		Game.gamePanel.add(GamePanel.gameOverPanel);
		
		try { 
			Main.game.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				ImageIO.read(getClass().getResource("/Images/Cursor.png")), new Point(), "strawberry"));
			File writename = new File(Play.class.getResource("Score/Highest_Score.txt").getFile());
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(String.valueOf(highestScore));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Main.game.highestScoreLabel.setText("Highest Score : " + Play.GetHighestScore());
	}
	
	public static int GetScore() {
		return score;
	} // GetScore
	
	public static int GetHighestScore() {
		return highestScore;
	}
}; // class Play
