package ru.example.gamecalendar.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ru.example.gamecalendar.model.Game;
import ru.example.gamecalendar.model.Month;

public class GamesParse {
    public static ArrayList<Game> parsingGames(String url) {
        System.out.println(url);
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(document == null)
            return null;

        ArrayList<Game> gameArrayList = new ArrayList<>();

        Elements elements = document.select(".gamecalendar_box");
        for (Element element : elements){
            Game game = new Game();
            Element img = element.selectFirst("img");
            Element info = element.selectFirst(".info_block_textbox");
            String sourceImg = img.attr("src");
            if(sourceImg.indexOf("http") != 0){
                sourceImg = "https:"+ sourceImg;
            }
            game.setPicURL(sourceImg);
            game.setName(info.selectFirst(".release_name").text());
            game.setDate(info.selectFirst(".release_data").text());

            Elements platformElements = info.selectFirst(".platform").select("a");
            String platformString = "";
            for(Element platformElement : platformElements){
                platformString += platformElement.text() + " ";
            }
            game.setPlatform(platformString.trim());

            Elements genreElements = info.selectFirst(".genre").select("a");
            String genreString = "";
            for(Element genreElement : genreElements){
                genreString += genreElement.text() + " ";
            }
            game.setGenre(genreString.trim());
            gameArrayList.add(game);
        }

        return gameArrayList;
    }

    public static Map<Integer, ArrayList<Month>> parsingListDate(String url){
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(document == null)
            return null;

        Map<Integer, ArrayList<Month>> listDate = new HashMap<>();

        Elements filterItems = document.selectFirst(".filter_menu").select("a.filter_menu_link");
        for(Element filterItem : filterItems){
            String href = filterItem.attr("href");
            String [] splitedHref = href.split("/");
            String month = splitedHref[splitedHref.length-1];
            Integer year = Integer.parseInt(splitedHref[splitedHref.length-2]);
            if(listDate.containsKey(year)){
                listDate.get(year).add(Month.equalsName(month));
            }
            else{
                ArrayList<Month> months = new ArrayList<>();
                months.add(Month.equalsName(month));
                listDate.put(year, months);
            }
        }

        Set<Integer> keys = listDate.keySet();
        for(Integer key : keys){
            ArrayList<Month> months = listDate.get(key);
        }

        return listDate;
    }
}
