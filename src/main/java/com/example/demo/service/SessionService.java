package com.example.demo.service;





import java.io.IOException;

import org.json.JSONException;

import com.example.demo.model.Session;
public interface SessionService {
	Session getSessionByAdminCode(long id);
    void saveSession(Session session);
    Session findBysessionCode(long id);
    void deleteBySessionCode(long id);
	String getYtLink(String query);
	boolean addYtVideo(String query,long code) throws JSONException;
	boolean sessionExists(long code);
	boolean adminCodeExists(long code);
	void decreaseExpireNums();
	String getYtTitle(String link) throws IOException, JSONException;
}
