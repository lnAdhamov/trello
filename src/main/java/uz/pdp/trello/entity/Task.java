package uz.pdp.trello.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private boolean completedBeforeDeadline;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deadline;
    private LocalDateTime finishedAt;

    @ManyToOne
    private User createdBy;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> members;

    @ManyToOne
    private TaskList taskList;

    @OneToMany
    private List<TaskAttachment> files;

    public String formatCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return createdAt.format(formatter);
    }

    public String formatDeadLine() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return deadline.format(formatter);
    }

    public String formatFinishedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return finishedAt.format(formatter);
    }

    public boolean isFinishedBeforeDeadline() {
        if (taskList.getTitle().equalsIgnoreCase("completed")) {
            if (deadline == null) {
                return true;
            }
            if (finishedAt == null) {
                return false;
            } else {
                return finishedAt.isBefore(deadline);
            }
        }
        return false;
    }

    public boolean isFinishedAfterDeadLine() {
        if (taskList.getTitle().equalsIgnoreCase("completed")) {
            if (deadline == null || finishedAt == null) {
                return false;
            } else {
                return finishedAt.isAfter(deadline);
            }
        }
        return false;
    }

}
