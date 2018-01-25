package matsuo.rin.zinro.alpha;

import android.content.Context;
import android.support.annotation.*;

class Positions {
    static int num = 7; // hardcoded

    static Position createPositionById(int id) {
        if (id == Zinro.id) return new Zinro();
        else if (id == Uranai.id) return new Uranai();
        else if (id == Kishi.id) return new Kishi();
        else if (id == Reibai.id) return new Reibai();
        else if (id == Person.id) return new Person();
        else if (id == Fox.id) return new Fox();
        else if (id == Haitoku.id) return new Haitoku();
        else return null;
    }
}

abstract class Position {
    int id;
    int abilities;
    int sideId;
    int sideId_counted;
    int sideId_divined;
    String nightConfirmStr; // null if selection is not required

    Position (int id, int abilities, int sideId, int sideId_counted, int sideId_divined, String nightConfirmStr) {
        this.abilities = abilities;
        this.sideId = sideId;
        this.sideId_counted = sideId_counted;
        this.sideId_divined = sideId_divined;
        this.id = id;
        this.nightConfirmStr = nightConfirmStr;
    }

    String getName(Context context) {
        return getNameById(this.id, context);
    }

    static String getNameById(int id, Context context) {
        if (id == Zinro.id) return context.getString(R.string.zinro);
        else if (id == Uranai.id) return context.getString(R.string.uranai);
        else if (id == Kishi.id) return context.getString(R.string.kishi);
        else if (id == Reibai.id) return context.getString(R.string.reibai);
        else if (id == Person.id) return context.getString(R.string.person);
        else if (id == Fox.id) return context.getString(R.string.fox);
        else if (id == Haitoku.id) return context.getString(R.string.haitoku);
        else Error.error(context, "Positions#getPositionNameStrById : unsupported positionId");
        return null; // never reached
    }
}


// template positions
// if you want to add new, edit here, #PositionIds{}, Position.getPositionNameById(), PositionSettingsActivity#OnCreate#OnClick, activity_positions_settings.xml
class Zinro extends Position {
    static int id = 0;
    static int abilities = Abilities.BITE;
    static int sideId          = SideIds.WEREWOLVES;
    static int sideId_counted = SideIds.WEREWOLVES;
    static int sideId_divined = SideIds.WEREWOLVES;
    static String nightConfirmStr = confirmMessages.BITE_MSG;

    Zinro () {
        super(id, abilities, sideId, sideId_counted, sideId_divined, nightConfirmStr);
    }
}

class Uranai extends Position {
    static int id = 1;
    static int abilities = Abilities.DIVINE;
    static int sideId          = SideIds.PEOPLE;
    static int sideId_counted = SideIds.PEOPLE;
    static int sideId_divined = SideIds.PEOPLE;
    static String nightConfirmStr = confirmMessages.DIVINE_MSG;

    Uranai () {
        super(id, abilities, sideId, sideId_counted, sideId_divined, nightConfirmStr);
    }
}

class Kishi extends Position {
    static int id = 2;
    static int abilities = Abilities.DEFEND;
    static int sideId          = SideIds.PEOPLE;
    static int sideId_counted = SideIds.PEOPLE;
    static int sideId_divined = SideIds.PEOPLE;
    static String nightConfirmStr = confirmMessages.PROTECT_MSG;

    Kishi () {
        super(id, abilities, sideId, sideId_counted, sideId_divined, nightConfirmStr);
    }
}

class Reibai extends Position {
    static int id = 3;
    static int abilities = Abilities.KNOW_DEAD | Abilities.DOUBT;
    static int sideId          = SideIds.PEOPLE;
    static int sideId_counted = SideIds.PEOPLE;
    static int sideId_divined = SideIds.PEOPLE;
    static String nightConfirmStr = confirmMessages.DOUBT_MSG;

    Reibai () {
        super(id, abilities, sideId, sideId_counted, sideId_divined, nightConfirmStr);
    }
}

class Person extends Position {
    static int id = 4;
    static int abilities = Abilities.DOUBT;
    static int sideId          = SideIds.PEOPLE;
    static int sideId_counted = SideIds.PEOPLE;
    static int sideId_divined = SideIds.PEOPLE;
    static String nightConfirmStr = confirmMessages.DOUBT_MSG;

    Person () {
        super(id, abilities, sideId, sideId_counted, sideId_divined, nightConfirmStr);
    }
}

class Fox extends Position {
    static int id = 5;
    static int abilities = 0;
    static int sideId          = SideIds.FOXES;
    static int sideId_counted = SideIds.FOXES;
    static int sideId_divined = SideIds.FOXES;
    static String nightConfirmStr = confirmMessages.DOUBT_MSG;

    Fox() {
        super(id, abilities, sideId, sideId_counted, sideId_divined, nightConfirmStr);
    }
}

class Haitoku extends Position {
    static int id = 6;
    static int abilities = Abilities.KNOW_FOX | Abilities.DOUBT | Abilities.FOLLOW_FOX;
    static int sideId          = SideIds.FOXES;
    static int sideId_counted = SideIds.PEOPLE;
    static int sideId_divined = SideIds.PEOPLE;
    static String nightConfirmStr = confirmMessages.DOUBT_MSG;

    Haitoku() {
        super(id, abilities, sideId, sideId_counted, sideId_divined, nightConfirmStr);
    }
}

// static classes
class Abilities {
    static final int BITE         = 1 << 0;
    static final int DIVINE       = 1 << 1;
    static final int KNOW_DEAD   = 1 << 2;
    static final int DEFEND       = 1 << 3;
    static final int DEFEND_SELF = 1 << 4;
    static final int DOUBT        = 1 << 5;
    static final int KNOW_FOX     = 1 << 6;
    static final int FOLLOW_FOX   = 1 << 7;
}

class SideIds {
    static final int WEREWOLVES = 0;
    static final int PEOPLE   = 1;
    static final int FOXES      = 2;

    static @Nullable String getDivineResult(Context c, int sideId) {
        if (sideId == WEREWOLVES) return c.getString(R.string.divined_zinro);
        else if (sideId == PEOPLE) return c.getString(R.string.divined_not_zinro);
        else if (sideId == FOXES) return c.getString(R.string.divined_fox);
        else return null;
    }
}

/** file-private */
class confirmMessages {
    static final String DOUBT_MSG = "Do you really want to doubt this player?";
    static final String BITE_MSG = "Do you really want to bite this player?";
    static final String DIVINE_MSG = "Do you really want to know what this player is?";
    static final String PROTECT_MSG = "Do you really want to protect this player?";
}
