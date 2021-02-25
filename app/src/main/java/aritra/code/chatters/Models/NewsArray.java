package aritra.code.chatters.Models;

import java.util.ArrayList;

public class NewsArray  {

    private ArrayList<NewsPOJO> articles;
    private int totalResults;


    public ArrayList<NewsPOJO> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<NewsPOJO> articles) {
        this.articles = articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
