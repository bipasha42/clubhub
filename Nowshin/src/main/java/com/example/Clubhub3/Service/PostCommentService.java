package com.example.Clubhub3.Service;

import com.example.Clubhub3.dto.PostCommentDto;
import com.example.Clubhub3.model.PostComment;
import com.example.Clubhub3.model.User;
import com.example.Clubhub3.repo.PostCommentRepository;
import com.example.Clubhub3.repo.ClubAdminUniadminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final ClubAdminUniadminRepository userRepository;

    public void addComment(UUID postId, UUID userId, String content) {
        PostComment comment = PostComment.builder()
                .postId(postId)
                .authorId(userId)
                .content(content)
                .build();
        postCommentRepository.save(comment);
    }

    public List<PostCommentDto> getComments(UUID postId) {
        List<PostComment> comments = postCommentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        return comments.stream().map(comment -> {
            String authorName = "Unknown User";
            java.util.Optional<User> userOptional = userRepository.findById(comment.getAuthorId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                authorName = user.getFirstName() + " " + user.getLastName();
            }

            return PostCommentDto.builder()
                    .id(comment.getId())
                    .postId(comment.getPostId())
                    .authorId(comment.getAuthorId())
                    .authorName(authorName)
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    public int countCommentsByPostId(UUID postId) {
        return postCommentRepository.countByPostId(postId);
    }
}