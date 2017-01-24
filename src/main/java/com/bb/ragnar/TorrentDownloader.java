package com.bb.ragnar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wtekiela.opensub4j.api.OpenSubtitles;
import com.github.wtekiela.opensub4j.impl.OpenSubtitlesImpl;
import com.github.wtekiela.opensub4j.response.SubtitleFile;
import com.github.wtekiela.opensub4j.response.SubtitleInfo;

import jpa.Jpa;
import jpa.Query;
import jpa.Torrent;
import nl.stil4m.transmission.api.TransmissionRpcClient;
import nl.stil4m.transmission.api.domain.AddTorrentInfo;
import nl.stil4m.transmission.rpc.RpcClient;
import nl.stil4m.transmission.rpc.RpcConfiguration;
import nl.stil4m.transmission.rpc.RpcException;

@Component
public class TorrentDownloader extends TimerTask {

	@Autowired SeriesRepository seriesRepo;
	
	public void checkForSeries() {
		List<Series> seriesToday = seriesRepo.findByDownloadDate(Date.valueOf(LocalDate.now()));
		
		System.out.println(seriesToday.size() + " series found to download");
		
		for(Series s : seriesToday) {
			ArrayList<Torrent> torrents = new ArrayList<>();
			torrents = Jpa.Search(new Query(s.getName().toLowerCase() + " S0" + s.getCurrentSeason()+ "E" + s.getNextEpisode(), 0));
			
			Optional<Torrent> maxSeed = torrents.stream().max(Comparator.comparing(Torrent::getSeeds));
			
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			RpcConfiguration rpcConfiguration = new RpcConfiguration();
	        rpcConfiguration.setHost(URI.create("http://localhost:9091/transmission/rpc"));
	        RpcClient client = new RpcClient(rpcConfiguration, objectMapper);
	        TransmissionRpcClient rpcClient = new TransmissionRpcClient(client);
	       
	        if(maxSeed.isPresent()) {
		        AddTorrentInfo atd = new AddTorrentInfo();
		        atd.setFilename(maxSeed.get().getMagnet());
		        
		        try {
					rpcClient.addTorrent(atd);
					
					System.out.println(atd + " added to download.");
					
			        URI uri = URI.create("https://api.opensubtitles.org:443/xml-rpc");
			    	OpenSubtitles openSub = new OpenSubtitlesImpl(uri.toURL());
					
			    	openSub.login("en", "OSTestUserAgentTemp");
					List<SubtitleInfo> i = openSub.searchSubtitles("en", s.getName(), s.getCurrentSeason().toString(),s.getNextEpisode().toString() );

					s.incrementDownloadDate();
					s.incrementNextEpisode();
					
					seriesRepo.save(s);
					
					System.out.println(s + " updated.");
					
					if(i.isEmpty() != true) {
						List<SubtitleFile> result = openSub.downloadSubtitles(i.get(0).getSubtitleFileId());
						
						if(!result.isEmpty()) {
							
							SubtitleFile sf = result.get(0);
							File f = new File(s.getName()+".srt");
							FileWriter fw = new FileWriter(f);
							fw.write(sf.getContentAsString("UTF-8"));
							fw.flush();
							fw.close();
						}
						
					}
					
				} catch (RpcException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (XmlRpcException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        

	        }
    	
		}
	}

	@Override
	public void run() {
		checkForSeries();
	}
}
