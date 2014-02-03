package controllers;

import models.Tag;
import models.User;
import models.Video;
import play.modules.elasticsearch.Query;
import play.modules.elasticsearch.search.SearchResults;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.elasticsearch.action.TransportActions.Admin.Cluster.Node;
import org.elasticsearch.index.query.HasChildQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by Milya on 04.12.13.
 */
public class VideoController extends Controller {
      
    public static void index() {
    	List<Video> videos = Video.findAll();
    	//Node node = nodeBuilder().node();
    //	Client client = node.client();
  //  System.out.println("number of tags in this video is"+videos.get(0).tags.size());
        LinkedList<LinkedList<Video>> chunks = new LinkedList<LinkedList<Video>>();;
        int counter = 0;
        LinkedList<Video> chunk = new LinkedList<Video>();
        for (Video video : videos) {
        	chunk.add(video);
            counter++;
            if (counter==6){
            	chunks.add(chunk);
                chunk = new LinkedList<Video>();
                counter=0;
            }
        }
        if (chunk.size()>0){
        	chunks.add(chunk);
        }

        render(chunks);
    }

    public static void addVideo() {
    	if(session.get("id")!=null)
       	 session.put("id","");
        render();
    }

    public static void saveVideo() {
        String title = request.params.get("video-title");
        String desc = request.params.get("video-description");

        /* TODO: add thumbnail to Video class*/
        String thumbnail = request.params.get("video-thumbnail");
        String link = request.params.get("video-id");

        /* TODO: change to actual user*/
        User user = new User("email", "pass", "user");
        user.save();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        Video video = new Video(title, desc, link, new Date(year, month, day), new ArrayList<Tag>(), user);
        video.save();

        redirect("/video/" + video.id);
    }

    public static void video(String id){
        Video video = Video.findById(Long.parseLong(id));
        System.out.println("i am here");
    	//Video.searchQuery("Dynamic");
        if(video == null) {
            redirect("/");
        }

        /* TODO: change to actual user*/
        User user = new User("email", "pass", "user");
        user.save();

        render(video, user);
    }
 /*   public static List<Video> searchQuery(String query)
    {
    	//HasChildQueryBuilder a= QueryBuilders.hasChildQuery("owner.id", QueryBuilders.queryString(query));
    	//Query<Video> queryVideo1=play.modules.elasticsearch.ElasticSearch.query(a, Video.class);
    	
    	Query<Video> queryVideo=  play.modules.elasticsearch.ElasticSearch.query(QueryBuilders.queryString(query), Video.class);
    	queryVideo.hydrate(true);
    //	SearchResults<Video>list2= play.modules.elasticsearch.ElasticSearch.searchAndHydrate(QueryBuilders.hasChildQuery("notes", QueryBuilders.queryString(query)), Video.class);
    	
    	SearchResults<Video>list= play.modules.elasticsearch.ElasticSearch.search(QueryBuilders.queryString(query), Video.class);
    	//SearchResults<Video>listhyd= play.modules.elasticsearch.ElasticSearch.searchAndHydrate(queryBuilder, clazz, facets) (QueryBuilders.queryString(query), Video.class);
    	System.out.println(list.objects.size());
    	for(int i=0;i<list.objects.size();i++)
    	{
    		System.out.println(list.objects.get(i).id+" "+list.objects.get(i).title );
    		if(list.objects.get(i).tags!=null)
    			System.out.println("i am different");
    		
    	}
    //	return list.objects;
    	return queryVideo.fetch().objects;//list.objects;
    }*/
    public static void  search(String query)
    {
    	System.out.println("i am here");
    	System.out.println(query);
    	
    	Video.searchQuery(query);
       	List<Video> videos = Video.searchQuery(query);
       // System.out.println("number of tags in this video is"+videos.get(0).tags.size());
       	LinkedList<LinkedList<Video>> chunks = new LinkedList<LinkedList<Video>>();;
    	int counter = 0;
    	LinkedList<Video> chunk = new LinkedList<Video>();
    	for (Video video : videos) {
    	   	chunk.add(video);
    	    counter++;
    	    if (counter==6){
    	       	chunks.add(chunk);
    	        chunk = new LinkedList<Video>();
    	        counter=0;
    	            }
    	        }
    	        if (chunk.size()>0){
    	        	chunks.add(chunk);
    	        }
   	        render(chunks);
    
    	    

    }
   

}
