package mm.constants;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Constants {

	public final static String NN_SAVE_PATH = "C:\\Users\\Foad\\Desktop\\saveNN\\NN2018-09-17 05-54-06.ser";
	public static int INPUT_SIZE = 24;
	
	
	public final static String SUCCESS="success";
	public final static String USERNOTFOUND="user not found";
	public final static String WRONGPASSWORD="wrong password";
	public final static String WRONGFORMAT="unsupported file format";
	public final static int STATUS_SUCCESS=200;
	public final static int STATUS_MISSINGPARA=401;
	public final static int STATUS_WRONGPARA=402;
	public final static int STATUS_ERROR=404;
	public final static int STATUS_DB_ERROR=405;

//	public final static Type MENTOR_CLASS = new TypeToken<List<Mentor>>() {}.getType();


	public final static String DB_ERROR="error in db query";
	public final static String ERROR="error";
	public final static String MEETING_NOT_FOUND="meeting not found";
	public static final String INVALID_SESSION_TOKEN = "Invalid session token";
	public static final String FAILED_TO_ADD_MEETING = "failed to add meeting";


	
}
