package models;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class Post {
    private Integer userId;
    private Integer id;
    private String title;
    private String body;

    public Post(Integer userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}
