package com.hp.file_upload;

public class FrameworkComponents {


    private String componentName;
    private String componentType;
    private String fileFormat;
    
    public String getComponentName() {
		return componentName;
	}


	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}



	public String getComponentType() {
		return componentType;
	}



	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}


	public String getFileFormat() {
		return fileFormat;
	}


	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}



	


	@Override
    public String toString() {
        return "{" +
                "componentName='" + componentName + '\'' +
                ", componentType='" + componentType + '\'' +
                ", fileFormmat='" + fileFormat + '\'' +
                '}';
    }
    
}