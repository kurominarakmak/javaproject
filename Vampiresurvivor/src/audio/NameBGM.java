package audio;

public enum NameBGM {
	BGM_SSS("bgm_SSS"),
	BGM_SUSPECT("bgm_suspect"),
	BGM_ADTRUCK("bgm_adtruck"),
	BGM_GAMEOVER("bgm_gameover");

    private final String filename;

    NameBGM(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
