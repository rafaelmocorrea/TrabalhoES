package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.DTO.ExameDTO;
import com.esgrupo10.SATM.DTO.UploadDTO;
import com.esgrupo10.SATM.details.MedicoDetails;
import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.*;
import com.esgrupo10.SATM.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExameController {

    @Autowired
    private ExameService exameService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ArquivoService arquivoService;

    @GetMapping("/menumedico/reqexame")
    public String requisitaExame(Model model) {
        ExameDTO exameDTO = new ExameDTO();
        exameDTO.setPaciente_email("");
        exameDTO.setNome("");
        exameDTO.setDescricao("");
        exameDTO.setData(new java.sql.Date(System.currentTimeMillis()));
        model.addAttribute("exameDTO",exameDTO);

        return "requisitaexame";
    }

    @PostMapping(value = "/menumedico/atualizalink/{consid}",params = "exame")
    public String reqExamePac(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);
        ExameDTO exameDTO = new ExameDTO();
        exameDTO.setPaciente_email(cons.getPaciente().getEmail());
        exameDTO.setNome("");
        exameDTO.setDescricao("");
        exameDTO.setData(cons.getData());
        model.addAttribute("exameDTO",exameDTO);

        return "requisitaexame";
    }

    @PostMapping("/menumedico/confirma_exame")
    public String confirmaExame(ExameDTO exameDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pacienteService.encontraPorEmail(exameDTO.getPaciente_email());
        Exame exame = new Exame();
        exame.setMedico(med);
        exame.setPaciente(pac);
        exame.setData(exameDTO.getData());
        exame.setNome(exameDTO.getNome());
        exame.setDescricao(exameDTO.getDescricao());
        exame.setFeito(Boolean.FALSE);

        String retorno = exameService.criaExame(exame);

        if (retorno.equals("receita_sucesso")) {
            notificacaoService.notificaExame(pac,med,exame.getNome());
        }

        return retorno;
    }

    @GetMapping("/menumedico/acessarexames")
    public String acessarExame(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesMedico(med);

        model.addAttribute("exames",exames);

        return "listaexames";
    }

    @GetMapping("/menumedico/exame/{exid}")
    public String veExame(Model model, @PathVariable Long exid) {
        Exame exame = exameService.getExame(exid);
        model.addAttribute("exame",exame);

        return "exibeexame";
    }

    @GetMapping("/menumedico/apagaexame/{exid}")
    public String apagaExame(@PathVariable Long exid) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Exame exame = exameService.getExame(exid);
        if (exame.getFeito() == Boolean.TRUE) {
            return "falha_feito";
        } else if (!exame.getMedico().getCpf().equals(med.getCpf())) {
            return "falha_med";
        }

        exameService.apagaExame(exame);
        notificacaoService.notificaExameApagado(exame.getPaciente(),med,exame.getNome());

        return "menumedico";
    }

    @PostMapping(value="/menumedico/filtraexames",params="nao_feitos")
    public String filtraExameNaoFeitoMed(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesMedicoBoolean(med,Boolean.FALSE);
        model.addAttribute("exames",exames);

        return "listaexames";
    }

    @PostMapping(value="/menumedico/filtraexames",params="feitos")
    public String filtraExameFeitoMed(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesMedicoBoolean(med,Boolean.TRUE);
        model.addAttribute("exames",exames);

        return "listaexames";
    }

    @PostMapping(value="/menumedico/filtraexames",params="todas")
    public String filtraExameTodosMed(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesMedico(med);
        model.addAttribute("exames",exames);

        return "listaexames";
    }

    @GetMapping("/menupaciente/gerenciarexames")
    public String gerenciarExames(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesPaciente(pac);
        model.addAttribute("exames",exames);

        return "gerenciaexame";
    }

    @PostMapping(value = "/menupaciente/filtraexames",params="nao_feitos")
    public String filtraExameNaoFeitoPac(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesPacienteBoolean(pac, Boolean.FALSE);
        model.addAttribute("exames",exames);

        return "gerenciaexame";
    }

    @PostMapping(value = "/menupaciente/filtraexames",params="feitos")
    public String filtraExameFeitoPac(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesPacienteBoolean(pac, Boolean.TRUE);
        model.addAttribute("exames",exames);

        return "gerenciaexame";
    }

    @PostMapping(value = "/menupaciente/filtraexames",params="todas")
    public String filtraExameTodosPac(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Exame> exames = exameService.listaExamesPaciente(pac);
        model.addAttribute("exames",exames);

        return "gerenciaexame";
    }

    @GetMapping("/menupaciente/exame/{exid}")
    public String exibeExame(Model model, @PathVariable Long exid) {
        Exame exame = exameService.getExame(exid);
        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setExame(exame);
        model.addAttribute("exame",uploadDTO);

        return "examepac";
    }

    @PostMapping("/menupaciente/exame/upload")
    public String upload(UploadDTO uploadDTO) {
        try {
            Arquivo arq = arquivoService.salvaArquivo(uploadDTO.getFile());
            if (arq != null){
                Exame exame = uploadDTO.getExame();
                exame.setArquivo(arq);
                exame.setFeito(Boolean.TRUE);
                exameService.updateExame(exame);
                notificacaoService.notificaExameUpload(exame.getPaciente(),exame.getMedico(),exame.getNome());
            }
            return "menupaciente";
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/menupaciente/download/{exid}")
    public ResponseEntity<ByteArrayResource> downloadExame(@PathVariable Long exid) {
        Exame exame = exameService.getExame(exid);
        Long id = exame.getArquivo().getId();
        Arquivo arq = arquivoService.getArquivo(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arq.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+arq.getDocName()+"\"")
                .body(new ByteArrayResource(arq.getDados()));
    }

    @GetMapping("/menumedico/download/{exid}")
    public ResponseEntity<ByteArrayResource> downloadExameM(@PathVariable Long exid) {
        Exame exame = exameService.getExame(exid);
        Long id = exame.getArquivo().getId();
        Arquivo arq = arquivoService.getArquivo(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arq.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+arq.getDocName()+"\"")
                .body(new ByteArrayResource(arq.getDados()));
    }

    @GetMapping("/menumedico/pacexames/{pacid}")
    public String exibeExamesPac(Model model, @PathVariable Long pacid) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pacienteService.encontraPorId(pacid);
        List<Exame> exames;
        if (pac.getPermite_exames()) {
            exames = exameService.listaExamesPaciente(pac);
        } else {
            exames = exameService.listaExamesPacienteMedico(pac,med);
        }
        model.addAttribute("exames",exames);

        return "examespaciente";
    }

    @GetMapping("/menumedico/menuexame")
    public String menuExameMedico() {
        return "menuexamem";
    }

}
