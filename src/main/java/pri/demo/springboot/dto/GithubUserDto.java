package pri.demo.springboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * github用户
 *
 * @author woieha320r
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class GithubUserDto {

    private String login;
    private Long id;
    private String nodeId;
    private String avatarUrl;
    private String gravatarId;
    private String url;
    private String htmlUrl;
    private String followersUrl;
    private String followingUrl;
    private String gistsUrl;
    private String starredUrl;
    private String subscriptionsUrl;
    private String organizationsUrl;
    private String reposUrl;
    private String eventsUrl;
    private String receivedEventsUrl;
    private String type;
    private Boolean siteAdmin;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private String hireable;
    private String bio;
    private String twitterUsername;
    private Long publicRepos;
    private Long publicGists;
    private Long followers;
    private Long following;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long privateGists;
    private Long totalPrivateRepos;
    private Long ownedPrivateRepos;
    private Long diskUsage;
    private Long collaborators;
    private Boolean twoFactorAuthentication;
    private Plan plan;

    @Setter
    @Getter
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Plan {

        private String name;
        private Long space;
        private Integer collaborators;
        private Integer privateRepos;

    }
}
