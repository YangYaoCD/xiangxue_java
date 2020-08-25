package ThreadSafe_7.performance;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
/**
  *缩减锁的范围
 */

public class ReduceLock {
	
	private Map<String,String> matchMap = new HashMap<>();
	
	public synchronized boolean isMatch(String name,String regexp) {
		String key = "user."+name;
		String job = matchMap.get(key);
		if(job == null) {
			return false;
		}else {
			return Pattern.matches(regexp, job);
		}
	}
	public synchronized boolean isMatch2(String name,String regexp) {
		String key = "user."+name;
		String job;
		synchronized (this){//缩减锁的范围
			job = matchMap.get(key);
		}
		if(job == null) {
			return false;
		}else {
			return Pattern.matches(regexp, job);
		}
	}
	
}
