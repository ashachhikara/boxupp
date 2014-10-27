package com.boxupp.responseBeans;
public class StatusBean{
		public String statusMessage;
		public Integer statusCode;
		public Object beanData;
		
		public Object getData() {
			return beanData;
		}
		public void setData(Object data) {
			this.beanData = data;
		}
		public String getStatusMessage() {
			return statusMessage;
		}
		public void setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
		}
		public Integer getStatusCode() {
			return statusCode;
		}
		public void setStatusCode(Integer statusCode) {
			this.statusCode = statusCode;
		}
	}