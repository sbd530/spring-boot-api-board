package study.board.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ParamPost {
    @NotEmpty
    @Size(min = 2, max = 50)
    @ApiModelProperty(value = "작성자명", required = true)
    private String author;

    @NotEmpty
    @Size(min = 2, max = 100)
    @ApiModelProperty(value = "제목", required = true)
    private String title;

    @NotEmpty
    @Size(min = 2, max = 500)
    @ApiModelProperty(value = "내용", required = true)
    private String content;
}
