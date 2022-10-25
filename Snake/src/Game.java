import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Game extends JFrame {
	static JPanel StartPanel;
	JLabel snakeLabel;
	JLabel highestScoreLabel;
	JLabel difficultyLabel;
	LevelLabel[] levelLabels = new LevelLabel[3];
	LevelLabel hardLabel;
	LevelLabel normalLabel;
	LevelLabel easyLabel;
	int nowLabelIndex = 0;
	boolean isNonStart = true;

	ExecutorService executorService = Executors.newCachedThreadPool();
	public static GamePanel gamePanel;

	public Game() {
		super("Snake");

		setLayout(new BorderLayout());

		try {
			this.setIconImage(ImageIO.read(getClass().getResource("/Images/Snake_Icon.png")));
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					ImageIO.read(getClass().getResource("/Images/Cursor.png")), new Point(), "strawberry"));
		} catch (HeadlessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IndexOutOfBoundsException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			File filename = new File(Game.class.getResource("Score/Highest_Score.txt").getFile());
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine();
			Play.highestScore = Integer.parseInt(line);
		} catch (FileNotFoundException e1) {
			Play.highestScore = 0;
		} catch (IOException e1) {
			Play.highestScore = 0;
		} catch (NumberFormatException e1) {
			Play.highestScore = 0;
		}

		StartPanel = new JPanel(new GridBagLayout());
		StartPanel.setBackground(Color.BLACK);
		Font titleFont = new Font("Rockwell", Font.BOLD, Main.Zoom(200));
		snakeLabel = new JLabel("SNAKE");
		snakeLabel.setFont(titleFont);
		snakeLabel.setForeground(Color.WHITE);
		snakeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		Font contentFont = new Font("Rockwell", Font.ROMAN_BASELINE, Main.Zoom(60));
		highestScoreLabel = new JLabel("Highest Score : " + Play.GetHighestScore());
		highestScoreLabel.setFont(contentFont);
		highestScoreLabel.setForeground(Color.WHITE);
		highestScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

		difficultyLabel = new JLabel("Please choose difficulty");
		difficultyLabel.setFont(contentFont);
		difficultyLabel.setForeground(Color.WHITE);
		difficultyLabel.setHorizontalAlignment(SwingConstants.CENTER);

		Font levelFont = new Font("Rockwell", Font.BOLD, Main.Zoom(40));
		LevelListener levelListener = new LevelListener();
		hardLabel = new LevelLabel("HARD");
		normalLabel = new LevelLabel("NORMAL");
		easyLabel = new LevelLabel("EASY");

		levelLabels[0] = hardLabel;
		levelLabels[1] = normalLabel;
		levelLabels[2] = easyLabel;

		for (JLabel label : levelLabels) {
			label.setFont(levelFont);
			label.setForeground(Color.YELLOW);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.addMouseListener(levelListener);
		}

		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (!Play.gameStart) {
					LevelLabel nowLabel = levelLabels[nowLabelIndex];
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						for (LevelLabel label : levelLabels) {
							if (label.CheckIsFocused()) {
								Difficulty difficulty = Difficulty.easy;
								switch (label.getText()) {
								case "<  HARD  >":
									difficulty = Difficulty.hard;
									break;
								case "<  NORMAL  >":
									difficulty = Difficulty.normal;
									break;
								case "<  EASY  >":
									difficulty = Difficulty.easy;
									break;
								default:
									difficulty = Difficulty.easy;
									break;
								}
								GameStart(difficulty);
							}
						}
					} else {
						for (LevelLabel labels : levelLabels) {
							if (labels.CheckIsFocused())
								labels.UnFocus();
						}

						if (e.getKeyCode() == KeyEvent.VK_UP) {
							if (nowLabel.CheckIsFocused())
								nowLabel.UnFocus();
							if (nowLabelIndex == 0)
								nowLabelIndex = 3;
							levelLabels[--nowLabelIndex].Focus();
						}
						if (e.getKeyCode() == KeyEvent.VK_DOWN) {
							if (nowLabel.CheckIsFocused())
								nowLabel.UnFocus();
							if (nowLabelIndex == 2)
								nowLabelIndex = -1;
							levelLabels[++nowLabelIndex].Focus();
						}
					}
				} else {
					if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
						Play.KeyIn = Direction.UP;
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
						Play.KeyIn = Direction.DOWN;
					} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
						Play.KeyIn = Direction.LEFT;
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
						Play.KeyIn = Direction.RIGHT;
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER && Play.gameOver == true) {
						Game.gamePanel.setVisible(false);
						Game.StartPanel.setVisible(true);
						Play.gameStart = false;
					}
				}
			}
		});
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if(Play.gameStart && Play.gameOver) {
					Game.gamePanel.setVisible(false);
					Game.StartPanel.setVisible(true);
					Play.gameStart = false;
				}
			}
		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.gridheight = 3;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		StartPanel.add(snakeLabel, gbc);

		gbc.gridy = 3;
		gbc.gridheight = 1;
		StartPanel.add(highestScoreLabel, gbc);

		gbc.gridy = 4;
		gbc.gridheight = 1;
		StartPanel.add(difficultyLabel, gbc);

		gbc.gridy = 5;
		gbc.gridheight = 1;
		StartPanel.add(hardLabel, gbc);

		gbc.gridy = 6;
		gbc.gridheight = 1;
		StartPanel.add(normalLabel, gbc);

		gbc.gridy = 7;
		gbc.gridheight = 1;
		StartPanel.add(easyLabel, gbc);

		add(StartPanel);
	} // Game

	public void GameStart(Difficulty difficulty) {
		StartPanel.setVisible(false);
		gamePanel = new GamePanel();
		gamePanel.setBackground(Color.GRAY);
		this.add(gamePanel);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("")), new Point(0, 0), "cursor"));
		executorService.execute(new Play(difficulty));
	}

	public class LevelListener extends MouseAdapter {

		public void mouseEntered(MouseEvent event) {
			for (LevelLabel labels : levelLabels) {
				if (labels.CheckIsFocused())
					labels.UnFocus();
			} // for

			LevelLabel nowLabel = (LevelLabel) event.getSource();
			if (!nowLabel.CheckIsFocused()) {
				nowLabel.Focus();
			} // if
		} // mouseEntered

		public void mouseExited(MouseEvent event) {
			LevelLabel nowLabel = (LevelLabel) event.getSource();
			if (nowLabel.CheckIsFocused()) {
				nowLabel.UnFocus();
			} // if
		} // mouseExited

		public void mouseClicked(MouseEvent event) {
			Difficulty difficulty = Difficulty.easy;
			if (event.getSource().equals(levelLabels[0])) {
				difficulty = Difficulty.hard;
			} // if
			else if (event.getSource().equals(levelLabels[1])) {
				difficulty = Difficulty.normal;
			} // else if
			else if (event.getSource().equals(levelLabels[2])) {
				difficulty = Difficulty.easy;
			} // else if

			GameStart(difficulty);
		} // mouseClicked
	}
}; // class LevelListener