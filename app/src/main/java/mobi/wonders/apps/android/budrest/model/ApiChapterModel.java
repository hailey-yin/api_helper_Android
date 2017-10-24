package mobi.wonders.apps.android.budrest.model;

import java.util.List;

/**
 * Created by qinyuanmao on 15/12/9.
 */
public class ApiChapterModel {
    private String chapter;
    private List<Title> titles;

    public static class Title {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public List<Title> getTitles() {
        return titles;
    }

    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
