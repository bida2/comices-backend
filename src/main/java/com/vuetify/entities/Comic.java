package com.vuetify.entities;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TermVector;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;




@AnalyzerDef(name = "customanalyzer", tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class), filters = {
        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = { @Parameter(name = "language", value = "English") }),
})
@Entity
@Indexed
public class Comic {
	@Id
	@GeneratedValue
	private Long comicId;
	@Field(termVector = TermVector.YES,analyze = Analyze.YES,analyzer = @Analyzer(definition = "customanalyzer"))
	@Column(name="comic_name",nullable=false, unique=true)
	private String comicName;
	@Column(name="comic_desc",nullable=true,columnDefinition="TEXT")
	private String comicDesc;
	@Column(name="release_date",nullable=true)
	private LocalDate releaseDate;
	@Column(name="release_date_formatted",nullable=true)
	private String releaseDateFormatted;
	@Column(name="author",nullable=true)
	private String comicAuthor;
	// Should be either 'released', 'upcoming' or 'classic'
	@Column(name="status",nullable=true)
	private String status;
	@Column(name="comic_cover",nullable=true)
	private URL comicCoverURL;
	@Column(name="buy_comic_URL",nullable=true)
	private URL buyURL;
	@Column(name="series", nullable=true)
	private URL seriesURL;
	@Column(name="price", nullable=false)
	private float price;
	@ElementCollection
	@OrderColumn
	@Column(name="price_history", nullable=true)
	private List<PriceAtDate> priceHistory = new ArrayList<PriceAtDate>(0);
	
	// Constructors
	public Comic(String comicName, String comicDesc, LocalDate releaseDate, String comicAuthor, String releaseStatus, URL comicCoverURL, URL buyURL, URL seriesURL, float price) {
		this.comicName = comicName;
		this.comicDesc = comicDesc;
		this.releaseDate = releaseDate;
		this.comicAuthor=comicAuthor;
		this.status = releaseStatus;
		this.comicCoverURL = comicCoverURL;
		this.buyURL = buyURL;
		this.releaseDateFormatted = releaseDate.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
		this.price = price;
		this.seriesURL = seriesURL;
	}
	
	public Comic(String comicName, String comicDesc, LocalDate releaseDate, String comicAuthor, String releaseStatus, URL comicCoverURL, URL buyURL, URL seriesURL, float price, ArrayList<PriceAtDate> history) {
		this.comicName = comicName;
		this.comicDesc = comicDesc;
		this.releaseDate = releaseDate;
		this.comicAuthor=comicAuthor;
		this.status = releaseStatus;
		this.comicCoverURL = comicCoverURL;
		this.buyURL = buyURL;
		this.releaseDateFormatted = releaseDate.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
		this.price = price;
		this.seriesURL = seriesURL;
		this.priceHistory = history;
	}
	
	public Comic(String comicName, String authorName, LocalDate relDate, String releaseStatus, URL buyURL, URL coverURL, URL seriesURL, float price) {
		this.comicName = comicName;
		this.comicAuthor = authorName;
		this.releaseDate = relDate;
		this.status = releaseStatus;
		this.buyURL = buyURL;
		this.comicCoverURL = coverURL;
	}
	
	public Comic() {}
	


	// Getters and Setters
	public Long getComicId() {
		return comicId;
	}
	public void setComicId(Long comicId) {
		this.comicId = comicId;
	}
	public String getComicName() {
		return comicName;
	}
	public void setComicName(String comicName) {
		this.comicName = comicName;
	}
	public String getComicDesc() {
		return comicDesc;
	}
	public void setComicDesc(String comicDesc) {
		this.comicDesc = comicDesc;
	}
	public LocalDate getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getComicAuthor() {
		return comicAuthor;
	}
	public void setComicAuthor(String comicAuthor) {
		this.comicAuthor = comicAuthor;
	}
	public String getReleaseStatus() {
		return status;
	}
	public void setReleaseStatus(String releaseStatus) {
		this.status = releaseStatus;
	}
	public URL getComicCoverURL() {
		return comicCoverURL;
	}
	public void setComicCoverURL(URL comicCoverURL) {
		this.comicCoverURL = comicCoverURL;
	}
	public URL getBuyURL() {
		return buyURL;
	}
	public void setBuyURL(URL buyURL) {
		this.buyURL = buyURL;
	}
	public String getReleaseDateFormatted() {
		return releaseDateFormatted;
	}
	public void setReleaseDateFormatted(String releaseDateFormatted) {
		this.releaseDateFormatted = releaseDateFormatted;
	}
	public URL getSeriesURL() {
		return seriesURL;
	}
	public void setSeriesURL(URL seriesURL) {
		this.seriesURL = seriesURL;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PriceAtDate> getPriceHistory() {
		return priceHistory;
	}

	public void setPriceHistory(List<PriceAtDate> priceHistory) {
		this.priceHistory = priceHistory;
	}
	
	
	
}
