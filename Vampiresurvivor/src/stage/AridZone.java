package stage;

import javafx.scene.image.Image;
import map.BorderMap1;
import map.Map1Collide;

public class AridZone extends Stage {

	public AridZone() {
		super(	"Arid Zone",
				"A once-fertile land now turned into a harsh, sun-scorched wasteland, where fierce winds, relentless heat, and hidden dangers test the survival of only the strongest.",
				new Image(ClassLoader.getSystemResourceAsStream("images/obj/stage_arid_zone.png")),
				"/Background/Map1Background.png",
				new Map1Collide(),new BorderMap1());
	}

}
