package co.com.wolox.WChallenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.Set;

@Data
@Document(collection = "albums")
public class Album {
    @Id
    private int id;
    private int userId;
    private String title;
    private Set<SharedUser> sharedUsers;

    @Data
    @AllArgsConstructor
    public static class SharedUser {
        private int userId;
        private boolean rPermissions;
        private boolean wPermissions;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SharedUser that = (SharedUser) o;
            return userId == that.userId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId);
        }
    }
}
