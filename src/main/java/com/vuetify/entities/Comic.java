package com.vuetify.entities;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.vuetify.enums.ComicType;




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
	@Enumerated(EnumType.STRING)
	@Column(name="status",nullable=true)
	private ComicType status;
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
	public Comic(String comicName, String comicDesc, LocalDate releaseDate, String comicAuthor, ComicType releaseStatus, URL comicCoverURL, URL buyURL, URL seriesURL, float price) {
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
	
	public Comic(String comicName, String comicDesc, LocalDate releaseDate, String comicAuthor, ComicType releaseStatus, URL comicCoverURL, URL buyURL, URL seriesURL, float price, ArrayList<PriceAtDate> history) {
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
	
	public Comic(String comicName, String authorName, LocalDate relDate, ComicType releaseStatus, URL buyURL, URL coverURL, URL seriesURL, float price) {
		this.comicName = comicName;
		this.comicAuthor = authorName;
		this.releaseDate = relDate;
		this.status = releaseStatus;
		this.buyURL = buyURL;
		this.comicCoverURL = coverURL;
	}
	
	public Comic() {}
	
	public Comic(Comic.Builder builder) {
		this.comicName = builder.comicName;
		this.comicDesc = builder.comicDesc;
		this.releaseDate = builder.releaseDate;
		this.comicAuthor=builder.comicAuthor;
		this.status = builder.status;
		this.comicCoverURL = builder.comicCoverURL;
		this.buyURL = builder.buyURL;
		this.releaseDateFormatted = builder.releaseDate.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
		this.price = builder.price;
		this.seriesURL = builder.seriesURL;
		this.priceHistory = builder.priceHistory;
	}
	
	public static class Builder {
		private String comicName;
		private String comicDesc;
		private LocalDate releaseDate;
		private String comicAuthor;
		private ComicType status;
		private URL comicCoverURL;
		private URL buyURL;
		private URL seriesURL;
		private float price;
		private List<PriceAtDate> priceHistory = new ArrayList<PriceAtDate>(0);
		
		public Comic.Builder addComicName(String comicName) {
			this.comicName = comicName;
			return this;
		}
		public Comic.Builder addComicDesc(String comicDesc) {
			this.comicDesc = comicDesc;
			return this;
		}
		public Comic.Builder addReleaseDate(LocalDate releaseDate) {
			this.releaseDate = releaseDate;
			return this;
		}
		public Comic.Builder addComicAuthor(String comicAuthor) {
			this.comicAuthor = comicAuthor;
			return this;
		}
		public Comic.Builder addComicType(ComicType status) {
			this.status = status;
			return this;
		}
		public Comic.Builder addCoverUrl(URL comicCoverURL) {
			this.comicCoverURL = comicCoverURL;
			return this;
		}
		public Comic.Builder addBuyUrl(URL buyURL) {
			this.buyURL = buyURL;
			return this;
		}
		public Comic.Builder addSeriesUrl(URL seriesURL) {
			this.seriesURL = seriesURL;
			return this;
		}
		public Comic.Builder addPrice(float price) {
			this.price = price;
			return this;
		}
		public Comic.Builder addPriceHistory(ArrayList<PriceAtDate> priceHistory) {
			this.priceHistory = priceHistory;
			return this;
		}
		public Comic build() {
			Comic builderComic = new Comic(this);
			return builderComic;
			
		}
 	}

	// Helper methods 
	public static Comic.Builder builder() {
		return new Builder();
	}

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
	public ComicType getReleaseStatus() {
		return status;
	}
	public void setReleaseStatus(ComicType releaseStatus) {
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
	
	public List<PriceAtDate> getPriceHistory() {
		return priceHistory;
	}

	public void setPriceHistory(List<PriceAtDate> priceHistory) {
		this.priceHistory = priceHistory;
	}
	
	
	
}
