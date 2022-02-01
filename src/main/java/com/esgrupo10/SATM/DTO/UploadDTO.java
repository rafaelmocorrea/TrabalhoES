package com.esgrupo10.SATM.DTO;

import com.esgrupo10.SATM.model.Arquivo;
import com.esgrupo10.SATM.model.Exame;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UploadDTO {

    private Exame exame;

    private MultipartFile file;
}
