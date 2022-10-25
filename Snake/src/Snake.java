
import java.util.Vector;

public class Snake {
	private Vector<COORD> bodyCoords = new Vector<COORD>();
	private Direction direction;
	private COORD head;
	private Vector<COORD> eatList = new Vector<COORD>();

	public Snake() {
		direction = Direction.LEFT;
		bodyCoords.addElement(new COORD(45, 25));
		bodyCoords.addElement(new COORD(46, 25));
		bodyCoords.addElement(new COORD(47, 25));
		head = bodyCoords.firstElement();
	} // Snake

	public Vector<COORD> GetBodyLocation() {
		return bodyCoords;
	} // GetBodyLocation

	public int GetLength() {
		return bodyCoords.size();
	} // GetLength

	public boolean HitItself() {
		for (int i = 1; i < GetLength(); i++) {
			if (bodyCoords.elementAt(i).equals(head)) {
				return true;
			} // if
		} // for

		return false;
	} // HitItself

	public Direction GetDirection() {
		return direction;
	} // GetDirection

	public boolean HitWall() {
		if (head.X < 0 || head.X >= 90 || head.Y < 0 || head.Y >= 50) {
			return true;
		} // if

		return false;
	} // HitWall

	public void MoveSnake(Direction nextDirection) {
		direction = nextDirection;
		boolean grow = false;
		COORD temp = null;
		if (!eatList.isEmpty()) {
			if (eatList.elementAt(0).equals(bodyCoords.lastElement())) {
				temp = new COORD(eatList.elementAt(0).X, eatList.elementAt(0).Y);
				eatList.removeElementAt(0);
				grow = true;
			} // if
		} // if

		for (int i = GetLength() - 1; i > 0; i--) {
			COORD tempCoord = new COORD(bodyCoords.elementAt(i - 1).X, bodyCoords.elementAt(i - 1).Y);
			bodyCoords.setElementAt(tempCoord, i);
		} // for

		if (direction == Direction.UP) {
			bodyCoords.elementAt(0).Y--;
		} // if
		else if (direction == Direction.DOWN) {
			bodyCoords.elementAt(0).Y++;
		} // else if
		else if (direction == Direction.LEFT) {
			COORD tempCoord = bodyCoords.elementAt(0);
			tempCoord.X--;
			bodyCoords.setElementAt(tempCoord, 0);
		} // else if
		else if (direction == Direction.RIGHT) {
			bodyCoords.elementAt(0).X++;
		} // else if

		head = bodyCoords.firstElement();
		if (grow) {
			bodyCoords.addElement(temp);
		} // if
		
		bodyCoords.firstElement().direction = this.direction ;
		for ( int i = 1 ; i < GetLength() ; i++) {
			try {
				bodyCoords.elementAt(i).SetDirection(bodyCoords.elementAt(i-1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // for
	} // MoveSnake

	public boolean EatFood(COORD foodCoord) {
		if (head.equals(foodCoord)) {
			COORD tempCoord = new COORD(foodCoord.X, foodCoord.Y) ;
			eatList.addElement(tempCoord);
			return true;
		} // if

		return false;
	} // EatFood

}; // class Snake
