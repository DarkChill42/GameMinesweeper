package Game;

import java.awt.*;

public class Util {

    public static Color getRequiredColor(int number)
    {
        return switch (number) {
            case 1 -> new Color(0x2727B4);
            case 2 -> new Color(0x019012);
            case 3 -> new Color(0xFF0000);
            case 4 -> new Color(0x0000FF);
            case 5 -> new Color(0x6F0606);
            case 6 -> new Color(0x1AAA98);
            case 7 -> new Color(0x000000);
            case 8 -> new Color(0x68686D);
            default -> null;
        };
    }
}
