package map;

import javafx.scene.image.Image;

public class BorderBase {
	protected String Top;
	protected String Bottom;
	protected String Left;
	protected String Right;
	
	public BorderBase(String Top,String Bottom,String Left,String Right) {
		this.Top = Top;
		this.Bottom=Bottom;
		this.Left=Left;
		this.Right = Right;
	}

	public String getTop() {
		return Top;
	}

	public String getBottom() {
		return Bottom;
	}

	public String getLeft() {
		return Left;
	}

	public String getRight() {
		return Right;
	}
	
	
	
}
