package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Codes;
import com.example.demo.model.Message;
import com.example.demo.model.Queue;
import com.example.demo.model.Session;
import com.example.demo.service.SessionService;

@RestController
public class SessionController {
	
	@Autowired
    private SessionService sessionService;
	
	
	@GetMapping("/{code}/delete/{adminCode}/{ytlink}")
	public Message deleteSong(@PathVariable(value="code") long code,@PathVariable(value="adminCode") long adminCode, @PathVariable(value="ytlink") String ytlink) {
		Message msg = new Message();
		Session session = sessionService.findBysessionCode(code);
		List <String> links = session.getQueue();
		if(links.remove(ytlink)) {
			session.setQueue(links);
			sessionService.saveSession(session);
			msg.setData("Removed Song");
			return msg;
		}
		msg.setData("Authentication error");
		return msg;
	}
	
	@RequestMapping(value="/{code}",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Queue> listSongs(@PathVariable(value="code") long code) throws IOException, JSONException {
		
			Session session = sessionService.findBysessionCode(code);
			List<Queue> queueList = new ArrayList<>();
			
			for(String link : session.getQueue()) {
				Queue queue = new Queue();
				queue.setLink(link);
				queue.setName(sessionService.getYtTitle(link));
				queueList.add(queue);
			}
			
			return queueList;
		
	}
	
	public long getRandAdmin() {
		long randAdminCode = 0 + (long) (Math.random() * (1000000 - 0));
		
		while(sessionService.adminCodeExists(randAdminCode)) {
			getRandAdmin();
		}
		return randAdminCode;
		
	}
	public long getRandSession() {
		long randSessionCode = 1000001 + (long) (Math.random() * (100000000 - 1000001));
		while(sessionService.adminCodeExists(randSessionCode)) {
			getRandSession();
		}
		return randSessionCode;
	}
	
	@RequestMapping(value="/createcode",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Codes> createCode(HttpServletResponse response) {
		
		
		
			List<Codes> list= new ArrayList<>();
			long randSessionCode = getRandSession();
			long randAdminCode = getRandAdmin();
			//create session and add admin to cookies
		    
		    
			List<String> ytLinks= new ArrayList<>();
			Session newSession = new Session();
	        	
			newSession.setSessionCode(randSessionCode);
			newSession.setQueue(ytLinks);
			newSession.setExpireNum(15);
			newSession.setAdminCode(randAdminCode);
			System.out.println(newSession.getSessionCode());
	        sessionService.saveSession(newSession);
	        
	        Codes codes = new Codes();
	        codes.setAdmin(randAdminCode);
	        codes.setSession(randSessionCode);
	        list.add(codes);
	        return list;
		}
		//return randAdminCode + " " + randSessionCode;
	
	@RequestMapping(value="/{code}/clear/{adminCode}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Message clearQueue(@PathVariable(value = "code") long code,@PathVariable(value= "adminCode") long adminCode ){
		
		Session session = sessionService.findBysessionCode(code);
		
		
		Message message = new Message();
		
		if(session.getAdminCode() == adminCode) {
			//can Clear
			List<String> blankList= new ArrayList<>();
			session.setQueue(blankList);
			sessionService.saveSession(session);
			
			message.setData("Cleared Queue");
			return message;
		}else {
			message.setData("Authenticator Error");
			return message;
		}
	}
	
	
	@RequestMapping(value="/{code}/add/{query}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Message addSong(@PathVariable(value = "code") long code,@PathVariable(value = "query") String query) throws JSONException {
		Message msg = new Message();
		
		/*List<String> ytLinks= new ArrayList<>();
		ytLinks.add("youtube link 1");
		ytLinks.add("youtube link 2");
		
		Session first = new Session();
        	
		first.setSessionCode(6969);
        first.setQueue(ytLinks);
        first.setExpireNum(2);
        first.setAdminCode(45);
        this.sessionService.saveSession(first);
		Session session = sessionService.findBysessionCode(6969);
        return session.getQueue();
        
        */
		
		
		if(sessionService.sessionExists(code)){
			sessionService.addYtVideo(query, code);
			msg.setData("Adding Song");
			return msg;
		}else {
			msg.setData("Session Expired");
		return msg;
		}
    }
}
