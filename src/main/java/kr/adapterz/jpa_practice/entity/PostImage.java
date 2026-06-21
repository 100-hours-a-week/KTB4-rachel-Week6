package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "contentImage_seq",
        sequenceName = "contentImage_seq"
) // allocatedsize는 없앴다. 수정이 바로바로 이뤄져야하니까..
public class PostImage {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contentImage_seq")
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "content_image", nullable = false, length = 255)
    private String contentImage;

    protected PostImage() {}

    public PostImage(String contentImage, Post post){
        this.post = post;
        this.contentImage = contentImage;
    }

    public void changeContentImage(String contentImage) {

        this.contentImage = contentImage;
    }

}
