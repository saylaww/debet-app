package uz.nukuslab.debetapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private Long id;

    private String firstName;
    private String lastName;
    private String phone;
    private Long companyId;


}
