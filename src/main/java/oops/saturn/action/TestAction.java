package oops.saturn.action;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import oops.saturn.manager.TestManager;

@ParentPackage("default")
@Namespace("/test")
@SuppressWarnings("serial")
public class TestAction extends ActionSupport {
	private Map<String, Object> ajaxData;
	private String testString;
	
	
	
	private TestManager testManager;
	
	public String getTestString() {
		return testString;
	}

	public void setTestString(String testString) {
		this.testString = testString;
	}

	public Map<String, Object> getAjaxData() {
		return ajaxData;
	}

	public void setAjaxData(Map<String, Object> ajaxData) {
		this.ajaxData = ajaxData;
	}

	public TestManager getTestManager() {
		return testManager;
	}

	public void setTestManager(TestManager testManager) {
		this.testManager = testManager;
	}


	@Action(value = "testStruts", results = { @Result(name = "success", params = { "root", "result" }, type = "json") })
	public String testStruts() {
		String result = this.testManager.saveSharon(ajaxData);
		ActionContext.getContext().put("result", result);
		return SUCCESS;
	}
}

