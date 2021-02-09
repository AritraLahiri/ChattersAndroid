package aritra.code.chatters.Models;

import java.util.ArrayList;

public class NewsArray  {

    private ArrayList<NewsPOJO> articles;
    private int totalResults;


    public ArrayList<NewsPOJO> getArticles() {
        return articles;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
