import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Map extends JPanel {
	private Image Snake_Body_Horizontal_image;
	private Image snake_Body_Vertical_image;
	private Image[] Snake_Head = new Image[4];
	private Image food_image;

	public Map() {
		try {
			Snake_Body_Horizontal_image = ImageIO.read(getClass().getResource("/Images/Snake_Body_Horizontal.png"));
			snake_Body_Vertical_image = ImageIO.read(getClass().getResource("/Images/Snake_Body_vertical.png"));
			food_image = ImageIO.read(getClass().getResource("/Images/Banana.png"));
			Snake_Head[0] = ImageIO.read(getClass().getResource("/Images/Snake_Head_Up.png"));
			Snake_Head[1] = ImageIO.read(getClass().getResource("/Images/Snake_Head_Down.png"));
			Snake_Head[2] = ImageIO.read(getClass().getResource("/Images/Snake_Head_Left.png"));
			Snake_Head[3] = ImageIO.read(getClass().getResource("/Images/Snake_Head_Right.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		switch (Play.snake.GetDirection()) {
		case UP:
			g.drawImage(Snake_Head[0], Play.snake.GetBodyLocation().firstElement().X * Main.Zoom(15),
					Play.snake.GetBodyLocation().firstElement().Y * Main.Zoom(15), Main.Zoom(15), Main.Zoom(15), null);
			break;
		case DOWN:
			g.drawImage(Snake_Head[1], Play.snake.GetBodyLocation().firstElement().X * Main.Zoom(15),
					Play.snake.GetBodyLocation().firstElement().Y * Main.Zoom(15), Main.Zoom(15), Main.Zoom(15), null);
			break;
		case LEFT:
			g.drawImage(Snake_Head[2], Play.snake.GetBodyLocation().firstElement().X * Main.Zoom(15),
					Play.snake.GetBodyLocation().firstElement().Y * Main.Zoom(15), Main.Zoom(15), Main.Zoom(15), null);
			break;
		case RIGHT:
			g.drawImage(Snake_Head[3], Play.snake.GetBodyLocation().firstElement().X * Main.Zoom(15),
					Play.snake.GetBodyLocation().firstElement().Y * Main.Zoom(15), Main.Zoom(15), Main.Zoom(15), null);
			break;
		default:
			break;
		}

		for (int i = 1; i < Play.snake.GetLength(); i++) {
			if(Play.snake.GetBodyLocation().elementAt(i).direction == Direction.RIGHT || Play.snake.GetBodyLocation().elementAt(i).direction == Direction.LEFT) {
				g.drawImage(Snake_Body_Horizontal_image, Play.snake.GetBodyLocation().elementAt(i).X * Main.Zoom(15),
					Play.snake.GetBodyLocation().elementAt(i).Y * Main.Zoom(15), Main.Zoom(15), Main.Zoom(15), null);
			}else {
				g.drawImage(snake_Body_Vertical_image, Play.snake.GetBodyLocation().elementAt(i).X * Main.Zoom(15),
						Play.snake.GetBodyLocation().elementAt(i).Y * Main.Zoom(15), Main.Zoom(15), Main.Zoom(15), null);
			}
			
		}

		if (Play.foodCoord != null) {
			g.drawImage(food_image, Play.foodCoord.X * Main.Zoom(15), Play.foodCoord.Y * Main.Zoom(15), Main.Zoom(15),
					Main.Zoom(15), null);
		}
	}
}
