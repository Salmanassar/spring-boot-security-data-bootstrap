package web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Component
@NoArgsConstructor
@Table(name = "role")
public class Role implements GrantedAuthority, Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "roles", nullable = false)
    private String role;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<User> users;

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public List<Role> setRoleString(String role) {
        List<Role> listRole = new ArrayList<>();
        listRole.add(new Role(0L, role));
        return listRole;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' + '}';
    }

    @Override
    public String getAuthority() {
        return getRole();
    }
}
