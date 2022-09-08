package uz.nukuslab.debetapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.Role;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String phone;

    private Long companyId;

    private double balance = 0;

    private Long roleId;

    public UserDto(String firstName, String lastName, String username, String password, String phone, Long companyId, Long roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.companyId = companyId;
        this.roleId = roleId;
    }
}
