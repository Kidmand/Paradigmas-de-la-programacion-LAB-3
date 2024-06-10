package utils;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import feed.*;

public class HandleFeedsData {
    
    static public void articlesToArchive(List<Article> articles, String filePath){

        String data = getTextFromArticles(articles);

        try {
            Files.write(Paths.get(filePath), data.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private String getTextFromArticles(List<Article> articles){
        String text = "";
        for (Article article : articles) {
            text += article.getTitle() + "\n";
            text += article.getDescription() + "\n";
        }
        return text;
    }
}
