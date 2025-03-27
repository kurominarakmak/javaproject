package stage;

import javafx.scene.image.Image;

import map.BorderMap2;
import map.Map2Collide;

public class IDUNNO extends Stage{
	
	public IDUNNO() {
		super(	"IDUNNO Zone",
				"Yeyyy",
				new Image(ClassLoader.getSystemResourceAsStream("images/obj/stage_idunno.png")),
				"/Background/Map2Background.png",
				new Map2Collide(),new BorderMap2());
	}
}
