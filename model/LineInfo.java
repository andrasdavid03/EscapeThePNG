package model;

public class LineInfo {
    private final int lineHeight, lineStart, lineEnd, hitSide, colorCode;
    private final double textureX;

    public LineInfo(int lineHeight, int lineStart, int lineEnd, int hitSide, int colorCode, double textureX) {
        this.lineHeight = lineHeight;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.hitSide = hitSide;
        this.colorCode = colorCode;
        this.textureX = textureX;
    }

    public int getLineHeight() { return lineHeight; }
    public int getLineStart() { return lineStart; }
    public int getLineEnd() { return lineEnd; }
    public int getHitSide() { return hitSide; }
    public int getColorCode() { return colorCode; }
    public double getTextureX() { return textureX; }
}
