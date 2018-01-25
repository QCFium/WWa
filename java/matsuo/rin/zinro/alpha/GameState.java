package matsuo.rin.zinro.alpha;

import java.util.ArrayList;

class GameState {
    static ArrayList<Player> playerList = new ArrayList<>();
    static ArrayList<Integer> positions_setting = new ArrayList<>();

    static int days;
    static boolean isNoon;
    static int curPlayerId;

    private static boolean initialized = false;

    static int getPlayerNum() {
        return playerList.size();
    }

    static void init() {
        if (!initialized /*|| getPlayerNum() <= curPlayerId*/) {
            playerList = new ArrayList<>();
            positions_setting = new ArrayList<>();

            days = 0;
            isNoon = false;
            curPlayerId = 0;

            initialized = true;
        }
    }
}
