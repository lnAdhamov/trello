package uz.pdp.trello.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @ManyToOne
    private Board board;

    private Integer orderNumber;
}
