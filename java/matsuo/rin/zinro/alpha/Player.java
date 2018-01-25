package matsuo.rin.zinro.alpha;

class Player {
    String name;
    boolean isAlive;
    boolean killedLast;
    boolean followed_fox;
    int position_id_settings;
    Position position;
    int lastSelectedPlayerId;

    // temporary stuff, only referenced by GameActivity#processUserSelection()
    boolean isProtected = false;
    boolean beingKilled = false;

    void kill(boolean night) {
        /*
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println(name + " killed(night is " + Boolean.toString(night) + " ).");
        */
        isAlive = false;
        if (night) killedLast = true;
    }

    void follow_fox() {
        followed_fox = true;
        this.kill(true);
    }

    Player(String name) {
        this.name = name;
        isAlive = true;
        killedLast = false;
        followed_fox = false;
        position_id_settings = -1;
        lastSelectedPlayerId = -1;
    }
}
