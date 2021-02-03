package com.vuetify.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.WordUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.vuetify.entities.Announcement;
import com.vuetify.entities.Author;
import com.vuetify.entities.Comic;
import com.vuetify.entities.ComicBookNews;
import com.vuetify.entities.ComicComments;
import com.vuetify.entities.ComicRating;
import com.vuetify.entities.ComicReview;
import com.vuetify.entities.ComicReviewRequest;
import com.vuetify.entities.ComicSuggestion;
import com.vuetify.entities.Contact;
import com.vuetify.entities.ForumPost;
import com.vuetify.entities.ForumThread;
import com.vuetify.entities.PriceAtDate;
import com.vuetify.entities.UserPreferences;
import com.vuetify.entities.VideoMaterial;
import com.vuetify.exceptions.ComicBookNameExistsException;
import com.vuetify.exceptions.NotLoggedInException;
import com.vuetify.repositories.AnnouncementRepository;
import com.vuetify.repositories.AuthorRepository;
import com.vuetify.repositories.ComicBookNewsRepository;
import com.vuetify.repositories.ComicRatingRepository;
import com.vuetify.repositories.ComicRepository;
import com.vuetify.repositories.ComicReviewRequestRepository;
import com.vuetify.repositories.ComicSearchRepository;
import com.vuetify.repositories.ComicSuggestionRepository;
import com.vuetify.repositories.CommentsRepository;
import com.vuetify.repositories.ContactRepository;
import com.vuetify.repositories.ForumPostRepository;
import com.vuetify.repositories.ForumThreadRepository;
import com.vuetify.repositories.ReviewRepository;
import com.vuetify.repositories.UserPreferencesRepository;
import com.vuetify.repositories.VideoMaterialRepository;
import com.vuetify.services.EmailService;
import com.vuetify.services.StorageService;
import com.vuetify.singletons.SearchTags;
import com.vuetify.utils.FieldValidation;
import com.vuetify.utils.JWTDecoder;
import com.vuetify.utils.MiscFunctions;

import fr.plaisance.bitly.Bit;
import fr.plaisance.bitly.Bitly;

@RestController
public class RESTVuetify {
	
	@Autowired
	private ComicRepository comicRepo;
	
	@Autowired
	private AuthorRepository authorRepo;
	
	@Autowired
	private ContactRepository contactRepo;
	
	@Autowired
    private StorageService storageService;
	
	@Autowired
	private CommentsRepository commentRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private ComicRatingRepository ratingRepo;
	
	@Autowired
	private ForumThreadRepository threadRepo;
	
	@Autowired 
	private ForumPostRepository postRepo;
	
	@Autowired
	private HttpServletRequest servReq;
	
	@Autowired
	private ComicReviewRequestRepository reviewRequestRepo;
	
	@Autowired
	private UserPreferencesRepository userPrefRepo;
	
	@Autowired
	private MiscFunctions miscFunc;
	
	@Autowired
	private ComicSuggestionRepository comicSuggestRepo;
	
	@Autowired
	private AnnouncementRepository announceRepo;
	
	@Autowired
	private ComicBookNewsRepository newsRepo;
	
	@Autowired
	private ComicSearchRepository searchRepo;
	
	@Autowired
	private VideoMaterialRepository videoRepo;
	
	private JWTDecoder jwt = new JWTDecoder();
	
	private static final Gson gson = new Gson();
	
	// Holds all search terms in SearchTags' tags Map<String,Integer> member variable
	private SearchTags tags = SearchTags.getInstance(new HashMap<String, Integer>());

	
	@GetMapping("/comicType")
	public List<Comic> getByComicType(@RequestParam("t") String type) {
		return comicRepo.findByStatus(type.toLowerCase());
	}
	
	@GetMapping("/authors")
	public List<Author> comicAuthors() {
		return authorRepo.findAll();
	}
	
	// Checked for validation - 11/3/2020
	@PostMapping("/addComic")
	public ResponseEntity<String> addComic(@RequestHeader("USER-TOKEN") String accessToken,@RequestParam("comicName") String comicName, 
			@RequestParam("comicDesc") String comicDesc, @RequestParam("releaseStatus") String releaseStatus,@RequestParam("author") String author,
			@RequestParam("comicCoverURL") String comicCoverURL, @RequestParam("buyComicURL") String buyURL,
			@RequestParam("seriesURL") String seriesURL, @RequestParam("price") float price) throws MalformedURLException {
		if (!jwt.decodeJwt(accessToken, "admins")) 
			return new ResponseEntity<String>("Couldn't add a new comic - Insufficient priviliges!", HttpStatus.BAD_REQUEST);
		if (!FieldValidation.isComicNameValid(comicName) || !FieldValidation.containsOnlyLetters(author) 
				|| !FieldValidation.isValidUrl(comicCoverURL) || !FieldValidation.isValidUrl(buyURL) 
				|| !FieldValidation.isValidUrl(seriesURL) || !FieldValidation.containsOnlyIntegerOrFloat(price)) {
			return new ResponseEntity<String>("Incorrect data entered - check your data and try again!", HttpStatus.BAD_REQUEST);
		}
		if (comicRepo.findByComicName(comicName) != null) 
			throw new ComicBookNameExistsException("Comic Book with name " + comicName + " already exists!");
		Comic comic = new Comic(comicName, comicDesc, LocalDate.now(), author, releaseStatus.toLowerCase(), new URL(comicCoverURL), new URL(buyURL), new URL(seriesURL), price);
		comicRepo.save(comic);
		return new ResponseEntity<String>("Comic added successfully!", HttpStatus.OK);
		
	}
	
	
	@GetMapping("/getAllNews")
	public Page<ComicBookNews> getAllNews() {
		return newsRepo.findAll(PageRequest.of(0, 9, Sort.by("newsArticleFormattedDate")));
	}
	
	@GetMapping("/getVideo")
	public VideoMaterial getAVideo(@RequestParam("vId") long vId) {
		return videoRepo.getOne(vId);
	}
	
