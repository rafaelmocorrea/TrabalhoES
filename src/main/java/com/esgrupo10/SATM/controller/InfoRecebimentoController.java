package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.details.MedicoDetails;
import com.esgrupo10.SATM.model.InfoRecebimento;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.service.InfoRecebimentoService;
import com.esgrupo10.SATM.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class InfoRecebimentoController {

    @Autowired
    InfoRecebimentoService infoRecebimentoService;

    @Autowired
    MedicoService  medicoService;

    @GetMapping("/menumedico/metodos/")
    public String administraMetodos(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);

        List<InfoRecebimento> metodos = infoRecebimentoService.metodosRecebimentoMedico(med);
        model.addAttribute("metodos",metodos);

        return "administrametodosrec";
    }

    @GetMapping("/menumedico/apagametodo/{metid}")
    public String apagaMetodo(@PathVariable Long metid) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        InfoRecebimento inf = infoRecebimentoService.encontraInfoRecebimento(metid);
        if (med.getId() != inf.getMedico().getId()) {
            return "falha";
        }
        infoRecebimentoService.deletaMetodo(inf);

        return "menumedico";
    }

    @GetMapping("/menumedico/adicionametodo")
    public String adicionaMetodo(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        InfoRecebimento infoRecebimento = new InfoRecebimento();
        infoRecebimento.setMedico(med);
        model.addAttribute("metodo",infoRecebimento);

        return "cadastro_metodo_recebimento";
    }

    @PostMapping("/menumedico/addmetodo")
    public String addMetodo(InfoRecebimento info) {
        return infoRecebimentoService.adicionarMetodoRecebimento(info);
    }

}
