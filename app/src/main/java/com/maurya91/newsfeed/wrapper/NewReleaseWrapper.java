package com.maurya91.newsfeed.wrapper;

import android.util.Log;

import com.maurya91.newsfeed.model.NewRelease;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Mukesh Kumar Maurya on 20-07-2016 in project News Feed.
 */
public class NewReleaseWrapper {
    private NewRelease mNewRelease;
    private ArrayList<NewRelease> mNewReleaseList;
    private static final String A_TAG="a";
    private static final String IMAGE_TAG="img";
    private static final String HREF_ATTRIB="href";
    private static final String SRC_ATTRIB="src";
    private static final String H4_TAG="h4";
    private static final String H1_TAG="h1";
    private static final String H2_TAG="h2";
    private static final String ASIDE_TAG="aside";
    private static final String UL_TAG="ul";
    private static final String LI_TAG="li";
    private static final String P_TAG="p";

    public ArrayList<NewRelease> getNewReleaseList(Document document){
        mNewReleaseList=new ArrayList<>();
        Elements asideContent=document.select(ASIDE_TAG+".secondary-content");
        Elements ul=asideContent.select(UL_TAG+".game");
        Elements listItems=ul.select(LI_TAG+".game-background");
        for(Element element:listItems){
            mNewRelease =new NewRelease();
            Elements url=element.select(A_TAG+".game-info");
            Log.d("Content<><<>","Url:: "+url.attr(HREF_ATTRIB));
            mNewRelease.setUrl(url.attr(HREF_ATTRIB));
//            Elements img=element.select("li[style^=url(]");
           String imageUrl= element.attr("style");
            Log.d("Content<><<>","imageUrl:: "+imageUrl);
          String imgStr=imageUrl.substring(23,imageUrl.length()-2);
            Log.d("Content<><<>","imageUrl:: "+imgStr);
            mNewRelease.setImageUrl(imgStr);
            Elements title=element.select(H4_TAG+".game-title");
            Log.d("Content<><<>","Title:: "+title.text());
            mNewRelease.setTitle(title.text());
            Elements date=element.select(P_TAG+".game-meta");
            mNewRelease.setDate(date.text());
            Log.d("Content<><<>","Date:: "+date.text());
            mNewReleaseList.add(mNewRelease);

        }
     return mNewReleaseList;
    }

}
