package com.bb.ragnar;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.xmlrpc.XmlRpcException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wtekiela.opensub4j.api.OpenSubtitles;
import com.github.wtekiela.opensub4j.impl.OpenSubtitlesImpl;
import com.github.wtekiela.opensub4j.response.SubtitleInfo;

import jpa.Jpa;
import jpa.Query;
import jpa.Torrent;
import nl.stil4m.transmission.api.TransmissionRpcClient;
import nl.stil4m.transmission.api.domain.AddTorrentInfo;
import nl.stil4m.transmission.api.domain.TorrentInfo;
import nl.stil4m.transmission.api.domain.TorrentInfoCollection;
import nl.stil4m.transmission.rpc.RpcClient;
import nl.stil4m.transmission.rpc.RpcConfiguration;
import nl.stil4m.transmission.rpc.RpcException;

@RestController
public class TorrentController {

	@RequestMapping("/current")
    public List<TorrentInfo> getCurrentTorrents() {
		
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		RpcConfiguration rpcConfiguration = new RpcConfiguration();
        rpcConfiguration.setHost(URI.create("http://localhost:9091/transmission/rpc"));
        RpcClient client = new RpcClient(rpcConfiguration, objectMapper);
        TransmissionRpcClient rpcClient = new TransmissionRpcClient(client);
        
        TorrentInfoCollection vmi = null;
		try {
			vmi = rpcClient.getAllTorrentsInfo();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return vmi.getTorrents();
    }
	
	@RequestMapping("/add")
    public void addSeries(@RequestParam String name) throws RpcException, MalformedURLException, XmlRpcException {
		
//		ArrayList<Torrent> torrents = new ArrayList<>();
//		torrents = Jpa.Search(new Query(name.toLowerCase(), 0));
//		
//		Optional<Torrent> maxSeed = torrents.stream().max(Comparator.comparing(Torrent::getSeeds));
//		
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		RpcConfiguration rpcConfiguration = new RpcConfiguration();
//        rpcConfiguration.setHost(URI.create("http://localhost:9091/transmission/rpc"));
//        RpcClient client = new RpcClient(rpcConfiguration, objectMapper);
//        TransmissionRpcClient rpcClient = new TransmissionRpcClient(client);
//       
//        if(maxSeed.isPresent()) {
//	        AddTorrentInfo atd = new AddTorrentInfo();
//	        atd.setFilename(maxSeed.get().getMagnet());
//	        
//	        rpcClient.addTorrent(atd);
//	        
//        }
//        
    	URI s = URI.create("https://api.opensubtitles.org:443/xml-rpc");
    	OpenSubtitles openSub = new OpenSubtitlesImpl(s.toURL());
		
    	openSub.login("en", "OSTestUserAgentTemp");
    	System.out.println(openSub.serverInfo());
    	
    	List<SubtitleInfo> i = openSub.searchSubtitles("en", "tt2582782");
    	
    	List<SubtitleInfo> subs = openSub.searchSubtitles("en", "big bang theory", "10", "10");
		
    	subs.forEach(x -> System.out.println(x.getFileName()));
    }
}
