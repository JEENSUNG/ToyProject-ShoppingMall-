package toy.toy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String email;
    private String address;
    private String phone;
    private String grade;
    private String role;

    private String filename;
    private String filepath;

    private int money;

    @OneToMany(mappedBy = "user")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<History> userHistory = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<History> sellerHistory = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private Cart cart;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createDate;

    @PrePersist
    public void createDate(){
        this.createDate = LocalDateTime.now();
    }
}
