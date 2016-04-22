package hello.model;

public class GetImageResponse {
    
    private ImgurImage data;

    private boolean success;
    
    private Integer status;

    public ImgurImage getData() {
        return data;
    }

    public void setData(ImgurImage data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    
}
