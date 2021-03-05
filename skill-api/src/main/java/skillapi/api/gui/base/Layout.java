package skillapi.api.gui.base;

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
public final class Layout {
    private int x;
    private int y;
    private int width;
    private int height;

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