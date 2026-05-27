package com.telemetria.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GrafanaService {

    @Value("${app.grafana.empresa-dashboard-url:${app.grafana.dashboard-url}}")
    private String empresaDashboardUrl;

    @Value("${app.grafana.ordenadores-dashboard-url:${app.grafana.dashboard-url}}")
    private String ordenadoresDashboardUrl;

    public String buildEmpresaDashboardUrl(Long empresaId) {
        return buildDashboardUrl(empresaDashboardUrl, empresaId);
    }

    public String buildOrdenadoresDashboardUrl(Long empresaId) {
        return buildDashboardUrl(ordenadoresDashboardUrl, empresaId);
    }

    private String buildDashboardUrl(String dashboardUrl, Long empresaId) {
        if (empresaId == null) {
            throw new IllegalArgumentException("El usuario autenticado no tiene empresa asociada");
        }

        return UriComponentsBuilder
                .fromUriString(toSoloPanelUrl(dashboardUrl))
                .queryParam("orgId", "1")
                .queryParam("theme", "dark")
                .queryParam("var-empresa", empresaId)
                .build()
                .toUriString();
    }

    private String toSoloPanelUrl(String dashboardUrl) {
        if (dashboardUrl == null || dashboardUrl.isBlank()) {
            throw new IllegalArgumentException("No se ha configurado la URL del dashboard de Grafana");
        }

        // Grafana muestra un panel individual con /d-solo/... y el parametro panelId.
        // Si en application.yml o en .env se mantiene una URL /d/ normal, la convertimos aqui.
        return dashboardUrl.replace("/d/", "/d-solo/");
    }
}
