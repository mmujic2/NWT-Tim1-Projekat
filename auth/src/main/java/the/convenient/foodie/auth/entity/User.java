package the.convenient.foodie.auth.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import the.convenient.foodie.auth.dto.RegisterRequest;
import the.convenient.foodie.auth.enums.Role;
import the.convenient.foodie.auth.util.UUIDGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
@Table(name="user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @NotNull(message = "Firstname must be specified!")
    @Column(name = "firstname", columnDefinition = "VARCHAR(60)")
    private String firstname;

    @NotNull(message = "Lastname must be specified!")
    @Column(name = "lastname", columnDefinition = "VARCHAR(60)")
    private String lastname;

    @NotNull(message = "Email address must be specified!")
    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(60)")
    private String email;


    @Column(name = "address", columnDefinition = "VARCHAR(60)",nullable = true)
    private String address;


    @NotNull(message = "Username must be specified!")
    @Column(name = "username",unique = true, columnDefinition = "VARCHAR(60)")
    private String username;


    @Column(name = "phone_number",nullable = true)
    private String phoneNumber;


    @Pattern(regexp ="^((-)?[0-9]?[0-9]\\.\\d+,(\\s)*(-)?[1]?[0-9]?[0-9]\\.\\d+)",message = "Map coordinates must represent latitude and longitude values!")
    @Column(name = "map_coordinates",nullable = true)
    private String mapCoordinates;

    @NotNull(message = "Password must be specified!")
    @Column(name = "password", columnDefinition = "VARCHAR(60)")
    private String password;

    @NotNull(message = "Role must be specified!")
    @Column(name="role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> userTokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User() {
    }

    public User(RegisterRequest request) {
        uuid = UUIDGenerator.generateType1UUID().toString();
        firstname = request.getFirstname();
        lastname = request.getLastname();
        email = request.getEmail();
        address = request.getAddress();
        username = request.getUsername();
        phoneNumber = request.getPhoneNumber();
        mapCoordinates = request.getMapCoordinates();
    }

    public User(Integer id,String uuid, String firstname, String lastname, String email, String address, String username, String phoneNumber,String mapCoordinates, String password, Role role, List<Token> userTokens) {
        this.id = id;
        this.uuid = uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.mapCoordinates = mapCoordinates;
        this.password = password;
        this.role = role;
        this.userTokens = userTokens;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Token> getUserTokens() {
        return userTokens;
    }

    public void setUserTokens(List<Token> userTokens) {
        this.userTokens = userTokens;
    }

    public String getMapCoordinates() {
        return mapCoordinates;
    }

    public void setMapCoordinates(String mapCoordinates) {
        this.mapCoordinates = mapCoordinates;
    }
}
