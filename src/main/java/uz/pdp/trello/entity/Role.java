package uz.pdp.trello.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    private Integer id;

    private String name;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
