package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import java.util.*;
import play.data.binding.As;
import play.data.validation.*;
import play.modules.morphia.Model;

@Entity
public class Post extends Model {

    @Required
    public String title;

    @Required @As("yyyy-MM-dd")
    public Date postedAt;

    @Required
    @MaxSize(10000)
    public String content;

    @Required
    public String authorEmail;

    @Reference
    public List<Comment> comments;

    public Set<String> tags = new HashSet<String>();

    public Post(User author, String title, String content) {
        this.comments = new ArrayList<Comment>();
        this.tags = new TreeSet();
        this.authorEmail = author.email;
        this.title = title;
        this.content = content;
        this.postedAt = new Date();
    }

    public User getAuthor() {
        return User.q("email", authorEmail).get();
    }

    public Post addComment(String author, String content) {
        new Comment(this, author, content).save();
        return this;
    }

    public Post previous() {
        return Post.find("postedAt < ?1 order by postedAt desc", postedAt).first();
    }

    public Post next() {
        return Post.find("postedAt > ?1 order by postedAt asc", postedAt).first();
    }

    public Post tagItWith(String name) {
        tags.add(name);
        return this;
    }

    public static List<Post> findTaggedWith(String tag) {
        return Post.q().filter("tags", tag).asList();
    }

    public static List<Post> findTaggedWith(String... tags) {
        return Post.q().filter("tags all", tags).asList();
    }

    public String toString() {
        return title;
    }
    
    @OnDelete void cascadeDelete() {
        for (Comment c: comments) {
            c.delete();
        }
    }
}
