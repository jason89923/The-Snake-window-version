import java.awt.Color;

import javax.swing.JLabel;

public class LevelLabel extends JLabel{
	String lebelStr = "";
	
	public LevelLabel(String lebelStr) {
		this.lebelStr = lebelStr;
		setText(lebelStr);
	}
	
	public boolean CheckIsFocused() {
		if (this.getText().substring(0, 1).equals("<"))
			return true;
		else
			return false;
	}
	
	public void Focus() {
		String labelStr = this.getText();
		this.setText("<  " + labelStr + "  >");
		this.setForeground(Color.RED);
	}
	
	public void UnFocus() {
		int labelStrLength = this.getText().length();
		String labelStr = this.getText().substring(3, labelStrLength - 3);
		this.setText(labelStr);
		this.setForeground(Color.YELLOW);
	}
}
