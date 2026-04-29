package com.example.sportsfirst.models;

import com.example.sportsfirst.R;

import java.util.ArrayList;
import java.util.List;

public class NewsData {

    public static List<NewsItem> getAllNews() {

        List<NewsItem> list = new ArrayList<>();

        list.add(new NewsItem(
                "Football Final",
                "Team A vs Team B",
                R.drawable.ath1,
                "Football",
                "featured"));

        list.add(new NewsItem(
                "Cricket World Cup",
                "Final match highlights",
                R.drawable.cric1,
                "Cricket",
                "featured"));

        list.add(new NewsItem(
                "Australia make call on Hazlewood replacement at T20 World Cup",
                "Australia have finally made a call on Josh Hazlewood’s replacement at the ICC Men’s T20 World Cup with news that veteran batter Steve Smith has been parachuted into their 15-player squad with immediate effect.",
                R.drawable.cric2,
                "Cricket",
                "latest"));

        list.add(new NewsItem("Pathirana receives NOC, set to join Kolkata Knight Riders squad",
                "Sri Lanka fast bowler Matheesha Pathirana has received his No-Objection Certificate (NOC) from Sri Lanka Cricket and is set to join the Kolkata Knight Riders squad for the ongoing Indian Premier League season.\n" +
                        "\n" +
                        "The 23-year-old pacer will be available for selection as early as KKR’s next fixture against Chennai Super Kings, scheduled to be played in Chennai on 14th April.",
                R.drawable.cric3,
                "Cricket",
                "latest"));

        list.add(new NewsItem("Fixtures revealed for Sri Lanka tour of India 2026",
                "The fixtures for Sri Lanka’s tour of India in 2026 have been officially confirmed, with the two teams set to face off in a white-ball series in December.\n" +
                        "\n" +
                        "The tour will consist of six matches, featuring three ODIs followed by three T20Is, promising an exciting end-of-year contest between the two Asian rivals.",
                R.drawable.cric4,
                "Cricket",
                "latest"));

        list.add(new NewsItem(
                "UUDS Tuskers powered by Sri Lankan players emerge Emirates Dubai Cup Champions (International Social)",
                "By seamlessly connecting social Rugby and Sri Lankan sporting pride, the UUDS Tuskers, the official Rugby team of UUDS AERO, clinched the Cup Championship of the International Men’s Social category at the Dubai Emirates Rugby 7’s 2025. In the Cup Final, the UUDS Tuskers side, captained by 2016 Isipathana College captain Kushan Indunil Pieris, posted a stunning 28-05 victory over the USA-based invitational rugby club, cementing their place as one of the elite Social Rugby Sevens outfits.",
                R.drawable.rug1,
                "Rugby",
                "latest"));

        list.add(new NewsItem(
                "Unbeaten Kingswood cruising towards promotion",
                "The decision to turndown division A rugby seemed to be paying off as Kingswood College, still unbeaten and cruising at the top of the Segment IIB points table, seek to round off a successful season as they take on the down cast from Segment A.",
                R.drawable.rug2,
                "Rugby",
                "latest"));

        list.add(new NewsItem(
                "Sri Lanka Football strengthens the U-19 outfit with the origin players",
                "The 7th edition of the SAFF U-19 Championship, which will be organized by the South Asian Football Federation (SAFF), will be kicked off at the Golden Jubilee Outdoor Stadium in Yupia, India, on 9th May 2025. Sri Lanka Football has meticulously selected a 23-member Sri Lanka Under-19 football team from the best talents showcased in the recently concluded schools football tournament.",
                R.drawable.foot1,
                "Football",
                "latest"));

        list.add(new NewsItem(
                "The schedule for the Champions League semi-finals has been announced",
                "The Champions League semi-final schedule has been set as the final four chase a place in the Wembley showpiece.\n" +
                        "\n" +
                        "Hopes of an all-English semi were ended on Wednesday as Arsenal and Manchester City both bowed out at the quarter-final stage.",
                R.drawable.foot2,
                "Football",
                "latest"));


        return list;
    }
}