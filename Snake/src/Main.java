import javax.swing.JFrame;

public class Main {
	public static double zoomRatio = 0.8;
	public static Game game;

	public static int Zoom(int pixel) {
		return (int) ((double) pixel * zoomRatio);
	} // Zoom

	public static void main(String[] args) {
		game = new Game();
		game.setSize(Zoom(1500), Zoom(1000));
		game.setResizable(false);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	} // main
}; // class Main