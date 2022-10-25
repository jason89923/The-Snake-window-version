import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GamePanel extends JPanel{
	JPanel scorePanel = new JPanel(new BorderLayout());
	static JLabel scoreLabel = new JLabel("SCORE : 0");
	static JLabel highestScoreLabel = new JLabel("HIGHEST SCORE : " + Play.GetHighestScore());
	static JLabel countDownLabel = new JLabel("3",SwingConstants.CENTER);
	static JPanel gameOverPanel;
	
	public GamePanel() {
		setLayout(null);
		Play.map = new Map();
		Play.map.setBounds(Main.Zoom(75), Main.Zoom(50), Main.Zoom(1350), Main.Zoom(750)); 
		Play.map.setBackground(Color.BLACK);
		scorePanel.setBounds(Main.Zoom(75), Main.Zoom(825), Main.Zoom(1350), Main.Zoom(60));
		scorePanel.setOpaque(false);
		
		Font scoreFont = new Font("Rockwell", Font.ROMAN_BASELINE, Main.Zoom(60));
		scoreLabel.setFont(scoreFont);
		scoreLabel.setForeground(Color.WHITE);
		highestScoreLabel.setFont(scoreFont);
		highestScoreLabel.setForeground(Color.WHITE);
		countDownLabel.setFont(scoreFont);
		countDownLabel.setForeground(Color.RED);
		scorePanel.add(scoreLabel, BorderLayout.WEST);
		scorePanel.add(countDownLabel, BorderLayout.CENTER);
		scorePanel.add(highestScoreLabel, BorderLayout.EAST);
		
		add(Play.map);
		add(scorePanel);
	} // GamePanel
}; // class GamePanel
