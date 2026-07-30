package tim.dev.gfs.google.constant;

public final class GoogleSheetsConstants {

    private GoogleSheetsConstants() {
        // Prevent instantiation
    }

    // Common Keys
    public static final String MODULE = "module";
    public static final String ACTION = "action";

    // Modules
    public static final String MODULE_EVENT = "EVENT";

    // Actions
    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_READ = "READ";
    public static final String ACTION_UPDATE = "UPDATE";
    public static final String ACTION_DELETE = "DELETE";

    // Event Fields
    public static final String EVENT_ID = "eventId";
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_DESCRIPTION = "eventDescription";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String EVENT_LOCATION = "eventLocation";
    public static final String EVENT_STATUS = "eventStatus";
    public static final String CREATED_ON = "createdOn";
    public static final String CREATED_BY = "createdBy";
    public static final String UPDATED_ON = "updatedOn";
    public static final String UPDATED_BY = "updatedBy";

}
