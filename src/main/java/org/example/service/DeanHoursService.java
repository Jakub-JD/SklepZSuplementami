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

/**
 * Serwis służący do wyszukiwania informacji o godzianach dziekańskich lub rektorskich oraz
 * sortujący je w kolejności od najstarszych do najmłodszych względem dat.
 */
public class DeanHoursService {
    private static final Pattern DATE_PATTERN = Pattern.compile("\\b(\\d{1,2})[./-](\\d{1,2})[./-](\\d{4})\\b");
    private static final int MAX_PAGES = 40;

    public List<DeanHoursEvent> getSortedDeanHoursEvents() {
        List<DeanHoursEvent> events = new ArrayList<>();
        int page = 1;
        boolean keepFetching = true;

        while (keepFetching && page <= MAX_PAGES) {
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
                    processArticle(article, events);
                }
                page++;
            } catch (HttpStatusException e) {
                if (e.getStatusCode() == 404) {
                    keepFetching = false;
                }
            } catch (IOException e) {
                keepFetching = false;
            }
        }

        events.sort(Comparator.comparing(DeanHoursEvent::getDate));
        return events;
    }

    private void processArticle(Element article, List<DeanHoursEvent> events) {
        Element h3 = article.select("h3").first();
        Elements paragraphs = article.select("p");

        String title = (h3 != null) ? h3.text().trim() : "Brak tytułu";
        StringBuilder fullContent = new StringBuilder();
        for (Element p : paragraphs) {
            fullContent.append(p.text()).append(" ");
        }
        String text = fullContent.toString().trim();

        if (text.isEmpty() && title.equals("Brak tytułu")) return;

        String combinedSearchText = (title + " " + text).toLowerCase();

        if (isDeanHoursRelated(combinedSearchText)) {
            LocalDate eventDate = extractDate(combinedSearchText);
            String itemUrl = extractUrl(article, h3);

            boolean alreadyExists = events.stream().anyMatch(e -> e.getTitle().equals(title));
            if (!alreadyExists) {
                events.add(new DeanHoursEvent(title, text, eventDate, itemUrl));
            }
        }
    }

    private boolean isDeanHoursRelated(String text) {
        return text.contains("godziny dziekańskie") ||
                text.contains("godziny rektorskie") ||
                text.contains("dziekańskie") ||
                text.contains("rektorskie");
    }

    private LocalDate extractDate(String text) {
        Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                int day = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2));
                String year = matcher.group(3);
                String formattedDateStr = String.format("%02d.%02d.%s", day, month, year);
                return LocalDate.parse(formattedDateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (Exception ignored) {}
        }
        return null;
    }

    private String extractUrl(Element article, Element h3) {
        if (h3 != null && h3.select("a").first() != null) {
            return h3.select("a").first().attr("abs:href");
        } else if (article.select("a").first() != null) {
            return article.select("a").first().attr("abs:href");
        }
        return "";
    }
}
