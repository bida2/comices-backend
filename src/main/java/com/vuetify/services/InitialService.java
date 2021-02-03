package com.vuetify.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vuetify.entities.Announcement;
import com.vuetify.entities.Comic;
import com.vuetify.entities.ComicBookNews;
import com.vuetify.entities.ComicReview;
import com.vuetify.entities.ForumPost;
import com.vuetify.entities.ForumThread;
import com.vuetify.entities.PriceAtDate;
import com.vuetify.entities.VideoMaterial;
import com.vuetify.repositories.AnnouncementRepository;
import com.vuetify.repositories.ComicBookNewsRepository;
import com.vuetify.repositories.ComicRepository;
import com.vuetify.repositories.ForumPostRepository;
import com.vuetify.repositories.ForumThreadRepository;
import com.vuetify.repositories.ReviewRepository;
import com.vuetify.repositories.VideoMaterialRepository;

@Service
public class InitialService {
	@Autowired
	private ComicRepository comicRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private ForumThreadRepository threadRepo;
	
	@Autowired
	private ForumPostRepository postRepo;
	
	@Autowired
	private AnnouncementRepository announceRepo;
	
	@Autowired
	private ComicBookNewsRepository newsRepo;
	
	@Autowired
	private VideoMaterialRepository videoRepo;
	
	
	@PostConstruct
	public void initData() throws MalformedURLException {
		// Disable this method going into production - unlike foreign-words, we don't have a user to check against here
		// Meaning that a comic might get deleted for example and our code would run if we were to check if comic exists in order to run the code
		ArrayList<PriceAtDate> test1 = new ArrayList<PriceAtDate>() {{ add(new PriceAtDate("January 2020", 3.45f));add(new PriceAtDate("February 2020", 3.15f));add(new PriceAtDate("March 2020", 3.35f));add(new PriceAtDate("April 2020", 3.25f));add(new PriceAtDate("May 2020", 3.45f));add(new PriceAtDate("June 2020", 3.54f)); add(new PriceAtDate("July 2020", 3.74f)); add(new PriceAtDate("August 2020", 3.24f)); }}; 
		ArrayList<PriceAtDate> test2 = new ArrayList<PriceAtDate>() {{ add(new PriceAtDate("January 2020", 3.45f));add(new PriceAtDate("February 2020", 3.15f));add(new PriceAtDate("March 2020", 3.35f));add(new PriceAtDate("April 2020", 3.25f));add(new PriceAtDate("May 2020", 3.45f));add(new PriceAtDate("June 2020", 3.11f)); add(new PriceAtDate("July 2020", 3.14f)); add(new PriceAtDate("August 2020", 3.28f)); }}; 
		// Create some comics for retrieval
		Comic superman = new Comic("Superman #32", "A rousing adventure by Mac Millan!", LocalDate.now(), "Mac Millan","upcoming", new URL("https://www.nostalgicbooksandcomics.com/wp-content/uploads/2014/06/1403551919000-SM-Cv32-ds.jpg"),new URL("https://www.dccomics.com/comics/superman-2011/superman-32"), new URL("https://www.dccomics.com/comics?seriesid=232275#browse"), 3.99f,  test1);
		Comic batman = new Comic("Batman #32", "A rousing adventure by Zack Snyder!", LocalDate.now(), "Zack Snyder", "upcoming", new URL("https://i0.wp.com/batman-news.com/wp-content/uploads/2017/10/Batman-32.jpg?w=1392&quality=80&strip=info&ssl=1"), new URL("https://www.dccomics.com/comics/batman-2016/batman-32"), new URL("https://www.dccomics.com/comics?seriesid=394511#browse"),2.99f, test2);
		Comic lantern = new Comic("Green Lanterns #32", "A rousing adventure by Geoff Johns!", LocalDate.now(), "Geoff Johns","classic",  new URL("https://images-na.ssl-images-amazon.com/images/S/cmx-images-prod/Item/559380/559380._SX1280_QL80_TTD_.jpg"), new URL("https://www.dccomics.com/comics/green-lanterns-2016/green-lanterns-32"), new URL("https://www.dccomics.com/comics?seriesid=394641#browse"), 2.99f);
		Comic wonderwoman = new Comic("Wonder Woman #32", "A rousing adventure by Mia Sivana!", LocalDate.now(), "Mia Sivana","classic", new URL("https://assets.comic-odyssey.com/products/covers/000/016/032/original/open-uri20171006-4-1gu7ivx?1507299327"), new URL("https://www.dccomics.com/comics/wonder-woman-2016/wonder-woman-32"), new URL("https://www.dccomics.com/comics?seriesid=394686#browse"), 2.99f);
		// Create some comic reviews for testing
		ComicReview supermanReview = new ComicReview("Superman #32 - Not quite what we expected...", "Lorem ipsum dolor amet set maligaros det rigoros", LocalDateTime.now(), Short.parseShort("1"));
		ComicReview batmanReview = new ComicReview("Batman #32 - Perhaps a new beginning for Catwoman and Batman?", "Lorem ipsum dolor amet set maligaros det rigoros", LocalDateTime.now(), Short.parseShort("3"));
		ComicReview lanternsReview = new ComicReview("Green Lanterns #32 - This series is starting to drag on..", "Lorem ipsum dolor amet set maligaros det rigoros", LocalDateTime.now(), Short.parseShort("2"));
		ComicReview lanternReview = new ComicReview("Green Lantern #32 - Hal Jordan is not dead?!?!?", "Lorem ipsum dolor amet set maligaros det rigoros", LocalDateTime.now(),Short.parseShort("5"), new URL("https://www.youtube.com/embed/CyVcjNuv9PI"));
		// Create some forum threads to test the forums and how posting works
		ForumThread announceThread = new ForumThread("Forums are now open!", LocalDateTime.now(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")));
		ForumThread registrationThread = new ForumThread("Forum registration is currently under construction!", LocalDateTime.now(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")));
		ForumPost announceInitPost = new ForumPost("Hey, we are open for posting!", "We are now open for posting on the forums! Have fun, guys!", announceThread.getThreadDateTime(),announceThread.getThreadDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")));
		announceInitPost.setOwnerThread(announceThread);
		ForumPost registrationInitPost = new ForumPost("Registration in progress....!", "The registration feature for the forums is currently under development!", registrationThread.getThreadDateTime(),announceThread.getThreadDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")));
		registrationInitPost.setOwnerThread(registrationThread);
		// Create some announcements
		Announcement one = new Announcement("New Forum Features!", "Head over here for a quick demo!", LocalDateTime.of(2020, 1, 20, 17, 30, 15), LocalDateTime.of(2020, 1, 20, 17, 30, 15).format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")));
		Announcement two = new Announcement("New Profile Features!", "You can now see your favourite comics!", LocalDateTime.of(2020, 1, 20, 14, 30, 30), LocalDateTime.of(2020, 1, 20, 14, 30, 30).format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")));
		Announcement three = new Announcement("ComicES Maintenance!", "Our service will be down from 08.04 to 09.04", LocalDateTime.of(2020, 1, 20, 10, 30, 30), LocalDateTime.of(2020, 1, 20, 10, 30, 30).format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")));
		Announcement four = new Announcement("A new moderator is here!", "Please give a warm welcome to our new moderator Bauli!", LocalDateTime.of(2020, 1, 20, 15, 30, 30), LocalDateTime.of(2020, 1, 20, 15, 30, 30).format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")));
		ComicBookNews newsOne = new ComicBookNews("Batman #45 coming out on 27.04.2020!", "A new Batman comic is coming to stores soon. We are eagerly awaiting it!",LocalDateTime.now(), new URL("https://www.youtube.com/embed/GriZdMwnhi8"));
		VideoMaterial videoOne = new VideoMaterial(new URL("https://www.youtube.com/embed/ZnuwB35GYMY"), new URL("https://img.youtube.com/vi/ZnuwB35GYMY/hqdefault.jpg"), "A video on how to embed videos", "It describes the quick and easy process to embed any video you please!", LocalDateTime.now());
		VideoMaterial videoTwo = new VideoMaterial(new URL("https://www.youtube.com/embed/vJWVXCeeKfI"), new URL("https://img.youtube.com/vi/vJWVXCeeKfI/hqdefault.jpg"),"A kickass metal song!","From Iced Earth's latest album, it is part of a concept based on heaven and hell", LocalDateTime.now());
		VideoMaterial videoThree = new VideoMaterial(new URL("https://www.youtube.com/embed/ICnxDNl2juk"),new URL("https://img.youtube.com/vi/ICnxDNl2juk/hqdefault.jpg"),"Another kickass metal song!", "From Iced Earth's latest album, it is part of a concept based on heaven and hell", LocalDateTime.now());
		VideoMaterial videoFour = new VideoMaterial(new URL("https://www.youtube.com/embed/CyVcjNuv9PI"),new URL("https://img.youtube.com/vi/CyVcjNuv9PI/hqdefault.jpg"),"A Yu-Gi-Oh GX Episode!", "Chad kicks major bro ass in this one", LocalDateTime.now());
		VideoMaterial test = new VideoMaterial(new URL("https://streamable.com/e/d8ovnd"), null, "A fun animated short!", null, LocalDateTime.now());
		newsRepo.save(newsOne);
		videoRepo.save(videoOne);
		videoRepo.save(videoTwo);
		videoRepo.save(videoThree);
		videoRepo.save(videoFour);
		videoRepo.save(test);
		announceRepo.save(one);
		announceRepo.save(two);
		announceRepo.save(three);
		announceRepo.save(four);
		reviewRepo.save(supermanReview);
		reviewRepo.save(batmanReview);
		reviewRepo.save(lanternsReview);
		reviewRepo.save(lanternReview);
		comicRepo.save(superman);
		comicRepo.save(batman);
		comicRepo.save(lantern);
		comicRepo.save(wonderwoman);
		threadRepo.save(announceThread);
		threadRepo.save(registrationThread);
		postRepo.save(announceInitPost);
		postRepo.save(registrationInitPost);
	}
}
