package hello.model;

public enum ImageType {
    JPEG("image/jpeg"),
    GIF("image/gif");
    
    private String externalValue;
    
    private ImageType(String externalValue) {
	this.setExternalValue(externalValue);
    }

    public String getExternalValue() {
	return externalValue;
    }

    public void setExternalValue(String externalValue) {
	this.externalValue = externalValue;
    }
    
}
