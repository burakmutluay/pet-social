package payload;

import lombok.Data;

@Data
public class FollowRequest {

    String follower;
    String followed;
}
