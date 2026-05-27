package com.telemetria.web.controller;

import com.telemetria.web.security.AdminPrincipal;
import com.telemetria.web.service.GrafanaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    private final GrafanaService grafanaService;

    public WebController(GrafanaService grafanaService) {
        this.grafanaService = grafanaService;
    }

    @GetMapping({"/", "/dashboard", "/dashboard/empresa"})
    public String dashboardEmpresa(@AuthenticationPrincipal AdminPrincipal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Long empresaId = principal.getEmpresaId();

        model.addAttribute("grafanaIframeUrl", grafanaService.buildEmpresaDashboardUrl(empresaId));
        model.addAttribute("empresaNombre", principal.getEmpresaNombre());
        model.addAttribute("empresaId", empresaId);
        model.addAttribute("dashboardTipo", "empresa");
        model.addAttribute("dashboardTitulo", "Dashboard de empresa");

        return "layout";
    }

    @GetMapping("/dashboard/ordenadores")
    public String dashboardOrdenadores(@AuthenticationPrincipal AdminPrincipal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Long empresaId = principal.getEmpresaId();

        model.addAttribute("grafanaIframeUrl", grafanaService.buildOrdenadoresDashboardUrl(empresaId));
        model.addAttribute("empresaNombre", principal.getEmpresaNombre());
        model.addAttribute("empresaId", empresaId);
        model.addAttribute("dashboardTipo", "ordenadores");
        model.addAttribute("dashboardTitulo", "Dashboard de ordenadores");

        return "layout";
    }

    @GetMapping("/licencias")
    public String licencias() {
        return "redirect:/dashboard/empresa";
    }
}
