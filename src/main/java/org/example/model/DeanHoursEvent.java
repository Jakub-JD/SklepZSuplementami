package org.example.model;

import java.time.LocalDate;

/**
 * Model reprezentujący ogłoszenie o godzinach dziekańskich lub rektorskich.
 */
public class DeanHoursEvent {
    String title;
    String content;
    LocalDate date;
    String url;

    public DeanHoursEvent(String title, String content, LocalDate date, String url) {
        this.title = title != null ? title : "Brak tytułu";
        this.content = content != null ? content : "";
        this.date = date != null ? date : LocalDate.MIN;
        this.url = url != null ? url : "";
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDate getDate() { return date; }
    public String getUrl() { return url; }

    @Override
    public String toString() {
        String dateStr = date.equals(LocalDate.MIN) ? "Brak daty w treści" : date.toString();
        String truncatedContent = content.length() > 120 ? content.substring(0, 120) + "..." : content;
        return "Tytuł: " + title +
                "\nData: " + dateStr +
                "\nTreść: " + truncatedContent +
                "\nLink: " + (url.isEmpty() ? "Brak" : url);
    }
}
