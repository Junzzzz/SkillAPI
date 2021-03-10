package skillapi.api.gui.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jun
 * @date 2020/11/18.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@SideOnly(Side.CLIENT)
public final class Layout {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y + height / 2;
    }

    public int getTop() {
        return y;
    }

    public int getBottom() {
        return y + height;
    }

    public int getRight() {
        return x + width;
    }

    public int getLeft() {
        return x;
    }

    public boolean isInBox(int mouseX, int mouseY) {
        return mouseX > this.x && mouseY > this.y && mouseX < width + this.x && mouseY < height + this.y;
    }

    public boolean isInHeight(int mouseY) {
        return mouseY > this.y && mouseY < height + this.y;
    }

    public boolean isInWidth(int mouseX) {
        return mouseX > this.x && mouseX < width + this.x;
    }
}