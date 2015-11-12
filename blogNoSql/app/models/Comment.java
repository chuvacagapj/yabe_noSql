package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import java.util.*;
import play.data.validation.*;
import play.modules.morphia.Model;

@Entity
public class Comment extends Model {

    @Required
    public String author;

    @Required
    public Date postedAt;

    @Required
    @MaxSize(10000)
    public String content;

    @Reference
    @Required
    public Post post;

    public Comment(Post post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }

    public String toString() {
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }

    @Added void cascadeAdd() {
        if (!post.comments.contains(this)) {
            post.comments.add(this);
            post.save();
        }
    }

}