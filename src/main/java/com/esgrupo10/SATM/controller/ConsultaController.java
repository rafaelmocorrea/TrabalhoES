package com.esgrupo10.SATM.controller;

import com.esgrupo10.SATM.DTO.ConsultaDTO;
import com.esgrupo10.SATM.details.MedicoDetails;
import com.esgrupo10.SATM.details.PacienteDetails;
import com.esgrupo10.SATM.model.Consulta;
import com.esgrupo10.SATM.model.Medico;
import com.esgrupo10.SATM.model.Paciente;
import com.esgrupo10.SATM.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalTime;
import java.util.List;

@Controller
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PedidoConsultaService pedidoConsultaService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private InfoPagamentoService infoPagamentoService;

    @GetMapping("/menumedico/aceitapedido/{pedid}")
    public String aceitaPedido(Model model, @PathVariable Long pedid){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setPedido_id(pedid);
        consultaDTO.setPaciente_email(pedidoConsultaService.getPedido(pedid).getPaciente().getEmail());
        consultaDTO.setMedico_email(username);
        consultaDTO.setDescricao(pedidoConsultaService.getPedido(pedid).getDescricao());

        model.addAttribute("consultaDTO",consultaDTO);
        return "aceitaconsulta";
    }

    @PostMapping("/menumedico/aceita_consulta")
    public String confirmaPedido(ConsultaDTO consultaDTO) {
        LocalTime t = LocalTime.parse(consultaDTO.getHora());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pedidoConsultaService.getPedido(consultaDTO.getPedido_id()).getPaciente();
        Consulta consulta = new Consulta();
        consulta.setPaciente(pac);
        consulta.setMedico(med);
        consulta.setData(consultaDTO.getData());
        consulta.setValor(consultaDTO.getValor());
        consulta.setDescricao(consultaDTO.getDescricao());
        consulta.setHora(java.sql.Time.valueOf(t));
        if (consultaDTO.getValor() > 0)
            consulta.setPaga(Boolean.FALSE);
        else
            consulta.setPaga(Boolean.TRUE);
        consulta.setStatus("Marcada");

        String retorno = consultaService.criaConsulta(consulta);
        if (retorno.equals("consulta_sucesso")) {
            pedidoConsultaService.deletaPedido(pedidoConsultaService.getPedido(consultaDTO.getPedido_id()));
            notificacaoService.notificaConsultaAceita(pac,med,consultaDTO.getData());
        }

        return retorno;
    }

    @GetMapping("/menumedico/agendaconsulta")
    public String agendaConsulta(Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setMedico_email(username);

        model.addAttribute("consultaDTO",consultaDTO);

        return "abre_consulta";
    }

    @PostMapping("/menumedico/confirma_consulta")
    public String confirmaConsulta(ConsultaDTO consultaDTO) {
        LocalTime t = LocalTime.parse(consultaDTO.getHora());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pacienteService.encontraPorEmail(consultaDTO.getPaciente_email());

        Consulta consulta = new Consulta();
        consulta.setMedico(med);
        consulta.setPaciente(pac);
        consulta.setDescricao(consultaDTO.getDescricao());
        consulta.setData(consultaDTO.getData());
        consulta.setValor(consultaDTO.getValor());
        consulta.setHora(java.sql.Time.valueOf(t));
        if (consultaDTO.getValor() > 0)
            consulta.setPaga(Boolean.FALSE);
        else
            consulta.setPaga(Boolean.TRUE);
        consulta.setStatus("Marcada");

        notificacaoService.notificaConsulta(pac,med,consulta.getData());

        return consultaService.criaConsulta(consulta);
    }

    @GetMapping("/menumedico/abrirconsulta")
    public String abrirConsulta(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        java.sql.Date data = new java.sql.Date(System.currentTimeMillis());
        List<Consulta> consultas = consultaService.listaConsultasDiaMedico(med,data);
        List<Consulta> consultas_atualizar = consultaService.listaConsultasPreDateNotStatusMedico(med,data,"Encerrada");

        if (consultas_atualizar != null) {
            for (Consulta c : consultas_atualizar) {
                c.setStatus("Encerrada");
                consultaService.updateConsulta(c);
            }
        }

        model.addAttribute("consultas",consultas);

        return "abreconsultas";
    }

    @GetMapping("/menumedico/abre/{consid}")
    public String abreConsulta(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);
        model.addAttribute("consulta",cons);

        return "insere_link";
    }

    @PostMapping("/menumedico/inserelink")
    public String insereLink(Consulta consulta) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        consulta.setStatus("Em andamento");
        if (!med.getEmail().equals(consulta.getMedico().getEmail())) {
            return "405";
        }
        consultaService.updateConsulta(consulta);
        notificacaoService.notificaLink(consulta.getPaciente(),consulta.getLinkvideoconf());

        return "sucesso_link";
    }

    @GetMapping("/menumedico/consulta/{consid}")
    public String consultaEmAndamento(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);
        model.addAttribute("consulta",cons);

        return "consulta";
    }

    @PostMapping(value = "/menumedico/atualizalink/{consid}",params = "atualizar")
    public String atualizaLink(Model model, @PathVariable Long consid,Consulta consulta) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        if (!med.getEmail().equals(consulta.getMedico().getEmail())) {
            return "405";
        }
        consultaService.updateConsulta(consulta);
        notificacaoService.notificaLinkUpdate(consulta.getPaciente(),consulta.getLinkvideoconf());
        model.addAttribute("consulta",consulta);

        return "consulta";
    }

    @PostMapping(value = "/menumedico/atualizalink/{consid}",params = "encerrar")
    public String encerraConsulta(Model model, @PathVariable Long consid,Consulta consulta) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        if (!med.getEmail().equals(consulta.getMedico().getEmail())) {
            return "405";
        }
        consulta.setStatus("Encerrada");
        consultaService.updateConsulta(consulta);
        model.addAttribute("consulta",consulta);

        return "menumedico";
    }

    @PostMapping(value = "/menumedico/atualizalink/{consid}",params = "cancelar")
    public String cancelaConsulta(Model model, @PathVariable Long consid,Consulta consulta) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        if (!med.getEmail().equals(consulta.getMedico().getEmail())) {
            return "405";
        }
        consultaService.deletaConsulta(consulta);
        notificacaoService.notificaConsultaCancelada(consulta.getPaciente(), consulta.getMedico(), consulta.getData());

        return "menumedico";
    }

    @GetMapping("/menupaciente/cancelaconsulta/{consid}")
    public String cancelaConsultaP(@PathVariable Long consid) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        if (!pac.getEmail().equals(consultaService.getConsulta(consid).getPaciente().getEmail())) {
            return "405";
        }
        consultaService.deletaConsulta(consultaService.getConsulta(consid));

        return "menupaciente";
    }

    @GetMapping("/menumedico/agendamedico")
    public String agendaMedico(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Medico med = medicoService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultasNotStatusMedico(med,"Encerrada");
        model.addAttribute("consultas",consultas);

        return "agendamedico";
    }

    @GetMapping("/menumedico/gerenciarconsultas")
    public String gerenciarConsultas(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultasMedico(med);
        model.addAttribute("consultas",consultas);

        return "consultas";
    }

    @PostMapping(value = "/menumedico/filtralista",params = "antigas")
    public String consultasEncerradas(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultaStatusMedico(med,"Encerrada");
        model.addAttribute("consultas",consultas);

        return "consultas";
    }

    @PostMapping(value = "/menumedico/filtralista",params = "marcadas")
    public String consultasMarcadas(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultaStatusMedico(med,"Marcada");
        model.addAttribute("consultas",consultas);

        return "consultas";
    }

    @PostMapping(value = "/menumedico/filtralista",params = "todas")
    public String todasConsultas(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultasMedico(med);
        model.addAttribute("consultas",consultas);

        return "consultas";
    }

    @GetMapping("/menupaciente/agendapaciente")
    public String agendaPaciente(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultasNotStatusPaciente(pac,"Encerrada");
        model.addAttribute("consultas",consultas);

        return "agendapaciente";
    }

    @GetMapping("/menupaciente/gerenciarconsultas")
    public String gerenciarConsultasPac(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultasPaciente(pac);
        model.addAttribute("consultas",consultas);

        return "consultasp";
    }

    @PostMapping(value = "/menupaciente/filtralista",params = "antigas")
    public String consultaEncerradasPac(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultaStatusPaciente(pac,"Encerrada");
        model.addAttribute("consultas",consultas);

        return "consultasp";
    }

    @PostMapping(value = "/menupaciente/filtralista",params = "marcadas")
    public String consultaMarcadaPac(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultaStatusPaciente(pac,"Marcada");
        model.addAttribute("consultas",consultas);

        return "consultasp";
    }

    @PostMapping(value = "/menupaciente/filtralista",params = "todas")
    public String todasConsultasPac(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        List<Consulta> consultas = consultaService.listaConsultasPaciente(pac);
        model.addAttribute("consultas",consultas);

        return "consultasp";
    }

    @GetMapping("/menupaciente/acessarconsultas")
    public String acessarConsultasPac(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        java.sql.Date data = new java.sql.Date(System.currentTimeMillis());
        List<Consulta> consultas = consultaService.listaConsultasDiaPaciente(pac,data);

        model.addAttribute("consultas",consultas);

        return "acessaconsultas";
    }

    @GetMapping("/menupaciente/consulta/{consid}")
    public String consultaEmAndamentoPac(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);
        if (cons.getPaga()) {
            model.addAttribute("consulta", cons);

            return "consultap";
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PacienteDetails) {
            username = ((PacienteDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Paciente pac = pacienteService.encontraPorEmail(username);
        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setConsulta_id(consid);

        consultaDTO.setInfoPagamentos(infoPagamentoService.metodosPagamentoPaciente(pac));
        model.addAttribute("consultaDTO",consultaDTO);

        return "pagaconsulta";
    }

    @GetMapping("/menupaciente/veconsulta/{consid}")
    public String veConsultaPac(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);

        model.addAttribute("consulta", cons);

        return "veconsultap";
    }

    @GetMapping("/menumedico/paccons/{pacid}")
    public String exibeConsultasPac(Model model, @PathVariable Long pacid) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MedicoDetails) {
            username = ((MedicoDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Medico med = medicoService.encontraPorEmail(username);
        Paciente pac = pacienteService.encontraPorId(pacid);
        List<Consulta> consultas;
        if (pac.getPermite_consultas()) {
            consultas = consultaService.listaConsultasNotStatusPaciente(pac,"Marcada");
        } else {
            consultas = consultaService.listaConsultasNotStatusPacienteMedico(pac,med,"Marcada");
        }
        model.addAttribute("consultas",consultas);

        return "consultaspaciente";
    }

    @GetMapping("/menupaciente/consultapaga/{consid}")
    public String consultaPaga(Model model, @PathVariable Long consid) {
        Consulta cons = consultaService.getConsulta(consid);
        cons.setPaga(Boolean.TRUE);
        consultaService.updateConsulta(cons);
        model.addAttribute("consulta", cons);

        return "consultap";
    }

    @GetMapping("/menumedico/menuconsulta")
    public String menuConsultMedico() {
        return "menuconsultam";
    }

    @GetMapping("/menupaciente/menuconsulta")
    public String menuConsultaPaciente() {
        return "menuconsultap";
    }

}
