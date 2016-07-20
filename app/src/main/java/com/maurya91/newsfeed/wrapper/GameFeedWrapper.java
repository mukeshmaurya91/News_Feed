package com.maurya91.newsfeed.wrapper;

import android.util.Log;

import com.maurya91.newsfeed.model.GameFeed;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Mukesh Kumar Maurya on 19-07-2016 in project News Feed.
 */
public class GameFeedWrapper {
    GameFeed mFeed;
    ArrayList<GameFeed> mFeedList;
    private static final String SITE="http://www.gamespot.com";
    private static final String ARTICLE_TAG="article";
    private static final String A_TAG="a";
    private static final String IMAGE_TAG="img";
    private static final String HREF_ATTRIB="href";
    private static final String SRC_ATTRIB="src";
    private static final String H3_TAG="h3";
    private static final String H1_TAG="h1";
    private static final String H2_TAG="h2";
    private static final String P_TAG="p";
    private static final String TIME_TAG="time";
    private static final String DATE_TIME_ATTRIB="datetime";

    public ArrayList<GameFeed> getFeedList(Document doc){
        mFeedList = new ArrayList<>();
        //Select All articles
        Elements articles = doc.select(ARTICLE_TAG+".media-article");
        for (Element element:articles){
            mFeed= new GameFeed();
            // ANCHOR TAG SELECT for feed Link
            Elements links=element.select(A_TAG);
            mFeed.setFeedUrl(SITE+links.attr(HREF_ATTRIB));
//            mk("Link :: "+links.attr(HREF_ATTRIB));
            //Select Image URL
            Elements imgLinks=element.select(IMAGE_TAG);
            mFeed.setImageUrl(imgLinks.attr(SRC_ATTRIB));
//           mk("img:: "+imgLinks.attr(SRC_ATTRIB));
            //Select feed title
            Elements title=element.select(H3_TAG);
            mFeed.setTitle(title.text());
//            mk("title:: "+title.text());
            Elements descriptionText=element.select(P_TAG);
            mFeed.setDescriptionText(descriptionText.text());
            //Select textual time
            Elements time=element.select(TIME_TAG);
            mFeed.setTextTime(time.text());
//            mk("time:: "+time.text());
            mFeed.setTime(time.attr(DATE_TIME_ATTRIB));
            mk(mFeed.toString());
            mFeedList.add(mFeed);
        }
        return mFeedList;
    }
    public GameFeed getDetailNewsFeed(Document doc){
        //Select articles
        Elements articles = doc.select(ARTICLE_TAG);

            mFeed= new GameFeed();
        mFeed.setFeedUrl(doc.location());
            // ANCHOR TAG SELECT for feed Link
            Elements author=articles.select(H3_TAG);
            mFeed.setAuthor("By: "+author.text());
            mk("author :: "+author.text());
        Elements detailNews=articles.select("div.js-content-entity-body");
        Elements newsPara=detailNews.select(P_TAG);
        StringBuilder news= new StringBuilder();
        for (Element element:newsPara){
            news.append(element.text()+"\n\n");
        }
        mFeed.setDetailNews(news.toString());
        mFeed.setAuthor(author.text());
        mk("author :: "+author.text());
            //Select Image URL
            Elements imgLinks=articles.select("figure");
            mFeed.setImageUrl(imgLinks.attr("data-img-src"));
//           mk("img:: "+imgLinks.attr(SRC_ATTRIB));
            //Select feed title
            Elements title=articles.select(H1_TAG);
            mFeed.setTitle(title.text());
            mk("title:: "+title.text());
            Elements descriptionText=articles.select(H2_TAG);
            mFeed.setDescriptionText(descriptionText.text());
            //Select textual time
            Elements time=articles.select(TIME_TAG);
            mFeed.setTextTime(time.text());
            mk("time:: "+time.text());
            mFeed.setTime(time.attr(DATE_TIME_ATTRIB));
            mk(mFeed.toString());
//        }
        return mFeed;
    }
    public static String getSite(){
        return SITE;
    }
    private void mk(String s) {
        Log.d("MK",":: "+s);
    }
}
