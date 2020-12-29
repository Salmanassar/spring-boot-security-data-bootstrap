package web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Fist Name is required.")
    private String firstName;

    @NotEmpty(message = "LastName is required.")
    private String lastName;

    @NotEmpty(message = "Email is required.")
    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty(message = "Password is required.")
    @Column(name = "password")
    private String password;

    @Transient
    @NotEmpty(message = "Password is required.")
    private String passwordConfirmation;

    @Column(name = "calendar")
    private Calendar created = Calendar.getInstance();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getRolesString() {
        return getRoles().stream().map(Role::getRole).collect(Collectors.joining(", "));
    }

    public boolean isAdmin() {
        return getRolesString().contains("ROLE_ADMIN");
    }

    public boolean isUser() { return getRolesString().contains("ROLE_USER"); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
