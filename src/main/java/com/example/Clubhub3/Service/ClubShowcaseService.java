package com.example.Clubhub3.Service;

import com.example.Clubhub3.dto.ClubShowcaseDto;
import com.example.Clubhub3.dto.PostDto;
import com.example.Clubhub3.dto.ClubMemberDto;
import com.example.Clubhub3.model.Club;
import com.example.Clubhub3.model.Post;
import com.example.Clubhub3.repo.ClubShowcaseRepository;
import com.example.Clubhub3.repo.PostRepository;
import com.example.Clubhub3.repo.ClubMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClubShowcaseService {

    @Autowired
    private ClubShowcaseRepository clubRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ClubMemberRepository clubMemberRepository;

    public List<ClubMemberDto> getClubMembers(UUID clubId) {
        System.out.println("\n--- ENTERING ClubShowcaseService.getClubMembers ---");
        List<Object[]> results = clubMemberRepository.findMembersByClubId(clubId);
        System.out.println("--- Service: Received " + results.size() + " member rows from repository. ---");
        System.out.println("--- Service: Now attempting to convert to DTOs... ---");

        List<ClubMemberDto> dtoList = results.stream()
                .map(this::convertToMemberDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("--- Service: Successfully converted " + dtoList.size() + " rows to DTOs. ---");
        if (results.size() > 0 && dtoList.isEmpty()) {
            System.err.println("---!!! SERVICE WARNING: All " + results.size() + " rows failed to convert to DTOs. Check for conversion errors above. !!!---");
        }
        System.out.println("--- EXITING ClubShowcaseService.getClubMembers ---\n");
        return dtoList;
    }

    private ClubMemberDto convertToMemberDto(Object[] row) {
        System.out.println("--- Service: Converting a new row to ClubMemberDto... ---");
        try {
            if (row == null || row.length < 9) {
                System.err.println("---!!! Conversion Error: Received row is null or has incorrect number of columns. Skipping. !!!---");
                return null;
            }
            System.out.println("--- Service: Raw row data: " + Arrays.toString(row) + " ---");

            UUID userId = row[2] instanceof UUID ? (UUID) row[2] : null;

            if (userId == null) {
                System.err.println("---!!! Conversion Error: user_id (row[2]) is null or not a UUID. Found type: " + (row[2] != null ? row[2].getClass().getName() : "null") + ". Skipping row. !!!---");
                return null;
            }

            String firstName = row[4] instanceof String ? (String) row[4] : "";
            String lastName = row[5] instanceof String ? (String) row[5] : "";
            String userName = (firstName + " " + lastName).trim();

            ClubMemberDto dto = new ClubMemberDto();

            dto.setId(userId);
            dto.setUserId(userId);
            dto.setUserName(userName.isEmpty() ? "Unnamed Member" : userName);

            if (row[3] instanceof Timestamp) {
                dto.setMemberSince(((Timestamp) row[3]).toLocalDateTime());
            }

            dto.setUserEmail(row[6] instanceof String ? (String) row[6] : null);
            dto.setProfilePictureUrl(row[7] instanceof String ? (String) row[7] : null);

            UUID adminId = row[8] instanceof UUID ? (UUID) row[8] : null;
            dto.setAdmin(Objects.equals(adminId, userId));

            System.out.println("--- Service: Successfully converted row for user: " + userName + " ---");
            return dto;

        } catch (Exception e) {
            System.err.println("\n---!!! SERVICE CRITICAL ERROR !!!---");
            System.err.println("---!!! FAILED to convert a database row to ClubMemberDto. Returning null for this row. ---");
            System.err.println("---!!! Raw row data that caused error: " + Arrays.toString(row) + " ---");
            System.err.println("---!!! EXCEPTION: " + e.getClass().getName());
            System.err.println("---!!! MESSAGE: " + e.getMessage());
            e.printStackTrace();
            System.err.println("---!!! END SERVICE ERROR !!!---\n");
            return null;
        }
    }

    // --- OTHER METHODS (UNCHANGED) ---
    public List<ClubShowcaseDto> getAllClubsForShowcase(UUID currentUserId) {
        try {
            return clubRepository.findAllClubsForShowcase().stream()
                    .map(club -> convertToShowcaseDto(club, currentUserId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getAllClubsForShowcase: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public ClubShowcaseDto getClubDetails(UUID clubId, UUID currentUserId) {
        try {
            System.out.println("=== DEBUG: Looking for club with ID: " + clubId + " ===");
            Club club = clubRepository.findClubWithDetails(clubId);
            System.out.println("=== DEBUG: Found club: " + (club != null ? club.getName() : "null") + " ===");
            return club != null ? convertToShowcaseDto(club, currentUserId) : null;
        } catch (Exception e) {
            System.err.println("Error in getClubDetails: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<PostDto> getClubPosts(UUID clubId, UUID currentUserId) {
        try {
            return postRepository.findPostsByClubId(clubId).stream()
                    .map(post -> convertToPostDto(post, currentUserId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getClubPosts: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public PostDto getPostDetails(UUID postId, UUID currentUserId) {
        try {
            Post post = postRepository.findPostWithAuthor(postId);
            return post != null ? convertToPostDto(post, currentUserId) : null;
        } catch (Exception e) {
            System.err.println("Error in getPostDetails: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private ClubShowcaseDto convertToShowcaseDto(Club club, UUID currentUserId) {
        try {
            ClubShowcaseDto dto = new ClubShowcaseDto();
            dto.setId(club.getId());
            dto.setName(club.getName());
            dto.setDescription(club.getDescription());
            dto.setLogoUrl(club.getLogoUrl());
            dto.setBannerUrl(club.getBannerUrl());
            dto.setCreatedAt(club.getCreatedAt());
            if (club.getUniversity() != null) {
                dto.setUniversityName(club.getUniversity().getName());
            }
            if (club.getAdmin() != null) {
                dto.setAdminName(club.getAdmin().getFirstName() + " " + club.getAdmin().getLastName());
            }
            dto.setMemberCount(clubRepository.countMembersByClubId(club.getId()));
            dto.setPostCount(clubRepository.countPostsByClubId(club.getId()));
            if (currentUserId != null) {
                dto.setFollowing(clubRepository.isUserFollowingClub(club.getId(), currentUserId));
            }
            return dto;
        } catch (Exception e) {
            System.err.println("Error in convertToShowcaseDto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private PostDto convertToPostDto(Post post, UUID currentUserId) {
        try {
            PostDto dto = new PostDto();
            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUpdatedAt(post.getUpdatedAt());
            dto.setClubId(post.getClubId());
            dto.setAuthorId(post.getAuthorId());
            if (post.getAuthor() != null) {
                dto.setAuthorName(post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName());
            }
            dto.setLikeCount(postRepository.countLikesByPostId(post.getId()));
            dto.setCommentCount(postRepository.countCommentsByPostId(post.getId()));
            if (currentUserId != null) {
                dto.setLikedByCurrentUser(postRepository.isPostLikedByUser(post.getId(), currentUserId));
            }
            return dto;
        } catch (Exception e) {
            System.err.println("Error in convertToPostDto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}