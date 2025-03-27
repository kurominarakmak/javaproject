package audio;

public enum NameSE {
	SND_MENU_SELECT("snd_menu_select"),
	SND_MENU_CONFIRM("snd_menu_confirm"),
	SND_MENU_BACK("snd_menu_back"),
	SND_MANAGELEVEL("snd_managelevel"),
	SND_BUYWORKER("snd_buyworker"),
	SND_CHARSELECTWOOSH("snd_charSelectWoosh"),
	SND_FAILED("snd_failed"),
	SND_DELIVERY("snd_delivery"),
	SND_OPENMENU("snd_openmenu"),
	SND_HAATOPROJ("snd_haatoproj"),
	SND_HIT3("snd_hit3"),
	SND_STOMP("snd_stomp"),
	SND_GETEXP("snd_getEXP"),
	SND_TOWAGUN("snd_towagun"),
	SND_BIGBOMBEXPLOSION("snd_bigbombexplosion")
	;

    private final String filename;

    NameSE(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
