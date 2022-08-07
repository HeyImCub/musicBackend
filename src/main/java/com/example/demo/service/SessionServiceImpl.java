package com.example.demo.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Session;
import com.example.demo.repository.SessionRepository;



@Service
@Transactional
public class SessionServiceImpl implements SessionService{
	@Autowired
	private SessionRepository sessionService;
	
	@Override
	public String getYtLink(String query){
		String output = "";
		try {
			output = sendGET("https://youtube.googleapis.com/youtube/v3/search?type=video&q="+query+"&key=key");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
		
		
	}
	
	
	public static String sendGET(String GET_URL) throws IOException {
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			return response.toString();
		} else {
			return "GET request not worked";
		}

	}
	
	@Override
	public Session getSessionByAdminCode(long id) {
		// TODO Auto-generated method stub
		//return sessionService.getSessionByAdminCode();
		Optional<Session> optional = sessionService.findByAdminCode(id);
        Session session = null;
        if (optional.isPresent()) {
            session = optional.get();
        } else {
            throw new RuntimeException(" Session not found for Admin Code :: " + id);
        }
        return session;
	}

	@Override
	public void saveSession(Session session) {
		// TODO Auto-generated method stub
		sessionService.save(session);
	}

	@Override
	public Session findBysessionCode(long id) {
		// TODO Auto-generated method stub
		Optional<Session> optional = sessionService.findBySessionCode(id);
        Session session = null;
        if (optional.isPresent()) {
            session = optional.get();
        } else {
            throw new RuntimeException(" Session not found for Admin Code :: " + id);
        }
        return session;
	}

	@Override
	public void deleteBySessionCode(long id) {
		// TODO Auto-generated method stub
		this.sessionService.deleteBySessionCode(id);
	}

	@Override
	public boolean addYtVideo(String query, long code) throws JSONException {
		
		
		Optional<Session> optional = sessionService.findBySessionCode(code);
        Session session = null;
        if (optional.isPresent()) {
        	//there is a session 
            session = optional.get();
            
            JSONObject obj = new JSONObject(getYtLink(query));
    		JSONArray arr = obj.getJSONArray("items");
    		JSONObject jsonobject = arr.getJSONObject(0);
    		JSONObject jsonobject2 = jsonobject.getJSONObject("id");
    		String ytcode = jsonobject2.getString("videoId");
  
    		
            
            List<String> queue = session.getQueue();
            queue.add(ytcode);
            
            Session updated = new Session();
            updated.setId(session.getId());
            updated.setQueue(queue);
            updated.setSessionCode(session.getSessionCode());
            updated.setExpireNum(15);
            updated.setAdminCode(session.getAdminCode());
            
            
            sessionService.save(updated);
            return true;
        } else {
            //no session
        	return false;
        }
        
	}

	@Override
	public boolean sessionExists(long code) {
		// TODO Auto-generated method stub
		
        if (sessionService.existsBySessionCode(code)) {
        	return true;
        }else {
        	return false;
        }
	}

	@Override
	public boolean adminCodeExists(long code) {
		if (sessionService.existsByAdminCode(code)) {
        	return true;
        }else {
        	return false;
        }
	}

	@Override
	public void decreaseExpireNums() {
		List<Session> sessions = sessionService.findAll();
		for(int n = 0; n < sessions.size(); n++) {
			Session updatedSession = sessions.get(n);
			if((updatedSession.getExpireNum() - 1) < 1) {
				sessionService.delete(updatedSession);
			}else {
				updatedSession.setExpireNum(updatedSession.getExpireNum() - 1); 
				sessionService.save(updatedSession);
			}
			
			
		}
		
	}


	@Override
	public String getYtTitle(String link) throws IOException, JSONException {
		String output = sendGET("https://youtube.googleapis.com/youtube/v3/videos?part=snippet&id="+link+"&key=key");
		
		JSONObject obj = new JSONObject(output);
		JSONArray arr = obj.getJSONArray("items");
		JSONObject jsonobject = arr.getJSONObject(0);
		JSONObject jsonobject2 = jsonobject.getJSONObject("snippet");
		String ytTitle = jsonobject2.getString("title");
		return ytTitle;
	}

	

	
}
