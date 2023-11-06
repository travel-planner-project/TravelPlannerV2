package com.travelplanner.v2.domain.post.post;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.travelplanner.v2.domain.post.comment.domain.Comment;
import com.travelplanner.v2.domain.post.image.Image;
import com.travelplanner.v2.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    private String postTitle;

    private String postContent;

    private LocalDateTime createdAt;


    // 연관 관계
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Builder.Default
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post",  cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();


    // 연관 관계 매핑
    public void mappingComment(Comment comment) {
        comments.add(comment);
    }
}
