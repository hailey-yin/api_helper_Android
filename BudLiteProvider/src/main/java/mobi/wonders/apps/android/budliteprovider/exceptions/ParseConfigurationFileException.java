package mobi.wonders.apps.android.budliteprovider.exceptions;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * ParseConfigurationFileException
 *
 * @author yuqing
 * @date 2015/12/23
 */
public class ParseConfigurationFileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * can not find the budlite.xml file by the given id.
     */
    public static final String CAN_NOT_FIND_BUDLITE_FILE = "budlite.xml file is missing. Please ensure it under assets folder.";

    /**
     * can not parse the budlite.xml, check if it's in correct format.
     */
    public static final String FILE_FORMAT_IS_NOT_CORRECT = "can not parse the budlite.xml, check if it's in correct format";

    /**
     * parse configuration is failed.
     */
    public static final String PARSE_CONFIG_FAILED = "parse configuration is failed";

    /**
     * IO exception happened.
     */
    public static final String IO_EXCEPTION = "IO exception happened";

    /**
     * Constructor of ParseConfigurationFileException.
     *
     * @param errorMessage
     *            the description of this exception.
     */
    public ParseConfigurationFileException(String errorMessage) {
        super(errorMessage);
    }
}
