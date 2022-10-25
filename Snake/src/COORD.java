

public class COORD {
	public int X;
	public int Y;
	public Direction direction;

	public COORD(int X, int Y) {
		this.X = X;
		this.Y = Y;
	} // COORD

	public boolean equals(COORD coord) {
		if (coord.X == X && coord.Y == Y) {
			return true;
		} // if

		return false;
	} // equals

	public void SetDirection(COORD previousCoord) throws Exception {
		COORD coord = new COORD(previousCoord.X - this.X, previousCoord.Y - this.Y);
		if (coord.X > 0) {
			if (coord.Y == 0) {
				direction = Direction.RIGHT;
				return;
			} // if
		} // if
		else if (coord.X < 0) {
			if (coord.Y == 0) {
				direction = Direction.LEFT;
				return ;
			} // if
		} // else if
		else {
			if (coord.Y > 0) {
				direction = Direction.DOWN ;
				return ;
			} // if
			else if ( coord.Y < 0) {
				direction = Direction.UP ;
				return ;
			} // else if
		} // else
		
		throw new Exception("COORD error!") ;
	} // SetDirection
};