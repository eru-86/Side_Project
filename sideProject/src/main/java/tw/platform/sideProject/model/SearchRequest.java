package tw.platform.sideProject.model;

import java.util.List;

public class SearchRequest {
	    private String userKeyword;
	    private List<String> options;

	    // getters and setters
	    public String getUserKeyword() {
	        return userKeyword;
	    }

	    public void setUserKeyword(String userKeyword) {
	        this.userKeyword = userKeyword;
	    }

	    public List<String> getOptions() {
	        return options;
	    }

	    public void setOptions(List<String> options) {
	        this.options = options;
	    }
	


}
