package in.akshay.newsbuddy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class newsmodel {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalResult")
    @Expose
    private int totalResult;

    @SerializedName("articles")
    @Expose
    private List<article> article;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<article> getArticle() {
        return article;
    }

    public void setArticle(List<article> article) {
        this.article = article;
    }
}
