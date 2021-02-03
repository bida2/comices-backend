package com.vuetify.utils;

import java.util.Map;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.context.annotation.Configuration;

import com.vuetify.SpringbootVuetifyApplication;
import com.vuetify.entities.Announcement;
import com.vuetify.entities.ComicBookNews;
import com.vuetify.entities.ComicReview;

@Configuration
public class MiscFunctions {
	
	
	// Returns the ComicReview object that has been passed in for rating
	
	public ComicReview getReviewRating(String ratingType, ComicReview rev, Map<String,String> users, String username) {
		if (ratingType.equals("Up") && !users.containsKey(username)) {
			rev.setThumbsUp(rev.getThumbsUp() + 1);
		} else if (ratingType.equals("Down") && !users.containsKey(username)) {
			rev.setThumbsDown(rev.getThumbsDown() + 1);
		}  else if (ratingType.equals("Up") && !users.get(username).equals(ratingType)) {
			rev.setThumbsUp(rev.getThumbsUp() + 1);
			if (rev.getThumbsDown() != 0)
				rev.setThumbsDown(rev.getThumbsDown() - 1);
		} else if (ratingType.equals("Down") && !users.get(username).equals(ratingType)) {
			rev.setThumbsDown(rev.getThumbsDown() + 1);
			if (rev.getThumbsUp() != 0)
				rev.setThumbsUp(rev.getThumbsUp() - 1);
		}
			return rev;
	}
	
	public Announcement getAnnRating(String ratingType, Announcement ann, Map<String,String> users, String username) {
		if (ratingType.equals("Up") && !users.containsKey(username)) {
			ann.setThumbsUp(ann.getThumbsUp() + 1);
		} else if (ratingType.equals("Down") && !users.containsKey(username)) {
			ann.setThumbsDown(ann.getThumbsDown() + 1);
		} else if (ratingType.equals("Up") && !users.get(username).equals(ratingType)) {
			ann.setThumbsUp(ann.getThumbsUp() + 1);
			if (ann.getThumbsDown() != 0)
				ann.setThumbsDown(ann.getThumbsDown() - 1);
		} else if (ratingType.equals("Down") && !users.get(username).equals(ratingType)) {
			ann.setThumbsDown(ann.getThumbsDown() + 1);
			if (ann.getThumbsUp() != 0)
				ann.setThumbsUp(ann.getThumbsUp() - 1);
		}
		return ann;
	}
	
	public ComicBookNews getNewsRating(String ratingType, ComicBookNews news, Map<String,String> users, String username) {
		if (ratingType.equals("Up") && !users.containsKey(username)) {
			news.setThumbsUp(news.getThumbsUp() + 1);
		} else if (ratingType.equals("Down") && !users.containsKey(username)) {
			news.setThumbsDown(news.getThumbsDown() + 1);
		} else if (ratingType.equals("Up") && !users.get(username).equals(ratingType)) {
			news.setThumbsUp(news.getThumbsUp() + 1);
			if (news.getThumbsDown() != 0)
				news.setThumbsDown(news.getThumbsDown() - 1);
		} else if (ratingType.equals("Down") && !users.get(username).equals(ratingType)) {
			news.setThumbsDown(news.getThumbsDown() + 1);
			if (news.getThumbsUp() != 0)
				news.setThumbsUp(news.getThumbsUp() - 1);
		}
		return news;
	}
	
	public String sanitizeHTMLAntiSamy(String inputHTML) throws ScanException, PolicyException {
		Policy policy = Policy 
                .getInstance(SpringbootVuetifyApplication.class.getResourceAsStream("/antisamy-slashdot-1.4.4.xml"));
        AntiSamy sanitizer = new AntiSamy(policy);
        CleanResults scanned = sanitizer.scan(inputHTML);
        String sanitized = scanned.getCleanHTML();
		return sanitized;
	}
	
}
