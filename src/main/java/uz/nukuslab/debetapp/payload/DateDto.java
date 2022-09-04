package uz.nukuslab.debetapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateDto {

    private String start;
    private String end;

    public DateDto(String start) {
        this.start = start;
    }
}
