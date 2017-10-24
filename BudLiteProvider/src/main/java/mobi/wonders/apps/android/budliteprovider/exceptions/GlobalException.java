package mobi.wonders.apps.android.budliteprovider.exceptions;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * GlobalException
 *
 * @author yuqing
 * @date 2015/12/23
 */
public class GlobalException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * 上下文为null
     */
    public static final String APPLICATION_CONTEXT_IS_NULL = "Application context is null. Maybe you haven't configured your application initialise with \"BudDataBaseManager.initContext(this, DEBUG)\".";

    public GlobalException(String errorMessage) {
        super(errorMessage);
    }
}