	// Checked for validation - 11/3/2020
	@PutMapping("/editVideo")
	public ResponseEntity<String> editAVideo(@RequestHeader("USER-TOKEN") String token, 
			@RequestParam("vId") long vId,
			@RequestParam("videoHeader") String header,
			@RequestParam("videoContent") String content,
			@RequestParam("embedUrl") URL embed) throws ScanException, PolicyException {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Cannot edit this video material!", HttpStatus.FORBIDDEN);
		if (embed == null || !FieldValidation.isValidUrl(embed.toString())) 
			return new ResponseEntity<String>("Check your entered embed URL and try again!", HttpStatus.BAD_REQUEST);
		VideoMaterial video = videoRepo.getOne(vId);
		header = miscFunc.sanitizeHTMLAntiSamy(header);
		content = miscFunc.sanitizeHTMLAntiSamy(content);
		video.setDescription(content);
		video.setSubtitleHeader(header);
		video.setEmbedUrl(embed);
		videoRepo.save(video);
		return new ResponseEntity<String>("Successfully edited video material!", HttpStatus.OK);
	}
	
	@GetMapping("/getVideoMaterials")
	public List<VideoMaterial> getAllVideoMaterials() {
		return videoRepo.findAll();
	}
	
	@PostMapping("/addVideo")
	public ResponseEntity<String> addVideoMaterial(@RequestHeader("USER-TOKEN") String token, @RequestParam(value="thumbUrl", required=false) String thumbUrl , 
			@RequestParam("videoTitle") String videoTitle,
			@RequestParam("embedUrl") String embedUrl, @RequestParam("description") String desc) throws MalformedURLException
	{
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Cannot remove this video material!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(videoTitle) || !FieldValidation.isValidUrl(embedUrl)
				|| !FieldValidation.isNotEmptyOrNull(desc) || (!thumbUrl.isEmpty() && !FieldValidation.isValidUrl(thumbUrl))) {
			return new ResponseEntity<String>("Incorrect or missing data - check your entered information and try again!", HttpStatus.BAD_REQUEST);
		}
		VideoMaterial video = null;
		if (thumbUrl.isEmpty() || thumbUrl == null) {
			video = new VideoMaterial(new URL(embedUrl), videoTitle, desc, LocalDateTime.now());
		} else {
			video = new VideoMaterial(new URL(embedUrl), new URL(thumbUrl), videoTitle, desc, LocalDateTime.now());
		}
		videoRepo.save(video);
		return new ResponseEntity<String>("Successfully saved video material!", HttpStatus.OK);
		
	}
	
	@DeleteMapping("/removeVideoMaterial")
	public ResponseEntity<String> removeVideoMaterials(@RequestHeader("USER-TOKEN") String token,@RequestParam("vId") long videoId) {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Cannot remove this video material!", HttpStatus.FORBIDDEN);
		if (videoRepo.findById(videoId).isPresent()) 
			videoRepo.deleteById(videoId);
		return new ResponseEntity<String>("Successfully deleted video material!", HttpStatus.OK);
	}
	
	@GetMapping("/getVideoPage")
	public Page<VideoMaterial> getVideoMaterialPage() {
		return videoRepo.findAll(PageRequest.of(0, 3, Sort.by("timePostedFormatted")));
	}
	
	@GetMapping("/getNewsArticle")
	public ComicBookNews getNewsArticle(@RequestParam("aId") long articleId)
	{
		return newsRepo.getOne(articleId);
	}
	
	// Checked for validation - 11/3/2020
	@PutMapping("/editNewsArticle") 
	public ResponseEntity<String> saveNewsArticleEdits(@RequestHeader("USER-TOKEN") String token,@RequestParam("aId") long aId, @RequestParam("title") String title, @RequestParam("content") String content) throws ScanException, PolicyException {
		if (!jwt.decodeJwt(token, "admins")) 
			return new ResponseEntity<String>("Couldn't edit news article!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(title) || !FieldValidation.isNotEmptyOrNull(content)) 
			return new ResponseEntity<String>("No news title or content entered!", HttpStatus.BAD_REQUEST);
		ComicBookNews article = newsRepo.getOne(aId);
		title = miscFunc.sanitizeHTMLAntiSamy(title);
		content = miscFunc.sanitizeHTMLAntiSamy(content);
		article.setNewsArticleTitle(title);
		article.setNewsArticleContent(content);
		newsRepo.save(article);
		return new ResponseEntity<String>("Saved news article successfully!", HttpStatus.OK);
	}
	
	@DeleteMapping("/delArticle")
	public ResponseEntity<String> deleteNewsArticle(@RequestHeader("USER-TOKEN") String token, @RequestParam("aId") long aId) {
		if (!jwt.decodeJwt(token, "admins")) 
			return new ResponseEntity<String>("Couldn't edit news article!", HttpStatus.FORBIDDEN);
		newsRepo.deleteById(aId);
		return new ResponseEntity<String>("Deleted news article successfully!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/4/2020
	@PutMapping("/editAnnouncement")
	public ResponseEntity<String> saveEditAnnouncement(@RequestHeader("USER-TOKEN") String token, @RequestParam("aId") long annId, 
			@RequestParam("annTitle") String annTitle, @RequestParam("annContent") String annContent ) throws ScanException, PolicyException {
		if (!jwt.decodeJwt(token, "admins")) 
			return new ResponseEntity<String>("Couldn't edit announcement!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(annTitle) || !FieldValidation.isNotEmptyOrNull(annContent)) 
			return new ResponseEntity<String>("No news title or news content entered!", HttpStatus.BAD_REQUEST);
		annTitle = miscFunc.sanitizeHTMLAntiSamy(annTitle);
		annContent = miscFunc.sanitizeHTMLAntiSamy(annContent);
		Announcement ann = announceRepo.getOne(annId);
		ann.setAnnTitle(annTitle);
		ann.setAnnContent(annContent);
		announceRepo.save(ann);
		return new ResponseEntity<String>("Successfully edited announcement!", HttpStatus.OK);
	}
	
	
	// Checked for validation - 11/4/2020
	@DeleteMapping("/delComic")
	public ResponseEntity<String> deleteComic(@RequestHeader("USER-TOKEN") String accessToken,@RequestParam("cId") long comicId) {
		if (!jwt.decodeJwt(accessToken, "admins")) 
			return new ResponseEntity<String>("Could not complete comic deletion!", HttpStatus.BAD_REQUEST);
		comicRepo.delete(comicRepo.findByComicId(comicId));
		return new ResponseEntity<String>("Comic deleted successfully!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/5/2020
	@PostMapping("/contact")
	public ResponseEntity<String> saveContact(@RequestHeader("USER-TOKEN") String token) throws MalformedURLException {
		if (!jwt.decodeJwt(token, "users"))
			return new ResponseEntity<String>("Cannot send message!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(servReq.getParameter("contactTopic")) || !FieldValidation.isNotEmptyOrNull(servReq.getParameter("contactEmail"))
				|| !FieldValidation.isNotEmptyOrNull(servReq.getParameter("contactContent")))
			return new ResponseEntity<String>("Missing data - please check your input and try again!", HttpStatus.BAD_REQUEST);
		if (servReq.getParameter("image").isEmpty() || servReq.getParameter("image") == null)
		 {
			Contact message = new Contact(servReq.getParameter("contactTopic"),servReq.getParameter("contactEmail"),servReq.getParameter("contactContent"));
			contactRepo.save(message);
			return new ResponseEntity<String>("Your message has been sent successfully!", HttpStatus.OK);
		} else {
			Bitly bitly = Bit.ly("0386b443e9f5f335a346ae03c9921ffa7c8817a0");
			String shortUrl = bitly.shorten(servReq.getParameter("image"));
			Contact message = new Contact(servReq.getParameter("contactTopic"),servReq.getParameter("contactEmail"),servReq.getParameter("contactContent"), new URL(shortUrl), new URL(servReq.getParameter("image")));
			contactRepo.save(message);
		}
		return new ResponseEntity<String>("Your message has been sent successfully!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/5/2020
	@PutMapping("/approveSuggestedComic")
	public ResponseEntity<String> saveApprovedComic(@RequestHeader("USER-TOKEN") String token,@RequestParam("cid") Long comicId ) {
		if (!jwt.decodeJwt(token, "admins")) 
			return new ResponseEntity<String>("Cannot approve comic!", HttpStatus.FORBIDDEN);
		ComicSuggestion suggComic = comicSuggestRepo.getOne(comicId);
		Comic appComic = new Comic(suggComic.getSuggestedComicName(), suggComic.getSuggestedAuthorName(), suggComic.getSuggestedReleaseDate(), 
				"upcoming", suggComic.getBuyComicURL(), suggComic.getComicCoverURL(), suggComic.getSeriesURL(), suggComic.getPrice());
		comicRepo.save(appComic);
		comicSuggestRepo.delete(suggComic);
		return new ResponseEntity<String>("Successfully approved comic!", HttpStatus.OK);
		
	}
	
	@GetMapping("/getAnnouncements")
	public Page<Announcement> getAnnouncements() {
		return announceRepo.findAll(PageRequest.of(0, 3, Sort.by("annFormattedDate")));
	}
	
	@GetMapping("/getAnnouncement")
	public Announcement getAnnouncement(@RequestParam("aId") long announcementId) {
		return announceRepo.getOne(announcementId);
	}
	
	// Checked for validation - 11/5/2020
	@PostMapping("/rateReview")
	public ResponseEntity<String> rateReview(@RequestHeader("USER-TOKEN") String token,@RequestParam("u") String username,@RequestParam("rId") long reviewId, @RequestParam("r") String ratingType) {
		if (!jwt.decodeJwt(token, "users")) 
			return new ResponseEntity<String>("Cannot rate announcement!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(username) || !FieldValidation.isNotEmptyOrNull(ratingType)) 
			return new ResponseEntity<String>("Something went wrong while rating the review - check your input and try again!", HttpStatus.BAD_REQUEST);
		ComicReview rev = reviewRepo.getOne(reviewId);
		Map<String,String> users = rev.getRatedByUsers();
		rev = miscFunc.getReviewRating(ratingType, rev, users, username);
		users.put(username, ratingType);
		rev.setRatedByUsers(users);
		reviewRepo.save(rev);
		return new ResponseEntity<String>("Successfully rated review!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/7/2020
	@PostMapping("/rateAnnouncement")
	public ResponseEntity<String> rateAnnouncement(@RequestHeader("USER-TOKEN") String token,@RequestParam("u") String username, @RequestParam("aId") long annId, @RequestParam("r") String ratingType) {
		if (!jwt.decodeJwt(token, "users")) 
			return new ResponseEntity<String>("Cannot rate announcement!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(username) || !FieldValidation.isNotEmptyOrNull(ratingType)) 
			return new ResponseEntity<String>("Something went wrong while rating the announcement - check your input and try again!", HttpStatus.BAD_REQUEST);
		Announcement ann = announceRepo.getOne(annId);
		Map<String,String> users = ann.getRatedByUsers();
		ann = miscFunc.getAnnRating(ratingType,ann, users, username);
		users.put(username, ratingType);
		ann.setRatedByUsers(users);
		announceRepo.save(ann);
		return new ResponseEntity<String>("Successfully rated announcement!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/8/2020
	@PostMapping("/rateArticle")
	public ResponseEntity<String> rateArticle(@RequestHeader("USER-TOKEN") String token,@RequestParam("u") String username, @RequestParam("aId") long annId, @RequestParam("r") String ratingType) {
		if (!jwt.decodeJwt(token, "users")) 
			return new ResponseEntity<String>("Cannot rate article!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(username) || !FieldValidation.isNotEmptyOrNull(ratingType)) 
			return new ResponseEntity<String>("Something went wrong while rating the article - check your input and try again!", HttpStatus.BAD_REQUEST);
		ComicBookNews news = newsRepo.getOne(annId);
		Map<String,String> users = news.getRatedByUsers();
		news = miscFunc.getNewsRating(ratingType, news, users, username);
		users.put(username, ratingType);
		news.setRatedByUsers(users);
		newsRepo.save(news);
		return new ResponseEntity<String>("Successfully rated news article!", HttpStatus.OK);
	}
	
	@PutMapping("/rejectSuggestedComic")
	public ResponseEntity<String> rejectSuggestedComic(@RequestHeader("USER-TOKEN") String token,@RequestParam("cid") Long comicId) {
		if (!jwt.decodeJwt(token, "admins")) 
			return new ResponseEntity<String>("Cannot reject comic!", HttpStatus.FORBIDDEN);
		ComicSuggestion suggComic = comicSuggestRepo.getOne(comicId);
		comicSuggestRepo.delete(suggComic);
		return new ResponseEntity<String>("Successfully rejected comic!", HttpStatus.OK);
	}
	
	@GetMapping("/getSuggestion")
	public ComicSuggestion getSuggestion(@RequestParam("id") long suggId) {
		return comicSuggestRepo.getOne(suggId);
	}
	
	@GetMapping("/getRating")
	public ComicRating getRating(@RequestParam("u") String user, @RequestParam("cId") long comicId)
	{
		return ratingRepo.findByUserAndComicId(user, comicId);
	}
	
	// Checked for validation - 11/8/2020
	@PostMapping("/setRating")
	public ResponseEntity<String> setComicRatingForUser(@RequestHeader("USER-TOKEN") String token,@RequestParam("u") String user, @RequestParam("r") int rating, @RequestParam("cId") long comicId) {
		if (!jwt.decodeJwt(token, "users")) 
			return new ResponseEntity<String>("Couldn't update the rating for this comic!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(user)) 
			return new ResponseEntity<String>("Something went wrong while rating the review - check your input and try again!", HttpStatus.BAD_REQUEST);
		ComicRating comicRating = ratingRepo.findByUserAndComicId(user,comicId);
		if (comicRating != null) {
			comicRating.setRating(rating);
			ratingRepo.save(comicRating);
		} else {
			comicRating = new ComicRating(rating,user,comicId);
			ratingRepo.save(comicRating);
		}
		return new ResponseEntity<String>("Successfully updated your rating for this comic!", HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteReview")
	public ResponseEntity<String> deleteReview(@RequestHeader("USER-TOKEN") String token, @RequestParam("rId") long rId) {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Couldn't delete review!", HttpStatus.FORBIDDEN);
		reviewRepo.deleteById(rId);
		return new ResponseEntity<String>("Deleted review successfully!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/8/2020
	@PutMapping("/submitSuggestionEdit")
	public ResponseEntity<String> submitSuggestionEdit(@RequestHeader("USER-TOKEN") String token,@RequestParam("id") long suggId,
			@RequestParam("comicName") String comicName,@RequestParam("authorName") String authorName,
			@RequestParam("buyURL") URL buyURL, @RequestParam("coverURL") URL coverURL,
			@RequestParam("comicReleaseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate) {
		if (!jwt.decodeJwt(token,"admins"))
			return new ResponseEntity<String>("Couldn't submit the edit for this comic suggestion!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isComicNameValid(comicName) || !FieldValidation.containsOnlyLetters(authorName) 
				|| !FieldValidation.isValidUrl(buyURL.toString()) || !FieldValidation.isValidUrl(coverURL.toString()) || !FieldValidation.isNotEmptyOrNull(releaseDate))
			return new ResponseEntity<String>("Something went wrong! Check your input and try again!", HttpStatus.BAD_REQUEST);
		ComicSuggestion editSugg = comicSuggestRepo.getOne(suggId);
		editSugg.setSuggestedComicName(comicName);
		editSugg.setSuggestedAuthorName(authorName);
		editSugg.setBuyComicURL(buyURL);
		editSugg.setComicCoverURL(coverURL);
		editSugg.setSuggestedReleaseDate(releaseDate);
		comicSuggestRepo.save(editSugg);
		return new ResponseEntity<String>("Saved edit successfully!", HttpStatus.OK);
	}
	
	@GetMapping("/getPost")
	public ForumPost getPostById(@RequestParam("pId") long postId) {
		return postRepo.getOne(postId);
	}
	
	@GetMapping("/getReviewRequests")
	public List<ComicReviewRequest> getAllReviewRequests() {
		return reviewRequestRepo.findAll();
	}
	
	@GetMapping("/getReviewRequest")
	public ComicReviewRequest getReviewRequest(@RequestParam("rId") long reviewId) {
		return reviewRequestRepo.getOne(reviewId);
	}
	
	// Checked for validation - 11/10/2020
	@PostMapping("/requestReview") 
	public ResponseEntity<String> requestComicReview(@RequestHeader("USER-TOKEN") String userToken,@RequestParam("comicSummaryURL") String comicSummaryURL,
			@RequestParam("comicName") String requestComicName,
			@RequestParam("comicCoverURL") String comicCoverURL) throws MalformedURLException {
		if (!jwt.decodeJwt(userToken, "users"))
			return new ResponseEntity<String>("Couldn't submit your review request!", HttpStatus.FORBIDDEN);		
		if (!FieldValidation.isValidUrl(comicSummaryURL) || !FieldValidation.isValidUrl(comicCoverURL) || !FieldValidation.isComicNameValid(requestComicName))
			return new ResponseEntity<String>("Something went wrong! Check the URLs and comic name and try again!", HttpStatus.BAD_REQUEST);
		ComicReviewRequest request = new ComicReviewRequest(requestComicName, new URL(comicSummaryURL), new URL(comicCoverURL));
		reviewRequestRepo.save(request);
		return new ResponseEntity<String>("Submitted request successfully!", HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteReviewRequest")
	public ResponseEntity<String> deleteReviewRequest(@RequestHeader("USER-TOKEN") String userToken, @RequestParam("rId") long requestReviewId) {
		if (!jwt.decodeJwt(userToken, "admins")) 
			return new ResponseEntity<String>("Couldn't delete review request!", HttpStatus.FORBIDDEN);
		reviewRequestRepo.deleteById(requestReviewId);
		return new ResponseEntity<String>("Review request deleted successfully!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/10/2020
	@PutMapping("/editPost") 
	public ResponseEntity<String> savePostEdit(@RequestHeader("USER-TOKEN") String token,@RequestParam("pId") long postId, @RequestParam("content") String postContent, @RequestParam("title") String postTitle) throws ScanException, PolicyException {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Couldn't edit post!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(postContent))
			return new ResponseEntity<String>("Post content is empty!", HttpStatus.BAD_REQUEST);
		// Sanitize the HTML input - mainly removes <script> tags and "javascript:...." in HTML tag attributes
		postTitle = miscFunc.sanitizeHTMLAntiSamy(postTitle);
		postContent = miscFunc.sanitizeHTMLAntiSamy(postContent);
		ForumPost editPost = postRepo.getOne(postId);
		editPost.setPostContent(postContent);
		editPost.setPostTitle(postTitle);
		postRepo.save(editPost);
		return new ResponseEntity<String>("Edited post successfully!", HttpStatus.OK);
	}
	
	
	@GetMapping("/allContacts")
	public List<Contact> allContacts(@RequestHeader("USER-TOKEN") String token) {
		if (!jwt.decodeJwt(token, "admins"))
			return new ArrayList<Contact>();
		return contactRepo.findAll();
	}
	
	@GetMapping("/getContact")
	public Contact getContact(@RequestHeader("USER-TOKEN") String token,@RequestParam("cId") long contactId) {
		if (!jwt.decodeJwt(token, "admins")) 
			return new Contact();
		return contactRepo.getOne(contactId);
	}
	
	@GetMapping("/getComicSummary")
	public Comic getComicForSummary(@RequestParam("cId") Long cId) {
		return comicRepo.findByComicId(cId);
	}
	
	// Checked for validation - 11/10/2020
	@PutMapping("/editReview")
	public ResponseEntity<String> saveReviewEdit(@RequestHeader("USER-TOKEN") String token, @RequestParam("rId") long reviewId, 
			@RequestParam("reviewTitle") String title, @RequestParam("reviewContent") String content) throws ScanException, PolicyException {
		if (!jwt.decodeJwt(token, "admins")) 
			return new ResponseEntity<String>("Insufficient priviliges to edit comic!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(title) || !FieldValidation.isNotEmptyOrNull(content))
			return new ResponseEntity<String>("Review title or review content is empty!", HttpStatus.BAD_REQUEST);
		// Sanitize both the review title and the review content here below
		title = miscFunc.sanitizeHTMLAntiSamy(title);
		content = miscFunc.sanitizeHTMLAntiSamy(content);
		ComicReview editReview = reviewRepo.findByReviewId(reviewId);
		editReview.setReviewTitle(title);
		editReview.setReviewContent(content);
		reviewRepo.save(editReview);
		return new ResponseEntity<String>("Successfully edited comic review!", HttpStatus.OK);
	}
	
	@GetMapping("/getFavourites")
	public List<Comic> getUserFavourites(@RequestParam("u") String username) {
		if (!FieldValidation.isNotEmptyOrNull(username))
			throw new NullPointerException();
		UserPreferences userPref = userPrefRepo.findByUsername(username);
		if (userPref != null) {
			return userPref.getComics();
		} else {
			userPref = new UserPreferences(username, new ArrayList<Comic>());
			return userPref.getComics();
		}
	}
	
	// Checked for validation - 11/11/2020
	@PostMapping("/addFavourite")
	public ResponseEntity<String> addFavouriteComic(@RequestHeader("USER-TOKEN") String userToken,@RequestParam("u") String username, @RequestParam("cId") Long comicId) {
		if (!jwt.decodeJwt(userToken, "users")) 
			return new ResponseEntity<String>("You must be logged in to favourite a comic!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(username))
			return new ResponseEntity<String>("Cannot add favourite comic! Exit and login again!", HttpStatus.BAD_REQUEST);
		UserPreferences userPref = userPrefRepo.findByUsername(username);
		if (userPref == null) 
			userPref = new UserPreferences(username, new ArrayList<Comic>());
		Comic comic = comicRepo.findByComicId(comicId);
		List<Comic> listOfComics = userPref.getComics();
		if (listOfComics.contains(comicRepo.getOne(comicId)))
			return new ResponseEntity<String>("Comic has already been added to Favourites!", HttpStatus.OK);
		listOfComics.add(comic);
		userPref.setComics(listOfComics);
		userPrefRepo.save(userPref);
		return new ResponseEntity<String>("Added to Favourites successfully!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/12/2020
	@PostMapping("/addComicSuggestion")
	public ResponseEntity<String> addComicSuggestion(@RequestParam("comicName") String comicName,
			@RequestParam("u") String username,
			@RequestParam("authorName") String authorName,
			@RequestParam("buyURL") URL buyURL,
			@RequestParam("coverURL") URL coverURL,
			@RequestParam("seriesURL") URL seriesURL,
			@RequestParam("price") float price,
			@RequestParam("comicReleaseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate,
			@RequestHeader("USER-TOKEN") String token) {
		if (!jwt.decodeJwt(token, "users")) 
			return new ResponseEntity<String> ("Cannot add comic suggestion - user not logged-in!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isComicNameValid(comicName) || !FieldValidation.isNotEmptyOrNull(username) || !FieldValidation.containsOnlyLetters(authorName) 
				|| !FieldValidation.isValidUrl(buyURL.toString()) || !FieldValidation.isValidUrl(coverURL.toString()) || !FieldValidation.isValidUrl(seriesURL.toString())
				|| !FieldValidation.containsOnlyIntegerOrFloat(price) || !FieldValidation.isNotEmptyOrNull(releaseDate))
			return new ResponseEntity<String>("Something went wrong - check your data and try again!", HttpStatus.BAD_REQUEST);
		ComicSuggestion sugg = new ComicSuggestion(username, comicName, authorName, releaseDate, buyURL, coverURL, seriesURL, price);
		comicSuggestRepo.save(sugg);
		return new ResponseEntity<String> ("Comic suggestion added successfully!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/12/2020
	// When testing, note that Gmail usually will reject emails with the same sender and recipient
	@PostMapping("/respondContactMessage")
	public ResponseEntity<String> respondToMessage(@RequestHeader("USER-TOKEN") String token,@RequestParam("email") String email, 
			@RequestParam("emailContent") String emailContent, 
			@RequestParam("senderEmail") String senderEmail) throws IOException {
		if (!jwt.decodeJwt(token, "admins")) 
			return new ResponseEntity<String>("Couldn't send e-mail! You need admin rights!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isValidEmail(senderEmail) || !FieldValidation.isValidEmail(email))
			return new ResponseEntity<String>("Sender or receiver e-mail adress is invalid!", HttpStatus.BAD_REQUEST);
		EmailService emailService = new EmailService();
		emailService.sendEmail(senderEmail, email, emailContent);
		return new ResponseEntity<String>("Successfully sent e-mail response!", HttpStatus.OK);
		
	}
	
	@DeleteMapping("/deleteContact")
	public ResponseEntity<String> deleteContactMessage(@RequestHeader("USER-TOKEN") String token,@RequestParam("cId") long contactId) {
		if (!jwt.decodeJwt(token, "admins")) 	
			return new ResponseEntity<String>("Couldn't delete contact message! You need admin rights!", HttpStatus.FORBIDDEN);
		contactRepo.deleteById(contactId);
		return new ResponseEntity<String>("Successfully removed contact message!", HttpStatus.OK);
	}
	
	@GetMapping("/getSuggestions")
	public List<ComicSuggestion> getSuggestionsByUser(@RequestParam("u") String user, @RequestHeader("USER-TOKEN") String token) {
		if (!jwt.decodeJwt(token, "users")) 
			return new ArrayList<ComicSuggestion>();
		List<ComicSuggestion> suggestions = comicSuggestRepo.findBySuggesterUsername(user);
		return suggestions;
	}
	
	@GetMapping("/getAllSuggestions")
	public List<ComicSuggestion> getAllSuggestions(@RequestHeader("USER-TOKEN") String token) {
		if (!jwt.decodeJwt(token, "admin")) 
			return new ArrayList<ComicSuggestion>();
		List<ComicSuggestion> suggestions = comicSuggestRepo.findAll();
		return suggestions;
	}
	
	// Check for validation - 11/12/2020
	// Method needs a more secure way to identify who the user is 
	@DeleteMapping("/removeFavourite")
	public ResponseEntity<String> removeFavouriteComic(@RequestHeader("USER-TOKEN") String token, @RequestParam("u") String username, @RequestParam("cId") Long comicId)
	{
		// Check for empty username here is handled by BootVuetifyControllerAdvice class - NullPointerException
		if (!jwt.decodeJwt(token, "users"))
			return new ResponseEntity<String>("Couldn't remove comic from Favourites!", HttpStatus.FORBIDDEN);
		UserPreferences userPref = userPrefRepo.findByUsername(username);
		Comic comic = comicRepo.findByComicId(comicId);
		List<Comic> listOfComics = userPref.getComics();
		listOfComics.remove(comic);
		userPref.setComics(listOfComics);
		userPrefRepo.save(userPref);
		return new ResponseEntity<String>("Removed from Favourites successfully!", HttpStatus.OK);
	}
	
	@GetMapping("/getThread")
	public ForumThread getThread(@RequestParam("tId") long threadId) {
		return threadRepo.getOne(threadId);
	}
	
	// Checked for validation - 11/14/2020
	@PutMapping("/editThread")
	public ResponseEntity<String> editThread(@RequestHeader("USER-TOKEN") String token,
			@RequestParam("tId") long threadId, @RequestParam("threadTitle") String title, @RequestParam("threadContent") String threadContent ) throws ScanException, PolicyException {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Couldn't edit thread!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(title) || !FieldValidation.isNotEmptyOrNull(threadContent))
			return new ResponseEntity<String>("Check your input and try again!", HttpStatus.BAD_REQUEST);
		title = miscFunc.sanitizeHTMLAntiSamy(title);
		threadContent = miscFunc.sanitizeHTMLAntiSamy(threadContent);
		ForumThread thread = threadRepo.getOne(threadId);
		thread.setThreadTopic(title);
		List<ForumPost> posts = thread.getThreadPosts();
		posts.get(0).setPostContent(threadContent);
		thread.setThreadPosts(posts);
		threadRepo.save(thread);
		return new ResponseEntity<String>("Successfully edited thread!", HttpStatus.OK);
	}
	
	// Checked for validation - 11/14/2020
	@PostMapping("/editComic")
	public ResponseEntity<String> editComic(@RequestHeader("USER-TOKEN") String accessToken,@RequestParam("cId") long comicId,@RequestParam("comicName") String comicName, 
			@RequestParam("comicDesc") String comicDesc, @RequestParam("releaseStatus") String releaseStatus,@RequestParam("author") String author,
			@RequestParam("comicCoverURL") String comicCoverURL, @RequestParam("seriesURL") URL seriesURL,@RequestParam("price") float price, @RequestParam("buyComicURL") String buyURL) throws MalformedURLException {
		if (!jwt.decodeJwt(accessToken, "admins")) 
			return new ResponseEntity<String>("Cannot edit comic!", HttpStatus.BAD_REQUEST);
		if (!FieldValidation.isComicNameValid(comicName) || !FieldValidation.isNotEmptyOrNull(releaseStatus) || !FieldValidation.containsOnlyLetters(author)
				|| !FieldValidation.isValidUrl(comicCoverURL.toString()) || !FieldValidation.isValidUrl(seriesURL.toString()) || !FieldValidation.containsOnlyIntegerOrFloat(price)
				|| !FieldValidation.isValidUrl(buyURL.toString())) {
			return new ResponseEntity<String>("Something went wrong! Check your data and try again!", HttpStatus.BAD_REQUEST);
		}
		try {
			Comic editComic = comicRepo.findById(comicId).get();
			editComic.setComicName(comicName);
			editComic.setComicDesc(comicDesc);
			editComic.setReleaseStatus(releaseStatus);
			editComic.setComicAuthor(author);
			editComic.setSeriesURL(seriesURL);
			if (price != editComic.getPrice()) {
				editComic.getPriceHistory().add(new PriceAtDate(WordUtils.capitalizeFully(LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear()), price));
				editComic.setPriceHistory(editComic.getPriceHistory());
			}
			editComic.setPrice(price);
			editComic.setComicCoverURL(new URL(comicCoverURL));
			editComic.setBuyURL(new URL(buyURL));
			comicRepo.save(editComic);
			return new ResponseEntity<String>("Comic edited successfully!", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>("Incorrect data for comic!", HttpStatus.BAD_REQUEST);
		} 
		catch(NoSuchElementException e) {
			return new ResponseEntity<String>("No such comic!", HttpStatus.BAD_REQUEST);
		}
	}
	
	// Checked for validation - 11/14/2020
	@PostMapping("/postComment")
	public ResponseEntity<String> saveComment(@RequestHeader("USER-TOKEN") String token,@RequestParam("cId") Long cId) {
		if (!jwt.decodeJwt(token, "users")) 
			return new ResponseEntity<String>("Couldn't post your comment!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(servReq.getParameter("commenterName")) || !FieldValidation.isNotEmptyOrNull(servReq.getParameter("commentContent")))
			return new ResponseEntity<String>("Empty nickname and/or comment field!", HttpStatus.BAD_REQUEST);
		ComicComments comment = new ComicComments(servReq.getParameter("commenterName"), servReq.getParameter("commentContent"), LocalDateTime.now(), comicRepo.getOne(cId));
		commentRepo.save(comment);
		return new ResponseEntity<String>("Successfully posted comment!", HttpStatus.OK);
	}
	
	@GetMapping("/getComments")
	public List<ComicComments> getComments(@RequestParam("cId") Long cId) {
		return commentRepo.findAllByCommentedComic(comicRepo.findByComicId(cId));
	}
	
	@GetMapping("/getReview")
	public ComicReview getReview(@RequestParam("rId") Long rId) {
		return reviewRepo.findByReviewId(rId);
	}
	
	@GetMapping("/getReviews")
	public List<ComicReview> getReviews() {
		return reviewRepo.findAll();
	}

	// Checked for validation - 11/15/2020
	@PostMapping("/addReview")
	public ResponseEntity<String> saveReview(@RequestHeader("USER-TOKEN") String token, @RequestParam("revScore") short score, 
			@RequestParam("reviewName") String reviewName,
			@RequestParam("reviewContent") String reviewContent,
			@RequestParam(value="reviewVideo", required=false) URL video) throws ScanException, PolicyException {
		if (!jwt.decodeJwt(token, "admins")) 
			return new ResponseEntity<String>("Failed to add review!", HttpStatus.FORBIDDEN);
		if (!FieldValidation.isNotEmptyOrNull(reviewName) || !FieldValidation.isNotEmptyOrNull(reviewContent))
			return new ResponseEntity<String>("No review name or review content entered!", HttpStatus.BAD_REQUEST);
		if (score >= 1 && score <= 5) {
			reviewName = miscFunc.sanitizeHTMLAntiSamy(reviewName);
			reviewContent = miscFunc.sanitizeHTMLAntiSamy(reviewContent);
			reviewRepo.save(new ComicReview(reviewName, reviewContent, LocalDateTime.now(), score, video));
		}
		else return new ResponseEntity<String>("Invalid reviewer score!", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<String>("Successfully added review!", HttpStatus.OK);
	}
	
	
	@GetMapping("/getRandComic")
	public Comic getRandomComic() {
		Random rng = new Random();
		List<Comic> comics = comicRepo.findAll();
		return comics.get(rng.nextInt(comics.size()));
	}
	
	
	@GetMapping("/getThreads")
	public List<ForumThread> getForumThreads() {
		return threadRepo.findAll();
	}
	
	@GetMapping("/getPosts")
	public List<ForumPost> getPostsForThread(@RequestParam("tId") long threadId) {
		ForumThread thread = threadRepo.getOne(threadId);
		return thread.getThreadPosts();
	}
	
	// Checked for validation - 11/15/2020
	@PostMapping("/addPost")
	public List<ForumPost> addPostAndGetPosts(@RequestHeader("USER-TOKEN") String accessToken,@RequestParam("tId") long threadId,
			@RequestParam("postTitle") String postTitle, @RequestParam("postContent") String postContent) throws NotLoggedInException, ScanException, PolicyException {
		if (!jwt.decodeJwt(accessToken, "users")) 
			throw new NotLoggedInException();
		if (!FieldValidation.isNotEmptyOrNull(postTitle) || !FieldValidation.isNotEmptyOrNull(postContent))
			throw new NullPointerException();
		postTitle = miscFunc.sanitizeHTMLAntiSamy(postTitle);
		postContent = miscFunc.sanitizeHTMLAntiSamy(postContent);
		ForumThread thread = threadRepo.findById(threadId).get();
		LocalDateTime postTime = LocalDateTime.now();
		ForumPost postNew = new ForumPost(postTitle, postContent, postTime, postTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss")), thread);
		postRepo.save(postNew);
		return thread.getThreadPosts();
	}
	
	// Checked for validation - 11/16/2020
	@DeleteMapping("/delPost")
	public ResponseEntity<String> deletePost(@RequestHeader("USER-TOKEN") String token, @RequestParam("pId") long postId) {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Couldn't delete post!", HttpStatus.FORBIDDEN);
		ForumPost delPost = postRepo.getOne(postId);
		if (delPost.getOwnerThread().getThreadPosts().size() == 1) {
			return new ResponseEntity<String>("A forum thread must have at least 1 post! Cannot delete post!", HttpStatus.BAD_REQUEST);
		}
		postRepo.delete(delPost);
		return new ResponseEntity<String>("Post successfully deleted!",HttpStatus.OK);
	}
	
	// Checked for validation - 11/16/2020
	@GetMapping("/searchComic")
	public List<Comic> searchForComicsWithWord(@RequestParam("f") String search) throws InterruptedException {
		if (!FieldValidation.isNotEmptyOrNull(search))
			throw new NullPointerException();
		// Titlecase the search string and remove any whitespace
		String fSearch = WordUtils.capitalizeFully(search).trim();
		Map<String, Integer> searchTags = tags.getTags();
		if (searchTags.get(fSearch) != null && !fSearch.equals("null"))
			searchTags.put(fSearch, searchTags.get(fSearch) + 1);
		else if (searchTags.get(fSearch) == null && !fSearch.equals("null")) 
			searchTags.put(fSearch, 1);
		else 
			return searchRepo.searchByComicName(fSearch);
		tags.setTags(searchTags);
		return searchRepo.searchByComicName(fSearch);
	}
	
	
	@GetMapping("/getTags")
	public Map<String, Integer> getAllTags() {
		return tags.getTags();
	}
	
	// Checked for validation - 11/17/2020
	@PostMapping("/uploadFileio")
	public ResponseEntity<String> uploadToFileio(@RequestHeader("USER-TOKEN") String token, @RequestParam("file") MultipartFile file) throws IOException {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Insufficient permissions to upload files!", HttpStatus.FORBIDDEN);
		if (file.getSize() == 0 || file.isEmpty())
			return new ResponseEntity<String>("File to upload is missing! Please check your input and try again!", HttpStatus.BAD_REQUEST);
		Path TO = Paths.get(RandomStringUtils.randomAlphanumeric(10) + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
		String filename = storageService.storeAndRename(file, TO);
		String command = "curl -F \"file=@" + filename + "\" https://file.io";
		ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
		processBuilder.directory(new File("./uploads/"));
		Process process = processBuilder.start();
		InputStream inputStream = process.getInputStream();
		return new ResponseEntity<String>(IOUtils.toString(inputStream, StandardCharsets.UTF_8.name()), HttpStatus.OK);
	}
	
	// Checked for validation - 11/18/2020
	@PostMapping("/uploadStreamable")
	public  ResponseEntity<String> uploadToStreamable(@RequestHeader("USER-TOKEN") String token, @RequestParam("file") MultipartFile file) throws IOException {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Insufficient permissions to upload files!", HttpStatus.FORBIDDEN);
		if (file.getSize() == 0 || file.isEmpty())
			return new ResponseEntity<String>("File to upload is missing! Please check your input and try again!", HttpStatus.BAD_REQUEST);
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		ArrayList<String> allowedFormats = new ArrayList<String>() {
			{
				add("mp4");
				add("wav");
				add("ogm");
				add("wmv");
				add("mpg");
				add("webm");
				add("ogv");
				add("mov");
				add("mpeg");
				add("m4v");
				add("avi");
			}
		};
		if (!allowedFormats.contains(extension)) 
			return ResponseEntity.ok(gson.toJson("Disallowed file extension!"));
		Path TO = Paths.get(RandomStringUtils.randomAlphanumeric(10) + "." + extension);
		String filename = storageService.storeAndRename(file, TO);
		String command = "curl https://api.streamable.com/upload -u ati_5@abv.bg:H545G1212 -F file=@" + filename;
		ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
		processBuilder.directory(new File("./uploads/"));
		Process process = processBuilder.start();
		InputStream inputStream = process.getInputStream();
		return new ResponseEntity<String>(IOUtils.toString(inputStream, StandardCharsets.UTF_8.name()), HttpStatus.OK);
	}
	
	// Checked for validation - 11/20/2020
    @PostMapping("/uploadToImgBb")
    public ResponseEntity<String> uploadToImgBb(@RequestHeader("USER-TOKEN") String token,@RequestParam("file") MultipartFile file) throws IOException {
    	if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Insufficient permissions to upload files!", HttpStatus.FORBIDDEN);
    	if (file.getSize() == 0 || file.isEmpty())
			return new ResponseEntity<String>("File to upload is missing! Please check your input and try again!", HttpStatus.BAD_REQUEST);
    	String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		ArrayList<String> allowedFormats = new ArrayList<String>() {
			{
				add("tiff");
				add("bmp");
				add("png");
				add("jpeg");
				add("jpg");
				add("gif");
			}
		};
		if (!allowedFormats.contains(extension)) 
			return ResponseEntity.badRequest().body(gson.toJson("Disallowed file extension!"));
    	StringBuilder sb = new StringBuilder();
    	sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(file.getBytes(), false)));
    	HttpResponse response = Request.Post("https://api.imgbb.com/1/upload?key=3d5c570b4c41ae69527cdb5eec6fd13a").bodyForm(
    		      Form.form().add("image", sb.toString()).build())
    		      .execute().returnResponse();
    	return new ResponseEntity<String>(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8), HttpStatus.OK);
    }
	
    // Checked for validation - 11/21/2020
	@PostMapping("/uploadFiles")
    public ResponseEntity<String> uploadMultipleFiles(@RequestHeader("USER-TOKEN") String token,@RequestParam("files") List<MultipartFile> files) {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<String>("Couldn't upload file!", HttpStatus.FORBIDDEN);
		// In Spring Boot 2.1 and up, the List of MultipartFiles always has at least 1 file (even if user submitted nothing) and that file is not
		// null - this means that our FieldValidation class check doesn't really work here - we should check the size of the first submitted file instead
		if (files.get(0).getSize() == 0)
			return new ResponseEntity<String>("File to upload is missing! Please check your input and try again!", HttpStatus.BAD_REQUEST);
		ArrayList<String> allowedFormats = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("docx");
				add("doc");
				add("pdf");
				add("jpg");
				add("png");
				add("gif");
			}
		};
		files.stream()
				.filter(file -> allowedFormats.contains(FilenameUtils.getExtension(file.getOriginalFilename())))
                .map(file -> storageService.uploadFile(file))
                .collect(Collectors.toList());
        return new ResponseEntity<String>("Successfully uploaded files!", HttpStatus.OK);
    }
	
	@GetMapping("/getFiles")
	public MultiValueMap getFiles(@RequestHeader("USER-TOKEN") String token) throws IOException {
		 if (!jwt.decodeJwt(token, "admins"))
			return new MultiValueMap(); 
		File uploadFolder = new File("C:/Users/Peter/Desktop/springboot-vuetify/uploads");
		MultiValueMap files = new MultiValueMap();
		for (File file : uploadFolder.listFiles()) {
			FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
			FileTime modifiedTime = Files.getLastModifiedTime(file.toPath());
			String cTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(creationTime.toMillis()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss"));
			String mTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(modifiedTime.toMillis()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss"));
			ArrayList<String> times = new ArrayList<String>() {
			{
				add(cTime);
				add(mTime);
			}};
			files.put(file.getName(), times);
		}
		return files;
	}
	
	// Checked for validation - 12/2/2020
	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestHeader("USER-TOKEN") String token, @RequestParam("name") String filename) throws IOException {
		if (!jwt.decodeJwt(token, "admins"))
			return new ResponseEntity<Resource>(HttpStatus.FORBIDDEN);
		System.out.println(filename);
		if (!FieldValidation.isNotEmptyOrNull(filename))
			throw new NullPointerException();
		File downloadFile = FileUtils.getFile(new File("C:/Users/Peter/Desktop/springboot-vuetify/uploads"), filename);
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(downloadFile.getAbsolutePath())));
		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
		return ResponseEntity.ok()
				.headers(headers)
	            .contentLength(downloadFile.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}
	
	// Check for validation - 12/2/2020
	@PostMapping("/addThread")
	public ResponseEntity<String> addThread(@RequestHeader("USER-TOKEN") String accessToken,@RequestParam("threadTitle") String threadTitle, @RequestParam("threadPostContent") String threadContent) throws ScanException, PolicyException {
		if (!jwt.decodeJwt(accessToken, "users")) 
			return new ResponseEntity<String>("Failed to add thread!", HttpStatus.BAD_REQUEST);
		if (!FieldValidation.isNotEmptyOrNull(threadContent) || !FieldValidation.isNotEmptyOrNull(threadTitle)) 
			return new ResponseEntity<String>("No post title or post content!", HttpStatus.BAD_REQUEST);
		threadTitle = miscFunc.sanitizeHTMLAntiSamy(threadTitle);
		threadContent = miscFunc.sanitizeHTMLAntiSamy(threadContent);
		LocalDateTime timeNow = LocalDateTime.now();
		String dateTimeFormat = timeNow.format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss"));
		ForumThread thread = new ForumThread(threadTitle, timeNow, dateTimeFormat );
		ForumPost initPost = new ForumPost(threadTitle,threadContent,timeNow,dateTimeFormat, thread);
		ArrayList<ForumPost> posts = new ArrayList<ForumPost>();
		posts.add(initPost);
		thread.setThreadPosts(posts);
		thread = threadRepo.save(thread);
		initPost.setOwnerThread(thread);
		postRepo.save(initPost);
		return new ResponseEntity<String>("Thread added successfully!", HttpStatus.OK);
	}
	
	@GetMapping("/delThread")
	public ResponseEntity<String> deleteThread(@RequestHeader("USER-TOKEN") String accessToken,@RequestParam("tId") long threadId) {
		if (!jwt.decodeJwt(accessToken, "admins")) 
			return new ResponseEntity<String>("Cannot delete thread!",HttpStatus.BAD_REQUEST);
		threadRepo.delete(threadRepo.getOne(threadId));
		return new ResponseEntity<String>("Thread successfully deleted!",HttpStatus.OK);
	}
	
	
	@GetMapping("/getToken")
	public ResponseEntity<String> getCsrfToken() {
		return new ResponseEntity<String>("Token returned!", HttpStatus.OK);
	}
}
