package stage;

import javafx.scene.image.Image;
import map.BorderBase;
import map.MapObstacle;

public class Stage {
	private String name;
	private String desc;
	private Image image;
	private String mapPath;
	private MapObstacle collidePath;
	private BorderBase borderBase;
	
	protected Stage(String name, String desc, Image image,String mapPath,MapObstacle collidePath,BorderBase borderBase) {
		this.name = name;
		this.desc = desc;
		this.image = image;
		this.mapPath = mapPath;
		this.collidePath = collidePath;
		this.borderBase=borderBase;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public Image getImage() {
		return image;
	}

	public String getMapPath() {
		return mapPath;
	}

	public MapObstacle getCollidePath() {
		return collidePath;
	}

	public BorderBase getBorderBase() {
		return borderBase;
	}
	
	
	
	
}
