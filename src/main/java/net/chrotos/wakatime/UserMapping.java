package net.chrotos.wakatime;

import lombok.NonNull;

public class UserMapping {
    public static String mapUser(@NonNull String user) {
        if (user.toLowerCase().endsWith("adihab")) {
            return "adihab@chrotos.net";
        }

        if (user.toLowerCase().endsWith("mr_magic_skull")) {
            return "mrmagicskull@chrotos.net";
        }

        if (user.toLowerCase().endsWith("gunter_lol")) {
            return "gunterlol@chrotos.net";
        }

        if (user.toLowerCase().endsWith("ezreal_l")) {
            return "kim@chrotos.net";
        }

        if (user.toLowerCase().endsWith("divinepuma64263")) {
            return "divinepuma@chrotos.net";
        }

        return user.toLowerCase() + "@chrotos.net";
    }
}
