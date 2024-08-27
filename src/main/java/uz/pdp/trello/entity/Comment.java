package uz.pdp.trello.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Task task;

    @ManyToOne
    private User commentedBy;

    @CreationTimestamp
    private LocalDateTime commentedAt;

    private String text;

    public String formatCommentedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM");
        return commentedAt.format(formatter);
    }
}
