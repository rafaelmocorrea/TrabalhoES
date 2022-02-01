package com.esgrupo10.SATM.service;

import com.esgrupo10.SATM.model.Arquivo;
import com.esgrupo10.SATM.repository.ArquivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
public class ArquivoService {

    @Autowired
    private ArquivoRepository arquivoRepository;

    public Arquivo salvaArquivo(MultipartFile file) {
        Arquivo arquivo = new Arquivo();
        try {
            arquivo.setDados(file.getBytes());
            arquivo.setDocType(file.getContentType());
            arquivo.setDocName(file.getOriginalFilename());
            arquivoRepository.save(arquivo);
        } catch (Exception e) {
            return null;
        }

        return arquivo;
    }

    @Transactional
    public Arquivo getArquivo(Long id) {
        return arquivoRepository.findById(id).get();
    }
}
