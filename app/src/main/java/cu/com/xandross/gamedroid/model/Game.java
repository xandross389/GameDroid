package cu.com.xandross.gamedroid.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by XandrOSS on 04/04/2016.
 */
@DatabaseTable(tableName = "games")
public class Game {
    public static  final String ID_FIELD_NAME = "id";
    public static  final String LAST_RELEASED_FIELD_NAME = "last_released";
    public static  final String COMING_SOON_FIELD_NAME = "coming_soon";
    public static  final String DECRIPTION_FIELD_NAME = "description";
    public static  final String REQUIREMENTS_FIELD_NAME = "requirements";
    public static  final String FULL_NAME_FIELD_NAME = "full_name";
    public static  final String SHORT_NAME_FIELD_NAME = "short_name";
    public static  final String PLATFORMS_FIELD_NAME = "platform";

	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private Integer id;
	
	@DatabaseField
    private String status; // terminada, por confirmar, en proceso
	
	@DatabaseField(columnName = SHORT_NAME_FIELD_NAME)
    private String shortName;
	
	@DatabaseField(columnName = FULL_NAME_FIELD_NAME)
    private String fullName;
	
	@DatabaseField(columnName = PLATFORMS_FIELD_NAME)
    private String platform;
	
	@DatabaseField(columnName = "date", dataType = DataType.DATE)
    private java.util.Date date;
	
	@DatabaseField
    private String kind;
	
	@DatabaseField
    private String distributor; // Ubisoft, Nintendo
	
	@DatabaseField
    private String developer; // Blizzard
	
	@DatabaseField
    private String format; // CD, DVD, UMD
	
	@DatabaseField(columnName = "space_on_disc")
    private String spaceOnDisk;
	
	@DatabaseField
    private String age;
	
	@DatabaseField
    private String language;

    @DatabaseField(columnName = DECRIPTION_FIELD_NAME)
    private String description;

    @DatabaseField(columnName = REQUIREMENTS_FIELD_NAME)
    private String requirements;

    @DatabaseField
    private Integer discs;

    @DatabaseField(columnName = LAST_RELEASED_FIELD_NAME)
    private boolean lastReleased; // released (estrenado)
	
	@DatabaseField(columnName = COMING_SOON_FIELD_NAME)
    private boolean comingSoon; // proximamente
			
	@DatabaseField(columnName = "image", dataType = DataType.BYTE_ARRAY)
    private byte[] imageBytes;

//    private Boolean isFavorite;
    
    public Game() {
	    // constructor sin argumentos necesario por ORMLite
    }

//    public Boolean isFavorite() {
//        return isFavorite;
//    }

//    public void setFavorite(Boolean favorite) {
//        isFavorite = favorite;
//    }

	public Integer getId() {
        return id;
    }
	/*
    public void setId(Integer id) {
        this.id = id;
    }
	*/
    public String getStatus() {
        return status.trim();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShortName() {
        return shortName.trim();
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName.trim();
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPlatform() {
        return platform.trim();
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getKind() {
        return kind.trim();
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDistributor() {
        return distributor.trim();
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }
	
	public String getDeveloper() {
        return developer.trim();
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getFormat() {
        return format.trim();
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSpaceOnDisk() {
        return spaceOnDisk.trim();
    }

    public void setSpaceOnDisk(String spaceOnDisk) {
        this.spaceOnDisk = spaceOnDisk;
    }

    public String getAge() {
        return age.trim();
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLanguage() {
        return language.trim();
    }

    public boolean isComingSoon() {
        return comingSoon;
    }

    public void setComingSoon(boolean comingSoon) {
        this.comingSoon = comingSoon;
    }

    public boolean isLastReleased() {
        return lastReleased;
    }

    public void setLastReleased(boolean lastReleased) {
        this.lastReleased = lastReleased;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDiscs() {
        return discs;
    }

    public void setDiscs(Integer discs) {
        this.discs = discs;
    }
	
	public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getDescription() {
        return description.trim();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements.trim();
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

}
