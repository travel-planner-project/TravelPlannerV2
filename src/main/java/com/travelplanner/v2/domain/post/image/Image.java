package com.travelplanner.v2.domain.post.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.travelplanner.v2.domain.post.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyName;

    private String postImgUrl;

    private Boolean isThumbnail;

    private Long sort;

    // 연관관계
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="post_id")
    @JsonBackReference
    private Post post;

}

