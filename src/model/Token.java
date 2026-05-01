package model;

public class Token {
    private int id;
    private String name;
    private String type;

    private String frontSide;
    private String backSide;
    private boolean isFrontSideUp;

    private int position;

    public Token(int id, String name, int initialPosition) {
        this.id = id;
        this.name = name;
        this.type = "DETECTIVE";

        this.position = initialPosition;

        this.frontSide = name;
        this.backSide = "Standard";
        this.isFrontSideUp = true;
    }

    public Token(String frontSide, String backSide) {
        this.id = 0;
        this.name = frontSide;
        this.type = detectType(frontSide, backSide);

        this.position = -1;

        this.frontSide = frontSide;
        this.backSide = backSide;
        this.isFrontSideUp = true;
    }

    public Token(int id, String frontSide, String backSide) {
        this.id = id;
        this.name = frontSide;
        this.type = detectType(frontSide, backSide);

        this.position = -1;

        this.frontSide = frontSide;
        this.backSide = backSide;
        this.isFrontSideUp = true;
    }

    private String detectType(String frontSide, String backSide) {
        if ("Hourglass".equals(backSide)) {
            return "TIME";
        }

        return "ACTION";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isDetectiveToken() {
        return type.equals("DETECTIVE");
    }

    public boolean isActionToken() {
        return type.equals("ACTION");
    }

    public boolean isTimeToken() {
        return type.equals("TIME");
    }

    public int getPosition() {
        return position;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public String getCurrentSide() {
        return isFrontSideUp ? frontSide : backSide;
    }

    public String head() {
        return getCurrentSide();
    }

    public boolean isFrontSideUp() {
        return isFrontSideUp;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void move(int steps) {
        this.position = (this.position + steps) % 12;

        if (this.position < 0) {
            this.position += 12;
        }
    }

    public void turn() {
        this.isFrontSideUp = !this.isFrontSideUp;
    }
}