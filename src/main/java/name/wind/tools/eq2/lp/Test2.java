package name.wind.tools.eq2.lp;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {

    public static void main(String... args) {
        String text = "\n" +
            "(1475006646)[Tue Sep 27 23:04:06 2016] Logging to 'logs/Thurgadin/eq2log_Breezzie.txt' is now *ON*\n" +
            "(1475006646)[Tue Sep 27 23:04:06 2016] You have entered Urartu's Guild Hall.\n" +
            "(1475006647)[Tue Sep 27 23:04:07 2016] Guild MOTD: Зал гильдии в Антонике. \n" +
            "DKP-система: http://urartu.igrograd.net\n" +
            "(1475006647)[Tue Sep 27 23:04:07 2016] Guildmate: Breezzie has logged in.\n" +
            "(1475006647)[Tue Sep 27 23:04:07 2016] Friend: Claudiaa has logged in\n" +
            "(1475006647)[Tue Sep 27 23:04:07 2016] Friend: Lavandela has logged in\n" +
            "(1475006647)[Tue Sep 27 23:04:07 2016] Friend: Ulcer has logged in\n" +
            "(1475006647)[Tue Sep 27 23:04:07 2016] Friend: Tryn has logged in\n" +
            "(1475006647)[Tue Sep 27 23:04:07 2016] Friend: Race_To_Trakanon.Aniko has logged in\n" +
            "(1475006648)[Tue Sep 27 23:04:08 2016] You have joined 'Урарту-2011' (6)\n" +
            "(1475006648)[Tue Sep 27 23:04:08 2016] You have joined 'BS' (8)\n" +
            "(1475006648)[Tue Sep 27 23:04:08 2016] You have joined 'Auction' (1)\n" +
            "(1475006648)[Tue Sep 27 23:04:08 2016] You have joined 'Fury' (2)\n";

        Pattern pattern = Pattern.compile("(?sm)^\\(\\d{10}\\)\\[.*\\]\\s.*?(?>(^\\(\\d{10}\\)))");
        Matcher matcher = pattern.matcher(text);

//        if (matcher.find()) {
//            System.out.println(matcher.group());
//        } else {
//            System.out.println("NOT FOUND");
//        }

        System.out.println(new Date("Tue Sep 27 23:04:06 2016").getTime());
        System.out.println(new Date(1475006646L * 1000L));
    }

}
