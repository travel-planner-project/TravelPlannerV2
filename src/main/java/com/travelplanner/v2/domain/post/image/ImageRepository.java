package com.travelplanner.v2.domain.post.image;

import com.travelplanner.v2.domain.post.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteAllByPostId(Long postId);
}
