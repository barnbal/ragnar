package com.bb.ragnar;

import java.net.MalformedURLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.xmlrpc.XmlRpcException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application  {
	
    public static void main( String[] args ) throws XmlRpcException, MalformedURLException{
    	SpringApplication.run(Application.class);

    }
    
    @Bean
	public CommandLineRunner demo(final SeriesRepository repository, final TorrentDownloader td) {
		return (args) -> {
			Series s = new Series();
			s.setName("Modern Family");
			s.setDownloadDate(Date.valueOf(LocalDate.of(2017, 1, 11)));
			s.setCurrentSeason(8);
			s.setNextEpisode(10);
			repository.save(s);
			
			Timer t = new Timer();
			t.scheduleAtFixedRate(td, Date.valueOf(LocalDate.now()), 99999999999999999l);
		};
    }
//  @Bean
//	public CommandLineRunner demo() {
//		return (args) -> {
//			URI s = URI.create("https://api.opensubtitles.org:443/xml-rpc");
//	    	OpenSubtitles openSub = new OpenSubtitlesImpl(s.toURL());
//			
//	    	openSub.login("en", "OSTestUserAgentTemp");
//	    	System.out.println(openSub.serverInfo());
//	    	
//	    	List<SubtitleInfo> i = openSub.searchSubtitles("en", "2582782");
//	    	
//	    	List<SubtitleInfo> subs = openSub.searchSubtitles("en", "big bang theory", "10", "10");
//			
//	    	subs.forEach(x -> System.out.println(x.getFileName()));
//	    	i.forEach(x -> System.out.println(x.getFileName()));
//		    
//		};
//  }
}
