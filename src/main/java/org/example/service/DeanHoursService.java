package org.example.service;

import org.example.model.DeanHoursEvent;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeanHoursService {
    public static void DeanHoursFunc() {
        List<DeanHoursEvent> events = new ArrayList<>();
        Pattern datePattern = Pattern.compile("\\b(\\d{1,2})[./-](\\d{1,2})[./-](\\d{4})\\b");

        int page = 1;
        boolean keepFetching = true;

        while (keepFetching && page <= 40) {
            System.out.print("\r\u001B[KPrzeszukiwanie strony " + page);
            System.out.flush();
            String url = (page == 1) ? "https://cs.pollub.pl/news/" : "https://cs.pollub.pl/news/page/" + page + "/";

            try {
                Document doc = Jsoup.connect(url).get();

                Elements articles = doc.select("article, .post");
                if (articles.isEmpty()) {
                    articles = doc.select("body");
                }

                for (Element article : articles) {
                    Element h3 = article.select("h3").first();
                    Elements paragraphs = article.select("p");

                    String title = (h3 != null) ? h3.text().trim() : "Brak tytułu";

                    StringBuilder fullContent = new StringBuilder();
                    for (Element p : paragraphs) {
                        fullContent.append(p.text()).append(" ");
                    }
                    String text = fullContent.toString().trim();

                    if (text.isEmpty() && title.equals("Brak tytułu")) continue;

                    String combinedSearchText = (title + " " + text).toLowerCase();

                    if (combinedSearchText.contains("godziny dziekańskie") ||
                            combinedSearchText.contains("godziny rektorskie") ||
                            combinedSearchText.contains("dziekańskie") ||
                            combinedSearchText.contains("rektorskie")) {

                        LocalDate eventDate = null;
                        Matcher matcher = datePattern.matcher(combinedSearchText);

                        if (matcher.find()) {
                            try {
                                String day = matcher.group(1);
                                String month = matcher.group(2);
                                String year = matcher.group(3);
                                String formattedDateStr = String.format("%02d.%02d.%s", Integer.parseInt(day), Integer.parseInt(month), year);
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                eventDate = LocalDate.parse(formattedDateStr, formatter);
                            } catch (Exception ignored) {}
                        }

                        String itemUrl = "";
                        if (h3 != null && h3.select("a").first() != null) {
                            itemUrl = h3.select("a").first().attr("abs:href");
                        } else if (article.select("a").first() != null) {
                            itemUrl = article.select("a").first().attr("abs:href");
                        }

                        String finalTitle = title;
                        boolean alreadyExists = events.stream().anyMatch(e -> e.getTitle().equals(finalTitle));
                        if (!alreadyExists) {
                            events.add(new DeanHoursEvent(title, text, eventDate, itemUrl));
                        }
                    }
                }

                page++;

            } catch (HttpStatusException e) {
                if (e.getStatusCode() == 404) {}
                keepFetching = false;
            } catch (IOException e) {
                keepFetching = false;
            }
        }

        events.sort(Comparator.comparing(DeanHoursEvent::getDate));

        System.out.println("\n  WYNIKI WYSZUKIWANIA: GODZINY DZIEKAŃSKIE/REKTORSKIE");
        System.out.println("  (Od najstarszego do najmłodszego)");
        System.out.println("==================================================");

        if (events.isEmpty()) {
            System.out.println("Nie znaleziono ogłoszeń o godzinach dziekańskich/rektorskich w archiwum.");
        } else {
            for (int i = 0; i < events.size(); i++) {
                System.out.println("\n[" + (i + 1) + "]");
                System.out.println(events.get(i));
                System.out.println("--------------------------------------------------");
            }
        }
    }
}
